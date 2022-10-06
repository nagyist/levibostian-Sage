package earth.levi.sage.android.widget

import android.content.Context
import android.util.AttributeSet
import android.net.Uri
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import earth.levi.sage.android.R
import earth.levi.sage.type.Photo
import java.io.File

open class PhotoImageView: AppCompatImageView {

    var shouldAnimateFromPlaceholder: Boolean = true

    var photo: Photo? = null
        set(value) {
            field = value
            val photo = field ?: return

            val picassoInstance = Picasso.get()

            val requestBuilder = when {
                photo.isLocal -> picassoInstance.load(Uri.fromFile(File(photo.localPhotoAsset!!.path)))
                photo.isRemote -> picassoInstance.load(photo.remotePhotoUrl!!)
                else -> throw RuntimeException("should not happen")
            }

            if (!shouldAnimateFromPlaceholder) requestBuilder.noFade()

            buildOnPicassoRequestBuilder(
                requestBuilder
                    .placeholder(R.color.remote_image_placeholder)
                    .error(R.color.remote_image_placeholder)
                    .fit()
            ).into(this)
        }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize()
    }

    open fun initialize() {
        scaleType = ScaleType.FIT_CENTER
        adjustViewBounds = true

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams = ViewGroup.LayoutParams(width, height)
    }

    open fun buildOnPicassoRequestBuilder(requestBuilder: RequestCreator): RequestCreator {
        return requestBuilder
            .centerInside()
    }

}