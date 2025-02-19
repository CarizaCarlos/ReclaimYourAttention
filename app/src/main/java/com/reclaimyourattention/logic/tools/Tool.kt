package com.reclaimyourattention.logic.tools

abstract class Tool {
    // Atributos
    protected abstract var title: String
    protected abstract var description: String
    protected var active: Boolean = false

    // Métodos
    abstract fun activate(vararg parameters: Any)

    abstract fun deactivate()

}