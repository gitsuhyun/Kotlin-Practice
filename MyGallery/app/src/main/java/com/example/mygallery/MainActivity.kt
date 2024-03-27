package com.example.mygallery

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.mygallery.databinding.ActivityMainBinding
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private fun getAllPhotos() {
        val uris = mutableListOf<Uri>() //수정 가능한 리스트 생성

        //모든 사진 정보 가져오기, contentResolver 객체의 query 메서드는 인자 5개를 받음
        contentResolver.query(
            //첫번째 인자 : 어떤 데이터를 가져오느냐를 URI 형태로 저장
            // (사진은 외부 저장소에 저장되어 있기 때문에 외부 저장소에 저장된 데이터를 가리키는 URI인 EXTERNAL_CONTENT_URI를 지정)
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            //두번째 인자 : 어떤 항목의 데이터를 가져올 것인지 String 배열로 지정
            //null로 지정하면 모든 항목을 가져옴
            null,
            //세번째 인자 : 데이터를 가져올 조건을 지정할 수 있음
            //null로 지정하면 전체 데이터를 가져옴
            null,
            //네번째 인자 : 세번째 인자와 조합하여 조건을 지정할 때 사용
            //사용하지 않는다면 null로 지정
            null,
            //다섯번째 인자 : 정렬 방법을 지정
            "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC" //찍은 날짜 내림차순
        )
            ?.use { cursor ->      //use 함수를 사용하면 사용이 끝나고 자동으로 close 메서드를 호출해줌 (close 메서드를 호출해주지 않으면 메모리 누수 발생)
                while (cursor.moveToNext()) {   //사진 정보를 담고있는 Cursor 객체의 내부 포인터로 다음 정보로 이동하는 메서드를 사용 (사진이 없다면 Cursor는 null)
                    //사진 정보 id
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    //Uri 얻기
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    //사진의 Uri들 리스트에 담기
                    uris.add(contentUri)
                }
            }
        Log.d("MainActivity", "getAllPhotos: $uris")

        //ViewPager2 어댑터 연결
        val adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        adapter.uris = uris

        binding.viewPager.adapter = adapter

        //3초마다 자동 슬라이드
        timer(period = 3000) {
            runOnUiThread {
                with(binding) {
                    if (viewPager.currentItem < adapter.itemCount - 1) {           //현재 페이지가 마지막 페이지가 아니라면
                        viewPager.currentItem = viewPager.currentItem + 1    //다음 페이지로 변경
                    } else {
                        viewPager.currentItem = 0                             //마지막 페이지라면 첫 페이지로 변ㄱㅇ
                    }
                }
            }
        }
    }

    //권한 요청 로직 작성
    private val requestPermissonLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            //권한 요청에 대한 처리를 작성하는 부분
                isGranted ->
            if (isGranted) {
                //권한 허용
                getAllPhotos()
            } else {
                //권한 거부
                Toast.makeText(this, "권한 거부 됨", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //권한이 부여되어 있는지 확인
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //이전에 권한이 허용되지 않음 -> true반환 (거부한 적 있음)
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                //이전에 이미 권한이 거부되었을 때 설명
                AlertDialog.Builder(this).apply {
                    setTitle("권한이 필요한 이유")
                    setMessage("사진 정보를 얻으려면 외부 저장소 권한이 필요합니다.")
                    setPositiveButton("권한 요청") { _, _ ->
                        requestPermissonLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                    setNegativeButton("거부", null)
                }.show()
            } else {
                //권한 요청
                requestPermissonLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            return
        }
        //권한이 이미 허용됨
        getAllPhotos()
    }

}