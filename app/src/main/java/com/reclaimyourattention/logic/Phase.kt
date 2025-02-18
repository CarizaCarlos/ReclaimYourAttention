package com.reclaimyourattention.logic

abstract class Phase {
    // Atributos
    abstract protected var title: String
    abstract protected var description: String
    abstract protected var tasks: MutableSet<Task>

    // Métodos
    abstract fun areRequirementsMet(): Boolean
}