package com.example.derek.trademeapi.api.interceptors

import com.example.derek.trademeapi.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URLEncoder
import java.util.*

/**
 * Created by derek on 30/04/18.
 */

/**
 * Custom header, use @HEADERS("@: NoAuth") to disable authentication header for the request
 * */
class AuthenticationHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response? {
        chain?.let {
            val original = chain.request()
            val customAnnotations = original.headers().values("@")
            var request : Request
            if (customAnnotations.contains("NoAuth")) {
                request = original.newBuilder().removeHeader("@").build()
            } else {
                val timeStamp = Math.round((Date()).time / 1000.0)
                request = original.newBuilder()
                        .header("Authorization",
                                "OAuth oauth_consumer_key=\"${URLEncoder.encode(BuildConfig.CONSUMER_KEY, "utf-8")}\", " +
                                        "oauth_signature=\"${URLEncoder.encode(BuildConfig.CONSUMER_SECRET + '&', "utf-8")}\", oauth_signature_method=\"PLAINTEXT\", " +
                                        "oauth_version=\"1.0\", oauth_timestamp=\"$timeStamp\"")
                        .method(original.method(), original.body())
                        .build()
            }

            return chain.proceed(request)
        }
        return null
    }
}