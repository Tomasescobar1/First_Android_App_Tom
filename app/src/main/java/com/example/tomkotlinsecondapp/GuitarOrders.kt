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
    var orderUpdate: Boolean = true,
    var instanceInd: Int = 0
)

data class OrderDataState (
    var colorInput: String = "White",
    var modelIndVal: String = "Telecaster",
    var customerInputVal: String = "",
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

    private val _deployedState = MutableStateFlow(false)

    val deployedState: StateFlow<Boolean> = _deployedState.asStateFlow()
    var orderList = mutableListOf<Guitar>()

    /*var dbOrderList = hashMapOf(
        "Customer" to " ",
        "Model" to " ",
        "Color" to " ",
        "ScaleLength" to 0.0
    )*/

    var dbOrderList: MutableMap<String, Any> = mutableMapOf(
        "Customer" to " ",
        "Model" to " ",
        "Color" to " ",
        "Scale Length" to 0.0
    )

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

                if(camIncrement.intValue > 3)
                {
                    camIncrement.intValue = 0

                    _dataState.update {currentDstate -> currentDstate.copy(cameraInd = camIncrement.intValue)}
                }
            }

            6 -> {
                _deployedState.update { !it }
            }

            7 -> {
                _deployedState.update{false}
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

            4 -> {
                if(input2)
                {
                    _orderState.update { currentState -> currentState.copy(orderUpdate = true) }
                }
                else
                {
                    _orderState.update { currentState -> currentState.copy(orderUpdate = false) }
                }
            }
        }
    }

    fun addListElement(customer: String = "Tom", model: String = "Telecaster", color: String = "White", scaleLength: Double = 25.5)
    {
        val newGuitar = Guitar(customer, model, color, scaleLength)

        orderList.add(newGuitar)

        dbOrderList.replace("Customer", orderList.last().customer.lowercase())

        dbOrderList.replace("Model", orderList.last().model)

        dbOrderList.replace("Color", orderList.last().color)

        dbOrderList.replace("Scale Length", orderList.last().scaleLength)

        println("Added ${orderList.last().color}")
    }
}