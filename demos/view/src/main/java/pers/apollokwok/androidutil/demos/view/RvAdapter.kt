package pers.apollokwok.androidutil.demos.view

import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import pers.apollokwok.androidutil.demos.view.databinding.ItemFooterBinding
import pers.apollokwok.androidutil.demos.view.databinding.ItemHeaderBinding
import pers.apollokwok.androidutil.demos.view.databinding.ItemPurpleBinding
import pers.apollokwok.androidutil.demos.view.databinding.ItemTealBinding
import pers.shawxingkwok.androidutil.view.KRecyclerViewAdapter

class RvAdapter(scope: CoroutineScope, var users: List<User>) : KRecyclerViewAdapter(scope) {
    override val holderCreators: Set<HolderCreator<ViewBinding>> =
        setOf(
            HolderCreator(ItemHeaderBinding::class),
            HolderCreator(ItemPurpleBinding::class),
            HolderCreator(ItemTealBinding::class),
            HolderCreator(ItemFooterBinding::class),
        )

    override fun arrange(binders: MutableList<HolderBinder<ViewBinding>>) {
        binders += HolderBinder(ItemHeaderBinding::class, null, null)

        binders += users.mapIndexed { i, user ->
            if (i % 2 == 0)
                HolderBinder(
                    bindingKClass = ItemPurpleBinding::class,
                    id = user.id,
                    contentId = user
                ){
                    it.binding.tv.text = user.toString()
                }
            else
                HolderBinder(
                    bindingKClass = ItemTealBinding::class,
                    id = user.id,
                    contentId = user
                ){
                    it.binding.tv.text = user.toString()
                }
        }

        binders += HolderBinder(ItemFooterBinding::class, null, null)
    }
}