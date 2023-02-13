package com.example.reactivekotlin

import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Configuration
class BreedInitializer {

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
                    val saved = breedRepository.save(breed)

                    entry.value
                        .map { subBreedRepository.save(SubBreed(it, breedId = saved?.id)) }
                }

//                //print
//                runBlocking {
//                    breedRepository.findAll().apply { println(it) }
//                }
            }

        }
}