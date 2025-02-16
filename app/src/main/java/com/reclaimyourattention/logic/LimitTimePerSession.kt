package com.reclaimyourattention.logic

class LimitTimePerSession: Tool() {
    // Variables Superclase
    override var title: String = "Limitar el Tiempo por Sesión"
    override var description: String =
        "Impide periodos prolongados e ininterrumpidos de uso"

    // Parámetros
        // Solicitados
        private var activeMinutesTreshold: Int = 25
        private var cooldownMinutes: Int = 15
        private var blockedPackages: MutableSet<String> = mutableSetOf()
        // Inmutables
        private val inactiveMinutesTreshold: Int = 2

    // Variables de Control
    private var activeMinutes = 0
    private var inactiveMinutes = 0

    // Métodos Superclase
    override fun activate(vararg parameters: Any) {
        active = true
    }

    override fun deactivate() {
        active = false
    }

    // Métodos
}