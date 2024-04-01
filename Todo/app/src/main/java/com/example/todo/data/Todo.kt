package com.example.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity //Room db에서 사용할 엔티티 클래스라는 뜻
data class Todo (   //data 클래스로 필요한 항목을 가지는 클래스를 만듬
    var title: String,
    var date: Long = Calendar.getInstance().timeInMillis    //따로 지정하지 않아도 기본값으로 혀재 날짜를 사용하도록 calendar 사용
){
    // id는 유일한 값이 되어야하므로 PrimaryKey로 지정해야함, 식별키
    @PrimaryKey(autoGenerate = true)    //기본키를 직접 지정하지 않아도 자동으로 증가하도록 autoGenerate 옵션 추가
    var id: Long =0
}