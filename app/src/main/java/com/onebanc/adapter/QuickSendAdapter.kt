package com.onebanc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.onebanc.R
import com.onebanc.model.QuickSender
import com.bumptech.glide.Glide
import com.onebanc.databinding.QuickSendRecyclerItemBinding

class QuickSendAdapter(
    private val context: Context,
    private val quickSenders: List<QuickSender>
) : RecyclerView.Adapter<QuickSendAdapter.QuickSendViewHolder>() {

    class QuickSendViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val senderName: TextView=itemView.findViewById(R.id.quick_sender_name)
        val senderPic:ImageView=itemView.findViewById(R.id.quick_sender_pic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickSendViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.quick_send_recycler_item, parent, false)
        return QuickSendViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuickSendViewHolder, position: Int) {
        val currentItem = quickSenders[position]
        holder.senderName.text=currentItem.name
        Glide.with(holder.itemView.context)
            .load(currentItem.imageResId)
            .into(holder.senderPic)
    }

    override fun getItemCount(): Int = quickSenders.size
}

