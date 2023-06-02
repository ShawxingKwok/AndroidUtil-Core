package pers.apollokwok.androidutil.demos.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import pers.apollokwok.androidutil.demos.view.databinding.FragmentMainBinding
import pers.shawxingkwok.androidutil.view.KFragment
import pers.shawxingkwok.androidutil.view.collectOnResume
import pers.shawxingkwok.ktutil.allDo

class MainFragment : KFragment<FragmentMainBinding>(FragmentMainBinding::class) {

    private val vm: MainViewModel by viewModels()

    private val topAdapter: SingleItemAdapter by withView {
        SingleItemAdapter(
            scope = vm.viewModelScope,
            initialItems = vm.topItems.value,
        )
        .also { binding.rvTop.adapter = it }
    }

    private val bottomAdapter: MultipleItemsAdapter by withView {
        MultipleItemsAdapter(
            scope = vm.viewModelScope,
            initialItems = vm.bottomItems.value
        )
        .also { binding.rvBottom.adapter = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.topItems.collectOnResume(topAdapter::submitList)
        vm.bottomItems.collectOnResume(bottomAdapter::submitList)
    }

    @OnClick(R.id.btn)
    private fun onClick1(){
        val newTopUserId = topAdapter.currentList.last().id + 1
        vm.topItems.value = topAdapter.currentList + User(newTopUserId)

        val newBottomUserId = bottomAdapter.currentList.last().id + 1
        vm.bottomItems.value = bottomAdapter.currentList + User(newBottomUserId)
    }
}