package hr.ferit.buyticketandroidappkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

public class RecyclerBonusAdapter(private val bonusPointsList: ArrayList<Bonus>) :
    RecyclerView.Adapter<RecyclerBonusAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = bonusPointsList[position]
        holder.ticketName.text = "Ulaznica " + currentItem.ticketId.toString()
        holder.bonusValue.text = currentItem.bonusPoints.toString()
        holder.desc.text = currentItem.description.toString()
    }

    override fun getItemCount(): Int {
        return bonusPointsList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ticketName: TextView = itemView.findViewById(R.id.tvTicketName)
        val bonusValue: TextView = itemView.findViewById(R.id.tvBonusValue)
        val desc: TextView = itemView.findViewById(R.id.tvDescription)

    }
}