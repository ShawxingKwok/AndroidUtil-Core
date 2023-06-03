package pers.shawxingkwok.androidutil.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 * Collects [this] with [collector] every [Fragment.onResume].
 *
 * Warning: Use this function in [Fragment.onCreateView] or [Fragment.onViewCreated].
 */
context(Fragment)
public fun <T> Flow<T>.collectOnResume(collector: FlowCollector<T>){
    require(
        viewLifecycleOwnerLiveData.value != null
        && viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.INITIALIZED
    ){
        "Use this function in 'onCreateView' or 'onViewCreated'."
    }

    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            collect(collector)
        }
    }
}

/**
 * Collects [this] with [collector] every [AppCompatActivity.onResume].
 *
 * Warning: Use this function in [AppCompatActivity.onCreate].
 */
context(AppCompatActivity)
public fun <T> Flow<T>.collectOnResume(collector: FlowCollector<T>){
    require(
        lifecycle.currentState == Lifecycle.State.INITIALIZED
    ){
        "Use this function in 'onCreate'."
    }

    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            collect(collector)
        }
    }
}