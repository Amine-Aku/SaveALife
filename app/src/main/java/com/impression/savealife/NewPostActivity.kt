package com.impression.savealife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        title = resources.getString(R.string.new_post)
    }
}
