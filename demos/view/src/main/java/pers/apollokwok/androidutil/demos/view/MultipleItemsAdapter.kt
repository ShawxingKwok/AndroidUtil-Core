package pers.apollokwok.androidutil.demos.view

import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import pers.apollokwok.androidutil.demos.view.databinding.ItemPurpleBinding
import pers.apollokwok.androidutil.demos.view.databinding.ItemTealBinding
import pers.shawxingkwok.androidutil.view.KListAdapter

class MultipleItemsAdapter(
    scope: CoroutineScope,
    initialItems: List<User>,
)
    : KListAdapter.Multiple<User>(scope, initialItems)
{
    private val tealBuilder =
        object : ViewBindingHolderBuilder<ItemTealBinding>(ItemTealBinding::class){
            override fun onViewBindingHolderCreated(
                holder: ViewBindingHolder<ItemTealBinding>
            ) {
                MLog("Teal")
            }

            override fun onBindViewBindingHolder(
                holder: ViewBindingHolder<ItemTealBinding>,
                position: Int,
            ) {
                holder.binding.tv.text = currentList[position].toString()
            }
        }

    private val purpleBuilder =
        object : ViewBindingHolderBuilder<ItemPurpleBinding>(ItemPurpleBinding::class){
            override fun onViewBindingHolderCreated(
                holder: ViewBindingHolder<ItemPurpleBinding>
            ) {
                MLog("Purple")
            }

            override fun onBindViewBindingHolder(
                holder: ViewBindingHolder<ItemPurpleBinding>,
                position: Int,
            ) {
                holder.binding.tv.text = currentList[position].toString()
            }
        }

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun arrange(position: Int): ViewBindingHolderBuilder<ViewBinding> =
        when{
            position % 2 == 0 -> tealBuilder
            else -> purpleBuilder
        }
}