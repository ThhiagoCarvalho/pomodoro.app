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
import kotlinx.coroutines.*
import kotlin.coroutines.resume

class MainActivity : AppCompatActivity() {
    private var descansando = false
    private var pomodo_concluido = 0
    private var job: Job? = null
    private var pausado = false
    private var duracaoPomodoro = 0

    private lateinit var textoTempo: TextView
    private lateinit var botaoIniciar: Button
    private lateinit var botaoPausa: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        botaoIniciar = findViewById(R.id.botaoIniciar)
        textoTempo = findViewById(R.id.textoTempo)
        botaoPausa = findViewById(R.id.botaoPausa)

        botaoIniciar.setOnClickListener {
            if (!descansando) {
                iniciarPomodoro()
            }
        }

        botaoPausa.setOnClickListener {
            pausarOuRetomar()
        }
    }

    private fun pausarOuRetomar() {
        pausado = !pausado 

        if (pausado) {
            botaoPausa.text = "Retomar"
        } else {
            botaoPausa.text = "Pausar"
            retomarPomodoro() 
        }
    }

    private fun iniciarPomodoro() {
        botaoIniciar.isEnabled = false
        pausado = false

        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            while (duracaoPomodoro < 60*25) {
                while (pausado) {
                    delay(500) 
                }
                duracaoPomodoro++
                atualizarTempo(duracaoPomodoro)
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

    private fun retomarPomodoro() {
        if (job == null || job?.isCancelled == true) {
            iniciarPomodoro()
        }
    }
    

    private fun iniciarDescansoCurto() {
        descansando = true
        iniciarDescanso(300) 
    }

    private fun iniciarDescansoLongo() {
        descansando = true
        iniciarDescanso(900) 
    }

    private fun iniciarDescanso(tempoTotal: Int) {
        var tempoRestante = tempoTotal
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            while (tempoRestante > 0) {
                while (pausado) {
                    delay(500) 
                }

                atualizarTempo(tempoRestante)
                tempoRestante--
                delay(1000)
            }

            vibrar(500)
            descansando = false
            botaoIniciar.isEnabled = true
            textoTempo.text = "Pronto para outro Pomodoro!"
        }
    }

    private fun atualizarTempo(tempoSegundos: Int) {
        val minutos = tempoSegundos / 60
        val segundos = tempoSegundos % 60
        textoTempo.text = "Tempo: %02d:%02d".format(minutos, segundos)
        Log.d("MainActivity", "Tempo: $tempoSegundos segundos restantes")
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
}
