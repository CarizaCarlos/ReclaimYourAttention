package com.reclaimyourattention.logic

abstract class Tool {
    protected abstract var title: String
    protected abstract var description: String
    protected var active: Boolean = false

    abstract fun activate(vararg parameters: Any)

    abstract fun deactivate()

}