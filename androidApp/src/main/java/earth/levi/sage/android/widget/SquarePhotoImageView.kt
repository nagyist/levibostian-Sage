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

class SquarePhotoImageView: PhotoImageView {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize()
    }

    override fun initialize() {
        scaleType = ScaleType.CENTER_CROP
        adjustViewBounds = true

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams = ViewGroup.LayoutParams(width, height)
    }

    override fun buildOnPicassoRequestBuilder(requestBuilder: RequestCreator): RequestCreator {
        return requestBuilder
            .centerCrop()
    }

}