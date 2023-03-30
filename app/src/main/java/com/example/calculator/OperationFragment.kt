package com.example.calculator

import android.os.Bundle
import constants.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calculator.databinding.FragmentOperationBinding

class OperationFragment : Fragment() {
    private lateinit var operation: String
    private lateinit var binding: FragmentOperationBinding
    private val zeroInDouble = 0E308
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentManager = parentFragmentManager
        binding = FragmentOperationBinding.inflate(inflater)
        if (savedInstanceState != null) {
            operation = savedInstanceState.getString(Constants.OPERATION.name).toString()
            binding.operationButton.text = operation
        }
        fragmentManager.setFragmentResultListener(Constants.OPERATION.name, this) { _, result ->
            binding.operationButton.text = result.getString(Constants.OPERATION.name).toString()
            operation = binding.operationButton.text.toString()
        }
        binding.operationButton.setOnClickListener {
            if (!(binding.number1EditText.text.isEmpty() || binding.number2EditText.text.isEmpty())) {
                if (!(binding.number2EditText.text.toString()
                        .toDouble() == 0.0 && binding.operationButton.text.toString() == getString(R.string.div))
                ) {
                    val number1 = binding.number1EditText.text.toString().toDouble()
                    val number2 = binding.number2EditText.text.toString().toDouble()
                    val result = Bundle()
                    val fragmentTransaction = fragmentManager.beginTransaction()
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
                    fragmentTransaction.remove(this)
                    fragmentManager.popBackStack()
                    fragmentTransaction.commit()
                } else {
                    Toast.makeText(context, R.string.non_zero_alert, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, R.string.edit_text_empty_message, Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.OPERATION.name, operation)

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
}