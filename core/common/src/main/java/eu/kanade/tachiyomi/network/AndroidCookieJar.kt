package eu.kanade.tachiyomi.network

import android.webkit.CookieManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class AndroidCookieJar : CookieJar {

    private val manager = CookieManager.getInstance()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val urlString = url.toString()

        cookies.forEach { manager.setCookie(urlString, it.toString()) }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return get(url)
    }

    fun get(url: HttpUrl): List<Cookie> {
        val cookies = manager.getCookie(url.toString())

        return if (cookies != null && cookies.isNotEmpty()) {
            cookies.split(";").mapNotNull { Cookie.parse(url, it) }
        } else {
            emptyList()
        }
    }

    fun remove(url: HttpUrl, cookieNames: List<String>? = null, maxAge: Int = -1): Int {
        val urlString = url.toString()
        val cookies = manager.getCookie(urlString) ?: return 0

        fun List<String>.filterNames(): List<String> {
            return if (cookieNames != null) {
                this.filter { it in cookieNames }
            } else {
                this
            }
        }

        return cookies.split(";")
            .map { it.substringBefore("=") }
            .filterNames()
            .onEach { manager.setCookie(urlString, "$it=;Max-Age=$maxAge") }
            .count()
    }

    fun removeAll() {
        manager.removeAllCookies {}
    }

    // TLMR -->
    fun addAll(url: HttpUrl, cookies: List<Cookie>) {
        val urlString = url.toString()

        // Get existing cookies for the URL
        val existingCookies = manager.getCookie(urlString)?.split("; ")?.associate {
            val (name, value) = it.split('=', limit = 2)
            name to value
        }?.toMutableMap() ?: mutableMapOf()

        // Add or update the cookies
        cookies.forEach { newCookie ->
            existingCookies[newCookie.name] = newCookie.value
        }

        // Convert the map back to a string and set it in the cookie manager
        val finalCookiesString = existingCookies.entries.joinToString("; ") { "${it.key}=${it.value}" }
        manager.setCookie(urlString, finalCookiesString)
    }
    // <-- TLMR
}
