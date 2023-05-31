package pers.apollokwok.androidutil.demos.view

import androidx.lifecycle.lifecycleScope
import pers.apollokwok.androidutil.demos.view.databinding.FragmentMainBinding
import pers.shawxingkwok.androidutil.view.KFragment

class MainFragment : KFragment<FragmentMainBinding>(FragmentMainBinding::class) {

    private val topAdapter: SingleItemAdapter by withBinding {
        SingleItemAdapter(
            scope = lifecycleScope,
            initialItems = (0..20).map(::User),
        )
        .also { binding.rvTop.adapter = it }
    }

    private val bottomAdapter: MultipleItemsAdapter by withBinding {
        MultipleItemsAdapter(
            scope = lifecycleScope,
            initialItems = (100..120).map(::User)
        )
        .also { binding.rvBottom.adapter = it }
    }

    @OnClick(R.id.btn)
    private fun onClick1(){
        val newTopUserId = topAdapter.currentList.last().id + 1
        val newTopList = topAdapter.currentList + User(newTopUserId)
        topAdapter.submitList(newTopList)

        val newBottomList = newTopList.map { User(it.id + 100) }
        bottomAdapter.submitList(newBottomList)
    }
}