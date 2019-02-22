package pt.orpheu.readyservice.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.CacheControl
import java.util.concurrent.TimeUnit

class CacheInterceptor(private val maxAgeMin: Int) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        // re-write response header to force use of cache
        val cacheControl = CacheControl.Builder()
                .maxAge(2, TimeUnit.MINUTES)
                .build()

        return response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
    }
}