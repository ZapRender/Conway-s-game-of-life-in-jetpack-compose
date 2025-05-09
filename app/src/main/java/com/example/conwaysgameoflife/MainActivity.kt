package com.example.conwaysgameoflife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.conwaysgameoflife.ui.theme.ConwaysGameOfLifeTheme

class MainActivity : ComponentActivity() {

    private val viewModel: Generation by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConwaysGameOfLifeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val cellSize = 15.dp

                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp.dp.value
                    val screenHeight = configuration.screenHeightDp.dp.value

                    val cols = (screenWidth / cellSize.value).toInt()
                    val rows = (screenHeight / cellSize.value).toInt()

                    viewModel.setMatrix(cols,rows)

                    GameBoardView(viewModel, cellSize)
                }
            }
        }
    }
}

@Composable
fun GameBoard(generation: Generation, cellSize: Dp){
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        val (grid, button) = createRefs()

        Row(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(grid) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            for (rowIndex in generation.matrix.indices) {
                Column {
                    for (columnIndex in generation.matrix[rowIndex].indices) {
                        Box(
                            modifier = Modifier
                                .size(cellSize)
                                .background(
                                    if (generation.getCellValue(
                                            rowIndex,
                                            columnIndex
                                        )
                                    ) Color.White else Color.Black
                                )
                                .clickable {

                                    val newMatrix = generation.matrix.mapIndexed { row, cells ->
                                        cells.mapIndexed { column, cell ->
                                            if (rowIndex == row && columnIndex == column) {

                                                Cell(rowIndex, columnIndex, !cell.isAlive)
                                            } else {
                                                cell
                                            }
                                        }
                                    }
                                    generation.changeCellValue(newMatrix)
                                }
                        )
                    }
                }
            }
        }

        Row(modifier = Modifier
            .fillMaxSize()
            .constrainAs(button) {
                top.linkTo(grid.bottom)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }) {
            Button(
                modifier = Modifier.wrapContentSize(),
                onClick = { generation.toggleGame() }
            ) {
                if (generation.isRunning) {
                    Icon(painter = painterResource(id = R.drawable.ic_pause_circle), "Play Button")
                } else {
                    Icon(painter = painterResource(id = R.drawable.ic_play_circle), "Play Button")
                }
            }

            Button(
                modifier = Modifier.wrapContentSize(),
                onClick = {
                    if(generation.isRunning){
                        generation.toggleGame()
                    }
                    generation.cleanMatrix()

                }
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_restart), "Play Button")
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameBoardView(generation: Generation, cellSize: Dp) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("conway's game of life") },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        },

    ) { innerPadding ->
        ConstraintLayout(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            GameBoard(generation, cellSize)
        }
    }
}
