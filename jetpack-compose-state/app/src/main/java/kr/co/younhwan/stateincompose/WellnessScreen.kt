package kr.co.younhwan.stateincompose

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WellnessScreen() {
    Column {
        WaterCounter()

        val list = remember { getWellnessTasks().toMutableStateList() }
        WellnessTaskList(list = list, onCloseTask = { task -> list.remove(task)})
    }
}

fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task $i") }

@Preview
@Composable
fun Preview() {
    WellnessScreen()
}
