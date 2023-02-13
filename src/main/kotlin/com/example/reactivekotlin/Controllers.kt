package com.example.reactivekotlin

import com.example.reactivekotlin.services.BreedsService
import io.netty.handler.codec.http2.Http2Exception
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/breeds")
class BreedsController(
    private val breedsService: BreedsService,
) {

    @Operation(summary = "Retrieves all breeds", description = "Returns 200 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
        ]
    )
    @OptIn(FlowPreview::class)
    @GetMapping("/")
    suspend fun findAll(): Flow<Breed> =
        breedsService.findAllCoroutine()

    @Operation(
        summary = "Retrieves breed by id",
        description = "Returns 200 if successful",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
            ApiResponse(responseCode = "404", description = "Such a breed does not exist"),
        ]
    )
    @GetMapping("/{id}")
    suspend fun findOne(
        @Parameter(description = "Breed id", example = "1")
        @PathVariable id: Int
    ) = breedsService.findByIdCoroutine(id)


    @Operation(
        summary = "Retrieves breed image", description = "Returns 200 if successful",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
            ApiResponse(responseCode = "404", description = "Such a breed does not exist"),
        ]
    )
    @GetMapping("/{breed}/images")
    suspend fun findImage(
        @Parameter(description = "Breed name", example = "terrier")
        @PathVariable breed: String
    ) = breedsService.findAndSaveImage(breed)


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
