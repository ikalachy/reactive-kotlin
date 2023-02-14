package com.example.reactivekotlin

import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.ErrorResponseException
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Component
@Order(-2)
class BaseControllerAdvice(
    errorAttributes: ErrorAttributes,
    resources: WebProperties.Resources,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, resources, applicationContext) {

    init {
        setMessageWriters(serverCodecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?) =
        router {
            RequestPredicates.all().invoke(this@BaseControllerAdvice::renderErrorResponse)
        }
    //RouterFunctions.route(RequestPredicates.all(),this::renderErrorResponse)

    fun renderErrorResponse(
        request: ServerRequest
    ): Mono<ServerResponse> {
        val errorPropertiesMap = getErrorAttributes(
            request,
            ErrorAttributeOptions.defaults()
        )
        val ex: ErrorResponseException = getError(request) as ErrorResponseException
        return ServerResponse.status(ex.statusCode.value())
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(ErrorResponseVO(ex.detailMessageCode, ex.message)))
    }

    data class ErrorResponseVO(val errorMessage: String, val errorCode: String)

}

@Configuration
class ResourceWebPropertiesConfig {
    @Bean
    fun resources(): WebProperties.Resources {
        return WebProperties.Resources()
    }
}