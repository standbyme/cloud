package standbyme.cloud.storage

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FileSystemStorageService @Autowired constructor(properties: StorageProperties) : StorageService {

    private val rootLocation: Path

    init {
        this.rootLocation = Paths.get(properties.location)
    }

    override fun store(filename: String, file: ByteArray) {
        try {
            if (file.isEmpty()) {
                throw StorageException("Failed to store empty file $filename")
            }
            if (filename.contains("..")) {
                // This is a security check
                throw StorageException(
                        "Cannot store file with relative path outside current directory $filename")
            }

            Files.copy(file.inputStream(), this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING)

        } catch (e: IOException) {
            throw StorageException("Failed to store file $filename", e)
        }
    }

    override fun load(filename: String): ByteArray {
        val byteArray = Files.newDirectoryStream(rootLocation, """$filename.*""").flatMap { Files.readAllBytes(it).toList() }.toByteArray()

        return if (byteArray.isEmpty()) {
            throw StorageFileNotFoundException(
                    "Could not read file: $filename")
        } else {
            byteArray
        }
    }

    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }
    }
}