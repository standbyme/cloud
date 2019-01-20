package standbyme.cloud

import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import standbyme.cloud.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import standbyme.cloud.storage.StorageFileNotFoundException

@Controller
@RequestMapping("objects")
class FileUploadController @Autowired constructor(private val storageService: StorageService) {

    @GetMapping("{filename:.+}")
    fun serveFile(@PathVariable filename: String): ResponseEntity<Resource> {

        val file = storageService.loadAsResource(filename)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.filename + "\"").body<Resource>(file)
    }

    @PutMapping("{filename:.+}")
    fun handleFileUpload(@RequestBody data: String, @PathVariable filename: String): ResponseEntity<*> {

        storageService.store(filename, data)
        return ResponseEntity.ok().build<Any>()
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException): ResponseEntity<*> {
        return ResponseEntity.notFound().build<Any>()
    }

}