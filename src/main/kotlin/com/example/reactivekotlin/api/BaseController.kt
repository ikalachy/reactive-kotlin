package com.example.reactivekotlin.api

import com.example.reactivekotlin.Breed
import com.example.reactivekotlin.Image
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kotlinx.coroutines.flow.Flow

interface BaseController {
    @Operation(summary = "Retrieves all breeds", description = "Returns 200 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
        ]
    )
    suspend fun findAll(): Flow<Breed>

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
    suspend fun findOne(
        @Parameter(description = "Breed id", example = "1")
        id: Int
    ): Breed


    @Operation(
        summary = "Retrieves breed image", description = "Returns 200 if successful",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
            ApiResponse(responseCode = "404", description = "Such a breed does not exist"),
        ]
    )
    suspend fun findImage(
        @Parameter(description = "Breed name", example = "terrier")
        breed: String
    ): Image
}