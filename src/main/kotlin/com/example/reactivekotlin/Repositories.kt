package com.example.reactivekotlin

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux


@Repository
interface BreedRepository : CoroutineCrudRepository<Breed, Int> {

    @Query("select * from breed b order by b.name")
    suspend fun findAllCoroutine(): Flow<Breed>

    @Query("select * from breed b  where b.id = :id order by b.name")
    suspend fun findByIdCoroutine(id: Int): Breed

    @Query("select * from breed b  where b.name = :name order by b.name")
    suspend fun findByName(name: String): Breed

    @Query("update breed b set b.image = :imageUrl where b.id = :id")
    suspend fun updateWithImageUrl(id: Int, imageUrl: String)

}

@Repository
interface SubBreedRepository : CoroutineCrudRepository<SubBreed, Int> {

    @Query("select name from sub_breed sb where sb.breed_id = :breedId order by sb.name")
    fun findByBreedId(breedId: Int?): Flow<String?>?

    @Query("select name from sub_breed sb where sb.breed_id = :breedId order by sb.name")
    suspend fun findByBreedIdCoroutine(breedId: Int?): List<String>


}

