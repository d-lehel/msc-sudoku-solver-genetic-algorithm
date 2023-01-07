package com.example.sodoku_solver

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // hide system ui
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // orientation fix
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // hide action bar
        supportActionBar?.hide()

        parameters.setOnClickListener {
            startActivity(Intent(this, ParametersActivity::class.java))
        }

        solve.setOnClickListener {
            GA.running = true
            runSimulation()
        }

        stop.setOnClickListener {
            GA.running = false
        }

        show_individual.setOnClickListener {
            if (!GA.running)
                Population.showOne(input_individual.text.toString().toInt())
            canvas.invalidate()
        }

        Board.setProblem(Parameters.given_numbers)
        canvas.invalidate()
    }

    private fun runSimulation() {
        CoroutineScope(Dispatchers.IO).launch {
            Population.individuals = arrayOf()
            Population.generation = 1

            Population.initialization(Parameters.population_size)
            Population.generation = 1
            GA.solved = ""
            GA.restarted = 0

            while (Population.generation != Parameters.max_generation && GA.running) {
                if (GA.stucked == Parameters.restart_after_stuck_gen) {
                    // reborn
                    Population.reInitialization(Parameters.reborn_rate)
                }

                GA.fitness(Population.individuals)

                // if one individual reach the max fitness then stop
                if (Population.individuals[Population.individuals.size - 1].fitness == 243) {
                    GA.solved = "true"
                    break
                }

                Population.sort()
                GA.selection(Population.individuals, Parameters.selection_rate)
                GA.crossover(
                    Population.individuals,
                    Parameters.selection_rate,
                    Parameters.crossover_type
                )
                GA.mutation(
                    Population.individuals,
                    Parameters.mutation_rate,
                    Parameters.mutation_strenght, Parameters.mutation_type
                )

                Population.generation++
                Population.showBest()
                delay(0)
                canvas.invalidate()
            }
            // for visualization
            if (Population.individuals[Population.individuals.size - 1].fitness != 243)
                GA.solved = "false"
            GA.fitness(Population.individuals)
            GA.running = false
            canvas.invalidate()
        }
    }
}
