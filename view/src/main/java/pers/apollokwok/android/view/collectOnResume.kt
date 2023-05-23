package pers.apollokwok.android.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 * Warning: use this function between 'onViewCreated' and 'onDestroyView'.
 */
context(Fragment)
public fun <T> Flow<T>.collectOnResume(collector: FlowCollector<T>){
    require(
        viewLifecycleOwnerLiveData.value != null
        && viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.INITIALIZED
    ){
        "Call this function in 'onCreateView' or 'onViewCreated'."
    }

    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            collect(collector)
        }
    }
}