package com.example.tomkotlinsecondapp
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Guitar (
    var customer: String = "Tom",
    var model: String = " Telecaster",
    var color: String = "White",
    var scaleLength: Double = 25.5
)

data class OrderUIState (
    var orderListFull: Boolean = false,
    var orderPlaceG: Boolean = false,
    var orderSuccess: Boolean = false,
    var orderFail: Boolean = false,
    var updateLoad: Boolean = false,
    var updateSuccess: Boolean = false,
    var updateLoadFail: Boolean = false,
    var orderUpdate: Boolean = true,
    var orderUpdateFail: Boolean = false,
    var orderFoundInd: Boolean = false,
    var orderFoundFail: Boolean = false,
    var orderDelete: Boolean = false,
    var orderDeleteFail: Boolean = false,
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

    var dbOrderList: MutableMap<String, Any> = mutableMapOf(
        "Customer" to " ",
        "Model" to " ",
        "Color" to " ",
        "Scale Length" to 0.0
    )

    var increment = mutableIntStateOf(0)

    private val _isLoading = MutableStateFlow(false)

    val isLoading = _isLoading.asStateFlow()

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val dbOrders = db.collection("Orders")

    private val _foundOrderString = MutableStateFlow("")

    val foundOrderString = _foundOrderString.asStateFlow()

    var foundDocumentId: String = ""

    private val _updatedOrderString = MutableStateFlow("")

    val updatedOrderString = _updatedOrderString.asStateFlow()

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
        when(input1)
        {
            1 -> {
                _orderState.update { currentState -> currentState.copy(orderListFull = input2) }
            }

            2 -> {
                _orderState.update { currentState -> currentState.copy(orderPlaceG = input2) }
            }

            3 -> {
                increment.intValue = 0

                _orderState.update { currentState -> currentState.copy(instanceInd = 0)}
            }

            4 -> {
                _isLoading.value = input2
            }

            5 -> {
                _orderState.update {currentState -> currentState.copy(orderFail = false)}
            }

            6 -> {
                _orderState.update {currentState -> currentState.copy(orderFoundInd = false)}

                if(input2)
                {
                    _orderState.update {currentState -> currentState.copy(orderFoundFail = false)}
                }
            }

            7 -> {
                _orderState.update {currentState -> currentState.copy(orderUpdate = false)}
            }

            8 -> {
                _orderState.update {currentState -> currentState.copy(updateSuccess = false)}
            }

            9 -> {
                _orderState.update {currentState -> currentState.copy(orderDelete = false)}
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

    fun addDataToFirestore(inputData: MutableMap<String, Any>)
    {
        viewModelScope.launch {
            try
            {
                _isLoading.value = true

                dbOrders.add(inputData).await()

                increment.intValue++

                _orderState.update { currentState -> currentState.copy(instanceInd = increment.intValue) }

                _orderState.update { currentState -> currentState.copy(orderSuccess = true) }
            }
            catch (e: Exception)
            {
                _orderState.update { currentState -> currentState.copy(orderFail = true)}

                println("Failed to add data, crap!")
            }
        }
    }

    fun orderFind(input: String = "")
    {
        if(input != "")
        {
            viewModelScope.launch {
                try
                {
                    val snapshot = dbOrders.whereEqualTo("Customer", input.lowercase()).get().await()

                    for(document in snapshot.documents)
                    {
                        if(document.data?.get("Customer") == input.lowercase()) {

                            foundDocumentId = document.id

                            val foundOrder: MutableMap<String, Any>? = document.data

                            if(foundOrder != null)
                            {
                                _foundOrderString.value = foundOrder.entries.joinToString(separator = "\n") { entry -> "${entry.key}: ${entry.value}" }
                            }

                            _orderState.update {currentState -> currentState.copy(orderFoundInd = true)}

                            break
                        }
                    }
                }
                catch (e: Exception)
                {
                    _orderState.update {currentState -> currentState.copy(orderFoundFail = true)}
                }
            }
        }
    }

    fun orderUpdate(customer: String = "", model: String = "Telecaster", color: String = "White", scaleLength: Double = 25.5)
    {
        val documentRef = dbOrders.document(foundDocumentId)

        if(customer != "")
        {
            addListElement(
                customer,
                model,
                color,
                scaleLength
            )

            viewModelScope.launch {
                try
                {
                    documentRef.update(dbOrderList).await()

                    _updatedOrderString.value = dbOrderList.entries.joinToString(separator = "\n") { entry -> "${entry.key}: ${entry.value}" }

                    _orderState.update {currentState -> currentState.copy(updateSuccess = true)}
                }
                catch (e: Exception)
                {
                    _orderState.update {currentState -> currentState.copy(orderUpdateFail = true)}
                }
            }

        }
    }

    fun orderDelete()
    {
        if(foundDocumentId != "")
        {
            viewModelScope.launch {
                try
                {
                    dbOrders.document(foundDocumentId).delete().await()

                    _orderState.update {currentState -> currentState.copy(orderDelete = true)}
                }
                catch (e: Exception)
                {
                    _orderState.update {currentState -> currentState.copy(orderDeleteFail = true)}
                }
            }
        }
    }

}