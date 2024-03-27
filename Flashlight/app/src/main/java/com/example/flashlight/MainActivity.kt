package com.example.flashlight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.flashlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.flashSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(Intent(this, TorchService::class.java).apply {
                    action = "on"
                })//스위치가 켜지면 flashOn 메서드를 호출해서 플래시 켜기
            } else {
                startService(Intent(this, TorchService::class.java).apply {
                    action = "off"
                })//스위치가 꺼지면 flashOff 메서드를 호출해서 플래시 끄기
            }
        }
    }
}