package pt.orpheu.readyservice.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pt.orpheu.readyservice.BuildConfig
import pt.orpheu.readyservice.ReadyServiceApp
import pt.orpheu.readyservice.api.ApiService
import pt.orpheu.readyservice.api.interceptor.CacheInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {

    private companion object {
        const val CONNECTION_TIMEOUT = 30L
        const val READ_TIMEOUT = 10L
        const val WRITE_TIMEOUT = 5L
        const val CACHE_SIZE = 40L * 1024 * 1024 // 40 MiB
        const val CACHE_MAX_AGE_MIN = 15
    }


    @Provides
    @Singleton
    fun provideCache(app: Application): Cache {
        return Cache(app.cacheDir, CACHE_SIZE)
    }



    @Singleton
    @Provides
    fun provideOkHttpClient(cache: Cache) : OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor( HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC})
            .addNetworkInterceptor(CacheInterceptor(CACHE_MAX_AGE_MIN))
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .cache(cache)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

}