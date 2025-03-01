package com.reclaimyourattention.logic.tools

object Diagnostic : Tool(){
    //Variables Superclase
    override val title: String
        get() = "Diagnóstico"
    override val description: String
        get() = "Realizar un cuestionario con el fin de entender los habitos del usuario"


    //Atributos
    private var autoDiagnostico: String="aqui van datos importantes del cuestionario, va a ser de tipo arraylist"
    private var objetivoDiarioUsuario: Int=10


    //Variables Superclase


    //Métodos Superclase
    override  fun activate(vararg parameters: Any) {
        TODO("Not yet implemented")
    }

    override fun deactivate() {
        TODO("Not yet implemented")
    }

    //Métodos

}