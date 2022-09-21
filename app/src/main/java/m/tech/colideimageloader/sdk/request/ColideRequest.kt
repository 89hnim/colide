package m.tech.colideimageloader.sdk.request

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import m.tech.colideimageloader.sdk.ColideLifecycleObserver
import m.tech.colideimageloader.sdk.engine.ColideEngine

/**
 * store all information, request options: animation, transformation
 */
class ColideRequest private constructor(owner: LifecycleOwner) {

    internal var url: String? = null
    internal var option: Option? = null
    internal var engine: ColideEngine? = null

    init {
        owner.lifecycle.addObserver(object : ColideLifecycleObserver() {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
            }
        })
    }

    fun requestOption(option: Option) = apply { this.option = option }

    fun load(url: String): ColideEngine {
        this.url = url
        val engine = ColideEngine.from(this).also {
            this.engine = it
        }
        return engine
    }

    /**
     * request option class
     */
    class Option private constructor(
        val placeholderDrawable: Drawable?,
        val placeholderId: Int?,
        val errorDrawable: Drawable?,
        val errorId: Int?,
    ) {

        class Builder {
            private var placeholderDrawable: Drawable? = null
            private var placeholderId: Int? = null
            private var errorDrawable: Drawable? = null
            private var errorId: Int? = null

            fun placeholder(drawable: Drawable) = apply { placeholderDrawable = drawable }
            fun placeholder(resourceId: Int) = apply { placeholderId = resourceId }
            fun error(drawable: Drawable) = apply { errorDrawable = drawable }
            fun error(resourceId: Int) = apply { errorId = resourceId }

            fun build() = Option(
                placeholderDrawable = placeholderDrawable,
                placeholderId = placeholderId,
                errorDrawable = errorDrawable,
                errorId = errorId
            )
        }

    }

    companion object {
        internal fun create(owner: LifecycleOwner) = ColideRequest(owner)
    }
}