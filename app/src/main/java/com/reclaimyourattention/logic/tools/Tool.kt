package com.reclaimyourattention.logic.tools

import com.reclaimyourattention.logic.StorageManager

abstract class Tool {
    // Atributos
    abstract val title: String
    abstract val description: String
    protected abstract val storageKey: String

    // Variables de Control
    var active: Boolean = false
        protected set

    // Métodos
    open fun saveState() {
        StorageManager.saveBoolean("${storageKey}_active", active)
    }

    open fun loadState() {
        active = StorageManager.getBoolean("${storageKey}_active", active)
    }

    abstract fun activate(vararg parameters: Any)

    abstract fun deactivate()
}