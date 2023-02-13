package com.example.reactivekotlin.services

import com.example.reactivekotlin.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody


@Service
class BreedsService(
    val breedsRepository: BreedRepository,
    val subBreedRepository: SubBreedRepository,
    val webClient: WebClient
) {
    @FlowPreview
    suspend fun findAllCoroutine(): Flow<Breed> {
        return breedsRepository
            .findAllCoroutine()
            .map { loadRelationsCoroutine(it) }
    }

    suspend fun findByIdCoroutine(id: Int): Breed {
        val breed = breedsRepository.findByIdCoroutine(id)
        loadRelationsCoroutine(breed)

        return breed
    }

//    private fun loadRelations(item: Breed): Mono<Breed> {
//        return Mono.just<Breed>(item).zipWith(subBreedRepository.findByBreedId(item.id)!!.collectList())
//            .map { result ->
//                result.t1.subBreed = result.t2
//                result.t1
//            }
//    }

    // coroutine version
    suspend fun loadRelationsCoroutine(item: Breed): Breed {
        val subBreed = subBreedRepository.findByBreedIdCoroutine(item.id)
        item.subBreed = subBreed
        return item
    }

    suspend fun findAndSaveImage(breedType: String): Image {
        val breed =
            breedsRepository.findByName(breedType)

        if (breed.image.isNotBlank())
            return Image(message = breed.image)

        println("There no image for $breedType")
        val image = retrieveImage(breedType)

        breedsRepository.updateWithImageUrl(breed.id, image.message)

        return image
    }

    suspend fun retrieveImage(breedType: String?): Image {
        return webClient
            .get()
            .uri("https://dog.ceo/api/breed/${breedType}/images/random")
            .retrieve()
            .awaitBody<Image>()
    }

}