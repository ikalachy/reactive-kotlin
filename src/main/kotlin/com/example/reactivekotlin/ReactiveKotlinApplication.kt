package com.example.reactivekotlin

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication



@SpringBootApplication
class ReactiveKotlinApplication

fun main(args: Array<String>) {
    runApplication<ReactiveKotlinApplication>(*args)

}
//
//@Bean
//fun breedsOpenApi(appVersion: String): GroupedOpenApi {
//    val paths = listOf<String>("/api/breeds")
//    return GroupedOpenApi.builder().group("breeds")
//        .addOpenApiCustomiser(OpenApiCustomiser { openApi ->
//            openApi.info(
//                Info().title("Breeds API").version(appVersion)
//            )
//        })
//        .pathsToMatch("/api/breeds/**")
//        .build()
//}