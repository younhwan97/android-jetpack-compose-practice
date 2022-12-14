package kr.co.younhwan.stateincompose

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WellnessScreen() {
    Column {
        WaterCounter()

        WellnessTaskList()
    }
}

@Preview
@Composable
fun Preview() {
    WellnessScreen()
}
