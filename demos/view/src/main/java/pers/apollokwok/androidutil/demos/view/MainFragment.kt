package pers.apollokwok.androidutil.demos.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import pers.apollokwok.androidutil.demos.view.databinding.FragmentMainBinding
import pers.shawxingkwok.androidutil.view.KFragment
import pers.shawxingkwok.androidutil.view.collectOnResume

class MainFragment : KFragment<FragmentMainBinding>(FragmentMainBinding::class) {

    private val vm: MainViewModel by viewModels()

    private val rvAdapter: RvAdapter by withView {
        RvAdapter(
            scope = viewLifecycleOwner.lifecycleScope,
            users = vm.items.value
        )
        .also { binding.rvTop.adapter = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.items.collectOnResume{
            rvAdapter.users = it
            rvAdapter.update()
        }
    }

    @OnClick(R.id.btn)
    private fun onClick1(){
        val newTopUserId = rvAdapter.users.last().id + 1
        vm.items.value = rvAdapter.users + User(newTopUserId)
    }
}