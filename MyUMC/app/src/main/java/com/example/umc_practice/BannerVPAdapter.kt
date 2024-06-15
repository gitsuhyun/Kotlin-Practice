package com.example.umc_practice

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) :FragmentStateAdapter(fragment) {
    private  val fragmentList : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return  fragmentList.size
    }
    //더 간단하게 쓰는 방식
//    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position] //0부터 끝까지 반환

    //처음에 아무것도 없을때 사용함
    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        //새로운 값을 추가해서 보여줌
        notifyItemInserted(fragmentList.size-1)
    }
}