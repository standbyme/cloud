package standbyme.cloud

import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.multipart.MultipartFile
import org.springframework.util.StringUtils.getFilename
import org.springframework.http.HttpHeaders
import java.util.stream.Collectors
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import sun.security.x509.OIDMap.addAttribute
import java.io.IOException
import org.springframework.ui.Model
import standbyme.cloud.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import standbyme.cloud.storage.StorageFileNotFoundException

@Controller
class FileUploadController @Autowired constructor(private val storageService: StorageService) {

//    @GetMapping("/")
//    @Throws(IOException::class)
//    fun listUploadedFiles(model: Model): String {
//
//        model.addAttribute("files", storageService.loadAll().map(
//                { path ->
//                    MvcUriComponentsBuilder.fromMethodName(FileUploadController::class.java,
//                            "serveFile", path.getFileName().toString()).build().toString()
//                })
//                .collect(Collectors.toList<T>()))
//
//        return "uploadForm"
//    }

    @GetMapping("/files/{filename:.+}")
    fun serveFile(@PathVariable filename: String): ResponseEntity<Resource> {

        val file = storageService.loadAsResource(filename)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body<Resource>(file)
    }

    @PostMapping("/files/{filename:.+}")
    fun handleFileUpload(@RequestBody data: String, @PathVariable filename: String): ResponseEntity<*> {

        storageService.store(filename, data)
        return ResponseEntity.ok().build<Any>()
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException): ResponseEntity<*> {
        return ResponseEntity.notFound().build<Any>()
    }

}