package standbyme.cloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import standbyme.cloud.storage.StorageProperties
import standbyme.cloud.storage.StorageService
import org.springframework.boot.CommandLineRunner
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Bean


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
@EnableDiscoveryClient
class StorageApplication {
    @Bean
    fun init(storageService: StorageService) = CommandLineRunner { args ->
        storageService.init()
    }
}

fun main(args: Array<String>) {
    runApplication<StorageApplication>(*args)
}



