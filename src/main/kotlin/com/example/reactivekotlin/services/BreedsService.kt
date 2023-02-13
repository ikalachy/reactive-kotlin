package com.example.reactivekotlin.services

import com.example.reactivekotlin.*
import kotlinx.coroutines.FlowPreview
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class BreedsService(
    val breedsRepository: BreedRepository,
    val subBreedRepository: SubBreedRepository,
    val webClient: WebClient
) {


    /**
     * Find all items
     * @return Find all items with the related objects loaded
     */
    fun findAll(): Flux<Breed> =
        breedsRepository
            .findAll()
            .flatMap(this::loadRelations)

    @FlowPreview
    suspend fun findAllCoroutine(): List<Breed> {
        val breeds = breedsRepository
            .findAllCoroutine()

        breeds.forEach { it -> loadRelationsCoroutine(it) }
        return breeds
    }


    fun findById(id: Int): Mono<Breed> =
        breedsRepository.findById(id)
            .flatMap(this::loadRelations)


    suspend fun findByIdCoroutine(id: Int): Breed {
        val breed = breedsRepository.findByIdCoroutine(id)
        loadRelationsCoroutine(breed)

        return breed
    }

    /**
     * Load the objects related to an item
     * @param item Item
     * @return The items with the loaded related objects (sub breeds)
     */
    private fun loadRelations(item: Breed): Mono<Breed> {
        return Mono.just<Breed>(item).zipWith(subBreedRepository.findByBreedId(item.id)!!.collectList())
            .map { result ->
                result.t1.subBreed = result.t2
                result.t1
            }
    }

    // coroutine version
    suspend fun loadRelationsCoroutine(item: Breed): Breed {
        val breed = subBreedRepository.findByBreedIdCoroutine(item.id)
        item.subBreed = breed
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


//    suspend fun retrieveImage(breed: Breed): Image = retrieveImage(breed.name)

    suspend fun retrieveImage(breedType: String?): Image {
        return webClient
            .get()
            .uri("https://dog.ceo/api/breed/${breedType}/images/random")
            .retrieve()
            .awaitBody<Image>()
    }

}