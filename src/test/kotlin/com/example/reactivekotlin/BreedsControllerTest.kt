package com.example.reactivekotlin

import com.example.reactivekotlin.config.WebConfig
import com.example.reactivekotlin.services.BreedsService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.web.reactive.server.WebTestClient


@WebFluxTest(BreedsController::class)
@Import(WebConfig::class, BreedsController::class, BreedsService::class)
class BreedsControllerTest(@Autowired private val webClient: WebTestClient) {

    @MockkBean
    lateinit var breedsRepository: BreedRepository

    @MockkBean
    lateinit var subBreedRepository: SubBreedRepository

    @BeforeEach
    fun setUp() {
        coEvery {
            breedsRepository.findAllCoroutine()
        } returns flowOf(aBreed(), anotherBreed())
        coEvery {
            subBreedRepository.findByBreedIdCoroutine(1)
        } returns listOf<String>("subbreed1.1", "subbreed1.2")
        coEvery {
            subBreedRepository.findByBreedIdCoroutine(2)
        } returns listOf<String>("subbreed2.1", "subbreed2.2")
    }

    @Test
    fun `Retrieve all breeds`() {
        this.webClient
            .get()
            .uri("/api/breeds/")
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(Breed::class.java)
            .hasSize(2)
            .contains(aBreed(), anotherBreed())
    }


    @Test
    fun `Retrieve breed by Id`() {
        // given
        coEvery { breedsRepository.findByIdCoroutine(1) } returns aBreed()

        this.webClient
            .get()
            .uri("/api/breeds/1")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Breed::class.java)
            .isEqualTo(aBreed())

    }

    @Test
    fun `Retrieve image for existing breed without image`() {
        // given
        coEvery { breedsRepository.findByName("retriever") } returns aBreed()
        coEvery { breedsRepository.updateWithImageUrl(1, any()) } returns Unit

        this.webClient
            .get()
            .uri("/api/breeds/retriever/images")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Image::class.java)
    }

    @Test
    fun `Retrieve image for existing breed with image`() {
        // given
        coEvery { breedsRepository.findByName("breed3") } returns aBreedWithImage()

        this.webClient
            .get()
            .uri("/api/breeds/breed3/images")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Image::class.java)
    }

    @Test
    fun `Retrieve image for nonexistent breed`() {
        // given
        coEvery { breedsRepository.findByName("breed1") } throws EmptyResultDataAccessException(1)

        this.webClient
            .get()
            .uri("/api/breeds/breed1/images")
            .exchange()
            .expectStatus()
            .is4xxClientError
            .expectBody(Error404::class.java)
    }

    private fun aBreed(): Breed {
        val breed = Breed(id = 1, name = "breed1")
        breed.subBreed = listOf<String>("subbreed1.1", "subbreed1.2")
        return breed
    }

    private fun anotherBreed(): Breed {
        val breed = Breed(id = 2, name = "breed2")
        breed.subBreed = listOf<String>("subbreed2.1", "subbreed2.2")
        return breed
    }

    private fun aBreedWithImage(): Breed {
        val breed = Breed(id = 3, name = "breed1")
        breed.image = "http://someimage"
        return breed
    }
}