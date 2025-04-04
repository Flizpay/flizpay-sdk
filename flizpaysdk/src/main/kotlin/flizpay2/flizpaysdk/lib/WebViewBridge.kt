package flizpay2.flizpaysdk.lib

import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class WebViewBridge(private val webView: WebView, private val activity: AppCompatActivity) {

    init {
        webView.addJavascriptInterface(this, "AndroidBridge")
    }

    fun overrideWindowClose() {
        webView.post {
            webView.evaluateJavascript("""
                (function() {
                    window.close = function() {
                        AndroidBridge.closeWebView();
                    };
                })();
            """.trimIndent(), null)
        }
    }

    @JavascriptInterface
    fun closeWebView() {
        activity.finish()
    }
}
