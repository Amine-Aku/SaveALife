package com.impression.savealife

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.impression.savealife.api.PostsAPI
import com.impression.savealife.models.Appuser
import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class NewPostActivity : AppCompatActivity() {



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        title = resources.getString(R.string.new_post)

//        Initializations
        val postBtn: Button = findViewById(R.id.post_btn)

        val patientNameField: EditText = findViewById(R.id.post_input_name)
        val hospitalField: EditText = findViewById(R.id.post_input_hospital)
        val centerDonationField: EditText = findViewById(R.id.post_input_donation_center)
        val bloodTypeField: EditText = findViewById(R.id.post_input_bloodType)
        val detailsField: EditText = findViewById(R.id.post_input_details)



        val retrofit = Retrofit.Builder()
            .baseUrl("https://save-a-life-web-server.herokuapp.com/home/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        postBtn.setOnClickListener {
//            Fetsh data from the Inputs fields
            val patientName: String = patientNameField.text.toString().trim()
            val date = LocalDateTime.now().toString()
            val hospital = hospitalField.text.toString().trim()
            val donationCenter = centerDonationField.text.toString().trim()
            val bloodType = bloodTypeField.text.toString().trim()
            val details: String = detailsField.text.toString().trim()

            val newPost = Post("", patientName, date, hospital,donationCenter,bloodType, details)
            val postsAPI = retrofit.create(PostsAPI::class.java)
            val call = postsAPI.addPost(newPost)

//            Toast.makeText(this, newPost.toString(), Toast.LENGTH_LONG).show()

            call.enqueue(object: Callback<Post>{
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    Toast.makeText(this@NewPostActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(!response.isSuccessful){
                        Toast.makeText(this@NewPostActivity, "Code : "+response.code(), Toast.LENGTH_LONG).show()
                        return
                    }
                    val post = response.body()
                }
            })
        }


    }
}
