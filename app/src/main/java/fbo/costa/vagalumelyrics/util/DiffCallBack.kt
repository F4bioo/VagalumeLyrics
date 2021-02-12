package fbo.costa.vagalumelyrics.util

import androidx.recyclerview.widget.DiffUtil
import fbo.costa.vagalumelyrics.model.Search

class DiffCallBack(
    private var oldCList: List<Search>,
    private var newList: List<Search>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldCList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCList[oldItemPosition].lyricId == newList[newItemPosition].lyricId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCList[oldItemPosition] == newList[newItemPosition]
    }
}
