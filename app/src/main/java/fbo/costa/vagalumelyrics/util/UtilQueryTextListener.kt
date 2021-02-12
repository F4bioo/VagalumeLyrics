package fbo.costa.vagalumelyrics.util

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UtilQueryTextListener(
    lifecycle: Lifecycle,
    private val utilTextQueryListener: (String?) -> Unit
) : SearchView.OnQueryTextListener, LifecycleObserver {

    private val coroutineScope = lifecycle.coroutineScope
    private var job: Job? = null

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        job?.cancel()
        job = coroutineScope.launch {
            newText?.let {
                delay(500)
                utilTextQueryListener(newText)
            }
        }
        return false
    }
}
