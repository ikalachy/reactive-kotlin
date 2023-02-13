package com.example.reactivekotlin.config

import com.example.reactivekotlin.services.BreedsService
import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class WebConfig {

    @Bean
    fun webClient() = WebClient.builder().baseUrl("http://localhost:8080").build()

//    @FlowPreview
//    @Bean
//    fun productRoutes(breedsService: BreedsService) = coRouter {
//        GET("/", breedsService.findAllCoroutine())
////        GET("/{id}", productsHandler::findOne)
////        GET("/{id}/stock", productsHandler::findOneInStock)
//    }
}