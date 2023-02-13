package com.example.reactivekotlin.config

import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator


@Configuration
@EnableR2dbcRepositories
class DatabaseConfig { // : AbstractR2dbcConfiguration()
    //    @Value("\${spring.datasource.username}")
    private val userName: String = "admin"

    //    @Value("\${spring.datasource.password}")
    private val password: String = "admin"


//    @Bean
//    fun initializer(connectionFactory: ConnectionFactory?): ConnectionFactoryInitializer? {
//        val initializer = ConnectionFactoryInitializer()
//        initializer.setConnectionFactory(connectionFactory!!)
//        initializer.setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
//        return initializer
//    }


//    @Bean
//    fun connectionFactory(): ConnectionFactory {
//        println("H1 init!!!!")
//        return H2ConnectionFactory(
//            H2ConnectionConfiguration.builder()
//                .inMemory("breeds")
//                .username(userName)
//                .password(password)
//                .build()
//        )
//    }

//    @Bean
//    fun connectionFactory(): ConnectionFactory {
//        println("H1 init!!!!")
//        return ConnectionFactoryBuilder
//            .derivedFrom(
//                H2ConnectionFactory(
//                    H2ConnectionConfiguration.builder()
//                        .inMemory("breedsdb")
//                        .username("sa")
//                        .password("sa")
//                        .build()
//                )
//            ).build()
//
//    }
}

