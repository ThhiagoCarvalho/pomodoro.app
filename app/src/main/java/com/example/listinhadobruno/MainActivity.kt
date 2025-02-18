package com.example.listinhadobruno

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibrationEffect
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import android.text.TextWatcher
import android.text.Editable

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextNumero = findViewById<EditText>(R.id.editTextNumero)
        editTextNumero.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textoDigitado = s.toString()
                val numero: Int? = textoDigitado.toIntOrNull()

                if (numero != null) {
                    Log.d("MainActivity", "Número: $numero")
                    exemploVibracao()
                } else {
                    Log.d("MainActivity", "Texto inválido, não é um número.")
                    Toast.makeText(applicationContext, "Por favor, digite um número!", Toast.LENGTH_SHORT).show()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun vibrar(context: Context, tempo: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect = VibrationEffect.createOneShot(tempo, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(tempo)
            }
        }
    }

    fun exemploVibracao() {
        vibrar(this, 500)
    }

}