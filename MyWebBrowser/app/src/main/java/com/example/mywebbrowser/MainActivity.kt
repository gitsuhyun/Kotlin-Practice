package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.mywebbrowser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //웹뷰 기본 설정
        // 1. javaScriptEnabled 기능을 킨다
        // 2. webViewClient 클래스를 지정해야 웹뷰에 페이지가 표시됨
        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.urlEditText.setText(url)
                }
            }
        }

        //"https://"가 포함된 url을 전달하면 웹뷰에 페이지가 로딩됨
        binding.webView.loadUrl("https://www.google.com")

        //setOnEditorActionListener는 EditText가 선택되고 글자가 입력될 때마다 호출됨
        //인자는 반응한 뷰, 액션ID, 이벤트 3가지이며 사용하지 않는 부분은 _로 대치할 수 있음
        binding.urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {                      //actionID값은 EditInfo 클래스에 상수로 정의된 값 중에서 검색 버튼에 해당하는 상수와 비교하여 검색 버튼이 눌렸는지 확인함
                binding.webView.loadUrl(binding.urlEditText.text.toString())    //검색 창에 입력한 주소를 웹뷰에 전달하여 로딩함. 마지막으로 true를 반환하여 이벤트를 종료함
                true
            }
            else {
                false
            }
        }

        //컨텍스트 메뉴 등록
        //웹뷰를 길게 클릭하면 컨텍스트 메뉴가 표시됨
        registerForContextMenu(binding.webView)

    }

    //뒤로 가기 동작 정의
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu) //메뉴 리소스를 액티비티의 메뉴로 표시하려면 menuInflater 객체의 inflate 메서드를 사용하여 메뉴 리소스를 지정
        return true     //true를 반환하면 액티비티에 메뉴가 있다고 인식함
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {                                        //메뉴 아이템으로 분기 수행
            R.id.action_google, R.id.action_home -> {               //구글, 집 아이콘을 선택하면 구글 페이지 로딩
                binding.webView.loadUrl("https://www.google.com")
                return true
            }
            R.id.action_naver -> {                                  //네이버 클릭하면 네이버 페이지 로딩
                binding.webView.loadUrl("https://www.naver.com")
                return true
            }
            R.id.action_daum -> {                                   //다음 클릭하면 다음 페이지 로딩
                binding.webView.loadUrl("https://www.daum.net")
                return true
            }
            R.id.action_call -> {                                   //연락처 클릭하면 전화 앱을 열음, 이러한 방식을 암시적 인텐트라고 함
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:031-123-4567")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                binding.webView.url?.let { url ->
                    // 문자 보내기
                    sendSMS("031-123-4567", url)
                }
                return true
            }
            R.id.action_email -> {
                binding.webView.url?.let { url ->
                    // 이메일 보내기
                    email("test@example.com", "좋은 사이트", url)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)                    //각 메뉴 처리가 끝나고 true 반환
            //내가 처리하고자 하는 경우를 제외한 그 이외의 경우에는 super 메서드를 호출하는 것이 보편적인 규칙
    }

    //onCreate 메서드에 컨텍스트 메뉴를 표시하기 위한 코드 추가
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                binding.webView.url?.let { url -> //페이지 공유
                }
                return true
            }

            R.id.action_browser -> {
                binding.webView.url?.let { url -> //기본 웹 브라우저에서 열기
                }
                return true
            }
        }
        return super.onContextItemSelected(item)
    }


}