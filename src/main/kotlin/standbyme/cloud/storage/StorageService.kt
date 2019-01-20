package standbyme.cloud.storage

import org.springframework.core.io.Resource

import java.nio.file.Path

interface StorageService {
    fun init()

    fun store(filename: String, file: String)

    fun load(filename: String): Path

    fun loadAsResource(filename: String): Resource
}