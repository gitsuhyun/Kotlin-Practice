package com.example.todo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.adapter.TodoListAdapter
import com.example.todo.databinding.FragmentFirstBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FirstFragment : Fragment() {
    //프래그먼트에서 activityViewModel 확장 함수를 사용하면 프래그먼트가 속한 액티비티의 생명주기를 따르는 뷰 모델 클래스 인스턴스를 얻을 수 있음
    private val viewModel by activityViewModels<MainViewModel>()
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //layoutManager 설정으로 리사이클러뷰에 표시할 방법을 지정함
        //LinearLayout Manager는 일반 리스트 형태를 나타냄
        //프래그먼트에서 컨텍스트를 얻을 때 requireContext 메서드로 얻을 수 있음
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val todoListAdapter = TodoListAdapter { todo ->
            // 클릭시 처리
            viewModel.selectedTodo = todo
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.recyclerView.adapter = todoListAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect {
                    Log.d("FirstFragment", it.toString())
                    todoListAdapter.submitList(it)      // 표시할 아이템 리스트는 어댑터의 submitList 메서드에 전달함
                    //이 메서드는 ListAdapter클래스가 제공하는 메서드로, 자동으로 변경점을 비교하고 변경이 일어난 아이템을 교체함
                }
            }
        }

        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}