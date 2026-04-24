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

data class OrderDataState (
    var colorInput: String = "White",
    var modelIndVal: String = "Telecaster",
    var customerInputVal: String = " ",
    var scaleLengthInd: Double = 25.5,
    var cameraInd: Int = 0
)

class GuitarOrder : ViewModel()
{

    private val _dataState = MutableStateFlow(OrderDataState())

    val dataState: StateFlow<OrderDataState> = _dataState.asStateFlow()

    var camIncrement = mutableIntStateOf(0)

    private val _orderState = MutableStateFlow(OrderUIState())

    val orderState: StateFlow<OrderUIState> = _orderState.asStateFlow()
    var orderList = mutableListOf<Guitar>()

    var increment = mutableIntStateOf(0)

    fun updateDataState(input1: Int, input2: String, input3: Double)
    {
        when(input1)
        {
            1 -> {
                _dataState.update {currentDstate -> currentDstate.copy(colorInput = input2)}
            }

            2 -> {
                _dataState.update {currentDstate -> currentDstate.copy(modelIndVal = input2)}
            }

            3 -> {
                _dataState.update {currentDstate -> currentDstate.copy(customerInputVal = input2)}
            }

            4 -> {
                _dataState.update {currentDstate -> currentDstate.copy(scaleLengthInd = input3)}
            }

            5 -> {
                _dataState.update {currentDstate -> currentDstate.copy(cameraInd = camIncrement.intValue++)}
            }
        }
    }

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