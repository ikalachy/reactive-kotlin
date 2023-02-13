package com.example.reactivekotlin

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table

@Table
@Schema(description = "Model for a dealer's view of a car.")
data class Breed(
    var name: String?, @Id var id: Int = 0
) {
    @Transient
    var subBreed: List<String?> = listOf()
    var image: String = ""
}

@Table("sub_breed")
data class SubBreed(
    var name: String?,
    var breedId: Int?,
    @Id var id: Int = 0,
)

