package com.reclaimyourattention.logic.tools

class LimitTimeInApp: Tool() {
    //Variables Superclase
    override var title: String = "Limitar tiempo en aplicación"
    override var description: String =
        "Limitar el Tiempo en total en pantalla teniendo en cuenta el uso de las apps"

    //Parámetros
    private var timeLimitScreen: Int =25
    private var timeLeftScreen: Int =25
    private var problematicApp: Boolean= false
    private var numLimitesAlcanzados: Int=0

    // Métodos Superclase
    override fun activate(vararg parameters: Any) {
        TODO("Not yet implemented")

    }

    override fun deactivate() {
        TODO("Not yet implemented")
    }
    //Métodos
}