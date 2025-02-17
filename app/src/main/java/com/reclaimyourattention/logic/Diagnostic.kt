package com.reclaimyourattention.logic

class Diagnostic(override var title: String, override var description: String) : Tool(){
    //Atributos
    private var autoDiagnostico: String="aqui van datos importantes del autodiagnostico, va a ser de tipo arraylist"
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