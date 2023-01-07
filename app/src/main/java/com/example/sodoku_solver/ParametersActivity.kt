package com.example.sodoku_solver

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_parameters.*

class ParametersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameters)
        // hide system ui
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // orientation fix
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // hide action bar
        supportActionBar?.hide()

        restart.setText(Parameters.restart_after_stuck_gen.toString())
        given_numbers.setText(Parameters.given_numbers.toString())
        max_generation.setText(Parameters.max_generation.toString())
        population_size.setText(Parameters.population_size.toString())
        selection_rate.setText(Parameters.selection_rate.toString())
        selection_type.setText(Parameters.selection_type)
        crossover_type.setText(Parameters.crossover_type)
        mutation_rate.setText(Parameters.mutation_rate.toString())
        mutation_strenght.setText(Parameters.mutation_strenght.toString())
        mutation_type.setText(Parameters.mutation_type)

        set_parameters.setOnClickListener {
            // set new parameters from input cells
            Parameters.restart_after_stuck_gen = restart.text.toString().toInt()
            Parameters.given_numbers = given_numbers.text.toString().toInt()
            Parameters.max_generation = max_generation.text.toString().toInt()
            Parameters.population_size = population_size.text.toString().toInt()
            Parameters.selection_rate = selection_rate.text.toString().toFloat()
            Parameters.selection_type = selection_type.text.toString()
            Parameters.crossover_type = crossover_type.text.toString()
            Parameters.mutation_rate = mutation_rate.text.toString().toFloat()
            Parameters.mutation_strenght = mutation_strenght.text.toString().toInt()
            Parameters.mutation_type = mutation_type.text.toString()

            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}