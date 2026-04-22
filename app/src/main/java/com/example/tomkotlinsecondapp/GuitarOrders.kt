package com.example.tomkotlinsecondapp
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Guitar (
    var customer: String = "Tom",
    var model: String = " Telecaster",
    var color: String = "White",
    var scaleLength: Double = 25.5
)

data class OrderUIState (
    var orderListFull: Boolean = false,
    var orderPlaceG: Boolean = false,
    var instanceInd: Int = 0
)

class GuitarOrder : ViewModel()
{

    private val _orderState = MutableStateFlow(OrderUIState())

    val orderState: StateFlow<OrderUIState> = _orderState.asStateFlow()
    var orderList = mutableListOf<Guitar>()

    var increment = mutableIntStateOf(0)

    fun updateOrderState(input1: Int, input2: Boolean)
    {
        //var increment:Int = 0

        when(input1)
        {
            1 -> {
                _orderState.update { currentState -> currentState.copy(orderListFull = input2) }
            }

            2 -> {
                _orderState.update { currentState -> currentState.copy(orderPlaceG = input2) }
            }

            3 -> {
                if(input2)
                {
                    increment.intValue ++

                    _orderState.update { currentState -> currentState.copy(instanceInd = increment.intValue) }
                }
                else
                {
                    _orderState.update { currentState -> currentState.copy(instanceInd = 0)}
                }
            }
        }
    }

    fun addListElement(customer: String = "Tom", model: String = "Telecaster", color: String = "White", scaleLength: Double = 25.5)
    {
        val newGuitar = Guitar(customer, model, color, scaleLength)

        orderList.add(newGuitar)

        println("Added ${orderList.last().color}")
    }
}