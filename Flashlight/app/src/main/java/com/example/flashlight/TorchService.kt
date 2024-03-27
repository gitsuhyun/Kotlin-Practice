package com.example.flashlight

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TorchService : Service() {
    private val torch: Torch by lazy {  //Torch 클래스의 인스턴스를 얻기 위해 by lazy 사용
        Torch(this)
    }

    private var isRunning = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //인텐트에 action값을 설정하여 동작 실행
        when (intent?.action) {
            //앱에서 실행할 경우
            "on" -> {
                torch.flashOn()
                isRunning = true
            }
            "off" -> {
                torch.flashOff()
                isRunning = false
            }
            //서비스에서 실행할 경우
            else -> {
                isRunning != isRunning
                if(isRunning) {
                    torch.flashOn()
                } else {
                    torch.flashOff()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
        //아래 중 하나를 반환하여 값에 따라 시스템이 강제로 종료한 후에 시스템 자원이 회복되어 다시 서비스를 시작할 수 있을 때 어떻게 할지를 결정함
        //1. START_STICKY : null 인텐트로 다시 시작함. 무기한으로 실행중임 (미디어 플레이어와 비슷)
        //2. START_NOT_STICKY : 다시 시작하지 않음
        //3. START_REDELIVER_INTENT : 마지막 인텐트로 시작 (능동적으로 수행 중인 파일 다운로드같은 서비스)
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}