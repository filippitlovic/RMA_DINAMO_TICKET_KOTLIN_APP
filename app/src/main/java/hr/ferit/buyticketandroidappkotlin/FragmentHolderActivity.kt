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
import hr.ferit.buyticketandroidappkotlin.databinding.ActivityFragmentHolderBinding

@Suppress("DEPRECATION")
class FragmentHolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentHolderBinding

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentHolderBinding.inflate(layoutInflater)
         setContentView(binding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(navController)

        firebaseAuth = FirebaseAuth.getInstance()
        //checkUser()


        binding.toolbar.logoutBTN.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }





}