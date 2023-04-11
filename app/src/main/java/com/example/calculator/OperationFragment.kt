package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import constants.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calculator.databinding.FragmentOperationBinding

class OperationFragment : Fragment() {
    private var operation: String = R.string.empty_string.toString()
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
            val fragmentTransaction = fragmentManager.beginTransaction()
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (fragmentManager.backStackEntryCount == 0) {
                    fragmentTransaction.remove(this)
                    fragmentTransaction.commit()
                    val operationFragmentTransaction = fragmentManager.beginTransaction()
                    val operationFragment = OperationFragment()
                    operationFragment.arguments = arguments
                    operationFragmentTransaction.replace(R.id.fragment_holder, operationFragment)
                    operationFragmentTransaction.addToBackStack(null)
                    operationFragmentTransaction.commit()
                }
            }
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (fragmentManager.backStackEntryCount != 0) {
                    fragmentManager.popBackStack()
                    fragmentTransaction.commit()
                    val operationFragmentTransaction = fragmentManager.beginTransaction()
                    val operationFragment = OperationFragment()
                    operationFragment.arguments = arguments
                    operationFragmentTransaction.replace(
                        R.id.fragment_b_container,
                        operationFragment
                    )
                    operationFragmentTransaction.commit()
                }
            }
        }
        binding.operationButton.text = arguments?.getString(Constants.OPERATION.name).toString()
        arguments?.getString(Constants.EDITTEXT1.name)?.let {
            val editableText = Editable.Factory.getInstance().newEditable(it)
            binding.number1EditText.text = editableText
        }
        arguments?.getString(Constants.EDITTEXT2.name)?.let {
            val editableText = Editable.Factory.getInstance().newEditable(it)
            binding.number2EditText.text = editableText
        }
        arguments = Bundle()
        operation = binding.operationButton.text.toString()
        binding.operationButton.setOnClickListener {
            if ((!(binding.number1EditText.text.isEmpty() || binding.number2EditText.text.isEmpty()))) {
                if (isNumber(binding.number1EditText.text.toString()) && isNumber(binding.number2EditText.text.toString())) {
                    if (!(binding.number2EditText.text.toString()
                            .toDouble() == 0.0 && binding.operationButton.text.toString() == getString(
                            R.string.div
                        ))
                    ) {
                        val number1 = binding.number1EditText.text.toString().toDouble()
                        val number2 = binding.number2EditText.text.toString().toDouble()
                        val result = Bundle()
                        val operationFragmentTransaction = fragmentManager.beginTransaction()
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

                        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            fragmentManager.setFragmentResult(Constants.RESULT.name, result)
                            fragmentManager.popBackStack()
                        } else {
                            fragmentManager.setFragmentResult(Constants.RESULT.name, result)
                            operationFragmentTransaction.remove(this)
                        }
                        operationFragmentTransaction.commit()
                    } else {
                        Toast.makeText(context, R.string.non_zero_alert, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, R.string.alphabet_error, Toast.LENGTH_SHORT).show()
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
        arguments?.putString(Constants.OPERATION.name, binding.operationButton.text.toString())
        if (binding.number1EditText.text.isNotEmpty())
            arguments?.putString(Constants.EDITTEXT1.name, binding.number1EditText.text.toString())
        if (binding.number2EditText.text.isNotEmpty())
            arguments?.putString(Constants.EDITTEXT2.name, binding.number2EditText.text.toString())
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

    private fun isNumber(string: String): Boolean {
        var isNumber = true
        for (char in string) {
            if (char !in '0'..'9')
                isNumber = false
        }
        return isNumber
    }
}