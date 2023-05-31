package pers.apollokwok.androidutil.demos.view

import kotlinx.coroutines.CoroutineScope
import pers.apollokwok.androidutil.demos.view.databinding.ItemTealBinding
import pers.shawxingkwok.androidutil.view.KListAdapter

class SingleItemAdapter(
    scope: CoroutineScope,
    initialItems: List<User>,
)
    : KListAdapter<User, ItemTealBinding>(ItemTealBinding::class, scope, initialItems)
{
    override fun onViewHolderCreated(holder: ViewBindingHolder<ItemTealBinding>) {
        holder.binding.tv.setOnClickListener {
            MLog("Clicked single item ${holder.adapterPosition}.")
        }
    }

    override fun onBindViewHolder(holder: ViewBindingHolder<ItemTealBinding>, position: Int) {
        holder.binding.tv.text = currentList[position].toString()
    }

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return newItem == oldItem
    }
}