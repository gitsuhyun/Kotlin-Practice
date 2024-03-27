package com.example.mygallery

import androidx.core.content.ContentProviderCompat.requireContext
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.load

private const val ARG_URI = "uri"

class PhotoFragment : Fragment() {
   private lateinit var uri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //프래그먼트가 생성되면 onCreate가 호출되고 ARG_URI 키에 저장된 uri 값을 얻어서 변수에 저장
        arguments?.getParcelable<Uri>(ARG_URI)?.let {
            uri = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //프래그먼트에 표시될 뷰 생성, 액티비티가 아닌 곳에서 레이아웃 리소스를 가지고 오려면 LayoutInflater 객체의 inflate 메서드를 사용
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //프래그먼트에서 콘텐츠 프로바이더에 접근하려면 컨텍스트가 필요함
        val descriptor = requireContext().contentResolver.openFileDescriptor(uri, "r")
        descriptor?.use {
            val bitmap = BitmapFactory.decodeFileDescriptor(descriptor.fileDescriptor)
            //프래그먼트는 뷰바인딩을 사용하면 메모리 해제도 고려해야 하기때문에 더 복잡해짐 -> 전통적인 방식으로 사용
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            imageView.load(bitmap)
        }
    }

    //newInstance를 사용하여 프래그먼트를 생성할 수 있고 인자로 uri 값을 전달함
    //이 값은 Bundle 객체에 ARG_URI 키로 저장되고 arguments 프로퍼티에 저장됨
    companion object {
        @JvmStatic
        fun newInstance(uri: Uri) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_URI, uri)
                }
            }
    }
}