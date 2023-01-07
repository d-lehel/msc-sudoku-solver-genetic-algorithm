package com.example.sodoku_solver

class Parameters {
    companion object{
        val reborn_rate: Float = 0.5f // if stuck
        var restart_after_stuck_gen: Int = 20
        var given_numbers: Int = 40
        var max_generation: Int = 10000
        var population_size: Int = 3000
        var selection_rate: Float = 0.25f
        var selection_type: String = "tournament" //  random - tournament 2 from 4 parent
        var crossover_type: String = "grid" // grid - rows - columns
        var mutation_rate: Float = 0.7f // how many child will be mutated
        var mutation_strenght: Int = 1 // how many genoms will be mutated
        var mutation_type: String = "rand" // swap - big random small swap - increment - random
    }
}