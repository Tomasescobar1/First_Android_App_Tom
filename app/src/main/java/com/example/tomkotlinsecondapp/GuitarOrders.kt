package com.example.tomkotlinsecondapp

import androidx.lifecycle.ViewModel

data class Guitar(var customer: String = "Tom", var model: String = " Telecaster", var color: String = "White", var scaleLength: Double = 25.5)

class GuitarOrder : ViewModel()
{
    var orderList = mutableListOf<Guitar>()

    fun addListElement(customer: String = "Tom", model: String = "Telecaster", color: String = "White", scaleLength: Double = 25.5)
    {
        val newGuitar = Guitar(customer, model, color, scaleLength)

        orderList.add(newGuitar)

        println("Added ${orderList.last().color}")
    }
}