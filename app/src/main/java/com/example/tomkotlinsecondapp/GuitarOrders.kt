package com.example.tomkotlinsecondapp
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
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
import android.util.Log
import com.google.firebase.auth.GoogleAuthProvider
import com.google.rpc.context.AttributeContext
import kotlinx.coroutines.async
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AuthRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance())
{
    suspend fun signIn(email: String, pass: String): AuthResult{
        return auth.signInWithEmailAndPassword(email, pass).await()
    }
}

data class Guitar (
    var customer: String = "Tom",
    var model: String = " Telecaster",
    var color: String = "White",
    var scaleLength: Double = 25.5,
    var dateOfCreation: String =""
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

    private val credentialManager = CredentialManager.create(application)

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val uid = currentUser?.uid

    private val webClientID = "443758218420-roslrqrib5t3g8uq15c8p1a9gkdblld7.apps.googleusercontent.com"

    private val _authLoadingState = MutableStateFlow(false)

    val authLoadingState = _authLoadingState.asStateFlow()

    private val _authState = MutableStateFlow(false)

    val authState = _authState.asStateFlow()

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
        "Scale Length" to 0.0,
        "Date Of Creation" to " "
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

    fun formatDayMonthYear(timeStampMillis: Long): String
    {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())

        return formatter.format(Instant.ofEpochMilli(timeStampMillis))
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String)
    {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            val authResult = firebaseAuth.signInWithCredential(credential).await()

            val firebaseUser = authResult.user

            println("firebaseAuthWithGoogle method success, Firebase sign in success!")
        }
        catch(e: Exception)
        {
            println("firebaseAuthWithGoogle method, failed to authenticate, crap")
        }
    }

    fun signInWithGoogle(activityContext: Context)
    {
        viewModelScope.launch{
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientID)
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                _authLoadingState.value = true

                val result = credentialManager.getCredential(
                    request = request,
                    context = activityContext
                )

                when (val credential = result.credential)
                {
                    is CustomCredential -> {
                        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)
                        {
                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                            val idToken = googleIdTokenCredential.idToken

                            firebaseAuthWithGoogle(idToken)

                            println("The sign in with Google method worked!!!!, ID token: $idToken")

                            _authLoadingState.value = false

                            _authState.value = true
                        }
                    }
                    else -> {
                        _authLoadingState.value = false

                        println("Unexpected credential type, crap")
                    }
                }
            }
            catch(e: GetCredentialException)
            {
                _authLoadingState.value = false

                println("signInWithGoogle method sign in failed, exception caught.")

                println("Credential error type: ${e.type}")

                println("Credential error message: ${e.message}")
            }
        }
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


    fun addListElement(customer: String = "Tom", model: String = "Telecaster", color: String = "White", scaleLength: Double = 25.5, dateOfCreation: String = "")
    {
        val newGuitar = Guitar(customer, model, color, scaleLength, dateOfCreation)

        orderList.add(newGuitar)

        dbOrderList.replace("Customer", orderList.last().customer.lowercase())

        dbOrderList.replace("Model", orderList.last().model)

        dbOrderList.replace("Color", orderList.last().color)

        dbOrderList.replace("Scale Length", orderList.last().scaleLength)

        dbOrderList.replace("Date Of Creation", orderList.last().dateOfCreation)

        println("Added ${orderList.last().color}")
    }

    fun addDataToFirestore(inputOrderData: MutableMap<String, Any> = mutableMapOf(), inputMaintenanceData: MutableMap<String, Any> = mutableMapOf(), serviceOption: Boolean = false, serviceDate: String = "")
    {
        viewModelScope.launch {
                try {
                    if(currentUser != null && uid != null)
                    {
                        if (!serviceOption)
                        {
                            _isLoading.value = true

                            increment.intValue++

                            dbOrders.document(uid).collection(serviceDate).document("${serviceDate}_${increment.intValue}").set(inputOrderData).await()

                            /*val orderDataSet = async {
                                dbOrders.document(uid).collection(serviceDate).document().set(inputOrderData).await()
                            }

                            val order*/

                            //dbOrders.document(uid).collection("Dates of creation").document(serviceDate).set({}).await()

                            //increment.intValue++

                            //dbOrders.document(uid).collection("Dates of creation").document(serviceDate).set("").await()

                            _orderState.update { currentState -> currentState.copy(instanceInd = increment.intValue) }

                            _orderState.update { currentState -> currentState.copy(orderSuccess = true) }

                            println("Added order to Firestore, yaaaay!")
                        }
                        else
                        {
                            _maintenanceLoading.value = true

                            dbMaintenance.document(uid).collection(serviceDate).document().set(inputMaintenanceData).await()

                            _orderState.update { currentState ->
                                currentState.copy(
                                    maintenanceSuccess = true
                                )
                            }

                            println("Added maintenance to Firestore, yaaaay!")
                        }
                    }
                } catch (e: Exception) {
                    if (serviceOption) {
                        _orderState.update { currentState -> currentState.copy(maintenanceFail = true) }

                        println("Upload error message: ${e.message}")
                    } else {
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
                    val snapshot = dbOrders.document(uid.toString()).get().await()

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