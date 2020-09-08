package me.ryanpierce.channelsandworkerpooldemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.ryanpierce.channelsandworkerpooldemo.databinding.CustomListItemBinding

class UserListAdapter : ListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback()) {

    private val list: MutableList<User> = mutableListOf()

    /**
     * To keep this demo simple, I recreated the `add()` functionality
     * from ArrayAdapters in the pre-Jetpack era with `addUser()`. This was
     * to demonstrate the mechanics of Flow by populating a RecyclerView
     * with a Flow<data>.
     * In modern Android development with the JetPack RecyclerView and
     * ListAdapter, you might observe a Flow<List<data>> where the list
     * of data is the original list, updated with the new data value. This
     * ensures that the new state coming from your flow is the "full" new
     * state and not just the part of your state that changed.
     * You might call `submitList()` with the List<data> collected from
     * each emission of the flow and `DiffUtil.ItemCallback` will ensure
     * that existing values in the RecyclerView aren't duplicated.
     */
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