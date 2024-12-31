package com.example.cashiq


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton

class OnboardingActivity: AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorsLayout: LinearLayout
    private lateinit var signUpButton: MaterialButton
    private lateinit var loginButton: MaterialButton
    private lateinit var skipButton: TextView

    private val onboardingSlides = listOf(
        OnboardingSlide(
            R.drawable.ic_money_control,
            "Gain total control\nof your money",
            "Become your own money manager\nand make every cent count"
        ),
        OnboardingSlide(
            R.drawable.ic_track_money,
            "Know where your\nmoney goes",
            "Track your transaction easily,\nwith categories and financial report"
        ),
        OnboardingSlide(
            R.drawable.ic_planning,
            "Planning ahead",
            "Setup your budget for each category\nso you in control"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        initViews()
        setupViewPager()
        setupIndicators()


//Explicit Intent to Signup Page
        signUpButton.setOnClickListener {
            val intent = Intent(this@OnboardingActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }



//Explicit Intent to Login Page
        loginButton.setOnClickListener {
            val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)
        indicatorsLayout = findViewById(R.id.indicatorsLayout)
        signUpButton = findViewById(R.id.signUpButton)
        loginButton = findViewById(R.id.loginButton)
    }

    private fun setupViewPager() {
        viewPager.adapter = OnboardingAdapter(onboardingSlides)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(position)
            }
        })

        // Add animation transformer
        viewPager.setPageTransformer { page, position ->
            page.apply {
                val r = 1 - kotlin.math.abs(position)
                page.alpha = 0.5f + (r * 0.5f)
                page.scaleY = 0.85f + (r * 0.15f)
            }
        }
    }

    private fun setupIndicators() {
        val indicators = Array(onboardingSlides.size) { ImageView(this) }
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 0, 8, 0)
        }

        indicators.forEach { imageView ->
            imageView.setImageResource(R.drawable.indicator_inactive)
            imageView.layoutParams = layoutParams
            indicatorsLayout.addView(imageView)
        }
        updateIndicators(0)
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until indicatorsLayout.childCount) {
            val imageView = indicatorsLayout.getChildAt(i) as ImageView
            imageView.setImageResource(
                if (i == position) R.drawable.indicator_active
                else R.drawable.indicator_inactive
            )
        }
    }


    private fun navigateToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}

    data class OnboardingSlide(
        val image: Int,
        val title: String,
        val description: String
    )

    class OnboardingAdapter(private val slides: List<OnboardingSlide>) :
        RecyclerView.Adapter<OnboardingAdapter.SlideViewHolder>() {

        inner class SlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val imageView = view.findViewById<ImageView>(R.id.slideImage)
            private val titleView = view.findViewById<TextView>(R.id.slideTitle)
            private val descriptionView = view.findViewById<TextView>(R.id.slideDescription)

            fun bind(slide: OnboardingSlide) {
                imageView.setImageResource(slide.image)
                titleView.text = slide.title
                descriptionView.text = slide.description
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
            return SlideViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.slide_onboarding,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
            holder.bind(slides[position])
        }

        override fun getItemCount() = slides.size
    }





