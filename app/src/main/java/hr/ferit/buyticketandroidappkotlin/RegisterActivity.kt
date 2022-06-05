package hr.ferit.buyticketandroidappkotlin

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import hr.ferit.buyticketandroidappkotlin.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    //view binding
    private lateinit var binding:ActivityRegisterBinding
    //firebase auth
    private lateinit var firebaseAuth:FirebaseAuth

    //progres dialog
    private lateinit var progressDialog: ProgressDialog
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(("Please wait"))
        progressDialog.setCanceledOnTouchOutside(false)

        //back button click
        binding.backBTN.setOnClickListener{
            onBackPressed() //ide na prosli ekran
        }

        //registracija
        binding.registerBTN.setOnClickListener {
            validateData()
        }
    }
    private var name = ""
    private var email = ""
    private var password = ""

    private fun validateData() {
        //input data
        name = binding.nameET.text.toString().trim()
        email = binding.emailET.text.toString().trim()
        password = binding.passwordET.text.toString().trim()
        val cpassword = binding.cpasswordET.text.toString().trim()
        //validate data
        if (name.isEmpty()) {
            Toast.makeText(this, "Unesi ime", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Neispravna email adresa", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Unesi lozinku", Toast.LENGTH_SHORT).show()
        } else if (cpassword.isEmpty()) {
            Toast.makeText(this, "Potvrdi lozinku", Toast.LENGTH_SHORT).show()
        } else if (cpassword != password) {
            Toast.makeText(this, "Lozinke nisu jednake", Toast.LENGTH_SHORT).show()
        } else {
            createUserAccount()
        }
    }
        /*if(email.isNotEmpty() && password.isNotEmpty()){
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity()) {
                task ->
                if(task.isSuccessful){
                    Toast.makeText(this,"User added",Toast.LENGTH_SHORT).show()

                }else{

                    Toast.makeText(this,task.exception!!.message,Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this,"ne valja pass il loz",Toast.LENGTH_SHORT).show()

        }*/


    private fun createUserAccount() {
        ///kreiraj acc - firebase
        progressDialog.setMessage("Učitavanje")
        progressDialog.show()

        //kreiranje usera
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            progressDialog.dismiss()
            Toast.makeText(this,"Registracija je uspješna",Toast.LENGTH_SHORT).show()
            //updateUserInfo()
            addToDatabase()
            startActivity(Intent(this,LoginActivity::class.java))
        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(this,"Greška pri registraciji",Toast.LENGTH_SHORT).show()
        }
    }

    fun addToDatabase(){

     val uid = firebaseAuth.uid
     database =
         FirebaseDatabase.getInstance(" https://android-ticket-app-default-rtdb.europe-west1.firebasedatabase.app")
             .getReference("Users")
     val User = User(name, email, "0",uid)
     database.child(name).setValue(User).addOnSuccessListener {
         Toast.makeText(this, "Upisano u bazu", Toast.LENGTH_SHORT).show()
     }.addOnFailureListener {
         Toast.makeText(this, "Greška prilikom upisa u bazu", Toast.LENGTH_SHORT).show()
     }
    }
}
