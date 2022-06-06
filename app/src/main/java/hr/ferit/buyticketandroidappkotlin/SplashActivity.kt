package hr.ferit.buyticketandroidappkotlin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        firebaseAuth = FirebaseAuth.getInstance()

        Handler().postDelayed(Runnable {
            checkUser()
        },2000)
    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            ///korisnik nije ulogiran, odi u MainActivity
                Log.e(TAG,"ulogiran")
            startActivity(Intent(this,MainActivity::class.java))
        }
        else{
            startActivity(Intent(this@SplashActivity,FragmentHolderActivity::class.java))
            finish()
        }
    }

}