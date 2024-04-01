package com.example.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.todo.data.Todo
import com.example.todo.data.TodoDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

//AndroidViewModel은 액티비티와 수명을 같이 함 -> 액티비티의 수명이란 완전히 종료되는 것을 말함
class MainViewModel(application: Application) : AndroidViewModel(application) {
    //Room 데이터베이스
    //databaseBuilder에 전달하는 인자로는 Application 객체와 데이터베이스 클래스, 데이터베이스 이름이 필요함
    private val db = Room.databaseBuilder(
        application,
        TodoDatabase::class.java, "todo"
    ).build()

    // DB의 결과를 관찰할 수 있도록 하는 방법 ③
    private val _items = MutableStateFlow<List<Todo>>(emptyList())

    //StateFlow는 현재 데이터와 새로운 데이터 업데이트를 이를 관찰하는 곳에 보내는 데이터 흐름 표현하며
    //데이터를 UI에 노출시킬 때 사용
    val items: StateFlow<List<Todo>> = _items

    var selectedTodo: Todo? = null

    // 초기화시 모든 데이터를 읽어 옴 -> StateFlow로 외부에 노출되도록 함
    init {
        // ViewModel과 AndroidViewModel 클래스는 viewModelScope 코루틴 스코프를 제공
        // launch 함수 내에서 suspend 메서드를 실행할 수 있고 이는 비동기로 동작함
        viewModelScope.launch {
            // Flow 객체는 collect로 현재 값을 가져올 수 있음
            db.todoDao().getAll().collect { todos ->
                // StateFlow 객체는 value 프로퍼티로 현재 상태값을 읽거나 쓸 수 있음
                _items.value = todos
            }
        }
    }


    fun addTodo(text: String, date: Long) {
        //viewModelScope를 통해 코루틴 스코프 사용 가능
        //코루틴은 코틀린에서 제공하는 비동기 처리 방식
        //코루틴 코드는 코루틴 스코프라는 객체를 통해서 실행 가능
        viewModelScope.launch {
            db.todoDao().insert(Todo(text, date))
        }
    }

    fun updateTodo(text: String, date: Long) {
            selectedTodo?.let { todo ->                 //찾았다면 tite에 수정할 내용을
                todo.apply {        // date에 수정한 시간을 지정하여
                    this.title = text
                    this.date = date
                }

                // 업데이트 수행
                viewModelScope.launch {
                    db.todoDao().update(todo)
                }
                //수정이 완료되면 객체는 null로 할당해줘야함
                selectedTodo = null
            }
    }


    fun deleteTodo(id: Long) {
        _items.value
            .find { todo -> todo.id == id }     //id로 객체 검색
            ?.let { todo -> //찾으면
                viewModelScope.launch {
                    db.todoDao().delete(todo)       //삭제
                }
                //삭제가 완료되면 객체는 null로 할당해줘야함
                selectedTodo = null
            }
    }

}