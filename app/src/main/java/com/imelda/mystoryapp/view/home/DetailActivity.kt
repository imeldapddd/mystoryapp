package com.imelda.mystoryapp.view.home

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.imelda.mystoryapp.R

class DetailActivity : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var imageDetail: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_story)

        titleTextView = findViewById(R.id.teksDetail)
        descriptionTextView = findViewById(R.id.descriptionDetail)
        imageDetail = findViewById(R.id.gambarDetail)

        val storyName = intent.getStringExtra("STORY_NAME")
        val storyDescription = intent.getStringExtra("STORY_DESCRIPTION")
        val storyPhotoUrl = intent.getStringExtra("STORY_PHOTO_URL")

        titleTextView.text = storyName ?: "No title"
        descriptionTextView.text = storyDescription ?: "No description"

        Glide.with(this)
            .load(storyPhotoUrl)
            .placeholder(R.drawable.ic_image)
            .into(imageDetail)
    }
}