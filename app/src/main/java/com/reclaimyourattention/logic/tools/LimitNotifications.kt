package com.reclaimyourattention.logic.tools

class LimitNotifications: Tool() {
    //Variables Superclase
    override val title: String
        get() = "Limitar notificaciones"
    override val description: String
        get() = "Limitará las apps que pueden enviar notificaciones"

    companion object {
        private var blockedPackages: MutableSet<String> = mutableSetOf()

        fun getBlockedPackages(): MutableSet<String> {
            return blockedPackages
        }
    }

    //Parámetros
    private var appsToLimit: String = "Va a ser un ArrayList"

    //Métodos Superclase
    override fun activate(vararg parameters: Any) {
        TODO("Not yet implemented")
    }

    override fun deactivate() {
        TODO("Not yet implemented")
    }
}