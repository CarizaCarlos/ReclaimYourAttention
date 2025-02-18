package com.reclaimyourattention.logic

class LimitNotifications: Tool() {
    //Variables Superclase
    override var title: String = "Limitar notificaciones"
    override var description: String =
        "Limitará las apps que pueden enviar notificaciones"

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