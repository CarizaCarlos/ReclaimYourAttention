package com.reclaimyourattention.logic.tools

abstract class Tool {
    // Atributos
    protected abstract val title: String
    protected abstract val description: String
    protected var active: Boolean = false

    // Métodos
    abstract fun activate(vararg parameters: Any)

    abstract fun deactivate()
}