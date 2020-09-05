package com.bebetterprogrammer.trecox.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.adapter.SlideAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity(), OnPageChangeListener {

    private lateinit var slideAdapter: SlideAdapter
    private lateinit var mDots: Array<TextView?>
    private var currentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        FirebaseAuth.getInstance().currentUser?.let {
            Log.i(
                "OnBoardingActivity",
                "User is already logged in with name - ${FirebaseAuth.getInstance().currentUser?.displayName}!"
            )
            val i = Intent(this, HomePageActivity::class.java)
            startActivity(i)
            finish()
        }

        slideAdapter = SlideAdapter(this)
        view_page.adapter = slideAdapter
        addDotIndicator(0)
        view_page.addOnPageChangeListener(this)

        btn_next.setOnClickListener {
            if (currentPage == 2) {
                openActivity()
            } else {
                view_page.currentItem = currentPage + 1
            }
        }

        btn_skip.setOnClickListener {
            openActivity()
        }
    }

    private fun openActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun addDotIndicator(position: Int) {
        mDots = arrayOfNulls(3)

        dot_layout.removeAllViews()

        for (i in mDots.indices) {
            mDots[i] = TextView(this)
            mDots[i]?.text = Html.fromHtml("&#8226;")
            mDots[i]?.textSize = 35f
            mDots[position]?.setTextColor(Color.BLACK)

            dot_layout.addView(mDots[i])
        }

        if (mDots.isNotEmpty()) {
            mDots[position]?.setTextColor(resources.getColor(R.color.colorPrimaryDark))
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        addDotIndicator(position)
        currentPage = position
    }
}
