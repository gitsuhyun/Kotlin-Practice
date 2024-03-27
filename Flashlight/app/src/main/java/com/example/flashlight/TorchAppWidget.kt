package com.example.flashlight

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

//앱 위젯을 클릭할 때의 동작 작성

class TorchAppWidget : AppWidgetProvider() {
    //위젯이 업데이트되어야 할 때 호출
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {     //위젯이 여러개 배치되었다면 모든 위젯 업데이트
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    //위젯이 처음 생성될 때 호출
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    //위젯이 여러 개일 경우 마지막 위젯이 제거될 때 호출
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

//위젯을 업데이트할 때 수행되는 코드
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.torch_app_widget) //위젯은 액티비티와 달리, 배치하는 뷰가 따로 있음 -> RemoteViews로 가져올 수 있음
    views.setTextViewText(R.id.appwidget_text, widgetText)                  //setTextViewText 메서드는 RemoteViews 객체용으로 준비된 텍스트값을 변경하는 메서드

    val intent = Intent(context, TorchService::class.java)
    //TorchService를 실행하기 위해 전달할 인자는 아래의 4개이다.
    //컨텍스트, 리퀘스트 코드, 서비스 인텐트, 플래그
    val pendingIntent = PendingIntent.getService(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE    //위젯의 모양이 변하지 않는다면 FLAG_IMMUTABLE 반환
    )

    views.setOnClickPendingIntent(R.id.appwidget_layout,pendingIntent)  //클릭 이벤트 연결, 클릭이 발생할 뷰의 ID와 PendingIntent 필요

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}