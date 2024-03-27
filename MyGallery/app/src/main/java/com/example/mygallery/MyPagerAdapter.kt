package com.example.mygallery

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    //뷰페이저가 표시할 프래그먼트 목록, 외부에서 추가할 수 있도록 var로 정의
    var uris = mutableListOf<Uri>()

    //표시할 프래그먼트의 개수
    override fun getItemCount(): Int {
        return uris.size
    }

    //position 위치의 프래그먼트
    override fun createFragment(position: Int): Fragment {
        return PhotoFragment.newInstance(uris[position])
    }
}