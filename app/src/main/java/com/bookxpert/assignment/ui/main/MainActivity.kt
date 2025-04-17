package com.bookxpert.assignment.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bookxpert.assignment.R
import com.bookxpert.assignment.base.BaseActivity
import com.bookxpert.assignment.database.User
import com.bookxpert.assignment.network.Status
import com.bookxpert.assignment.ui.splash.SplashActivity
import com.bookxpert.assignment.util.Constants.LOGIN_IN
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity(), View.OnClickListener, ProductEditInterface,ProductInterface{

    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager
    private var swipe_refresh_layout: SwipeRefreshLayout? = null
    private var toolbar: Toolbar? = null
    private var open_pdf: Button? = null
    private var open_photo: Button? = null
    private var reset_db: Button? = null
    private var progress_bar: ProgressBar? = null
    private var text_view: TextView? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val model: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this@MainActivity)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Bookxpert" // Set the title of the toolbar
        open_pdf = findViewById(R.id.btn_open_pdf)
        open_pdf?.setOnClickListener(this)
        open_photo = findViewById(R.id.btn_open_photo)
        open_photo?.setOnClickListener(this)
        reset_db = findViewById(R.id.btn_reset_db)
        reset_db?.setOnClickListener(this)
        progress_bar = findViewById(R.id.progress_bar)
        text_view = findViewById(R.id.text_view)
        text_view?.setOnClickListener{
            if(isNetworkAvailable()) {
                text_view?.visibility = View.GONE
                apiCallData()
            }else{
                text_view?.visibility = View.VISIBLE
                text_view?.text = "Check Internet Connection and Click here again"
            }
        }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout)
        swipe_refresh_layout?.isEnabled= false

        lifecycleScope.launch {
            val allUsers: MutableList<User> = model.getAllUsers()
            if(allUsers.isEmpty()){
                if(isNetworkAvailable()) {
                    text_view?.visibility = View.GONE
                    apiCallData()
                }else{
                    text_view?.visibility = View.VISIBLE
                    text_view?.text = "Check Internet Connection and Click here again"
                }
            }else{
                userAdapter = UserAdapter(allUsers,this@MainActivity)
                recyclerView.adapter = userAdapter
            }
        }
    }

    override fun onClick(v: View?) {
        if(v?.id ==R.id.btn_open_pdf){
            if (isNetworkAvailable()) {
                openPdf(resources.getString(R.string.pdf_url))
            }else{
                Toast.makeText(this@MainActivity,"Check Internet Connection and Click here again",Toast.LENGTH_SHORT).show()
            }
        }else if (v?.id ==R.id.btn_open_photo){
            selectPhoto()
        }else if (v?.id ==R.id.btn_reset_db) {
            signOut()
        }
    }

    private fun selectPhoto(){
        val bottomSheet = ImagePickerBottomSheet(this@MainActivity)
        supportFragmentManager.let { bottomSheet.show(it, ImagePickerBottomSheet.TAG) }
    }

    private fun openPdf(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Open PDF"))
    }

    private fun apiCallData(){
        model.getData().observe(this) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    progress_bar?.visibility = View.GONE
                    response.data?.forEach { user ->
                        val userData = user.name?.let { user.id?.let { it1 -> User(it1, it) } }
                        Log.e(TAG,"userData "+userData.toString())
                        if (userData != null) {
                            model.saveData(userData)
                        }
                    }
                    model.getAllUsers(
                        onResult = { users ->
                            // Handle the list of users
                            // Initialize the adapter with the user list
                            userAdapter = UserAdapter(users,this)
                            recyclerView.adapter = userAdapter
                        },
                        onError = { exception ->
                            // Handle the error
                            Toast.makeText(this@MainActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }

                    )
                }
                Status.ERROR -> {
                    progress_bar?.visibility = View.GONE
                    text_view?.visibility = View.VISIBLE
                    text_view?.text = "Error in API"
                }
                Status.LOADING -> {
                    progress_bar?.visibility = View.VISIBLE
                }

            }
        }
    }

    fun signOut() {
        // Firebase sign out
        auth.signOut()
        // When a user signs out, clear the current user credential state from all credential providers.
        lifecycleScope.launch {
            try {
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)
                model.saveBoolean(LOGIN_IN,false)
                startActivity(Intent(this@MainActivity,SplashActivity::class.java))
                finishAffinity()

            } catch (e: ClearCredentialException) {
                Log.d(TAG, "Couldn't clear user credentials: ${e.localizedMessage}")
            }
        }
    }
    // [END sign_out]

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun productShow(user: User) {
        val bottomSheet = InputBottomSheetFragment(user,this)
        supportFragmentManager.let { bottomSheet.show(it, ImagePickerBottomSheet.TAG) }
    }

    override fun productEdit(user: User) {
        model.saveData(user)
        lifecycleScope.launch {
            val allUsers: MutableList<User> = model.getAllUsers()
            userAdapter.updateUserList(allUsers)
        }
    }

    override fun productDelete(user: User) {
        model.deleteData(user)
        lifecycleScope.launch {
            val allUsers: MutableList<User> = model.getAllUsers()
            userAdapter.updateUserList(allUsers)
        }
    }
}