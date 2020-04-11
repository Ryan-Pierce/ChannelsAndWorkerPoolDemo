package me.ryanpierce.channelsandworkerpooldemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.ryanpierce.channelsandworkerpooldemo.databinding.CustomListItemBinding

class UserListAdapter : ListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback()) {

    private val list: MutableList<User> = mutableListOf()

    fun addUser(user: User) {
        list.add(user)
        submitList(list) { notifyDataSetChanged() }
    }

    fun updateUserPhoto(photo: Photo) {
        list
            .filter { it.uid == photo.uid }
            .forEach { it.photo = photo }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(
            CustomListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = getItem(position)
        (holder as UserViewHolder).bind(user)
    }

    class UserViewHolder(
        private val binding: CustomListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) {
            binding.apply {
                user = item
                executePendingBindings()
            }
        }
    }
}

private class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}