package io.vladyslavvua.shorekeeper.jcef

import kotlinx.serialization.json.Json
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.browser.CefMessageRouter
import org.cef.callback.CefQueryCallback
import org.cef.handler.CefMessageRouterHandlerAdapter

class BasicRouterHandler {
    val router: CefMessageRouter =
        CefMessageRouter.create(
            CefMessageRouter.CefMessageRouterConfig("cefQuery", "cefQueryCancel")
        )


    init {

        router.addHandler(object : CefMessageRouterHandlerAdapter() {
            override fun onQuery(
                browser: CefBrowser,
                frame: CefFrame,
                queryId: Long,
                request: String,
                persistent: Boolean,
                callback: CefQueryCallback
            ): Boolean {
                val mappedRequest: JsToKotlinRequest

                try {
                    mappedRequest = Json.decodeFromString<JsToKotlinRequest>(request)
                } catch (e: Exception) {
                    callback.failure(1, "Invalid request")
                    return false
                }

                val response = handleIntent(mappedRequest)
                if (response.success) {
                    callback.success(Json.encodeToString(response))
                    return true
                } else if (response.reason == KotlinToJsResponse.FailureReason.METHOD_NOT_FOUND) {
                    return false
                } else {
                    callback.failure(0, "Invalid request")
                }

                return false
            }

            override fun onQueryCanceled(browser: CefBrowser, frame: CefFrame, queryId: Long) {
                // тут можна скасувати довгу операцію, якщо треба
            }
        }, true)
    }


    private fun handleIntent(request: JsToKotlinRequest): KotlinToJsResponse =
        when (request.method) {

            else -> KotlinToJsResponse(false, reason = KotlinToJsResponse.FailureReason.METHOD_NOT_FOUND)


        }
}