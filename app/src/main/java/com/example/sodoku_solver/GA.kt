package com.example.sodoku_solver

class GA {
    companion object {
        var running = false
        var solved = ""
        var stucked = 0
        var restarted = 0

        fun fitness(individuals: Array<Individual>) {
            for (individual in individuals) {
                for (i in 0..8) {
                    for (j in 0..8) {
                        individual.genomeValues[i][j] =
                            genomeValue(i, j, individual.body[i][j], individual)
                    }
                }
                individual.setFitness()
//                Log.i(
//                    "flow",
//                    "individual ${individual.id} calculated fintess: ${individual.fitness}"
//                )
            }
        }

        fun crossover(individuals: Array<Individual>, percentage: Float, type: String) {
            var n = (Population.individuals.size * percentage).toInt()

            var parentA = 0
            var parentB = 0
            var parentC = 0
            var parentD = 0

            for (i in 0 until n step 2) {
                // selection type
                when (Parameters.selection_type) {
                    "random" -> {
                        // select 2 random
                        parentA = (n - 1 until Population.individuals.size).random()
                        parentB = (n - 1 until Population.individuals.size).random()
                        while (parentA == parentB) {
                            parentA = (n - 1 until Population.individuals.size).random()
                            parentB = (n - 1 until Population.individuals.size).random()
                        }
                    }
                    // select 4 random
                    "tournament" -> {
                        parentA = (n - 1 until Population.individuals.size).random()
                        parentB = (n - 1 until Population.individuals.size).random()
                        parentC = (n - 1 until Population.individuals.size).random()
                        parentD = (n - 1 until Population.individuals.size).random()
                        while (parentA == parentB || parentB == parentC || parentC == parentD || parentD == parentA || parentA == parentC || parentD == parentA) {
                            parentA = (n - 1 until Population.individuals.size).random()
                            parentB = (n - 1 until Population.individuals.size).random()
                            parentC = (n - 1 until Population.individuals.size).random()
                            parentD = (n - 1 until Population.individuals.size).random()
                        }
                        // only two survive
                        parentA =
                            if (individuals[parentA].fitness >= individuals[parentC].fitness) parentA else parentC
                        parentB =
                            if (individuals[parentB].fitness >= individuals[parentD].fitness) parentB else parentD
                    }
                }

                // create new child
                when (type) {
                    "row" -> {
                        for (j in 0 until 9) {
                            for (k in 0 until 9) {
                                if (j % 2 == 0) individuals[i].body[j][k] =
                                    individuals[parentA].body[j][k]
                                else individuals[i].body[j][k] = individuals[parentB].body[j][k]
                            }
                        }
                    }
                    "col" -> {
                        for (j in 0 until 9) {
                            for (k in 0 until 9) {
                                if (k % 2 == 0) individuals[i].body[j][k] =
                                    individuals[parentA].body[j][k]
                                else individuals[i].body[j][k] = individuals[parentB].body[j][k]
                            }
                        }
                    }
                    "grid" -> {
                        for (ii in 0 until 9 step 3) {
                            for (jj in 0 until 9 step 3) {
                                var randParent = if ((0..1).random() == 1) parentA else parentB
                                for (iii in 0 until 3) {
                                    for (jjj in 0 until 3) {
                                        individuals[i].body[ii + iii][jj + jjj] =
                                            individuals[randParent].body[ii + iii][jj + jjj]
                                        // two child
                                        if (randParent == parentA) {
                                            individuals[i + 1].body[ii + iii][jj + jjj] =
                                                individuals[parentB].body[ii + iii][jj + jjj]
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        fun selection(individuals: Array<Individual>, percentage: Float) {
            var n = (Population.individuals.size * percentage).toInt()
            for (i in 0 until n) {
                individuals[i].kill()
            }
//            Log.i(
//                "flow",
//                "$n individual killed"
//            )
        }

        // type - > type of mutation
        fun mutation(
            individuals: Array<Individual>, rate: Float, strength: Int, type: String
        ) {
            // n -> how many child will be mutated
            var n = (Parameters.population_size * Parameters.selection_rate * rate).toInt()
            var counter = 0;
            var x = 0
            var y = 0
            for (i in 0 until n) {
                // intensity -> how many genom will be mutated
                while (counter < strength) {
                    // mutation type
                    when (type) {
                        "incr" -> {
                            x = (0..8).random()
                            y = (0..8).random()

                            // if not a given genome then change it
                            if (Board.problem[x][y] == 0) {
                                if (individuals[i].body[x][y] == 9) {
                                    individuals[i].body[x][y] = 1
                                    counter++
                                } else {
                                    individuals[i].body[x][y]++
                                    counter++
                                }
                            }
                        }
                        "swap small" -> {
                            // random small grid
                            var iPush = (0..2).random()
                            var jPush = (0..2).random()

                            // random two value inside the small grid
                            var x1 = (0..2).random()
                            var y1 = (0..2).random()
                            var x2 = (0..2).random()
                            var y2 = (0..2).random()
                            var temp = 0

                            // if the two random genome is not a given value then swap
                            if (Board.problem[x1 + (iPush * 3)][y1 + (jPush * 3)] == 0 && Board.problem[x2 + (iPush * 3)][y2 + (jPush * 3)] == 0) {
                                temp = Board.problem[x1 + (iPush * 3)][y1 + (jPush * 3)]
                                Board.problem[x1 + (iPush * 3)][y1 + (jPush * 3)] =
                                    Board.problem[x2 + (iPush * 3)][y2 + (jPush * 3)]
                                Board.problem[x2 + (iPush * 3)][y2 + (jPush * 3)] = temp
                                counter++
                            }
                        }
                        "rand" -> {
                            x = (0..8).random()
                            y = (0..8).random()

                            // if not a given genome then change it
                            if (Board.problem[x][y] == 0) {
                                individuals[i].body[x][y] = (1..9).random()
                                counter++
                            }
                        }
                        "swap big" -> {
                            // random two value inside the small grid
                            var x1 = (0..2).random()
                            var y1 = (0..2).random()
                            var x2 = (0..2).random()
                            var y2 = (0..2).random()
                            var temp = 0

                            // if not good a good genome generate new
                            while (individuals[i].genomeValues[x1][y1] == 3) {
                                x1 = (0..2).random()
                                y1 = (0..2).random()
                            }
                            while (individuals[i].genomeValues[x2][y2] == 3) {
                                x1 = (0..2).random()
                                y1 = (0..2).random()
                            }

                            // if the two random genome is not a given value then swap
                            if (Board.problem[x1][y1] == 0 && Board.problem[x2][y2] == 0 && (x1 != x2 && y1 != y2)) {
                                temp = Board.problem[x1][y1]
                                Board.problem[x1][y1] =
                                    Board.problem[x2][y2]
                                Board.problem[x2][y2] = temp
                                counter++
                            }
                        }

                    }


                }
            }
//                Log.i(
//                    "flow",
//                    "$i child is mutated"
//                )
        }


        private fun genomeValue(x: Int, y: Int, num: Int, individual: Individual): Int {
            var rowOk = true
            var columnOk = true
            var gridOk = true

            // check column
            for (i in 0..8) if (individual.body[i][y] == num && x != i) {
                columnOk = false
                break
            }
            // check row
            for (j in 0..8) if (individual.body[x][j] == num && y != j) {
                rowOk = false
                break
            }

            // check small matrix
            var iPush = 0;
            var jPush = 0;
            if (x > 2) iPush += 3
            if (x > 5) iPush += 3
            if (y > 2) jPush += 3
            if (y > 5) jPush += 3

            for (i in 0..2) for (j in 0..2)
            // todo not self
                if (individual.body[i + iPush][j + jPush] == num && (i + iPush != x || j + jPush != y)) {
                    gridOk = false
                    break
                }
            // ok if partial ok
            // return genom value 0-3
            var genomeValue = 0
            if (rowOk) genomeValue++
            if (columnOk) genomeValue++
            if (gridOk) genomeValue++

            return genomeValue
        }

        private fun genomeValue2(x: Int, y: Int, num: Int, individual: Individual): Int {

            // check column
            for (i in 0..8) if (individual.body[i][y] == num && x != i)
                return 0

            // check row
            for (j in 0..8) if (individual.body[x][j] == num && y != j)
                return 0

            // check small matrix
            var iPush = 0;
            var jPush = 0;
            if (x > 2) iPush += 3
            if (x > 5) iPush += 3
            if (y > 2) jPush += 3
            if (y > 5) jPush += 3

            for (i in 0..2)
                for (j in 0..2)
                    if (individual.body[i + iPush][j + jPush] == num && (i + iPush != x || j + jPush != y))
                        return 0

            return 1
        }
    }
}
