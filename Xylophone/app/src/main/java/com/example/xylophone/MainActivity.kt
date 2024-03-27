package com.example.xylophone

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.xylophone.databinding.ActivityMainBinding

//소리를 재생하는 방법 2가지
//1. MusicPlayer 클래스 : 소리를 한 번만 재생하는 경우 또는 노래나 배경음과 같은 경우 유용
//2. SoundPool 클래스 : 연속으로 소리를 재생하는 경우

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val soundPool = SoundPool.Builder().setMaxStreams(8).build()

    //텍스트뷰와 음원 파일의 리소스 ID를 연관 지은 Pair 객체 8개를 리스트 객체 sounds로 만듬
    private val sounds by lazy {
        listOf(
            Pair(binding.do1, R.raw.do1),
            Pair(binding.re, R.raw.re),
            Pair(binding.mi, R.raw.mi),
            Pair(binding.fa, R.raw.fa),
            Pair(binding.sol, R.raw.sol),
            Pair(binding.la, R.raw.la),
            Pair(binding.si, R.raw.si),
            Pair(binding.do1, R.raw.do2),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //화면이 가로 모드로 고정되게 하기
        //1. requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        //2. AndroidManifest.xml에서 screenOrientation 속성에 landscape를 설정

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //sounds 리스트와 같이 뷰가 여러 개인 경우, forEach 함수를 사용하여 동적으로 클릭 이벤트 구현
        sounds.forEach { tune(it) }
    }


    //Pair 객체를 받아서 load 메서드로 음원의 ID를 얻고
    private fun tune(pitch: Pair<TextView, Int>) {
        val soundId = soundPool.load(this, pitch.second, 1)
        pitch.first.setOnClickListener {
            //전달받은 Pair 객체의 첫번째 프로퍼티인텍스트 뷰를 얻고 텍스트 뷰를 클릭했을 때 음원을 재생
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        //앱을 종료할 때는 반드시 release 메서드를 호출하여 SoundPool 객체의 자원을 해제
        soundPool.release()
    }
}



