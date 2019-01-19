package standbyme.cloud.storage

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service;

import java.net.MalformedURLException
import org.springframework.core.io.UrlResource


@Service
class FileSystemStorageService @Autowired constructor(properties: StorageProperties) : StorageService {

    private val rootLocation: Path

    init {
        this.rootLocation = Paths.get(properties.location)
    }

    override fun store(filename: String, file: String) {
        try {
            if (file.isEmpty()) {
                throw StorageException("Failed to store empty file $filename")
            }
            if (filename.contains("..")) {
                // This is a security check
                throw StorageException(
                        "Cannot store file with relative path outside current directory $filename")
            }

            Files.copy(file.byteInputStream(), this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING)

        } catch (e: IOException) {
            throw StorageException("Failed to store file $filename", e)
        }
    }

    override fun load(filename: String): Path {
        return rootLocation.resolve(filename)
    }

    override fun loadAsResource(filename: String): Resource {
        try {
            val file = load(filename)
            val resource = UrlResource(file.toUri())
            return if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException(
                        "Could not read file: $filename")

            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not read file: $filename", e)
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