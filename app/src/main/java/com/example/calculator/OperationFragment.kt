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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentManager = parentFragmentManager
        binding = FragmentOperationBinding.inflate(inflater)
        if(savedInstanceState!=null){
            operation = savedInstanceState.getString(Constants.OPERATION.name).toString()
            binding.operationButton.text = operation
        }
        fragmentManager.setFragmentResultListener(Constants.OPERATION.name,this) { _, result ->
            binding.operationButton.text = result.getString(Constants.OPERATION.name).toString()
            operation = binding.operationButton.text.toString()
        }
        binding.operationButton.setOnClickListener {
            if (!(binding.number1EditText.text.isEmpty() || binding.number2EditText.text.isEmpty())) {
                val number1 = binding.number1EditText.text.toString().toInt()
                val number2 = binding.number2EditText.text.toString().toInt()
                val result = Bundle()
                val fragmentTransaction = fragmentManager.beginTransaction()
                result.putInt(Constants.NUMBER1.name, number1)
                result.putInt(Constants.NUMBER2.name, number2)
                result.putInt(
                    Constants.RESULT.name,
                    operation(number1, number2, binding.operationButton.text.toString())
                )
                result.putString(
                    Constants.OPERATION.name, when (binding.operationButton.text.toString()) {
                        getString(R.string.add) -> getString(R.string.plus)
                        getString(R.string.sub) -> getString(R.string.minus)
                        getString(R.string.mul) -> getString(R.string.asterisk)
                        else -> getString(R.string.slash)
                    }
                )
                result.putBoolean(Constants.VIEWCHANGED.name,true)
                fragmentManager.setFragmentResult(Constants.RESULT.name, result)
                fragmentTransaction.remove(this)
                fragmentManager.popBackStack()
                fragmentTransaction.commit()
            } else {
                Toast.makeText(context, R.string.edit_text_empty_message, Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.OPERATION.name,operation)

    }
    private fun operation(a:Int, b: Int, operation: String): Int{
        return when(operation){
            getString(R.string.add) -> a+b
            getString(R.string.sub) -> a-b
            getString(R.string.mul) -> a*b
            else -> a/b
        }
    }
}