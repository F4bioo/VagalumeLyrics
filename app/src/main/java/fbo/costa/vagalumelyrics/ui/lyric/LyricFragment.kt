package fbo.costa.vagalumelyrics.ui.lyric

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fbo.costa.vagalumelyrics.R

class LyricFragment : Fragment() {

    companion object {
        fun newInstance() = LyricFragment()
    }

    private lateinit var viewModel: LyricViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.lyric_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LyricViewModel::class.java)
        // TODO: Use the ViewModel
    }

}