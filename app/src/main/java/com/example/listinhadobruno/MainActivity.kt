package com.example.listinhadobruno

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibrationEffect
import android.util.Log
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var descansando = false
    private var pomodo_concluido = 0
    private var job: Job? = null

    private lateinit var textoTempo: TextView
    private lateinit var botaoIniciar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        botaoIniciar = findViewById<Button>(R.id.botaoIniciar)
        textoTempo = findViewById<TextView>(R.id.textoTempo)

        botaoIniciar.setOnClickListener {
            if (!descansando) {
                iniciarPomodoro()
            }
        }

    }

    private fun iniciarPomodoro() {
        var duracaoPomodoro = 0
        botaoIniciar.isEnabled = false
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {

            while (duracaoPomodoro < 1500) {
                duracaoPomodoro++
                val minutos =  duracaoPomodoro / 60
                val segundos =  duracaoPomodoro % 60

                textoTempo.text = "Tempo:  %02d:%02d".format(minutos, segundos)
                Log.d("MainActivity", "Tempo: $duracaoPomodoro")
                delay(1000)

            }

            pomodo_concluido++
            vibrar(500)

            if (pomodo_concluido % 4 == 0) {
                iniciarDescansoLongo()
            } else {
                iniciarDescansoCurto()
            }
        }
    }


    private fun iniciarDescansoCurto() {
        var duracaoDescansoCurto = 0 // Descanso curto de 10 segundos

        descansando = true

        textoTempo.text = "Descanso Curto: 5:00"
        Log.d("MainActivity", "Descanso Curto: 5:00")
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {

            while (duracaoDescansoCurto < 300) {
                duracaoDescansoCurto++

                val minutos = (300 - duracaoDescansoCurto) / 60
                val segundos = (300 - duracaoDescansoCurto) % 60
                textoTempo.text = "Tempo: %02d:%02d".format(minutos, segundos) +" restantes"
                Log.d(
                    "MainActivity",
                    "Tempo:" + (10 - duracaoDescansoCurto) + " restantes"
                )
                delay(1000)
            }

            vibrar(500)
            descansando = false
            botaoIniciar.isEnabled = true
            textoTempo.text = "Pronto para outro Pomodoro!"
        }
    }



    private fun iniciarDescansoLongo() {
        var duracaoDescansoLongo = 0 // Descanso longo de 50 segundos
        botaoIniciar.isEnabled = false

        descansando = true
        textoTempo.text = "Descanso Curto: 1:00"

        Log.d("MainActivity", "Descanso Curto: 5:00")
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {

            while (duracaoDescansoLongo < 900) {
                duracaoDescansoLongo++

                val minutos = (300 - duracaoDescansoLongo) / 60
                val segundos = (300 - duracaoDescansoLongo) % 60
                textoTempo.text = "Tempo: %02d:%02d".format(minutos, segundos) +" restantes"
                Log.d(
                    "MainActivity",
                    "Tempo:" + (50 - duracaoDescansoLongo) + " restantes"
                )
                delay(1000)
            }

            vibrar(500)
            descansando = false
            botaoIniciar.isEnabled = true
            textoTempo.text = "Pronto para outro Pomodoro!"
        }

    }

    private fun vibrar(tempo: Long) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(tempo, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(tempo)
            }
        }
    }


    fun exemploVibracao() {
        vibrar( 500)
    }

}







