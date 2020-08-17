package com.impression.savealife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        title = resources.getString(R.string.history)
    }
}
