package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //by lazy로 인해 늦은 초기화가 됨 이는 ActivityMainBinding이라는 클래스(activity_main.xml로부터 자동으로 생성된 클래스)로부터 생성됨
    //이 객체를 통해 activity_main.xml 파일에 정의한 뷰에 접근할 수 있음
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //onCreate 메서드에 추가한 코드에 의해 바인딩 객체의 근원인 activity_main.xml을 액티비티 화면으로 인식하게 됨
        //이 코드는 모든 액티비티를 만든 후 가장 먼저 선행되어야함
        super.onCreate(savedInstanceState)
        //binding.root로 수정
        setContentView(binding.root)

        loadData()

        binding.resultButton.setOnClickListener {
            if(binding.weightEditText.text.isNotBlank() && binding.heightEditText.text.isNotBlank()){
                //마지막에 입력한 내용 저장
                saveData(
                    binding.heightEditText.text.toString().toFloat(),
                    binding.weightEditText.text.toString().toFloat(),
                    )


                //결과 버튼이 클릭되면 할 일을 작성하는 부분
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("weight", binding.weightEditText.text.toString().toFloat())
                    putExtra("height", binding.heightEditText.text.toString().toFloat())

                }
                startActivity(intent)
                //안드로이드 액티비티를 전환할 때마다 인텐트 객체를 생성하고 startActivity 메서드를 호출함

            }
        }
    }


        private fun saveData(height: Float, weight: Float) {
            val pref = PreferenceManager.getDefaultSharedPreferences(this)    //프리퍼런스 매니저를 사용해 객체를 얻음
            val editor = pref.edit()    //객체의 에디터 객체를 얻음, 이 객체를 사용해 프리퍼런스에 데이터를 담을 수 있음

            //put 메서드는 기본 타입은 모두 지원함, SharePreference는 Double 형태를 지원하지 않으므로 Float 형을 사용
            editor.putFloat("KEY_HEIGHT", height)    //에디터 객체에 put[데이터 타입] 형식의 메서드를 사용하여 키와 값 형태의 쌍으로 저장을 함
                .putFloat("KEY_WEIGHT", weight)
                .apply()                  //설정한 내용 반영
        }

        private fun loadData() {
            val pref = PreferenceManager.getDefaultSharedPreferences(this)  //프리퍼런스 객체를 얻음
            val height = pref.getFloat("KEY_HEIGHT", 0f)   //getFloat 메서드로 키와 몸무게에 저장된 값을 불러옴, 0f는 저장된 값이 없을 때 기본값 0f를 리턴한다느 의미
            val weight = pref.getFloat("KEY_WEIGHT", 0f)

            if (height != 0f && weight != 0f) {     //키와 몸무게가 모두 0f인 경우, 즉 저장된 값이 없을 때는 아무것도 하지 않음
                binding.heightEditText.setText(height.toString())  //저장된 값이 있다면 키와 몸무게를 입력하는 에디터텍스트에 마지막에 저장된 값을 표시함
                binding.weightEditText.setText(weight.toString())
            }
        }

}