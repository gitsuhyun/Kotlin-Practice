package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stopwatch.databinding.ActivityMainBinding
import android.widget.TextView
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var time = 0 //시간을 계산할 변수를 0으로 초기화
    private var timerTask: Timer? = null //나중에 timer를 취소하기 위해 Timer 객체를 변수에 저장해둠
    private var isRunning = false
    private var lap = 1

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //onCreate에 동작 실행되도록 로직 짜기
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            isRunning = !isRunning //FAB가 클릭되면 타이머가 동작 중인지 저장하는 변수 반전

            if (isRunning) { //동작 확인 변수 값에 따라 메서드 실행
                start()
            } else {
                pause()
            }
        }

        binding.lapButton.setOnClickListener {
            recordLapTime()
        }

        binding.resetFab.setOnClickListener {
            reset()
        }
    }

    private fun pause() {
        //시작 이미지로 교체
        binding.fab
            .setImageResource(R.drawable.baseline_play_arrow_24)
        timerTask?.cancel() //실행 중인 타이머가 있으면 타이머 취소
    }

    private fun start() {
        //일시정지 이미지로 변경
        binding.fab.setImageResource(R.drawable.baseline_pause_24)
        timerTask = timer(period = 10) {// 0.01초마다 이 변수를 time++에서 증가하며 runOnUiThread에서 UI 갱신
            time++ //시간 변수 증가
            val sec = time / 100
            val milli = time % 100
            runOnUiThread { //UI 갱신, timer는 워커 스레드에서 동작하여 UI 조작이 불가능하므로 runOnUiThread로 감싸서 UI 조작이 가능하게 함
                binding.secTextView.text = "$sec"
                binding.milliTextView.text = "$milli"
            }
        }
    }

    private fun recordLapTime() {
        val lapTime = this.time //현재 시간을 지역 변수에 저장
        val textView = TextView(this) //동적으로 TextView를 생성하여 아래 형태로 설정
        textView.text = "$lap LAP : ${lapTime / 100}.${lapTime % 100}"

        // 맨 위에 랩타임 추가
        //addView를 사용하여 동적으로 뷰를 추가할 수 있음, 두번째 인자에 인덱스값을 지정하면 해당 위치에 뷰가 추가됨 -> 0은 맨 위
        binding.lapLayout.addView(textView, 0)
        lap++
    }

    private fun reset() {
        timerTask?.cancel()     //실행 중인 타이머가 있다면 취소

        // 모든 변수 초기화
        time = 0
        isRunning = false

        binding.fab.setImageResource(R.drawable.baseline_play_arrow_24)
        binding.secTextView.text = "0"
        binding.milliTextView.text = "00"

        // 모든 랩타임을 제거
        binding.lapLayout.removeAllViews()
        lap = 1 //랩 초기화
    }
}