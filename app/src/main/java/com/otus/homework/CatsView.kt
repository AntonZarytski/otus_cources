package com.otus.homework

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
    }

    override fun connectionError(message: String?) {
        launchInMainThread {
            Toast.makeText(context, message, LENGTH_SHORT).show()
        }
    }

    override fun setImage(imageUrl: String) {
        val imageView = findViewById<ImageView>(R.id.image)
        Picasso.get().load(imageUrl).into(imageView);
    }
}

interface ICatsView {

    fun populate(fact: Fact)
    fun connectionError(message: String?)
    fun setImage(imageUrl: String)
}