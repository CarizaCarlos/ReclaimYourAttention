package com.reclaimyourattention.logic

class WaitTimeForApp: Tool(){
    // Variables Superclase
    override var title: String = "Tiempo de Espera para Ingresar a Apps"
    override var description: String =
        "Evita que se ingrese inmediatamente a una app, antes se deberá esperar el tiempo establecido " +
        "mientras se muestra mensaje de reflexión"

    // Parámetros
        // Solicitados
        private var waitSeconds: Int = 20
        private var blockedPackages: MutableSet<String> = mutableSetOf()
        // Inmutables
        private val cooldownSeconds: Int = 30

    // Métodos Superclase
    override fun activate(vararg parameters: Any) {
        active = true
    }

    // Métodos
}