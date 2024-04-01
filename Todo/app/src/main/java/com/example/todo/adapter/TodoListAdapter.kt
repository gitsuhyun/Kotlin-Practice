package com.example.todo.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.Todo
import com.example.todo.databinding.ItemTodoBinding
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class TodoListAdapter(
    private val onClick: (Todo) -> Unit,    //생성자로 아이템이 클릭되었을 때 처리할 함수
) : ListAdapter<Todo, TodoListAdapter.TodoViewHolder>(TodoDiffUtilCallback()) {
//ListAdapter를 상속할 때 아이템 클래스와 뷰홀더 클래스 타입을 제네릭으로 지정
// 인자에 DiffUtil.ItemCallback을 구현한 객체를 전달

    private lateinit var binding: ItemTodoBinding

    //뷰홀더 클래스를 생성할 때 인자 전달
    class TodoViewHolder(
        private val binding: ItemTodoBinding,   //바인딩 객체
        private val onClick: (Todo) -> Unit,    //클릭되었을 때 처리할 함수
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {  //bind 메서드는 할 일 객체를 인자로 전달받아 실제로 화면에 표시
            binding.text1.text = todo.title
            binding.text2.text = DateFormat.format("yyyy/MM/dd", todo.date) //년/월/일 형태로 포맷
        }

        //바인딩 객체가 클릭되면 수행되도록 하는 setOnClickListener 메서드 작성
        fun setOnClickListener(todo: Todo) {
            binding.root.setOnClickListener {
                onClick(todo)
            }
        }
    }

    //뷰 홀더를 생성하는 로직을 작성, 바인등 객체를 얻고 뷰 홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        binding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding, onClick)
        //xml 레이아웃 파일을 바인딩 객체로 변경할 때는 액티비티 이외의 클래스에서는 LayoutInflater 클래스와 컨텍스트가 있으면 가능
        //뷰 홀더 객체를 생성, 전달 인자는 바인딩 객체와 클릭시 수행할 함수
    }

    //화면에 각 아이템이 보여질 때마다 호출됨 여기에서 실제로 보여질 내용 설정
    // 아이템이 계속 바뀌므로 바뀔 때마다 클릭 이벤트 설정도 다시 해줌
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setOnClickListener(getItem(position))
    }
}