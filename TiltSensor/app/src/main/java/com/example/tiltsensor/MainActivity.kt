package com.example.tiltsensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager

class MainActivity : AppCompatActivity(), SensorEventListener {
    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private lateinit var tiltView: TiltView //TiltView의 늦은 초기화 선언


    override fun onCreate(savedInstanceState: Bundle?) {
        //화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //화면이 가로 모드로 고정되게 하기
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)

        tiltView = TiltView(this)   //onCreate 메서드에서 생성자에 this를 넘겨서 TiltView를 초기화
        setContentView(tiltView)           //tiltView를 전체 레이아웃으로 선언
    }

    //일반적으로 센서의 사용 등록은 onResume 메서드에서 수행함
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(     //사용할 센서 등록하는 메서드
            this,   //첫번째 인자는 센서값을 받을 sensorEventListener로, 여기서는 this를 지정하여 액티비티에서 센서값을 받도록 함
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  //getDefaultSensor 메서드로 사용할 센서 종류를 지정함 -> 가속도 센서
            SensorManager.SENSOR_DELAY_NORMAL)                          //센서값을 얼마나 자주 받을지를 지정함
    }


    //센서값이 변경되면 호출됨, SensorEvent 객체에 센서가 측정한 값들과 여러 정보가 넘어옴
    override fun onSensorChanged(event: SensorEvent?) {
        // 센서 값이 변경되면 호출 됨
        // values[0]: x 축 값 : 위로 기울이면 -10~0, 아래로 기울이면 : 0~10
        // values[1]: y 축 값 : 왼쪽으로 기울이면 -10~0, 오른쪽으로 기울이면 : 0~10
        // values[2]: z 축 값 : 미사용

        event?.let {
            //디버그용 로그를 표시할 때 사용함, Log.d([태그], [메시지]) :
            //태그 : 로그캣에는 많은 내용이 표시되므로 필터링할 때 사용
            //메시지 : 출력할 메시지 작성
            Log.d("MainActivity", "onSensorChanged: " +
                    "x : ${event.values[0]}, y : ${event.values[1]}, z : ${event.values[2]}")

            //센서값이 변경되었을 때 TiltView에 센서값을 전달함
            tiltView.onSensorEvent(event)
        }
    }

    //센서 정밀도가 변경되면 호출됨
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    //액티비티가 동작 중일때만 센서를 사용하려면 화면이 꺼지기 직전인 onPause 메서드에서 센서를 해제
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


}