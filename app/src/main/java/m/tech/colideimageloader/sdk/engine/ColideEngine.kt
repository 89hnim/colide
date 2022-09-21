package m.tech.colideimageloader.sdk.engine

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.widget.ImageView
import kotlinx.coroutines.*
import m.tech.colideimageloader.sdk.request.ColideRequest
import java.lang.ref.WeakReference
import java.net.URL

/**
 * perform load image with [request]
 */
class ColideEngine private constructor(private val request: ColideRequest) {

    private val engineCoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var weakImageView: WeakReference<ImageView>? = null

    fun into(view: ImageView) {
        val url = request.url
        weakImageView = WeakReference(view)

        // setup placeholder
        loadPlaceholder()

        if (url.isNullOrEmpty()) {
            // fallback error image
            loadFallback()
        } else {
            // load image
            engineCoroutineScope.launch {
                delay(2000)
                val result = loadInternal(url)
                if (result != null) {
                    loadImage(result)
                } else {
                    // fallback error image
                    loadFallback()
                }
            }
        }
    }

    private fun loadFallback() {
        val option = request.option
        if (option != null) {
            val error = option.errorId ?: option.errorDrawable
            loadImage(error)
        }
    }

    private fun loadPlaceholder() {
        val option = request.option
        if (option != null) {
            val placeholder = option.placeholderId ?: option.placeholderDrawable
            loadImage(placeholder)
        }
    }

    private fun loadImage(source: Any?) {
        if (source == null) return
        when (source) {
            is Bitmap -> {
                weakImageView?.get()?.setImageBitmap(source)
            }
            is Int -> {
                weakImageView?.get()?.setImageResource(source)
            }
            is Drawable -> {
                weakImageView?.get()?.setImageDrawable(source)
            }
        }
    }

    private suspend fun loadInternal(imageUrl: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val url = URL(imageUrl)
                val conn = url.openConnection()
                val inputStream = conn.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                bitmap
            }.getOrElse {
                Log.e("DSK", "loadImage: $it")
                null
            }
        }
    }

    companion object {
        fun from(request: ColideRequest) = ColideEngine(request)
    }

}