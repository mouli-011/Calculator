package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ActivityB : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)
        val extras = intent.extras
        val operation = setTextForButton(extras)
        val button: Button = findViewById(R.id.operation_button)
        button.setOnClickListener {
            val editText1: EditText = findViewById(R.id.number1_edit_text)
            val editText2: EditText = findViewById(R.id.number2_edit_text)
            if(editText1.text.isEmpty()||editText2.text.isEmpty()){
                Toast.makeText(this,R.string.edit_text_empty_message,Toast.LENGTH_SHORT).show()
            }
            else{
                returnResult(operation,editText1,editText2)
            }
        }
    }
    private fun returnResult(operations: String,editText1: EditText,editText2: EditText){
        val resultIntent = Intent()
        val number1 = editText1.text.toString().toInt()
        val number2 = editText2.text.toString().toInt()
        val result = when(operations){
            getString(R.string.add) -> {
                number1+number2
            }
           getString(R.string.sub) -> {
                number1-number2
            }
            getString(R.string.mul) -> {
                number1*number2
            }
            else -> {
                number1/number2
            }
        }
        resultIntent.putExtra(getString(R.string.number1),number1)
        resultIntent.putExtra(getString(R.string.number2),number2)
        resultIntent.putExtra(getString(R.string.result),result)
        resultIntent.putExtra(getString(R.string.operation),when(operations){
            getString(R.string.add) -> getString(R.string.plus)
            getString(R.string.sub) -> getString(R.string.minus)
            getString(R.string.mul) -> getString(R.string.asterisk)
            else -> getString(R.string.slash)
        })
        setResult(RESULT_OK,resultIntent)
        finish()
    }
    private fun setTextForButton(extras: Bundle?): String{
        val operationButton: Button = findViewById(R.id.operation_button)
        return when(extras?.getString(getString(R.string.operation))){
            getString(R.string.add) -> {
                operationButton.text = getString(R.string.add)
                getString(R.string.add)
            }
            getString(R.string.sub) -> {
                operationButton.text = getString(R.string.sub)
                getString(R.string.sub)
            }
            getString(R.string.mul) -> {
                operationButton.text = getString(R.string.mul)
                getString(R.string.mul)
            }
            else -> {
                operationButton.text = getString(R.string.div)
                R.string.div.toString()
            }
        }
    }
}