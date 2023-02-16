package com.example.reactivekotlin

import com.example.reactivekotlin.api.BaseController
import com.example.reactivekotlin.services.BreedsService
import io.netty.handler.codec.http2.Http2Exception
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/breeds")
class BreedsController(
    val breedsService: BreedsService,
) : BaseController {

    @OptIn(FlowPreview::class)
    @GetMapping("/")
    override suspend fun findAll(): Flow<Breed> =
        breedsService.findAllCoroutine()

    @GetMapping("/{id}")
    override suspend fun findOne(
        @PathVariable id: Int
    ): Breed = breedsService.findByIdCoroutine(id)

    @GetMapping("/{breed}/images")
    override suspend fun findImage(
        @PathVariable breed: String
    ): Image = breedsService.findAndSaveImage(breed)


    @ExceptionHandler(Exception::class)
    fun exceptionBreedsHandler(exception: Exception): ResponseEntity<Error> {
        when (exception) {
            is Http2Exception -> {
                when (exception.error().code()) {
                    400L -> respondWithError(400, Error400(message = "Breed could not be found"))
                    404L -> respondWithError(404, Error404(message = "Breed could not be found"))
                    500L -> respondWithError(500, Error500(message = "Internal server error"))
                }
            }
        }
        return respondWithError(400, Error400(message = "Other error"))
    }

    private fun respondWithError(code: Int, error: Error): ResponseEntity<Error> {
        return ResponseEntity.status(code)
            .body(error)
    }

}
