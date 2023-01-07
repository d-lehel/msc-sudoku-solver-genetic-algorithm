package com.example.sodoku_solver

import android.view.View
import android.util.AttributeSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class CanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // draw board
        val board = Paint()
        board.style = Paint.Style.STROKE
        board.strokeWidth = 5f
        board.color = Color.GRAY

        canvas?.drawRect(1f, 1f, 900f, 900f, board)
        canvas?.drawRect(300f, 1f, 600f, 900f, board)
        canvas?.drawRect(1f, 300f, 900f, 600f, board)

        board.strokeWidth = 1f
        canvas?.drawRect(100f, 1f, 200f, 900f, board)
        canvas?.drawRect(400f, 1f, 500f, 900f, board)
        canvas?.drawRect(700f, 1f, 800f, 900f, board)

        canvas?.drawRect(1f, 100f, 900f, 200f, board)
        canvas?.drawRect(1f, 400f, 900f, 500f, board)
        canvas?.drawRect(1f, 700f, 900f, 800f, board)

        // draw possibleSolution
        val solution = Paint()
        solution.color = Color.WHITE
        solution.textSize = 70f
        for (i in 0..8)
            for (j in 0..8) {
                if (Board.individualBody[i][j] > 0) {

                    // partial ok
                    when (Board.individualGenomValues[i][j]) {
                        3 -> solution.color = Color.GREEN
                        2 -> solution.color = Color.RED//Color.YELLOW
                        1 -> solution.color = Color.RED//Color.rgb(255, 165, 0) // orange
                        0 -> solution.color = Color.RED
                    }

                    canvas?.drawText(
                        Board.individualBody[i][j].toString(),
                        100f * (j + 1) - 70,
                        100f * (i + 1) - 25,
                        solution
                    )
                }
            }

        // draw problem
        val problem = Paint()
        problem.color = Color.WHITE
        problem.textSize = 70f
        for (i in 1..9)
            for (j in 1..9) {
                if (Board.problem[i - 1][j - 1] > 0) {
                    canvas?.drawText(
                        Board.problem[i - 1][j - 1].toString(),
                        100f * j - 70,
                        100f * i - 25,
                        problem
                    )
                }
            }
        // draw fittest value
        problem.color = Color.WHITE
        problem.textSize = 40f

        if (Population.individuals.isNotEmpty()) {
            canvas?.drawText("fittests", 530f, 970f, problem)
            canvas?.drawText("worsts", 770f, 970f, problem)
            problem.textSize = 30f

            canvas?.drawText(
                Population.individuals[Population.individuals.size - 1].fitness.toString(),
                560f,
                1050f,
                problem
            )
            canvas?.drawText(
                Population.individuals[Population.individuals.size - 2].fitness.toString(),
                560f,
                1100f,
                problem
            )
            canvas?.drawText(
                Population.individuals[Population.individuals.size - 3].fitness.toString(),
                560f,
                1150f,
                problem
            )
            canvas?.drawText(
                Population.individuals[Population.individuals.size - 4].fitness.toString(),
                560f,
                1200f,
                problem
            )
            canvas?.drawText(
                Population.individuals[Population.individuals.size - 5].fitness.toString(),
                560f,
                1250f,
                problem
            )
            canvas?.drawText(
                Population.individuals[Population.individuals.size - 6].fitness.toString(),
                560f,
                1300f,
                problem
            )
            canvas?.drawText(
                Population.individuals[Population.individuals.size - 7].fitness.toString(),
                560f,
                1350f,
                problem
            )
            canvas?.drawText(
                Population.individuals[Population.individuals.size - 8].fitness.toString(),
                560f,
                1400f,
                problem
            )
            canvas?.drawText(
                Population.individuals[Population.individuals.size - 9].fitness.toString(),
                560f,
                1450f,
                problem
            )
            canvas?.drawText(
                Population.individuals[Population.individuals.size - 10].fitness.toString(),
                560f,
                1500f,
                problem
            )

            canvas?.drawText(Population.individuals[0].fitness.toString(), 800f, 1050f, problem)
            canvas?.drawText(Population.individuals[1].fitness.toString(), 800f, 1100f, problem)
            canvas?.drawText(Population.individuals[2].fitness.toString(), 800f, 1150f, problem)
            canvas?.drawText(Population.individuals[3].fitness.toString(), 800f, 1200f, problem)
            canvas?.drawText(Population.individuals[4].fitness.toString(), 800f, 1250f, problem)
            canvas?.drawText(Population.individuals[5].fitness.toString(), 800f, 1300f, problem)
            canvas?.drawText(Population.individuals[6].fitness.toString(), 800f, 1350f, problem)
            canvas?.drawText(Population.individuals[7].fitness.toString(), 800f, 1400f, problem)
            canvas?.drawText(Population.individuals[8].fitness.toString(), 800f, 1450f, problem)
            canvas?.drawText(Population.individuals[9].fitness.toString(), 800f, 1500f, problem)
        }

        problem.textSize = 40f
        canvas?.drawText(
            "generation: " + Population.generation.toString() + "/" + Parameters.max_generation,
            0f,
            970f,
            problem
        )
        canvas?.drawText(
            "population: " + Parameters.population_size.toString(), 0f, 1040f,
            problem
        )
        canvas?.drawText(
            "selection rate: " + Parameters.selection_rate.toString(), 0f, 1110f,
            problem
        )

        canvas?.drawText(
            "selection type: " + Parameters.selection_type.toString(), 0f, 1180f,
            problem
        )

        canvas?.drawText(
            "crossover type: " + Parameters.crossover_type.toString(), 0f, 1250f,
            problem
        )

        canvas?.drawText(
            "mutation rate: " + Parameters.mutation_rate.toString(), 0f, 1320f,
            problem
        )

        canvas?.drawText(
            "mutation type: " + Parameters.mutation_type, 0f, 1390f,
            problem
        )

        canvas?.drawText(
            "given numbers: " + Parameters.given_numbers.toString(), 0f, 1460f,
            problem
        )

        canvas?.drawText(
            "restarted: " + GA.restarted.toString(), 0f, 1530f,
            problem
        )

        if (GA.solved == "true") {
            problem.color = Color.GREEN
            canvas?.drawText(
                "solved :)", 0f, 1600f,
                problem
            )
        }
        if (GA.solved == "false") {
            problem.color = Color.RED
            canvas?.drawText(
                "failed :(", 0f, 1600f,
                problem
            )
        }
    }
}