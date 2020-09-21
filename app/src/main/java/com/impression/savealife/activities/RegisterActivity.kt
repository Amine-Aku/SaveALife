package com.impression.savealife.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.impression.savealife.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        title = resources.getString(R.string.register)
    }
}
