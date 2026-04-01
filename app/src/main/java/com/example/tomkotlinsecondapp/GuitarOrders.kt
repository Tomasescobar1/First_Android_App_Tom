package com.example.tomkotlinsecondapp

class GuitarOrder()
{
    var orderList = mutableListOf<Guitar>()

    fun addListElement(element: Guitar)
    {
        orderList.add(element)
    }
}