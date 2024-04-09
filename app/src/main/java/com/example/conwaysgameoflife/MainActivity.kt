package com.example.conwaysgameoflife

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.conwaysgameoflife.ui.theme.Black
import com.example.conwaysgameoflife.ui.theme.ConwaysGameOfLifeTheme
import com.example.conwaysgameoflife.ui.theme.White

class MainActivity : ComponentActivity() {

    private val viewModel: Generation by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConwaysGameOfLifeTheme {
                // A surface container using the 'background' color from the theme
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
fun GameBoardView(generation: Generation, cellSize: Dp) {

    ConstraintLayout(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
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
                                .padding(1.dp)
                                .size(cellSize)
                                .background(if( generation.matrix[rowIndex][columnIndex].isAlive) White else Black)
                                .clickable {
                                    Log.d("=============>", "${ generation.matrix[rowIndex][columnIndex].isAlive}")
                                    generation.changeCellValue(rowIndex, columnIndex, !generation.matrix[rowIndex][columnIndex].isAlive)
                                }
                        )
                    }
                }
            }
        }

        Button(modifier = Modifier
            .fillMaxSize()
            .constrainAs(button) {
                top.linkTo(grid.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onClick = {

            }) {
            Text(text ="Next Generation")

        }
    }


}
