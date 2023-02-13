package com.example.reactivekotlin

import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.util.function.Consumer

@Configuration
class BreedInitializer {

//    val other: List<SubBreed> = emptyList()

    @Bean
    fun initBreeds(breedRepository: BreedRepository, subBreedRepository: SubBreedRepository, webClient: WebClient) =
        ApplicationRunner {
            runBlocking {
                val response = webClient
                    .get()
                    .uri("https://dog.ceo/api/breeds/list/all")
                    .retrieve()
                    .awaitBody<Message>()

                response.message.entries.forEach { entry ->
                    val breed = Breed(name = entry.key)
                    val saved = breedRepository.save(breed).block()

                    entry.value.forEach(Consumer {
                        subBreedRepository.save(SubBreed(it, breedId = saved?.id)).block()
                    })

                }
            }

//            breedRepository.findAll().subscribe(System.out::println)
//            val btt1 = breedRepository.findAll().subscribe(Consumer {
//                println("consuming: $it")
//            })
//            val btt2 = subBreedRepository.findAll().subscribe(Consumer {
//                println("consuming: $it")
//            })
//
//            val btt1 = subBreedRepository.findByBreedId(breedId = 6)?.subscribe(Consumer {
//                println("consuming custom: $it")
//            })
//
//            runBlocking {
//                val res = subBreedRepository.findByBreedIdCoroutine(6)
//                println(res)
//            }

        }

}