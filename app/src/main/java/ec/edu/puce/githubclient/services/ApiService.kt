package ec.edu.puce.githubclient.services

import ec.edu.puce.githubclient.models.Repository
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(value = "user/repos")
    suspend fun getRepositories(
        @Query(value= "sort")sort: String= "created",
        @Query(value="order")order: String="desc",
        @Query(value="affiliation")affiliation: String= "owner",
        @Query(value="per_page")perPage: Int = 100,
        @Query(value="t")t: String="${System.currentTimeMillis()}",
    ) : List<Repository>
}