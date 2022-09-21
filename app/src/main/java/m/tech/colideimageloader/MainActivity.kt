package m.tech.colideimageloader

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import m.tech.colideimageloader.sdk.Colide
import m.tech.colideimageloader.sdk.request.ColideRequest
import java.net.URL

/**
 * https://blog.mindorks.com/how-the-android-image-loading-library-glide-and-fresco-works-962bc9d1cc40
 */
private const val IMAGE_URL =
    "https://fujifilm-x.com/wp-content/uploads/2019/08/xc16-50mmf35-56-ois-2_sample-images03.jpg"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.image)
        val tv = findViewById<TextView>(R.id.gc)
//        Glide.with(this).setDefaultRequestOptions(
//            RequestOptions()
//        ).setDefaultRequestOptions().setDefaultRequestOptions().load()
//
        Colide.with(this)
            .requestOption(
                ColideRequest.Option.Builder()
                    .placeholder(R.drawable.ic_baseline_downloading_24)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .build()
            )
            .load(IMAGE_URL)
            .into(imageView)

        imageView.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }

    }

    // step1: check if image in cache
    // step2.1: if has, load image from cache
    // step2.2: if not, load image into stream
    // step3: cache the stream with format [URL + ?]
    // step4: return bitmap
    // note: step 3 + step 4 do parallel
    // step5: create bitmap bool instead new bitmap every time download new bitmap
    // https://github.com/koush/UrlImageViewHelper/blob/master/UrlImageViewHelper/src/com/koushikdutta/urlimageviewhelper/UrlImageViewHelper.java
    // https://github.com/amitshekhariitbhu/GlideBitmapPool

    // todo: clear request
    private suspend fun loadImage(imageUrl: String): Bitmap? {
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

}