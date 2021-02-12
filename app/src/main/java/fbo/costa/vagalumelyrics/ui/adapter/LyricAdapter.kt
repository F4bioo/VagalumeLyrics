package fbo.costa.vagalumelyrics.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fbo.costa.vagalumelyrics.R
import fbo.costa.vagalumelyrics.databinding.ItemGridAdapterBinding
import fbo.costa.vagalumelyrics.databinding.ItemListAdapterBinding
import fbo.costa.vagalumelyrics.model.Search
import fbo.costa.vagalumelyrics.util.DiffCallBack

class LyricAdapter(
    private val onClickListener: (search: Search) -> Unit
) : RecyclerView.Adapter<LyricAdapter.ViewHolder>() {

    private val lyricList = arrayListOf<Search>()
    private var withGrid = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val biding = if (withGrid) itemGrid(parent) else itemList(parent)
        return ViewHolder(biding.root, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val search = lyricList[position]
        holder.viewBind(search)
    }

    override fun getItemCount(): Int {
        return lyricList.size
    }

    inner class ViewHolder(
        itemView: View,
        private val onClickListener: (search: Search) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val imageArtist = itemView.findViewById<ImageView>(R.id.image_artist)
        private val textTitle = itemView.findViewById<TextView>(R.id.text_title)
        private val textArtist = itemView.findViewById<TextView>(R.id.text_artist)

        fun viewBind(search: Search) {
            imageArtist.loadImage(search.imageUrl)
            textTitle.text = search.title
            textArtist.text = search.band

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

    fun setBinding(withGrid: Boolean) {
        this.withGrid = withGrid
    }

    private fun ImageView.loadImage(imageUrl: String?) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_image_placeholder_100dp)
            .error(R.drawable.ic_image_error_100dp)
            .centerCrop()
            .into(this)
    }

    private fun itemGrid(parent: ViewGroup) =
        ItemGridAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    private fun itemList(parent: ViewGroup) =
        ItemListAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
}
