package com.reclaimyourattention.logic

class Diagnostic() : Tool(){
    //Variables Superclase
    override var title: String = "Diagnóstico"
    override var description: String =
        "Realizar un cuestionario con el fin de entender los habitos del usuario"


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