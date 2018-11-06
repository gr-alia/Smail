package com.alyona.smail

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.alyona.smail.api.RetrofitService
import com.alyona.smail.model.Thread
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 101
    private var googleSignInClient: GoogleSignInClient? = null
    private var myJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://mail.google.com/"))
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_in_button.setOnClickListener { signIn() }
    }

    override fun onStart() {
        super.onStart()
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        Log.d("Kot", if (account != null) "User exists" else "User doesnt exist")
    }

    override fun onDestroy() {
        myJob?.cancel()
        super.onDestroy()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            val account = completedTask?.getResult(ApiException::class.java)
            var id = "me"
            if (account != null) {
                id = account.id ?: "me"
            }

            myJob = CoroutineScope(Dispatchers.IO).launch {
                val result = getThreads(id)
                withContext(Dispatchers.Default) {
                    //do something with result
                }
            }


            // Signed in successfully, show authenticated UI.
            //  updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to theb  class reference for more information.
            Log.w("Kot", "signInResult:failed code=" + e.statusCode)
            // updateUI(null)
        }

    }

    private suspend fun getThreads(userId: String): ArrayList<Thread> {
        return RetrofitService.getApi().getThreads(userId).await().threads

    }
}
