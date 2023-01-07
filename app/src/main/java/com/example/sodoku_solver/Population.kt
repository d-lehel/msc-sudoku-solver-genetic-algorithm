package com.example.sodoku_solver

import android.util.Log

class Population {
    companion object {
        // Log.i("debug", )
        var individuals: Array<Individual> = arrayOf()
        var generation = 1
        var fittest: Int = 0

        fun initialization(n: Int) {
            for (n in 1..n) {
                var individual = Individual()
                individual.id = n

                // fill with given genoms and randoms
                for (i in 0..8) {
                    for (j in 0..8) {
                        if (Board.problem[i][j] > 0)
                            individual.body[i][j] = Board.problem[i][j]
                        else
                            individual.body[i][j] = (1..9).random()
                    }
                }
                individuals += individual
            }
            Log.i("flow", "${individuals?.size} individual created")
        }

        fun showBest() {
            for (i in 0..8) {
                for (j in 0..8) {
                    Board.individualBody[i][j] = individuals[individuals.size - 1].body[i][j]
                    Board.individualGenomValues[i][j] =
                        individuals[individuals.size - 1].genomeValues[i][j]
                }
            }
        }

        fun showOne(index: Int) {
            for (i in 0..8) {
                for (j in 0..8) {
                    Board.individualBody[i][j] = individuals[index - 1].body[i][j]
                    Board.individualGenomValues[i][j] = individuals[index - 1].genomeValues[i][j]
                }
            }
        }

        fun sort() {
            individuals = individuals.sortedWith(compareBy { it.fitness }).toTypedArray()
            Log.i(
                "flow",
                "fittest: ${individuals[individuals.size - 1].id} fitness value: ${individuals[individuals.size - 1].fitness}"
            )

            if (individuals[individuals.size - 1].fitness > fittest) {
                GA.stucked = 0
                Log.i(
                    "debug",
                    "stucked=0"
                )
                fittest = individuals[individuals.size - 1].fitness
            } else {
                GA.stucked++
                Log.i(
                    "debug",
                    "stucked++(stucked=" + GA.stucked.toString() + ")"
                )
            }
        }

        fun reInitialization(rebornRate: Float) {
            var n = (individuals.size * rebornRate).toInt()
            GA.stucked=0

           GA.restarted++

            Log.i(
                "debug",
                "reborn n = $n"
            )

            for (ii in 0 until n) {
                for (i in 0..8) {
                    for (j in 0..8) {
                        if (Board.problem[i][j] > 0)
                            individuals[ii].body[i][j] = Board.problem[i][j]
                        else
                            individuals[ii].body[i][j] = (1..9).random()
                    }
                }
            }
        }
    }
}