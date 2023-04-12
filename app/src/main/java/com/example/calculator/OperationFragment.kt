package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import constants.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.calculator.databinding.FragmentOperationBinding

class OperationFragment : Fragment() {
    private var operation: String = R.string.empty_string.toString()
    private lateinit var binding: FragmentOperationBinding
    private val zeroInDouble = 0E308
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOperationBinding.inflate(inflater)
        arguments?.getString(Constants.OPERATION.name)?.let {
            binding.operationButton.text = arguments?.getString(Constants.OPERATION.name).toString()
        }
        arguments = Bundle()
        operation = binding.operationButton.text.toString()
        if (binding.operationButton.text == getString(R.string.empty_string)) {
            binding.number1EditText.makeInaccessible()
            binding.number2EditText.makeInaccessible()
        }
        val fragmentManager = parentFragmentManager
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (binding.operationButton.text == getString(R.string.empty_string)) {
                val operationFragment = fragmentManager.findFragmentById(R.id.fragment_b_container)
                if (operationFragment != null) {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.detach(operationFragment)
                    fragmentTransaction.commit()
                }
                activity?.let {
                    val fragmentBContainer: FrameLayout =
                        activity?.findViewById(R.id.fragment_b_container)!!
                    val fragmentBLayoutParams =
                        fragmentBContainer.layoutParams as LinearLayout.LayoutParams
                    val fragmentAContainer: FrameLayout =
                        activity?.findViewById(R.id.fragment_a_container)!!
                    val fragmentALayoutParams =
                        fragmentAContainer.layoutParams as LinearLayout.LayoutParams
                    fragmentALayoutParams.weight = 1f
                    fragmentBLayoutParams.weight = 0f
                    fragmentAContainer.layoutParams = fragmentALayoutParams
                    fragmentBContainer.layoutParams = fragmentBLayoutParams
                }
            } else {
                activity?.let {
                    val fragmentBContainer: FrameLayout =
                        activity?.findViewById(R.id.fragment_b_container)!!
                    val fragmentBLayoutParams =
                        fragmentBContainer.layoutParams as LinearLayout.LayoutParams
                    val fragmentAContainer: FrameLayout =
                        activity?.findViewById(R.id.fragment_a_container)!!
                    val fragmentALayoutParams =
                        fragmentAContainer.layoutParams as LinearLayout.LayoutParams
                    fragmentALayoutParams.weight = 0f
                    fragmentBLayoutParams.weight = 1f
                    fragmentAContainer.layoutParams = fragmentALayoutParams
                    fragmentBContainer.layoutParams = fragmentBLayoutParams
                }
            }
        }
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val operationFragment =
                        fragmentManager.findFragmentById(R.id.fragment_b_container)
                    if (operationFragment != null) {
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.detach(operationFragment)
                        fragmentTransaction.commit()
                    }
                    binding.operationButton.text = getString(R.string.empty_string)
                    activity?.let {
                        val fragmentBContainer: FrameLayout =
                            activity?.findViewById(R.id.fragment_b_container)!!
                        val fragmentBLayoutParams =
                            fragmentBContainer.layoutParams as LinearLayout.LayoutParams
                        val fragmentAContainer: FrameLayout =
                            activity?.findViewById(R.id.fragment_a_container)!!
                        val fragmentALayoutParams =
                            fragmentAContainer.layoutParams as LinearLayout.LayoutParams
                        fragmentALayoutParams.weight = 1f
                        fragmentBLayoutParams.weight = 0f
                        fragmentAContainer.layoutParams = fragmentALayoutParams
                        fragmentBContainer.layoutParams = fragmentBLayoutParams
                    }
                }
            }
            activity?.onBackPressedDispatcher?.addCallback(
                viewLifecycleOwner,
                onBackPressedCallback
            )
        }
        binding.operationButton.setOnClickListener {
            if (binding.operationButton.text != getString(R.string.empty_string)) {
                if ((!(binding.number1EditText.text.isEmpty() || binding.number2EditText.text.isEmpty()))) {
                    if (!(binding.number2EditText.text.toString()
                            .toDouble() == 0.0 && binding.operationButton.text.toString() == getString(
                            R.string.div
                        ))
                    ) {
                        val number1 = binding.number1EditText.text.toString().toDouble()
                        val number2 = binding.number2EditText.text.toString().toDouble()
                        val result = Bundle()
                        val resultString = getString(
                            R.string.result_string,
                            number1.toLong(),
                            when (binding.operationButton.text.toString()) {
                                getString(R.string.add) -> getString(R.string.plus)
                                getString(R.string.sub) -> getString(R.string.minus)
                                getString(R.string.mul) -> getString(R.string.asterisk)
                                else -> getString(R.string.slash)
                            },
                            number2.toInt(),
                            operation(number1, number2, operation)
                        )
                        result.putString(Constants.RESULT.name, resultString)
                        fragmentManager.setFragmentResult(Constants.RESULT.name, result)
                        binding.operationButton.text = getString(R.string.empty_string)
                        with(binding.number1EditText) {
                            text.clear()
                            makeInaccessible()
                        }
                        with(binding.number2EditText) {
                            text.clear()
                            makeInaccessible()
                        }
                        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            val operationFragment =
                                fragmentManager.findFragmentById(R.id.fragment_b_container)
                            if (operationFragment != null) {
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.detach(operationFragment)
                                fragmentTransaction.commit()
                            }
                            activity?.let {
                                val fragmentBContainer: FrameLayout =
                                    activity?.findViewById(R.id.fragment_b_container)!!
                                val fragmentBLayoutParams =
                                    fragmentBContainer.layoutParams as LinearLayout.LayoutParams
                                val fragmentAContainer: FrameLayout =
                                    activity?.findViewById(R.id.fragment_a_container)!!
                                val fragmentALayoutParams =
                                    fragmentAContainer.layoutParams as LinearLayout.LayoutParams
                                fragmentALayoutParams.weight = 1f
                                fragmentBLayoutParams.weight = 0f
                                fragmentAContainer.layoutParams = fragmentALayoutParams
                                fragmentBContainer.layoutParams = fragmentBLayoutParams
                            }
                        }
                    } else {
                        Toast.makeText(context, R.string.non_zero_alert, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, R.string.edit_text_empty_message, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(context, R.string.no_operation_chosen_error, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.OPERATION.name, operation)
        if (this::binding.isInitialized) {
            arguments?.putString(Constants.OPERATION.name, binding.operationButton.text.toString())
            if (binding.number1EditText.text.isNotEmpty())
                arguments?.putString(
                    Constants.EDITTEXT1.name,
                    binding.number1EditText.text.toString()
                )
            if (binding.number2EditText.text.isNotEmpty())
                arguments?.putString(
                    Constants.EDITTEXT2.name,
                    binding.number2EditText.text.toString()
                )
        }
    }

    private fun operation(a: Double, b: Double, operation: String): String {
        return when (operation) {
            getString(R.string.add) -> {
                with(a + b) {
                    if (this.isDecimalZero())
                        this.toInt()
                    else
                        this
                }
            }
            getString(R.string.sub) -> {
                with(a - b) {
                    if (this.isDecimalZero())
                        this.toInt()
                    else
                        this
                }
            }
            getString(R.string.mul) -> {
                with(a * b) {
                    if ((this.isDecimalZero()) && (this < Long.MAX_VALUE.toDouble())) {
                        this.toLong()
                    } else
                        this
                }
            }
            else -> {
                if (b == 0.0) {
                    R.string.undefined
                } else {
                    with(a / b) {
                        if (this.isDecimalZero())
                            this.toInt()
                        else
                            this
                    }
                }
            }
        }.toString()
    }

    private fun Double.isDecimalZero() = when (this % 1) {
        zeroInDouble -> true
        else -> false
    }

    private fun EditText.makeInaccessible() {
        isClickable = false
        isFocusable = false
    }
}