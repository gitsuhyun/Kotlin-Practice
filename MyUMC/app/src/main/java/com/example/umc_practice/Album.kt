package com.example.umc_practice

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = true) var id:Int =0,
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    //수록곡 리스트이므로 생략 가능
    //var songs: ArrayList<Song>? = null
)