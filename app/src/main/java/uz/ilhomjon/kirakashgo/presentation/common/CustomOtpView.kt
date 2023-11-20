package uz.ilhomjon.kirakashgo.presentation.common// uz.ilhomjon.kirakashgo.presentation.common.CustomOtpView.kt
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import uz.ilhomjon.kirakashgo.R

class CustomOtpView : LinearLayout {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.custom_otp_view, this, true)

        val editText1 = findViewById<EditText>(R.id.editText1)
        val editText2 = findViewById<EditText>(R.id.editText2)
        val editText3 = findViewById<EditText>(R.id.editText3)
        val editText4 = findViewById<EditText>(R.id.editText4)
        val editText5 = findViewById<EditText>(R.id.editText5)

        // Add TextWatcher to move focus to the next EditText on input
        editText1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    focusOnNextEditText(editText1, editText2)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editText2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    focusOnNextEditText(editText2, editText3)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editText3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    focusOnNextEditText(editText3, editText4)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editText4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    focusOnNextEditText(editText4, editText5)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun focusOnNextEditText(current: EditText, next: EditText) {
        // Move focus to the next EditText
        next.requestFocus()
    }
}
