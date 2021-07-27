package com.example.mynotes.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.entities.Notes
import kotlinx.android.synthetic.main.fragment_create_note.view.*
import kotlinx.android.synthetic.main.item_rv_notes.view.*
import kotlinx.android.synthetic.main.item_rv_notes.view.tvDateTime


class NotesAdapter() :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    var listener: OnItemClickListener? = null
    var arrList = ArrayList<Notes>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_notes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.itemView.tvTitle.text = arrList[position].title
        holder.itemView.tvDesc.text = arrList[position].noteText
        holder.itemView.tvDateTime.text = arrList[position].dateTime
        if (arrList[position].color != null) {
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(arrList[position].color))
        }
        if (arrList[position].imagePath != null) {
            holder.itemView.roundImageNote.setImageBitmap(BitmapFactory.decodeFile(arrList[position].imagePath))
            holder.itemView.roundImageNote.visibility = View.VISIBLE

        } else {

            holder.itemView.roundImageNote.visibility = View.GONE

        }
        if (arrList[position].webLink != null) {
            holder.itemView.ttvWebLink.text = arrList[position].webLink
            holder.itemView.ttvWebLink.visibility = View.VISIBLE

        } else {

            holder.itemView.ttvWebLink.visibility = View.GONE

        }
        holder.itemView.cardView.setOnClickListener {
            listener!!.onClicked(arrList[position].id!!)
        }
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun setData(arrNotesList: List<Notes>) {
        arrList = arrNotesList as ArrayList<Notes>
    }
    fun setOnClickListener(listener1:OnItemClickListener){
        listener=listener1

    }

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    interface OnItemClickListener {
        fun onClicked(notesId: Int)
    }
}


