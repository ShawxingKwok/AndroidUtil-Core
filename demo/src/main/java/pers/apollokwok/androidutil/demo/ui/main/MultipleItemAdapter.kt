package pers.apollokwok.androidutil.demo.ui.main

import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import pers.apollokwok.android.view.KListAdapter
import pers.apollokwok.androidutil.demo.MLog
import pers.apollokwok.androidutil.demo.databinding.ItemPurpleBinding
import pers.apollokwok.androidutil.demo.databinding.ItemTealBinding
import java.lang.Exception

class MultipleItemAdapter(
    scope: CoroutineScope,
    initialItems: List<User>,
)
    : KListAdapter.Multiple<User>(scope, initialItems)
{
    private val tealVBBuilder =
        object : ViewBindingHolderBuilder<ItemTealBinding>(ItemTealBinding::class){
            override fun onViewBindingHolderCreated(
                holder: ViewBindingHolder<ItemTealBinding>
            ) {
                holder.binding.tv.setOnClickListener {
                    MLog("Teal")
                    MLog.i("Error in teal", e = Exception())
                }
            }

            override fun onBindViewBindingHolder(
                holder: ViewBindingHolder<ItemTealBinding>,
                position: Int,
            ) {
                holder.binding.tv.text = currentList[position].toString()
            }
        }

    private val purpleVBBuilder =
        object : ViewBindingHolderBuilder<ItemPurpleBinding>(ItemPurpleBinding::class){
            override fun onViewBindingHolderCreated(
                holder: ViewBindingHolder<ItemPurpleBinding>
            ) {
                MLog.v("Purple")
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
            position % 2 == 0 -> tealVBBuilder
            else -> purpleVBBuilder
        }
}