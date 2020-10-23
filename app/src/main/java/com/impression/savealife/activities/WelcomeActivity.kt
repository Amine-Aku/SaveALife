package com.impression.savealife.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.impression.savealife.R

class WelcomeActivity : AppCompatActivity() {

    private val REQUEST_EXIT_CODE = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        findViewById<Button>(R.id.welcome_login_btn).setOnClickListener {
            startActivityForResult(Intent(this, LoginActivity::class.java), REQUEST_EXIT_CODE)
        }

        findViewById<Button>(R.id.welcome_home_btn).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EXIT_CODE) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
