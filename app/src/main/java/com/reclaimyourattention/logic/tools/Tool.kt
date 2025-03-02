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

    open fun loadState() { // Al sobreescribir poner el super.saveState() de últimas para que tome todos los cambios
        active = StorageManager.getBoolean("${storageKey}_active", active)

        // Reactiva si la herramienta estaba activa
        if (active) {
            reactivate()
        }
    }

    abstract fun activate(vararg parameters: Any)

    abstract fun reactivate()

    abstract fun deactivate()
}