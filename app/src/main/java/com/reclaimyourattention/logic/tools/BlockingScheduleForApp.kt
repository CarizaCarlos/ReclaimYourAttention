package com.reclaimyourattention.logic.tools

import com.reclaimyourattention.logic.StorageManager

object BlockingScheduleForApp: Tool()  {
    //Variables Superclase
    override val title: String = "Horario de bloqueo de apps"
    override val description: String = "Bloqueará ciertas apps en determinados momentos del día"
    override val storageKey: String = "BlockingScheduleForApp"

    // Parámetros Solicitados al User
    var blockedPackages: MutableSet<String> = mutableSetOf()
        private set

    // Métodos Superclase
    override fun saveState() {
        super.saveState()
        StorageManager.saveStringSet("${storageKey}_blockedPackages", blockedPackages)
    }

    override fun loadState() {
        super.loadState()
        blockedPackages = StorageManager.getStringSet("${storageKey}_blockedPackages", blockedPackages) as MutableSet<String>
    }
    override fun activate(vararg parameters: Any) {
        TODO("Not yet implemented")
    }

    override fun deactivate() {
        TODO("Not yet implemented")
    }
}