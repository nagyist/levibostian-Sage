package earth.levi.sage.android.view


import android.widget.LinearLayout
import android.content.Context
import android.util.AttributeSet
import android.annotation.TargetApi
import android.os.Build.VERSION_CODES
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import earth.levi.sage.android.R

class CTAButtonView: CardView {

    private lateinit var text: TextView
    private lateinit var button: Button

    var description: String = ""
        set(value) {
            field = value
            this.text.text = value
        }

    var buttonText: String? = null
        set(value) {
            field = value

            if (value == null) {
                this.button.visibility = View.GONE
            } else {
                this.button.visibility = View.VISIBLE

                this.button.text = value
            }
        }

    var onClick: (() -> Unit)? = null
        set(value) {
            field = value
            this.button.setOnClickListener { value?.invoke() }
        }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs, 0)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(context, attrs, defStyleAttr)
    }

    private fun initialize(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.view_ctabutton, this, true)

        radius = 80.toFloat()

        //setBackgroundColor(android.R.color.transparent)

        //layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setPadding(10, 10, 10, 10)

        text = findViewById(R.id.text)
        button = findViewById(R.id.button)
    }

}