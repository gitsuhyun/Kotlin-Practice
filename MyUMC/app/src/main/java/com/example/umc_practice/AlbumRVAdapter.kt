package com.example.umc_practice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_practice.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface  MyItemClickListener{
        fun onItemClick(album: Album)

        fun onRemoveAlbum(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    //스크롤할 때 생성
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position]) //받아온 데이터를 객체에 넣어주는 작업

        //position 값을 가지고 있기 때문에 recyclerView의 클릭 이벤트를 처리할 수 있다.
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(albumList[position])
        }

//        holder.binding.itemAlbumTitleTv.setOnClickListener {
//            mItemClickListener.onRemoveAlbum(position)
//        }
    }

    override fun getItemCount(): Int =  albumList.size

    //뷰홀더란 아이템 객체를 재활용하기 위해 날라가지 않도록 붙잡아두는 역할을 함
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource((album.coverImg!!))
        }
    }

    fun addItem(album: Album) {
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }


}