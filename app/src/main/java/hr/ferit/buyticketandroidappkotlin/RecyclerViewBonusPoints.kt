package hr.ferit.buyticketandroidappkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import hr.ferit.buyticketandroidappkotlin.Model.Bonus
import hr.ferit.buyticketandroidappkotlin.databinding.ActivityRecyclerViewBonusPointsBinding


    class RecyclerViewBonusPoints : AppCompatActivity() {

    private lateinit var binding:ActivityRecyclerViewBonusPointsBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var bonusRecyclerView: RecyclerView
    private lateinit var bonusPointsList: ArrayList<Bonus>
    private lateinit var adapter: RecyclerBonusAdapter

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBonusPointsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bonusRecyclerView = binding.recyclerViewBonusPoints
        bonusRecyclerView.layoutManager = LinearLayoutManager(this)
        bonusRecyclerView.setHasFixedSize(true)
        bonusPointsList = arrayListOf<Bonus>()
        getUserData()
        binding.btnBack.setOnClickListener{
            finish()
        }
    }

        private fun checkSize() {
            if( bonusPointsList.isEmpty()){
                binding.tvNoContent.isVisible = true
            }
        }


        private fun getUserData() {
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val id = firebaseUser?.uid
        databaseRef =
            FirebaseDatabase.getInstance(" https://android-ticket-app-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Users")

        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        var uid = userSnapshot.child("userId").getValue()
                        if(uid == id){
                            val bonusDATA = userSnapshot.getValue(Bonus::class.java)
                            bonusPointsList.add(bonusDATA!!)
                        }
                    }
                    bonusRecyclerView.adapter = RecyclerBonusAdapter(bonusPointsList)
                }
                //checkSize()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        }
}