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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class SignupActivity : AppCompatActivity() {

    lateinit var inputEmail: EditText
    lateinit var inputPassword: EditText
    lateinit var btnSignIn: Button
    lateinit var btnSignUp: Button
    lateinit var btnResetPassword: Button
    lateinit var progressBar: ProgressBar
    lateinit var auth: FirebaseAuth

    fun init() {

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        btnResetPassword = findViewById(R.id.btn_reset_password);


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        init()
        btnResetPassword.setOnClickListener {
            startActivity(Intent(this@SignupActivity, ResetPasswordActivity::class.java))
        }

        btnSignIn.setOnClickListener {
            finish()
        }

        btnSignUp.setOnClickListener { signup() }
    }

    fun signup() {

        val email = inputEmail.getText().toString().trim { it <= ' ' }
        val password = inputPassword.getText().toString().trim { it <= ' ' }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(
                applicationContext,
                "Password too short, enter minimum 6 characters!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        //create user
        //create user
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@SignupActivity,
                OnCompleteListener<AuthResult?> { task ->
                    Toast.makeText(
                        this@SignupActivity,
                        "createUserWithEmail:onComplete:" + task.isSuccessful,
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            this@SignupActivity, "Authentication failed." + task.exception,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        startActivity(Intent(this@SignupActivity, SplashActivity::class.java))
                        finish()
                    }
                })
    }
}