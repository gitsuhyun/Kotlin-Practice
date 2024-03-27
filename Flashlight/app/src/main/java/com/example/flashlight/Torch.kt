package com.example.flashlight

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

class Torch(context: Context) {
    private var cameraId: String? = null
    //getSystemService를 통해 안드로이드 시스템에서 제공하는 각종 서비스를 관리하는 매니저 클래스를 생성함
    private val cameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    //카메라를 키고 끄려면 카메라 ID가 필요하다. 클래스 초기화 때 카메라 ID를 얻는다.
    init {
        cameraId = getCameraId()
    }

    //플래시를 키고 끄려면 CameraManager 객체가 필요하고 이를 얻으려면 Context 객체가 필요하다.따라서 Context를 생성자로 받는다.
    fun flashOn() {
        cameraId?.let {
            cameraManager.setTorchMode(it, true)
        }
    }

    fun flashOff() {
        cameraId?.let {
            cameraManager.setTorchMode(it, false)
        }

    }

    private fun getCameraId(): String? {        //카메라 ID를 얻는 메서드
        val cameraIds = cameraManager.cameraIdList  //cameraManager는 기기가 가지고 있는 모든 카메라에 대한 정보 목록을 제공
        for (id in cameraIds) {
            val info = cameraManager.getCameraCharacteristics(id)
            val flashAvailable =
                info.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)    //플래시 가능여부
            val lensFacing = info.get(CameraCharacteristics.LENS_FACING)//렌즈 방향

            if (flashAvailable != null  //플래시 사용 가능
                && flashAvailable
                && lensFacing != null
                && lensFacing == CameraCharacteristics.LENS_FACING_BACK //카메라가 기기의 뒷면을 향하고 있음
            ) {
                return id   //위의 조건 해당하면 반환
            }
        }
        return null         //조건에 해당하지 않으면 null 반환
    }

}




