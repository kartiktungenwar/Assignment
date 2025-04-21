package com.bookxpert.assignment.base


import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.bookxpert.assignment.R
import com.bookxpert.assignment.util.AppApplication
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Poonamchand Sahu 11 Nov 2022
 */
abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var appContext: AppApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Method used for displaying progress while calling api
     */
    open fun showLoading() {

    }

    /**
     * Method used for hide progress after calling api
     */
    open fun hideLoading() {
    }

    /**
     * Method used for set title after calling api
     */
    open fun setToolbarTitle(title:String) {
    }

    /**
     * Method used to check is Network Available
     */
    open fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)
        }
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    /**
     * Method toggle Theme
     */
    open fun toggleTheme(isNightModeOn: Boolean,isFirstStart: Boolean,Tag: String) {
        Log.d(Tag,"isNightModeOn "+isNightModeOn.toString()+" isFirstStart "+isFirstStart)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !isFirstStart ){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else{
            when {
                isNightModeOn -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            }
        }
    }

    open fun isNightMode(context: Context): Boolean {

        return (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    }

    companion object {
        private const val TAG = "BaseActivity"
    }
}