package hr.ferit.buyticketandroidappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import hr.ferit.buyticketandroidappkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //login button
        val scanner = IntentIntegrator(this)
        scanner.initiateScan()
        binding.loginBTN.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }

        binding.registerBTN.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

}