package hr.ferit.buyticketandroidappkotlin.ui.profile

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import hr.ferit.buyticketandroidappkotlin.RecyclerViewBonusPoints
import hr.ferit.buyticketandroidappkotlin.databinding.FragmentPersonBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PersonFragment : Fragment() {

    private var _binding: FragmentPersonBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var username: String
    private var bonusPoints = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPersonBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //binding.textView.text = requireArguments().getString("itemId").toString()

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val email = firebaseUser?.email
        val id = firebaseUser?.uid
        val personViewModel =
            ViewModelProvider(this).get(PersonViewModel::class.java)

        binding.emailDataTV.text = email.toString()
        database =
            FirebaseDatabase.getInstance(" https://android-ticket-app-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Users")
        binding.btnCheckPoints.setOnClickListener {
            val intent: Intent =
                Intent(requireActivity().application, RecyclerViewBonusPoints::class.java)
            startActivity(intent)
        }

        addAllBonusPoints()
        var getData = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var sbName = StringBuilder()
                var sbBonus = StringBuilder()
                for (i in snapshot.children) {
                    var name = i.child("name").getValue()
                    var uid = i.child("id").getValue()
                    var bonusPoints = i.child("bonusPoints").getValue()
                    if (uid == id) {
                        sbName.append("$name")
                        sbBonus.append("$bonusPoints")
                        username = name.toString()
                    }
                }
            }
        }

        if (this::username.isInitialized) {
            binding.nameDataTV.text = username
            binding.bonusPointsDataTV.text = bonusPoints.toString()
        }
        database.addValueEventListener(getData)
        database.addListenerForSingleValueEvent(getData)
        return root
    }

    private fun addAllBonusPoints() {
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val id = firebaseUser?.uid
        var bonus = 0
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {

                        var uid = userSnapshot.child("userId").getValue()
                        var bonusPointsData = userSnapshot.child("bonusPoints").getValue()
                        if (uid == id) {
                                bonus += checkBonusPoints(bonusPointsData.toString())
                                bonusPoints = bonus

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun checkBonusPoints(bonusPointsData: Any): Int {
        var bonusData = 0
        if (bonusPointsData == "1") {
            bonusData = 1
        } else if (bonusPointsData == "2") {
            bonusData = 2
        } else if (bonusPointsData == "3") {
            bonusData = 3
        } else if (bonusPointsData == "4") {
            bonusData = 4
        }

        Log.e(TAG,bonusData.toString() + "sfssfsfsf")
        return bonusData
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
    }
}

