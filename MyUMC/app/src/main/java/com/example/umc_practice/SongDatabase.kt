package com.example.umc_practice

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class, User::class], version = 1, exportSchema = false)
abstract class SongDatabase :RoomDatabase(){
    abstract fun SongDao(): SongDao
    abstract fun UserDao(): UserDao


    companion object {
        private var instance : SongDatabase? = null

        @Synchronized
        fun getInstance(context : Context): SongDatabase? {
            if (instance == null){
                synchronized(SongDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database" //다른 데이터 베이스와 이름 겹치면 꼬임
                    ).allowMainThreadQueries().build() //효율적인 설계를 위해 메인스레드에 넘기면 안됨 (임시적)
                }
            }
            return instance
        }
    }
}