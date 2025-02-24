package com.reclaimyourattention.logic.tools

class BlockingScheduleForApp: Tool()  {
    //Variables Superclase
    override val title: String
        get() = "Horario de bloqueo de apps"
    override val description: String
        get() = "Bloqueará ciertas apps en determinados momentos del día"

    companion object {
        private var blockedPackages: MutableSet<String> = mutableSetOf()

        fun getBlockedPackages(): MutableSet<String> {
            return blockedPackages
        }
    }

    override fun activate(vararg parameters: Any) {
        TODO("Not yet implemented")
    }

    override fun deactivate() {
        TODO("Not yet implemented")
    }
}