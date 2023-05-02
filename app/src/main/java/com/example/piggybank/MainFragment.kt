package com.example.piggybank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.piggybank.databinding.MainFragmentBinding

class MainFragment : Fragment(R.layout.main_fragment) {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter = CategoriesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listCategories.adapter = adapter
        adapter.submitList(listOf(
            CategoryItem("clothes", R.drawable.ic_clothes, false),
            CategoryItem("food", R.drawable.ic_food, false),
            CategoryItem("gift", R.drawable.ic_gift, true),
        ))
        setUpKeyboard()
    }

    private fun setUpKeyboard() {
        with(binding.keyboard) {
            fun setNumbers(input: String) {
                tvNumbers.text = input
            }

            arrayOf(
                tv0,
                tv1,
                tv2,
                tv3,
                tv4,
                tv5,
                tv6,
                tv7,
                tv8,
                tv9,
                tvDot,
                tvPlus,
                tvMinus
            ).forEach { textView ->
                textView.setOnClickListener {
                    setNumbers(tvNumbers.text.toString() + textView.text.toString())
                }
            }

            tvReset.setOnClickListener {
                setNumbers("")
            }
            btnDelete.setOnClickListener {
                setNumbers(tvNumbers.text.toString().dropLast(1))
            }

            tvSum.setOnClickListener {
                setNumbers(Calculator().calculate(tvNumbers.text.toString()).toString())
            }
        }
    }
}


