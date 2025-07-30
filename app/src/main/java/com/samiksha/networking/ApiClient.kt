package com.samiksha.networking


import com.samiksha.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {


    private var retrofit: Retrofit? = null

    @JvmStatic
    val client: NetworkService
        get() {
            if (retrofit == null) {


                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY

                val httpClient = OkHttpClient.Builder()
                httpClient.addInterceptor(ConnectivityInterceptor())
                httpClient.connectTimeout(100, TimeUnit.SECONDS)
                httpClient.readTimeout(30, TimeUnit.SECONDS)
                httpClient.writeTimeout(30, TimeUnit.SECONDS)
                httpClient.callTimeout(100,TimeUnit.MINUTES)

                httpClient.addInterceptor(logging)







                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .client(httpClient.build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(NetworkService::class.java)
        }


}