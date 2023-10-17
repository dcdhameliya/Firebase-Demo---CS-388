package com.dc.firebasedemo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {

    lateinit var inputEmail: EditText
    lateinit var inputPassword: EditText
    lateinit var progressBar: ProgressBar
    lateinit var btnSignup: Button
    lateinit var btnLogin: Button
    lateinit var btnReset: Button

    lateinit var ivGoogleLogin: ImageView

    lateinit var auth: FirebaseAuth


    lateinit var googleSignInClient: GoogleSignInClient

    val TAG = "LoginActivity"


    fun init() {
        inputEmail = findViewById(R.id.email)
        inputPassword = findViewById(R.id.password)
        progressBar = findViewById(R.id.progressBar)
        btnSignup = findViewById(R.id.btn_signup)
        btnLogin = findViewById(R.id.btn_login)
        btnReset = findViewById(R.id.btn_reset_password)
        ivGoogleLogin = findViewById(R.id.ivGoogleLogin)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()


        btnSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }
        btnReset.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))
        }

        btnLogin.setOnClickListener { loginWithEmail() }

        ivGoogleLogin.setOnClickListener { loginWithGoogle() }

    }

    fun loginWithEmail() {
        val email: String = inputEmail.getText().toString()
        val password = inputPassword.getText().toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address !", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password ! ", Toast.LENGTH_LONG).show()
            return
        }
        progressBar.visibility = View.VISIBLE

        //auth user
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
            this@LoginActivity
        ) { task ->
            progressBar.visibility = View.GONE
            if (!task.isSuccessful) {
                //there was an error
                if (password.length < 6) {
                    inputPassword.error = getString(R.string.minimum_pass)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.auth_failed),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            } else {
                val intent = Intent(
                    this@LoginActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
    }

    fun loginWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        val intent = googleSignInClient.signInIntent
        googleSignInActivityResultLauncher.launch(intent)

    }

    private val googleSignInActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult : ${result.data!!.extras}")

                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = accountTask.getResult(ApiException::class.java)
                    Log.d(TAG, "onActivityResult : $account")

                    firebaseAuthWithGoogleAccount(account)
                } catch (e: ApiException) {
                    Log.w(TAG, "onActivityResult : ${e.message}")
                }
            } else {
                Log.w(TAG, "onActivityResult : ${result.data}")
            }
        }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)

        auth.signInWithCredential(credential)
            .addOnSuccessListener { authRes ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount : ${authRes.user}")

//                if (authRes.additionalUserInfo!!.isNewUser) {
//                    /** Create Account */
//                    updateUI(true)
//
//                } else {
//                    /** Logged In Account */
//                    updateUI(false)
//                }
                val intent = Intent(
                    this@LoginActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { err ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount : ${err.message}")
                Toast.makeText(this, "${err.message}", Toast.LENGTH_SHORT).show()
            }
    }
}