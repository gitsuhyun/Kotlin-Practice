package com.example.umc_practice

data class Album(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    //수록곡 리스트이므로 생략 가능
    var songs: ArrayList<Song>? = null
)
