package com.example.cemeterywithserver.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cemeterywithserver.data.entitites.Grave
import com.example.cemeterywithserver.databinding.GraveListItemBinding


class GraveListAdapter(val clickListener: GraveListListener) : ListAdapter<Grave, GraveListAdapter.ViewHolder>(GraveDiffUtilCallback()) {

    class ViewHolder(val binding: GraveListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Grave, listener: GraveListListener){
            binding.grave = item
            binding.clickListener = listener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GraveListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)

    }
}

class GraveDiffUtilCallback: DiffUtil.ItemCallback<Grave>(){
    override fun areItemsTheSame(oldItem: Grave, newItem: Grave): Boolean {
        return oldItem.graveId == newItem.graveId
    }

    override fun areContentsTheSame(oldItem: Grave, newItem: Grave): Boolean {
        return oldItem == newItem
    }
}
class GraveListListener(val clickListener: (id: String) -> Unit) {
    fun onClick(grave: Grave) {
        clickListener(grave.graveId)
    }
}