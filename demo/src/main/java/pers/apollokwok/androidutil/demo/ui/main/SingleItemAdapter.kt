package pers.apollokwok.androidutil.demo.ui.main

import android.view.LayoutInflater
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import pers.apollokwok.android.view.KListAdapter
import pers.apollokwok.androidutil.demo.databinding.ItemTealBinding

class SingleItemAdapter(
    scope: CoroutineScope,
    initialItems: List<User>,
)
    : KListAdapter<User, ItemTealBinding>(ItemTealBinding::class, scope, initialItems)
{
    override fun onViewHolderCreated(holder: ViewBindingHolder<ItemTealBinding>) {
        holder.binding.tv.setOnClickListener {
            // TODO
        }
    }

    override fun onBindViewHolder(holder: ViewBindingHolder<ItemTealBinding>, position: Int) {
        holder.binding.tv.text = currentList[position].toString()
    }

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return newItem == oldItem
    }
}