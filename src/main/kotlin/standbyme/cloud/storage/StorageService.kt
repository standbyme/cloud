package standbyme.cloud.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

import java.nio.file.Path

interface StorageService {
    fun init()

    fun store(filename: String, file: String)

//    fun loadAll(): Stream<Path>

    fun load(filename: String): Path

    fun loadAsResource(filename: String): Resource

//    fun deleteAll()
}