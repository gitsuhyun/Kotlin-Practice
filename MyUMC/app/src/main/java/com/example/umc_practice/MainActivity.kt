package com.example.umc_practice

import LockerFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.umc_practice.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var song: Song = Song()
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_UMCpractice)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        inputDummyAlbums()
        initBottomNavigation()


        val song = Song(
            binding.mainMiniplayerTitleTv.text.toString(),
            binding.mainMiniplayerSingerTv.text.toString(),
            0,
            60,
            false,
            "music_lilac"
        )

        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))
        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.putInt("songId", song.id)
        editor.apply()

        val intent = Intent(this, SongActivity::class.java)

        startActivity(intent)
        }


        Log.d("Song", song.title + song.singer)
    }

    private fun setMiniPlayer(song : Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressbarView.progress = (song.second*100000)/song.playTime
    }
    override fun onStart() {
        super.onStart()
        //액티비티 전환이 될 때 onStart에서 시작되게됨
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song = if (songJson == null) {
//            //데이터가 처음에 없을 때 상황
//            Song("title", "아이유(IU)", 0, 60, false, "music_lilac")
//        } else {
//            //이후 데이터가 존재할 때
//            gson.fromJson(songJson, Song::class.java)
//        }

        val spf=getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0) {
            songDB.SongDao().getSong(1)
        }else{
            songDB.SongDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())

        //mini플레이어에 반영하는 함수
        setMiniPlayer(song)
    }

    private fun initBottomNavigation() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.SongDao().getSongs()

        if(songs.isNotEmpty()) return

        songDB.SongDao().insert(
            Song(
                "Lilac",
                "IU",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.SongDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
            )
        )

        songDB.SongDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                R.drawable.img_album_exp3,
                false,
            )
        )


        songDB.SongDao().insert(
            Song(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
            )
        )


        songDB.SongDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
            )
        )

        val _songs = songDB.SongDao().getSongs()
        Log.d("DB data", _songs.toString())

    }

    private fun inputDummyAlbums(){
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.AlbumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.AlbumDao().insert(
            Album(
                0,
                "IU 5th Album 'LILAC'",
                "아이유(IU)",
                R.drawable.img_album_exp2
            )
        )

        songDB.AlbumDao().insert(
            Album(
                1,
                "FLU",
                "아이유(IU)",
                R.drawable.img_album_exp2
            )
        )

        songDB.AlbumDao().insert(
            Album(
                2,
                "Butter",
                "BTS",
                R.drawable.img_album_exp
            )
        )

        songDB.AlbumDao().insert(
            Album(
                3,
                "Next Level",
                "AESPA",
                R.drawable.img_album_exp3
            )
        )

        songDB.AlbumDao().insert(
            Album(
                4,
                "Boy with Luv",
                "music_boy",
                R.drawable.img_album_exp4
            )
        )

        songDB.AlbumDao().insert(
            Album(
                5,
                "BBoom BBoom",
                "MOMOLAND",
                R.drawable.img_album_exp5
            )
        )

        val _songs = songDB.SongDao().getSongs()
        Log.d("DB data", _songs.toString())

    }

}