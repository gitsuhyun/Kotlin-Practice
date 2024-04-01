package com.example.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todo.databinding.FragmentSecondBinding
import java.time.Month
import java.util.Calendar

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedTodo?.let {
            binding.todoEditText.setText(it.title)
            binding.calendarView.date = it.date
        }

        //CalendarView에서 선택한 날짜를 저장할 Calendar 객체를 선언
        val calendar = Calendar.getInstance()

        //날짜가 변경되면 setOnDateChangeListener를 통해 년, 월, 일, 값을 얻음
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        }

        binding.doneFab.setOnClickListener {
            if (binding.todoEditText.text.toString().isNotEmpty()) {
                if(viewModel.selectedTodo != null) {
                    viewModel.updateTodo(
                        binding.todoEditText.text.toString(),
                        calendar.timeInMillis,  //timeInMillis를 통해 객체에 저장된 시간 정보를 적용
                    )
                } else {
                    viewModel.addTodo(
                        binding.todoEditText.text.toString(),
                        calendar.timeInMillis,  //timeInMillis를 통해 객체에 저장된 시간 정보를 적용
                    )
                }
                //findNavController는 이 프래그먼트가 속한 액티비티가 가지고 있는 NavHostFragment의 컨트롤러 객체를 찾음
                //popBackStack 메서드로 이전 화면으로 돌아갈 수 있음
                findNavController().popBackStack()
            }
        }

        binding.deleteFab.setOnClickListener {
            viewModel.deleteTodo(viewModel.selectedTodo!!.id)
            findNavController().popBackStack()
        }

        //선택된 할 일이 없을 때는 지우기 버튼 감추기
        if(viewModel.selectedTodo == null) {
            binding.deleteFab.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}