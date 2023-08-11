package com.bullhead.androidequalizer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bullhead.equalizer.SimpleViewModel

class ChooseRawDialogFragment(dialog:Int) : DialogFragment(dialog) {

    lateinit var id_recycle: RecyclerView;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id_recycle = view.findViewById(R.id.id_recycle)
        val title = MainActivity.titles
        id_recycle.layoutManager = LinearLayoutManager(null,LinearLayoutManager.VERTICAL,false)
        id_recycle.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(TextView(parent.context).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                    setPadding(50,20,20,50)
                }) {}
            }

            override fun getItemCount(): Int = title.size

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder.itemView as TextView).text = title[position]
                holder.itemView.setOnClickListener {
                    ViewModelProvider(requireActivity())[SimpleViewModel::class.java].position = position
                    dismiss()
                }
            }

        }
    }


}