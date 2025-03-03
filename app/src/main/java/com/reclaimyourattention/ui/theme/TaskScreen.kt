package com.reclaimyourattention.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.reclaimyourattention.logic.phases.Task
import com.reclaimyourattention.logic.services.ToolType
import com.reclaimyourattention.ui.MainScreen

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    // Ejemplo Task
    val task = Task(
        id = "01",
        title = "Beneficios de la Escala de Grices y Cómo Activarla",
        body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Nulla tellus nibh, tempor quis metus sed, malesuada porta justo. " +
                "Curabitur pellentesque leo purus, eu ultrices felis scelerisque sit amet. " +
                "Quisque vitae ultricies quam, quis facilisis nibh. " +
                "Curabitur accumsan tellus et urna rutrum lobortis. " +
                "Nam scelerisque ante odio, a dignissim massa vehicula ac. " +
                "Nullam pharetra purus eget dui venenatis gravida. " +
                "Cras id risus at justo volutpat pharetra sed at ligula. " +
                "Quisque purus odio, vehicula nec felis vitae, luctus pellentesque nisl. " +
                "Proin sodales scelerisque nisi, et blandit augue finibus eget. " +
                "Aliquam ac ligula lacinia mi vulputate tristique ut a orci. " +
                "Mauris imperdiet tempus augue eu scelerisque.\n" +

                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Nulla tellus nibh, tempor quis metus sed, malesuada porta justo. " +
                "Curabitur pellentesque leo purus, eu ultrices felis scelerisque sit amet. " +
                "Quisque vitae ultricies quam, quis facilisis nibh. " +
                "Curabitur accumsan tellus et urna rutrum lobortis. " +
                "Nam scelerisque ante odio, a dignissim massa vehicula ac. " +
                "Nullam pharetra purus eget dui venenatis gravida. " +
                "Cras id risus at justo volutpat pharetra sed at ligula. " +
                "Quisque purus odio, vehicula nec felis vitae, luctus pellentesque nisl. " +
                "Proin sodales scelerisque nisi, et blandit augue finibus eget. " +
                "Aliquam ac ligula lacinia mi vulputate tristique ut a orci. " +
                "Mauris imperdiet tempus augue eu scelerisque.",
        tool = ToolType.LIMIT_DAILY,
        taskPrerrequisitesID = null,
        isMandatory = true,
        readingMinutes = 4
    )

//    ReclaimYourAttentionTheme {
//        TaskScreen(task)
//    }
}
//
//@Composable
//class TaskScreen(task: Task) {
//
//}