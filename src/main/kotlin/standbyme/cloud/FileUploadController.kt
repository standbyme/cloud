package standbyme.cloud

import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import standbyme.cloud.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import standbyme.cloud.storage.StorageFileNotFoundException

@Controller
@RequestMapping("objects")
class FileUploadController @Autowired constructor(private val storageService: StorageService) {

    @GetMapping("{filename:.+}")
    fun get(@PathVariable filename: String): ResponseEntity<ByteArray> {

        val file = storageService.load(filename)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"").body<ByteArray>(file)
    }

    @PutMapping("{filename:.+}")
    fun put(@RequestBody data: String, @PathVariable filename: String): ResponseEntity<*> {

        storageService.store(filename, data)
        return ResponseEntity.ok().build<Any>()
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException): ResponseEntity<*> {
        return ResponseEntity.notFound().build<Any>()
    }

}