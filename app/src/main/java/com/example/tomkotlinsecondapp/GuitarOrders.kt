package com.example.tomkotlinsecondapp
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

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
    var orderSearchLoad: Boolean = false,
    var updateLoad: Boolean = false,
    var updateSuccess: Boolean = false,
    var updateLoadFail: Boolean = false,
    var orderUpdate: Boolean = true,
    var orderUpdateFail: Boolean = false,
    var orderFoundInd: Boolean = false,
    var orderFoundFail: Boolean = false,
    var orderFoundFailMode: Boolean = false,
    var orderDelete: Boolean = false,
    var orderDeleteFail: Boolean = false,
    var instanceInd: Int = 0,
    var maintenanceSuccess: Boolean = false,
    var maintenanceFail: Boolean = false
)

data class OrderDataState (
    var colorInput: String = "White",
    var modelIndVal: String = "Telecaster",
    var customerInputVal: String = "",
    var scaleLengthInd: Double = 25.5,
    var cameraInd: Int = 0
)

data class FloatingActionState(
    var deployedState: Boolean = false,
    var invBackground: Boolean = false,
    var exitDeploy: Boolean = false
)

class GuitarOrder(application: Application) : AndroidViewModel(application)
{
    private val _dataState = MutableStateFlow(OrderDataState())

    val dataState: StateFlow<OrderDataState> = _dataState.asStateFlow()

    private val _orderState = MutableStateFlow(OrderUIState())

    val orderState: StateFlow<OrderUIState> = _orderState.asStateFlow()

    private val _deployedState = MutableStateFlow(FloatingActionState())

    val deployedState: StateFlow<FloatingActionState> = _deployedState.asStateFlow()
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

    private val _maintenanceLoading = MutableStateFlow(false)

    val maintenanceLoading = _maintenanceLoading.asStateFlow()

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val dbOrders = db.collection("Orders")

    val dbMaintenance = db.collection("Maintenance")

    private val _foundOrderString = MutableStateFlow("")

    val foundOrderString = _foundOrderString.asStateFlow()

    var foundDocumentId: String = ""

    private val _updatedOrderString = MutableStateFlow("")

    val updatedOrderString = _updatedOrderString.asStateFlow()

    private val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isOffline = MutableStateFlow(!isCurrentlyOnline())

    val isOffline = _isOffline.asStateFlow()

    private fun isCurrentlyOnline() : Boolean
    {
        val activeNetwork = connectivityManager.activeNetwork ?: return false

        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback()
    {
        override fun onAvailable(network: Network)
        {
            _isOffline.value = false

            db.enableNetwork()
        }

        override fun onLost(network: Network)
        {
            _isOffline.value = true

            db.disableNetwork()
        }
    }

    init
    {
        if(isOffline.value)
        {
            db.disableNetwork()
        }
        else
        {
            db.enableNetwork()
        }

        val request = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    override fun onCleared()
    {
        super.onCleared()

        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun updateDataState(input1: Int, input2: String, input3: Double, input4: Boolean = false)
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
                _deployedState.update {currentState -> currentState.copy(exitDeploy = true)}

                if(input4)
                {
                    _deployedState.update {currentState -> currentState.copy(exitDeploy = false)}
                }
            }

            6 -> {
                _deployedState.update { currentState -> currentState.copy(deployedState = true) }
            }

            7 -> {
                _deployedState.update{ currentState -> currentState.copy(deployedState = false)}
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
                if(!input2)
                {
                    _orderState.update { currentState -> currentState.copy(orderFail = false) }
                }
                else
                {
                    _orderState.update {currentState -> currentState.copy(maintenanceFail = false)}
                }
            }

            6 -> {
                _orderState.update {currentState -> currentState.copy(orderFoundInd = false)}

                if(input2)
                {
                    _orderState.update {currentState -> currentState.copy(orderFoundFail = false)}

                    _orderState.update {currentState -> currentState.copy(orderFoundFailMode = false)}
                }
            }

            7 -> {
                _orderState.update {currentState -> currentState.copy(orderUpdate = input2)}
            }

            8 -> {
                _orderState.update {currentState -> currentState.copy(updateSuccess = false)}
            }

            9 -> {
                _orderState.update {currentState -> currentState.copy(orderDelete = false)}
            }

            10 -> {
                _orderState.update {currentState -> currentState.copy(orderSearchLoad = false)}
            }

            11 -> {
                if(input2)
                {
                    _orderState.update { currentState -> currentState.copy(maintenanceSuccess = false) }
                }
                else
                {
                    _orderState.update { currentState -> currentState.copy(maintenanceFail = false)}
                }
            }

            12 -> {
                _maintenanceLoading.value = input2
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

    fun addDataToFirestore(inputOrderData: MutableMap<String, Any> = mutableMapOf(), inputMaintenanceData: MutableMap<String, Any> = mutableMapOf(), serviceOption: Boolean = false)
    {
        viewModelScope.launch {
            try
            {
                if (!serviceOption)
                {
                    _isLoading.value = true

                    dbOrders.add(inputOrderData).await()

                    increment.intValue++

                    _orderState.update { currentState -> currentState.copy(instanceInd = increment.intValue) }

                    _orderState.update { currentState -> currentState.copy(orderSuccess = true) }

                    println("Added order to Firestore, yaaaay!")
                }
                else
                {
                    _maintenanceLoading.value = true

                    dbMaintenance.add(inputMaintenanceData).await()

                    _orderState.update { currentState -> currentState.copy(maintenanceSuccess = true) }

                    println("Added maintenance to Firestore, yaaaay!")
                }
            }
            catch (e: Exception)
            {
                if (serviceOption)
                {
                    _orderState.update { currentState -> currentState.copy(maintenanceFail = true) }
                } else
                {
                    _orderState.update { currentState -> currentState.copy(orderFail = true) }
                }
                println("Failed to add data, crap!")
            }
        }
    }

    fun orderFind(input: String = "", orderType: Boolean = false)
    {
        if(input != "" && !orderType)
        {
            _orderState.update{currentState -> currentState.copy(orderSearchLoad = true)}

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

                    if(!orderState.value.orderFoundInd)
                    {
                        _orderState.update {currentState -> currentState.copy(orderFoundFail = true)}
                    }
                }
                catch (e: Exception)
                {
                    _orderState.update {currentState -> currentState.copy(orderFoundFailMode = true)}

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