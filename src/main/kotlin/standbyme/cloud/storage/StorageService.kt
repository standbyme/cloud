package standbyme.cloud.storage

interface StorageService {
    fun init()

    fun store(filename: String, file: String)

    fun load(filename: String): ByteArray
}