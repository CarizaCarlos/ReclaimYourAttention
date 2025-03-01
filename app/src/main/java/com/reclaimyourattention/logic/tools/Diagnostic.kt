package com.reclaimyourattention.logic.tools

object Diagnostic : Tool(){
    //Variables Superclase
    override val title: String = "Diagnóstico"
    override val description: String = "Realizar un cuestionario con el fin de entender los habitos del usuario"
    override val storageKey: String = "Diagnostic"

    //Atributos
    private var autoDiagnostico: String="aqui van datos importantes del cuestionario, va a ser de tipo arraylist"
    private var objetivoDiarioUsuario: Int=10

    //Métodos Superclase
    override  fun activate(vararg parameters: Any) {
        TODO("Not yet implemented")
    }

    override fun deactivate() {
        TODO("Not yet implemented")
    }
}