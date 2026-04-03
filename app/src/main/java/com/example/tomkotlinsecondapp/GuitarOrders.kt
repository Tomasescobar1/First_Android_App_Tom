package com.example.tomkotlinsecondapp

class Guitar(var customer: String = "Tom", var model: String = " Telecaster", var color: String = "White", var scaleLength: Double = 25.5)

class GuitarOrder()
{
    var orderList = mutableListOf<Guitar>()
    fun addListElement(element: Guitar)
    {
        orderList.add(element)
    }
}