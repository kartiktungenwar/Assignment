package com.bookxpert.assignment.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.bookxpert.assignment.R
import com.bookxpert.assignment.base.BaseActivity
import com.bookxpert.assignment.ui.main.MainActivity
import com.bookxpert.assignment.util.Constants.LOGIN_IN
import com.bookxpert.assignment.util.Constants.isFirstTime
import com.bookxpert.assignment.util.Constants.isNightMode
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shobhitpuri.custombuttons.GoogleSignInButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var signIn: GoogleSignInButton
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager
    private val model: SplashViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private var isNightModeStr by Delegates.notNull<Boolean>()
    private var isFirstTimeStr by Delegates.notNull<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            isNightModeStr = model.getBooleanValue(isNightMode)
            isFirstTimeStr = model.getBooleanValue(isFirstTime)
            toggleTheme(isNightModeStr,isFirstTimeStr, TAG)
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this@SplashActivity)
        signIn = findViewById(R.id.sign_in_button)
        signIn.visibility = View.GONE
        signIn.setOnClickListener {
            if (isNetworkAvailable()) {
                launchCredentialManager(this@SplashActivity)
            }else{
                Toast.makeText(this@SplashActivity,"Check Internet Connection and Click here again",Toast.LENGTH_SHORT).show()
            }
        }


        handler.postDelayed({
            checkLogin(LOGIN_IN)
        }, 1000) // 10 seconds
    }

    private fun launchCredentialManager(context: Context) {
        // [START create_credential_manager_request]
        // Instantiate a Google sign-in request
        val googleIdOption = GetGoogleIdOption.Builder()
            // Your server's client ID, not your Android client ID.
            .setServerClientId(getString(R.string.default_web_client_id))
            // Only show accounts previously used to sign in.
            .setFilterByAuthorizedAccounts(false)
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        // [END create_credential_manager_request]

        lifecycleScope.launch {
            try {
                // Launch Credential Manager UI
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                // Extract credential from the result returned by Credential Manager
                handleSignIn(result.credential)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }


    // [START handle_sign_in]
    private fun handleSignIn(credential: Credential) {
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.d(TAG, "Credential is not of type Google ID!")
        }
    }
    // [END handle_sign_in]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    lifecycleScope.launch {
                        model.saveBoolean(LOGIN_IN,true)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !isFirstTimeStr ) {
                            model.saveBoolean(isFirstTime, true)
                            model.saveBoolean(isNightMode,isNightMode(this@SplashActivity))
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user
                    Log.d(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
    // [END auth_with_google]

    companion object {
        private const val TAG = "SplashActivity"
    }

    private fun checkLogin(userKey: String) {
            lifecycleScope.launch {
                model.getBoolean(userKey).collect { value ->
                    // Handle the retrieved boolean value
                    if(value == true){
                        startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                        finishAffinity()
                    }else{
                        signIn.visibility = View.VISIBLE
                    }
                }
        }
    }

}