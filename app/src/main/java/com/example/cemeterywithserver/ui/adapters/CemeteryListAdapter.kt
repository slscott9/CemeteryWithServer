package com.example.cemeterywithserver.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cemeterywithserver.data.entitites.Cemetery
import com.example.cemeterywithserver.databinding.CemeteryListItemBinding

class CemeteryListAdapter(val clickListner: CemeteryListener): ListAdapter<Cemetery, CemeteryListAdapter.ViewHolder>(CemeteryDiffUtilCallback()) {


    class ViewHolder (val binding: CemeteryListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(
            item: Cemetery,
            clickListener: CemeteryListener
        ){
            binding.cemetery = item
            binding.clickListener = clickListener //register xml's clickListener variable
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CemeteryListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListner)
    }
}


/*
    CemeteryListener has a lambda member of type (Int) -> return type is nothing

    The lambda's name is clickListener it receives an id of type Int and returns nothing
 */
class CemeteryListener(val clickListener: (id: String) -> Unit){

    //onclick is called when user clicks the cardview which passes a cemetery object in the cardview xml and calls clickListener lambda passing row id
    fun onClick(cemetery: Cemetery){
        clickListener(cemetery.cemeteryId) //invoke clickListener lambda when onClick is called and give clickListener its int parameter which is row id
    }
}

class CemeteryDiffUtilCallback: DiffUtil.ItemCallback<Cemetery>() {
    override fun areItemsTheSame(oldItem: Cemetery, newItem: Cemetery): Boolean {
        return oldItem.cemeteryId == newItem.cemeteryId
    }

    override fun areContentsTheSame(oldItem: Cemetery, newItem: Cemetery): Boolean {
        return oldItem == newItem
    }
}