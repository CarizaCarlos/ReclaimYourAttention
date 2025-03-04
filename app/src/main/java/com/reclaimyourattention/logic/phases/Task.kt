package com.reclaimyourattention.logic.phases

import com.reclaimyourattention.logic.services.ToolType

data class Task(
    val id: String,
    val title: String,
    val body: String =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
        "Maecenas tortor leo, posuere vel magna sit amet, auctor convallis risus. " +
        "Integer sit amet lorem at tellus tempor semper. " +
        "Donec vulputate dapibus justo, vel porttitor odio vulputate et. " +
        "Fusce eu suscipit urna. " +
        "Sed in justo fringilla, vestibulum nulla a, consectetur sapien.\n\n" +
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
        "Maecenas tortor leo, posuere vel magna sit amet, auctor convallis risus. " +
        "Integer sit amet lorem at tellus tempor semper. " +
        "Donec vulputate dapibus justo, vel porttitor odio vulputate et. " +
        "Fusce eu suscipit urna. " +
        "Sed in justo fringilla, vestibulum nulla a, consectetur sapien.\n",
    val tool: ToolType?,
    val taskPrerrequisitesID: Set<String>?,
    val isMandatory: Boolean,
    val readingMinutes: Int
)