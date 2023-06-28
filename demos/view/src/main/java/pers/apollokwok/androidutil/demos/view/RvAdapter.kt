package pers.apollokwok.androidutil.demos.view

import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import pers.apollokwok.androidutil.demos.view.databinding.ItemFooterBinding
import pers.apollokwok.androidutil.demos.view.databinding.ItemHeaderBinding
import pers.apollokwok.androidutil.demos.view.databinding.ItemPurpleBinding
import pers.apollokwok.androidutil.demos.view.databinding.ItemTealBinding
import pers.shawxingkwok.androidutil.view.KListAdapter

class RvAdapter(
    scope: CoroutineScope,
    initialItems: List<User>,
)
    : KListAdapter<User>(scope, initialItems)
{
    private val header = object : HolderBuilder<ItemHeaderBinding>(ItemHeaderBinding::class) {
        override fun onHolderCreated(holder: ViewBindingHolder<ItemHeaderBinding>) {
            MLog()
        }

        override fun onBindHolder(holder: ViewBindingHolder<ItemHeaderBinding>, position: Int) {
            MLog()
        }
    }

    private val footer = object : HolderBuilder<ItemFooterBinding>(ItemFooterBinding::class){
        override fun onHolderCreated(holder: ViewBindingHolder<ItemFooterBinding>) {
            MLog()
        }

        override fun onBindHolder(holder: ViewBindingHolder<ItemFooterBinding>, position: Int) {
            MLog()
        }
    }

    private val teal = object : HolderBuilder<ItemTealBinding>(ItemTealBinding::class) {
        override fun onHolderCreated(
            holder: ViewBindingHolder<ItemTealBinding>
        ) {
            MLog()
        }

        override fun onBindHolder(
            holder: ViewBindingHolder<ItemTealBinding>,
            position: Int,
        ) {
            holder.binding.tv.text = currentList[position - 1].toString()
        }
    }

    private val purple = object : HolderBuilder<ItemPurpleBinding>(ItemPurpleBinding::class) {
        override fun onHolderCreated(holder: ViewBindingHolder<ItemPurpleBinding>) {
            MLog()
        }

        override fun onBindHolder(
            holder: ViewBindingHolder<ItemPurpleBinding>,
            position: Int,
        ) {
            holder.binding.tv.text = currentList[position - 1].toString()
        }
    }

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun arrange(builders: MutableList<HolderBuilder<ViewBinding>>) {
        builders += header

        builders += List(currentList.size) { i->
            if (i % 2 == 0)
                teal
            else
                purple
        }

        builders += footer
    }
}