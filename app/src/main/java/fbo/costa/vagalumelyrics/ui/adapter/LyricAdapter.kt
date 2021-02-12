package fbo.costa.vagalumelyrics.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fbo.costa.vagalumelyrics.R
import fbo.costa.vagalumelyrics.databinding.ItemAdapterBinding
import fbo.costa.vagalumelyrics.model.Search
import fbo.costa.vagalumelyrics.util.DiffCallBack

class LyricAdapter(
    private val onClickListener: (search: Search) -> Unit
) : RecyclerView.Adapter<LyricAdapter.ViewHolder>() {

    private val lyricList = arrayListOf<Search>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val biding = ItemAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(biding, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val search = lyricList[position]
        holder.viewBind(search)
    }

    override fun getItemCount(): Int {
        return lyricList.size
    }

    inner class ViewHolder(
        private val biding: ItemAdapterBinding,
        private val onClickListener: (search: Search) -> Unit
    ) : RecyclerView.ViewHolder(biding.root) {

        fun viewBind(search: Search) {
            biding.apply {
                imageItem.loadImage(search.imageUrl)
                textTitle.text = search.title
                textArtist.text = search.band
            }

            itemView.setOnClickListener {
                onClickListener(search)
            }
        }
    }

    fun submitList(newList: List<Search>) {
        val oldList = lyricList
        val diffResult = DiffUtil.calculateDiff(
            DiffCallBack(oldList, newList)
        )

        lyricList.clear()
        lyricList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun ImageView.loadImage(imageUrl: String?) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_image_placeholder_100dp)
            .error(R.drawable.ic_image_error_100dp)
            .centerCrop()
            .into(this)
    }
}
