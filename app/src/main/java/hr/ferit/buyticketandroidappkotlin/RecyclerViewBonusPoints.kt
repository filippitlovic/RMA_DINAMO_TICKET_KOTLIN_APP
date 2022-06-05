package hr.ferit.buyticketandroidappkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RecyclerViewBonusPoints : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var bonusRecyclerView: RecyclerView
    private lateinit var bonusPointsList: ArrayList<Bonus>
    private lateinit var adapter: RecyclerBonusAdapter

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_bonus_points)

        bonusRecyclerView = findViewById(R.id.recyclerViewBonusPoints)
        bonusRecyclerView.layoutManager = LinearLayoutManager(this)
        bonusRecyclerView.setHasFixedSize(true)
        bonusPointsList = arrayListOf<Bonus>()
        getUserData()
        if(adapter.itemCount==0){
            ///bindig tvNoContent
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
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        }
}