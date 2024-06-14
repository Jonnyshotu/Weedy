import com.example.weedy.data.remote.StrainResponse
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.otreeba.com/"

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val client = OkHttpClient.Builder().addInterceptor { chain ->
    val newRequest = chain.request().newBuilder()
        .build()
    chain.proceed(newRequest)
}.addInterceptor(loggingInterceptor)
    .build()

val moshi = Moshi.Builder()
    .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
    .build()

val retrofitApi = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()


interface StrainApiService {

    @GET("v1/strains")
    suspend fun getRemoteStrainResponse(
        @Query("page") page: Int,
        @Query("count") count: Int = 50,
        @Query("sort") sort: String = "name"
    ): StrainResponse
}


object StrainApi {
    val apiService: StrainApiService by lazy { retrofitApi.create(StrainApiService::class.java) }
}