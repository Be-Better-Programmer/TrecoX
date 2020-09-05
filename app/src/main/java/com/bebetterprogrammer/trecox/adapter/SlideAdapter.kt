package com.bebetterprogrammer.trecox.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bebetterprogrammer.trecox.R
import kotlinx.android.synthetic.main.slide_layout.view.*

class SlideAdapter(private val context: Context) : PagerAdapter() {

    var slideImage = arrayOf(
        R.color.orange,
        R.color.colorPrimary,
        R.color.green
    )

    var slideTitle = arrayOf(
        "Title 1",
        "Title 2",
        "Title 3"
    )

    var slideDescription = arrayOf(
        "Description 1",
        "Description 2",
        "Description 3"
    )

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.slide_layout, container, false)

        view.vp_image.setImageResource(slideImage[position])
        view.vp_title.text = slideTitle[position]
        view.vp_description.text = slideDescription[position]

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as ConstraintLayout?)
    }

    override fun getCount(): Int = slideTitle.size
}
