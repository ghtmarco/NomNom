package com.example.nomnom.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URLEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class OAuthInterceptor(
    private val consumerKey: String,
    private val consumerSecret: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val method = originalRequest.method

        val oauthParams = mutableMapOf(
            "oauth_consumer_key" to consumerKey,
            "oauth_nonce" to System.currentTimeMillis().toString(),
            "oauth_signature_method" to "HMAC-SHA1",
            "oauth_timestamp" to (System.currentTimeMillis() / 1000).toString(),
            "oauth_version" to "1.0"
        )

        val queryParams = mutableMapOf<String, String>()
        originalUrl.queryParameterNames.forEach { name ->
            queryParams[name] = originalUrl.queryParameter(name) ?: ""
        }

        val allParams = mutableMapOf<String, String>()
        allParams.putAll(oauthParams)
        allParams.putAll(queryParams)

        val baseString = buildBaseString(method, originalUrl.toString(), allParams)
        val signature = generateSignature(baseString, "$consumerSecret&")
        oauthParams["oauth_signature"] = signature

        val newUrl = originalUrl.newBuilder()
        oauthParams.forEach { (key, value) ->
            newUrl.addQueryParameter(key, value)
        }

        val newRequest = originalRequest.newBuilder()
            .url(newUrl.build())
            .build()

        return chain.proceed(newRequest)
    }

    private fun buildBaseString(method: String, url: String, params: Map<String, String>): String {
        val baseUrl = url.split("?")[0]
        val sortedParams = params.entries.sortedBy { it.key }
        val paramString = sortedParams.joinToString("&") { "${encode(it.key)}=${encode(it.value)}" }
        return "${method.uppercase()}&${encode(baseUrl)}&${encode(paramString)}"
    }

    private fun generateSignature(baseString: String, key: String): String {
        val keySpec = SecretKeySpec(key.toByteArray(), "HmacSHA1")
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(keySpec)
        val bytes = mac.doFinal(baseString.toByteArray())
        return android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
    }

    private fun encode(value: String): String {
        return URLEncoder.encode(value, "UTF-8")
            .replace("+", "%20")
            .replace("*", "%2A")
            .replace("%7E", "~")
    }
}
