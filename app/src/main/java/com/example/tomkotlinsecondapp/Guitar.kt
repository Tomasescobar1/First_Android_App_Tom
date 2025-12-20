package com.example.tomkotlinsecondapp

class Guitar(var model: String = "Telecaster", var scaleLength: Double = 25.50, numberOfStrings : Int = 6) {

    var color: String = "Red"

    constructor(model: String ="Tele", scaleLength: Double, numberOfStrings : Int, color1 : String) : this(model, scaleLength, numberOfStrings) {

        this.color = color1

        println("The name of  the guitar model is $model")

    }

}

class GuitarTwo(modelTwo: String = "Telecaster", scaleLengthTwo : Double = 25.50, numberOfStringsTwo : Int = 6) {

    init {
        val modelName:String = modelTwo
    }

}