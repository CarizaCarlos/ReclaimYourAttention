package com.reclaimyourattention.logic.tools

class LimitTimeInApp: Tool() {
    //Variables Superclase
    override val title: String
        get() = "Limitar tiempo en aplicación"
    override val description: String
        get() = "Limitar el Tiempo en total en pantalla teniendo en cuenta el uso de las apps"

    companion object {
        private var blockedPackages: MutableSet<String> = mutableSetOf()

        fun getBlockedPackages(): MutableSet<String> {
            return blockedPackages
        }
    }

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