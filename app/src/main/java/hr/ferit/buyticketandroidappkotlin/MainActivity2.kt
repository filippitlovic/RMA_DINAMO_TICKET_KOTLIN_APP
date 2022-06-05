package hr.ferit.buyticketandroidappkotlin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.buyticketandroidappkotlin.databinding.ActivityMain2Binding

@Suppress("DEPRECATION")
class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
         setContentView(binding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragmentContainerView)
        //val appBarConfiguration = AppBarConfiguration(setOf(R.id.firstFragment,R.id.secondFragment))
        //setupActionBarWithNavController(navController,appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        firebaseAuth = FirebaseAuth.getInstance()
        //checkUser()


        binding.toolbar.logoutBTN.setOnClickListener{

            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

/*
    private fun openScanner() {
        val intent = Intent(this, QRScanner::class.java)
        startActivity(intent)
    }*/

    private fun logoOut() {
        firebaseAuth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
    }

}