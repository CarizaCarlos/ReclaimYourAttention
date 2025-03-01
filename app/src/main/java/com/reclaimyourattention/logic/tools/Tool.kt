package com.reclaimyourattention.logic.tools

abstract class Tool {
    // Atributos
    abstract val title: String
    abstract val description: String
    var active: Boolean = false
        protected set

    // Métodos
    abstract fun activate(vararg parameters: Any)

    abstract fun deactivate()
}