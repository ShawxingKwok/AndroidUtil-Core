package pers.apollokwok.androidutil.demo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.flowOf
import pers.apollokwok.android.view.KFragment
import pers.apollokwok.android.view.collectOnResume
import pers.apollokwok.androidutil.demo.MLog
import pers.apollokwok.androidutil.demo.R
import pers.apollokwok.androidutil.demo.databinding.FragmentMainBinding

class MainFragment : KFragment<FragmentMainBinding>(FragmentMainBinding::class) {

    private val topAdapter: SingleItemAdapter by withView {
        SingleItemAdapter(
            scope = lifecycleScope,
            initialItems = (0..20).map(::User),
        )
        .also { binding.rvTop.adapter = it }
    }

    private val bottomAdapter: MultipleItemAdapter by withView {
        MultipleItemAdapter(
            scope = lifecycleScope,
            initialItems = (100..120).map(::User)
        )
        .also { binding.rvBottom.adapter = it }
    }

    @OnClick(R.id.btn)
    private fun onClick1(){
        val newTopUserId = topAdapter.currentList.last().id + 1
        val newTopList = topAdapter.currentList + User(newTopUserId)
        val newBottomList = newTopList.map { User(it.id + 100) }
        topAdapter.submitList(newTopList)
        bottomAdapter.submitList(newBottomList)
    }
}