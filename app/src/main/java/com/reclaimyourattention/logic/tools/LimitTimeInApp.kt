package com.reclaimyourattention.logic.tools

import kotlin.math.min

object LimitTimeInApp: Tool() {
    //Variables Superclase
    override val title: String
        get() = "Limitar tiempo en aplicación"
    override val description: String
        get() = "Limitar el Tiempo en total en pantalla teniendo en cuenta el uso de las apps"

    // Parámetros Solictados al user
    var blockedPackages: MutableSet<String> = mutableSetOf()
        private set
    var maxTotalMinutes: Int = 120
        private set
    var maxForEachMinutes: Int = 60
        private set

    // Runnables
    private val limitForEach = object : Runnable {
        val minRefreshSeconds: Int = 5
        val maxForEachSeconds: Int = maxForEachMinutes*60
        override fun run() {
            var minDifference: Int? = null

            // Itera sobre los paquetes, bloqueando los que se encesiten y calculando el mínimo tiempo para bloquear otra app que no se halla bloqueado aún
            for (appPackage in blockedPackages) {
                // TODO("Obtiene tiempod e uso del paquete hoy")
                val usageSeconds = 0

                // Verifica si supera maxForEachMinutes
                if (usageSeconds >= maxForEachSeconds) {
                    // TODO("Bloquea la app")
                } else {
                    // Calcula cuanto tiempo falta para que se supere maxForEachMinutes
                    val differenece = maxForEachSeconds-usageSeconds

                    // Verifica si minDifference es nula
                    if (minDifference == null) {
                        minDifference = differenece
                    } else {
                        minDifference = min(minDifference, differenece)
                    }
                }
            }

            // Verifica si minDifference no es null, si no lo es, implica que aún hay paquetes que no han superado el tiempo máximo
            if (minDifference != null) {
                // Calcula el delay dinámicamente
                val refreshSeconds = min(minDifference, minRefreshSeconds)

                // TODO("Postear dentro de refreshSeconds")
            }
        }
    }

    private val limitTotal = object : Runnable {
        val minRefreshSeconds: Int = 5
        val maxTotalSeconds: Int = maxTotalMinutes*60
        override fun run() {
            // TODO("Sumar el uso de todas las apps marcadas hoy")
            val totalUsageSeconds = 0

            // Verifica si supera maxTotalMinutes
            if (totalUsageSeconds >= maxTotalSeconds) {
                // TODO("Bloquea las apps")
            } else {
                // Calcula cuanto tiempo falta para que se supere maxTotalMinutes
                val differenece = maxTotalSeconds-totalUsageSeconds

                // Calcula un delay dinámico para no gastar demasiados recursos
                val refreshSeconds = min(maxTotalSeconds, differenece)

                // TODO("Postear dentro de refreshSeconds")
            }
        }
    }

    // Métodos Superclase
    override fun activate(vararg parameters: Any) {
        TODO("Not yet implemented")

    }

    override fun deactivate() {
        TODO("Not yet implemented")
    }

    //Métodos

}