package com.reclaimyourattention.logic.tools

object BlockingScheduleForApp: Tool()  {
    //Variables Superclase
    override val title: String
        get() = "Horario de bloqueo de apps"
    override val description: String
        get() = "Bloqueará ciertas apps en determinados momentos del día"

    // Parámetros Solicitados al User
    var blockedPackages: MutableSet<String> = mutableSetOf()
        private set

    override fun activate(vararg parameters: Any) {
        TODO("Not yet implemented")
    }

    override fun deactivate() {
        TODO("Not yet implemented")
    }
}