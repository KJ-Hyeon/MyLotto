package com.example.mylotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val clearButton by lazy { findViewById<Button>(R.id.btn_clear) }
    private val addButton by lazy { findViewById<Button>(R.id.btn_add) }
    private val autoButton by lazy { findViewById<Button>(R.id.btn_auto) }
    private val numberPicker by lazy { findViewById<NumberPicker>(R.id.np_num) }

    private val numTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6)
        )
    }

    private var didAuto = false
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initAddButton()
        initAutoButton()
        initClearButton()

    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            when {
                didAuto -> showToast("초기화 후에 시도해주세요!")
                pickNumberSet.size >= 5 -> showToast("숫자는 최대 5까지 추가가 가능합니다!")
                pickNumberSet.contains(numberPicker.value) -> showToast("이미 선택된 숫자입니다!")
                else -> {
                    val textView = numTextViewList[pickNumberSet.size]
                    textView.isVisible = true
                    textView.text = numberPicker.value.toString()
                    setNumBack(numberPicker.value, textView)
                    pickNumberSet.add(numberPicker.value)

                }

            }
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numTextViewList.forEach {
                it.isVisible = false
            }
            didAuto = false
            numberPicker.value = 1
        }
    }

    private fun initAutoButton() {
        autoButton.setOnClickListener {
            val list = getRandom()
            didAuto = true
            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)
            }
        }
    }

    private fun getRandom(): List<Int> {
        val numbers = (1..45).filter { it !in pickNumberSet }
        return (pickNumberSet + numbers.shuffled().take(6- pickNumberSet.size)).sorted()

    }

    private fun setNumBack(number: Int, textView: TextView) {
        val background = when (number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }
        textView.background = ContextCompat.getDrawable(this, background)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
