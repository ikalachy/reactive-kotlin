package com.example.reactivekotlin

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux


@Repository
interface BreedRepository : ReactiveCrudRepository<Breed, Int> {

    @Query("select * from breed b join sub_breed sb on b.id = sb.breed_id where sb.breed_id = :itemId order by b.name")
    fun findWithSubBreedsByBreedId(itemId: Int?): Flux<Breed?>?

    @Query("select * from breed b order by b.name")
    suspend fun findAllCoroutine(): List<Breed>

    @Query("select * from breed b  where b.id = :id order by b.name")
    suspend fun findByIdCoroutine(id: Int): Breed

    @Query("select * from breed b  where b.name = :name order by b.name")
    suspend fun findByName(name: String): Breed

    @Query("update breed b set b.image = :imageUrl where b.id = :id")
    suspend fun updateWithImageUrl(id: Int, imageUrl: String): Breed


}

@Repository
interface SubBreedRepository : ReactiveCrudRepository<SubBreed, Int> {

    @Query("select name from sub_breed sb where sb.breed_id = :breedId order by sb.name")
    fun findByBreedId(breedId: Int?): Flux<String?>?

    @Query("select name from sub_breed sb where sb.breed_id = :breedId order by sb.name")
    suspend fun findByBreedIdCoroutine(breedId: Int?): List<String>


}

