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
                Toast.makeText(this,"Numbers Should Not Be Empty",Toast.LENGTH_SHORT).show()
            }
            else{
                returnResult(operation,editText1,editText2)
            }
        }
    }
    private fun returnResult(operations: String,editText1: EditText,editText2: EditText){
        val resultIntent = Intent()
        val a = editText1.text.toString().toInt()
        val b = editText2.text.toString().toInt()
        val result = when(operations){
            OPERATIONS.ADD.string -> {
                a+b
            }
            OPERATIONS.SUB.string -> {
                a-b
            }
            OPERATIONS.MUL.string -> {
                a*b
            }
            else -> {
                a/b
            }
        }
        resultIntent.putExtra("a",a)
        resultIntent.putExtra("b",b)
        resultIntent.putExtra("result",result)
        setResult(RESULT_OK,resultIntent)
        finish()
    }
    private fun setTextForButton(extras: Bundle?): String{
        val operationButton: Button = findViewById(R.id.operation_button)
        return when(extras?.getString("OPERATION")){
            OPERATIONS.ADD.string -> {
                operationButton.text = OPERATIONS.ADD.string
                OPERATIONS.ADD.string
            }
            OPERATIONS.SUB.string -> {
                operationButton.text = OPERATIONS.SUB.string
                OPERATIONS.SUB.string
            }
            OPERATIONS.MUL.string -> {
                operationButton.text = OPERATIONS.MUL.string
                OPERATIONS.MUL.string
            }
            else -> {
                operationButton.text = OPERATIONS.DIV.string
                OPERATIONS.DIV.string
            }
        }
    }
}