package com.example.conwaysgameoflife

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class Generation():ViewModel() {

    var m by mutableStateOf(0)
    var n by mutableStateOf(0)

    var matrix by mutableStateOf(List(m) { List(n) { Cell() } })

    fun setMatrix(col: Int, row: Int){
        m = col
        n = row
        matrix = List(m) { List(n) { Cell() } }
    }
    fun changeCellValue(i: Int, j: Int, value: Boolean){

        matrix[i][j].isAlive = value
    }
    fun EvaluateGen(){

        val newGen = List(m) { List(n) { Cell() } }
        for(column in matrix.indices){
            for(row in matrix[column].indices){
                val aliveNeighbours = countAliveNeighbours(matrix[column][row])
                if (matrix[column][row].isAlive) {
                    /* A cell dead if it has more than 3 living neighbours around it or just have one neighbour alive or none*/
                    if (aliveNeighbours < 2 || aliveNeighbours > 3) {
                        newGen[column][row].isAlive = false
                    } else {
                        newGen[column][row].isAlive = true
                    }
                } else {
                    /** A cell born if it has exactly 3 cells alive **/
                    if (aliveNeighbours == 3) {
                        newGen[column][row].isAlive = true
                    } else {
                        newGen[column][row].isAlive = false
                    }
                }

            }
        }

        matrix = newGen
    }

    fun countAliveNeighbours(cell: Cell): Int {
        var count = 0

        for (i in -1..1) {
            for (j in -1..1) {
                val x = cell.positionInM + i
                val y = cell.positionInN + j
                if (i == 0 && j == 0) {
                    continue
                }
                if (x >= 0 && x < m && y >= 0 && y < n) {
                    if (matrix[x][y].isAlive) {
                        count++
                    }
                }
            }
        }

        return count
    }

}