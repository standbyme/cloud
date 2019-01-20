package standbyme.cloud

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.then
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import standbyme.cloud.storage.StorageService
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import standbyme.cloud.storage.StorageFileNotFoundException
import org.mockito.BDDMockito.given
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get


@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
class FileUploadTests {
    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private val storageService: StorageService? = null

    @Test
    @Throws(Exception::class)
    fun shouldSaveUploadedFile() {
        this.mvc!!.perform(post("/objects/filename").content("Successful content"))
                .andExpect(status().is2xxSuccessful)

        then(this.storageService).should()!!.store("filename","Successful content")
    }

    @Test
    @Throws(Exception::class)
    fun should404WhenMissingFile() {
        given(this.storageService!!.loadAsResource("filename"))
                .willThrow(StorageFileNotFoundException::class.java)

        this.mvc!!.perform(get("/objects/filename")).andExpect(status().isNotFound)
    }
}