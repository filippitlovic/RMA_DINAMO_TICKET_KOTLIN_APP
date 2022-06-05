package hr.ferit.buyticketandroidappkotlin.ui.profile

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.zxing.integration.android.IntentIntegrator
import hr.ferit.buyticketandroidappkotlin.Bonus

import hr.ferit.buyticketandroidappkotlin.databinding.FragmetQrscannerBinding
import java.lang.StringBuilder


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class QRScannerFragment : Fragment() {

    private var _binding:  FragmetQrscannerBinding? = null
    private val binding get() = _binding!!
    private  var scannedValue = 0
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var username: String
    private lateinit var bonusPointss : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmetQrscannerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        database = FirebaseDatabase.getInstance(" https://android-ticket-app-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val email = firebaseUser?.email
        val uid = firebaseUser?.uid

       database.addValueEventListener(getData)

        binding.btnScan.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this@QRScannerFragment)
            integrator.setOrientationLocked(false)
            integrator.setPrompt("Scan QR code")
            integrator.setBeepEnabled(false)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.initiateScan()
        }
        binding.btnQuit.setOnClickListener {
            binding.tvQRcode.isVisible = false
            binding.textScanResult.isVisible = false
            binding.btnAcceptBonus.isVisible = false
            binding.btnQuit.isVisible = false
        }

        binding.btnAcceptBonus.setOnClickListener {
            validateData()
        }

        return root
    }

    var getData = object : ValueEventListener {

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            firebaseAuth = FirebaseAuth.getInstance()
            val firebaseUser = firebaseAuth.currentUser
            val id = firebaseUser?.uid

            for(i in snapshot.children){
                var name= i.child("name").getValue()
                var uid = i.child("id").getValue()
                var bonus = i.child("bonusPoints").getValue()
                if( uid == id) {
                    username = name.toString()
                    bonusPointss = bonus.toString()
                }
            }
        }

    }

    /*private fun updateData(bonusPoints: Int) {
        var bonus = bonusPoints
        val user = mapOf<String,String>(
            "bonusPoints" to bonus
        )
          // database.child(username).updateChildren(user).addOnSuccessListener {

        //}
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Scanned : " + result.contents, Toast.LENGTH_LONG).show()
            }
        }
        binding.tvQRcode.isVisible = true
        binding.btnAcceptBonus.isVisible = true
        binding.btnQuit.isVisible = true
        binding.textScanResult.text = result.contents.toString()
        scannedValue = result.contents.toInt()
        if(scannedValue == 1){
            binding.tvQRcode.text = "Tribina JUG! Broj bonus bodova koje možete preuzeti:"
        }
        if(scannedValue == 2){
            binding.tvQRcode.text = "Tribina ISTOK! Broj bonus bodova koje možete preuzeti:"
        }
        if(scannedValue == 3){
            binding.tvQRcode.text = "Tribina ZAPAD! Broj bonus bodova koje možete preuzeti:"
        }
        if(scannedValue == 4){
            binding.tvQRcode.text = "Tribina SJEVER! Broj bonus bodova koje možete preuzeti:"
        }
    }

    private var ticketId = 0
    private var userId = ""
    private var ticketName = ""
    private var ticketDescription = ""
    private fun validateData() {
        //input data
        if(scannedValue == 1){
            ticketDescription = "Tribina JUG"
        }
        if(scannedValue == 2){
            ticketDescription = "Tribina ISTOK"
        }
        if(scannedValue == 3){
            ticketDescription = "Tribina ZAPAD"
        }
        if(scannedValue == 4){
            ticketDescription = "Tribina SJEVER"
        }
        //ticketId = binding.passwordET.text.toString().trim()
        //validate data
        addToDatabase()

    }


    fun addToDatabase() {
        checkTicketDescription()
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val id = firebaseUser?.uid
        if (id != null) {
            userId = id
        }
        ticketId = checkTicketId()
        ticketName = "Ulaznica "+ ticketId


        database =
            FirebaseDatabase.getInstance(" https://android-ticket-app-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Users")
        val Bonus = Bonus(ticketId,scannedValue,userId,ticketDescription)
        database.child(ticketName).setValue(Bonus).addOnSuccessListener {
        }.addOnFailureListener {
        }
        binding.tvQRcode.isVisible = false
        binding.textScanResult.isVisible = false
        binding.btnAcceptBonus.isVisible = false
        binding.btnQuit.isVisible = false

    }

    private fun checkTicketDescription() {
        if(scannedValue == 1){
            ticketDescription = "Tribina JUG"
        }
        else if(scannedValue == 2){
            ticketDescription = "Tribina ISTOK"
        }
        else if(scannedValue == 3){
            ticketDescription = "Tribina ZAPAD"
        }
        else if(scannedValue == 4){
            ticketDescription = "Tribina SJEVER"
        }
    }


    private fun checkTicketId() : Int {
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val id = firebaseUser?.uid
        var random = (0..10000).shuffled().last()
        database.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        var uid = userSnapshot.child("ticketId").getValue()

                        if(uid == random){
                            checkTicketId()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return random
    }

    companion object {
    }
}