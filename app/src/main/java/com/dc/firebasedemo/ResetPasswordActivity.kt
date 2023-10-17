package com.dc.firebasedemo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class ResetPasswordActivity : AppCompatActivity() {


    lateinit var inputEmail: EditText
    lateinit var btnReset: Button
    lateinit var btnBack: Button
    lateinit var auth: FirebaseAuth
    lateinit var progressBar: ProgressBar


    fun init() {

        inputEmail = findViewById(R.id.email)
        btnReset = findViewById(R.id.btn_reset_password)
        btnBack = findViewById(R.id.btn_back)
        progressBar = findViewById(R.id.progressBar)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        init()

        btnBack.setOnClickListener {
            val intent = Intent(
                this@ResetPasswordActivity,
                LoginActivity::class.java
            )
            startActivity(intent)
        }
        btnReset.setOnClickListener { resetPassword() }

    }

    fun resetPassword() {
        val email = inputEmail.getText().toString().trim { it <= ' ' }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter your registered email", Toast.LENGTH_LONG)
                .show()
            return
        }
        progressBar.visibility = View.VISIBLE
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "We have sent you instructions to reset your password!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Failed to send reset email!",
                    Toast.LENGTH_LONG
                ).show()
            }
            progressBar.visibility = View.GONE
        }
    }
}