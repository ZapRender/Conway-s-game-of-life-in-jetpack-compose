package com.example.conwaysgameoflife

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Generation:ViewModel() {

    private var m by mutableIntStateOf(0)
    private var n by mutableIntStateOf(0)

    private var gameJob: Job? = null
    private var isRunning by mutableStateOf(false)

    var matrix by mutableStateOf(List(0) { List(0) { Cell() } })

    fun setMatrix(col: Int, row: Int){
        m = col
        n = row
        matrix = List(col) { List(row) { Cell() } }
    }

    fun changeCellValue(newMatrix: List<List<Cell>>){
        matrix = newMatrix
    }

    fun getCellValue(i: Int, j: Int): Boolean{
        return matrix[i][j].isAlive
    }
    fun evaluateGen(){
        val newGen = List(m) { List(n) { Cell() } }
        for(column in matrix.indices){
            for(row in matrix[column].indices){
                val aliveNeighbours = countAliveNeighbours(column, row)
                if (matrix[column][row].isAlive) {
                    /* A cell dead if it has more than 3 living neighbours around it or just have one neighbour alive or none*/
                    newGen[column][row].isAlive = !(aliveNeighbours < 2 || aliveNeighbours > 3)
                } else {
                    /** A cell born if it has exactly 3 cells alive **/
                    newGen[column][row].isAlive = aliveNeighbours == 3
                }

            }
        }
        matrix = newGen
    }

    private fun countAliveNeighbours(column:Int, row:Int): Int {
        var count = 0

        for (i in -1..1) {
            for (j in -1..1) {
                val x = column + i
                val y = row + j

                if (i == 0 && j == 0) continue

                if (x in 0 until m && y in 0 until n) {
                    if (matrix[x][y].isAlive) {
                        count++
                    }
                }
            }
        }

        return count
    }

    private fun startGame() {
        gameJob = viewModelScope.launch {
            while (true) {
                evaluateGen()
                delay(100)
            }
        }
    }

    private fun stopGame() {
        gameJob?.cancel()
    }

    fun toggleGame() {
        isRunning = !isRunning
        if (isRunning) {
            startGame()
        } else {
            stopGame()
        }
    }

}