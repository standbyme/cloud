package standbyme.cloud.storage

interface StorageService {
    fun init()

    fun store(filename: String, file: ByteArray)

    fun load(filename: String): ByteArray
}