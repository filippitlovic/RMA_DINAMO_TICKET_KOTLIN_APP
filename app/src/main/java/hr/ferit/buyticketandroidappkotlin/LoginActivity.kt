 package hr.ferit.buyticketandroidappkotlin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.buyticketandroidappkotlin.databinding.ActivityLoginBinding

 class LoginActivity : AppCompatActivity() {

    //view binding
     private lateinit var binding:ActivityLoginBinding
     private lateinit var firebaseAuth: FirebaseAuth
     //progres dialog
     private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(("Molimo sačekajte trenutak"))
        progressDialog.setCanceledOnTouchOutside(false)

        //login
        binding.loginBTN.setOnClickListener {
            validateData()
        }
        binding.registerBTN.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

     private var email=""
     private var password =""
     private fun validateData() {
         email = binding.emailTV.text.toString().trim()
         password = binding.passwordTV.text.toString().trim()

         if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
             Toast.makeText(this,"Pogrešno unešen email..", Toast.LENGTH_SHORT).show()
         }else if(password.isEmpty()){
             Toast.makeText(this,"Unesi lozinku",Toast.LENGTH_SHORT).show()
         }
         else{
             loginUser()
         }
     }

     private fun loginUser() {
         progressDialog.setMessage("Prijavljivanje..")
         progressDialog.show()

         firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
             val intent = Intent(this,FragmentHolderActivity::class.java)
             intent.putExtra("Data","")
             startActivity(intent)
         } .addOnFailureListener{
             progressDialog.dismiss()
             Toast.makeText(this,"Neuspješna prijava",Toast.LENGTH_SHORT).show()
         }
     }
 }