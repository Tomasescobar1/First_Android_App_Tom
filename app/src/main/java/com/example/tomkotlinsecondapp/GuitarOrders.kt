package com.example.tomkotlinsecondapp
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
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
import android.nfc.Tag
import android.util.Log
import androidx.compose.runtime.currentRecomposeScope
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.AggregateSource
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
    var orderModInd: Boolean = false,
    var orderUpdate: Boolean = true,
    var orderUpdateFail: Boolean = false,
    var orderFoundInd: Boolean = false,
    var orderFoundFail: Boolean = false,
    var orderFoundFailMode: Boolean = false,
    var orderDelete: Boolean = false,
    var orderDeleteFail: Boolean = false,
    var instanceInd: Int = 0,
    var maintenanceInstance: Int = 0,
    var maintenanceSuccess: Boolean = false,
    var maintenanceFull: Boolean = false,
    var maintenanceDelete: Boolean = false,
    var maintenanceFail: Boolean = false,
    var maintenanceUpdate: Boolean = false
)

data class OrderDataState (
    var colorInput: String = "White",
    var modelIndVal: String = "Telecaster",
    var customerInputVal: String = "",
    var scaleLengthInd: Double = 25.5,
    var cameraInd: Int = 0
)

data class FetchedOrderData (
    var color: String = "",
    var modelInd: String = "",
    var customerOrdering: String = "",
    var scaleLength: Double = 0.0,
    var dateOfCreation: String = ""
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

    private val _orderSlotState = MutableStateFlow(false)

    val orderSlotState = _orderSlotState.asStateFlow()

    private val _maintenanceSlotState = MutableStateFlow(false)

    val maintenanceSlotState = _maintenanceSlotState.asStateFlow()

    private val _userOrderView = MutableStateFlow(false)

    val userOrderView = _userOrderView.asStateFlow()

    private val _userMaintenanceView = MutableStateFlow(false)

    val userMaintenanceView = _userMaintenanceView.asStateFlow()

    private val _orderFetchLoad = MutableStateFlow(false)

    val orderFetchLoad = _orderFetchLoad.asStateFlow()

    private val _maintenanceFetchLoad = MutableStateFlow(false)

    val maintenanceFetchLoad = _maintenanceFetchLoad.asStateFlow()

    private val _specificFetchedMaintenance = MutableStateFlow(false)

    val specificFetchedMaintenance = _specificFetchedMaintenance.asStateFlow()

    private val _dataState = MutableStateFlow(OrderDataState())

    val dataState: StateFlow<OrderDataState> = _dataState.asStateFlow()

    private val _orderState = MutableStateFlow(OrderUIState())

    val orderState = _orderState.asStateFlow()

    private val _deployedState = MutableStateFlow(FloatingActionState())

    val deployedState: StateFlow<FloatingActionState> = _deployedState.asStateFlow()

    var orderList = mutableListOf<Guitar>()

    var dbOrderList: MutableMap<String, Any> = mutableMapOf(
        "customerOrdering" to " ",
        "modelInd" to " ",
        "color" to " ",
        "scaleLength" to 0.0,
        "dateOfCreation" to " "
    )

    var orderDateList: List<String>? = listOf<String>()

    var fetchedOrderList: List<String> = listOf<String>()

    var maintenanceDateList: List<String>? = listOf<String>()

    var fetchedMaintenanceList: List<String> = listOf<String>()
    var increment = mutableIntStateOf(0)

    var maintenanceFetchedMap: MutableMap<String, Any>? = mutableMapOf<String, Any>()

    private val _isLoading = MutableStateFlow(false)

    val isLoading = _isLoading.asStateFlow()

    private val _maintenanceLoading = MutableStateFlow(false)

    val maintenanceLoading = _maintenanceLoading.asStateFlow()

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val dbOrders = db.collection("Orders")

    val dbMaintenance = db.collection("Maintenance")

    private val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isOffline = MutableStateFlow(!isCurrentlyOnline())

    val isOffline = _isOffline.asStateFlow()

    private val _orderSpecs = MutableStateFlow(FetchedOrderData())

    val orderSpecs = _orderSpecs.asStateFlow()

    private val _maintenanceSpecs = MutableStateFlow(maintenanceFetchedMap)

    val maintenanceSpecs = _maintenanceSpecs.asStateFlow()

    private val _specificFetchedOrder = MutableStateFlow(false)

    val specificFetchedOrder = _specificFetchedOrder.asStateFlow()

    private val _fetchedOrderDate = MutableStateFlow("")

    val fetchedOrderDate = _fetchedOrderDate.asStateFlow()

    private val _specificDocName = MutableStateFlow("")

    val specificDocName = _specificDocName.asStateFlow()

    private val _fetchedMaintenanceDate = MutableStateFlow("")

    val fetchedMaintenanceDate = _fetchedMaintenanceDate.asStateFlow()

    private val _specificMaintenanceDoc = MutableStateFlow("")

    val specificMaintenanceDoc = _specificMaintenanceDoc.asStateFlow()

    fun dateSetter(input: Int? = 1) : MutableMap<String, Int?>
    {
        val outputMap = mutableMapOf("OrderNumber" to input)
        return outputMap
    }

    fun dateList(input1: Int?, input2: String): MutableMap<String, String>
    {
        val outputMap = mutableMapOf("Date ${input1.toString()}" to input2)
        return outputMap
    }

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

                            checkSlotAvailability(true)

                            checkSlotAvailability()

                            checkSavedDates(true)

                            checkSavedDates()

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

    fun updateOrderState(input1: Int, input2: Boolean, input3: String = "")
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

                if(!input2)
                {
                    _orderState.update {currentState -> currentState.copy(orderSuccess = false)}
                }
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

            13 -> {
                _orderFetchLoad.value = false
            }

            14 -> {
                _specificFetchedOrder.value = false

                if(input2)
                {
                    _orderState.update {currentState -> currentState.copy(orderModInd = true)}
                }
                else
                {
                    _orderState.update {currentState -> currentState.copy(orderModInd = false)}
                }
            }

            15 -> {
                if(input2)
                {
                    _fetchedOrderDate.value = input3
                }
                else
                {
                    _specificDocName.value = input3
                }
            }

            16 -> {
                if(input2)
                {
                    _fetchedMaintenanceDate.value = input3
                }
                else
                {
                    _specificMaintenanceDoc.value = input3
                }
            }

            17 -> {
                _maintenanceFetchLoad.value = false
            }

            18 -> {
                _specificFetchedMaintenance.value = input2
            }

            19 -> {
                _orderState.update {currentState -> currentState.copy(maintenanceDelete = false)}
            }
        }
    }


    fun addListElement(customer: String = "Tom", model: String = "Telecaster", color: String = "White", scaleLength: Double = 25.5, dateOfCreation: String = "")
    {
        val newGuitar = Guitar(customer, model, color, scaleLength, dateOfCreation)

        orderList.add(newGuitar)

        if(customer != "")
        {
            dbOrderList.replace("customerOrdering", orderList.last().customer.lowercase())
        }

        dbOrderList.replace("modelInd", orderList.last().model)

        dbOrderList.replace("color", orderList.last().color)

        dbOrderList.replace("scaleLength", orderList.last().scaleLength)

        dbOrderList.replace("dateOfCreation", orderList.last().dateOfCreation)

        println("Added ${orderList.last().color}")
    }

    fun checkSlotAvailability(maintenanceToggle: Boolean  = false)
    {
        if(!maintenanceToggle)
        {
            viewModelScope.launch {
                try {
                    if (currentUser != null && uid != null) {
                        val snapshot = dbOrders.document(uid).collection("User preferences").document("Date quantity").get().await()

                        val snapshotLong: Int? = snapshot.getLong("OrderNumber")?.toInt()

                        if (snapshot.exists())
                        {
                            if (snapshotLong != null)
                            {
                                if (snapshotLong == 5)
                                {
                                    _orderSlotState.value = false

                                    println("Available order slots null")
                                }
                                else
                                {
                                    _orderSlotState.value = true

                                    println("There are available order slots!")
                                }
                            }
                            else
                            {
                                _orderSlotState.value = true

                                println("The snapshotLong variable is null... But can Still be written on.")
                            }
                        }
                        else
                        {
                            _orderSlotState.value = true

                            println("The snapshot doesn't exist yet.")
                        }
                    }
                }
                catch (e: Exception)
                {
                    println("Couldn't check the availability of the order slots")
                }
            }
        }
        else
        {
            viewModelScope.launch {

                try {
                    if (currentUser != null && uid != null)
                    {
                        val maintenanceSnapshot = dbMaintenance.document(uid).collection("User preferences").document("Date quantity").get().await()

                        val maintenanceSnapshotLong: Int? = maintenanceSnapshot.getLong("OrderNumber")?.toInt()

                        if (maintenanceSnapshot.exists())
                        {
                            if (maintenanceSnapshotLong != null)
                            {
                                if (maintenanceSnapshotLong == 5)
                                {
                                    _maintenanceSlotState.value = false

                                    println("Available maintenance slots null")
                                }
                                else
                                {
                                    _maintenanceSlotState.value = true

                                    println("There are available maintenance slots!")
                                }
                            }
                            else
                            {
                                _maintenanceSlotState.value = true

                                println("The snapshotLong variable is null... But can Still be written on.")
                            }
                        }
                        else
                        {
                            _maintenanceSlotState.value = true

                            println("The maintenance snapshot doesn't exist yet.")
                        }
                    }
                }
                catch (e: Exception)
                {
                    println("Couldn't check the availability of the maintenance slots")
                }
            }
        }
    }

    fun checkSavedDates(input: Boolean = false)
    {
        viewModelScope.launch {

            if(currentUser != null && uid != null)
            {
                try
                {
                    if(!input)
                    {
                        val ordersRef = dbOrders.document(uid).collection("User preferences").document("Dates placed").get().await()

                        if (ordersRef.exists())
                        {
                            orderDateList = ordersRef.get("Date Items") as? List<String>

                            for (i in 0 until (orderDateList?.size ?: 5))
                            {
                                println("Date number $i: ${orderDateList?.get(i)}")
                            }

                            _userOrderView.value = true
                        }
                    }
                    else
                    {
                        val maintenanceRef = dbMaintenance.document(uid).collection("User preferences").document("Dates placed").get().await()

                        if(maintenanceRef.exists())
                        {
                            maintenanceDateList = maintenanceRef.get("Date Items") as? List<String>

                            for (i in 0 until (maintenanceDateList?.size ?: 5))
                            {
                                Log.d("checkedSavedDates, Input = true", "Date number $i: ${maintenanceDateList?.get(i)}")
                            }

                            _userMaintenanceView.value = true
                        }
                    }
                }
                catch (e: Exception)
                {
                    println("Unable to fetch placed dates.")
                }
            }
        }
    }

    fun addDataToFirestore(inputOrderData: MutableMap<String, Any> = mutableMapOf(), inputMaintenanceData: MutableMap<String, Any> = mutableMapOf(), serviceOption: Boolean = false, serviceDate: String = "", update: Boolean = false, dateUpdate: String = "")
    {
        viewModelScope.launch {

            if(currentUser != null && uid != null)
            {
                try
                {
                    if (!serviceOption)
                    {
                        _isLoading.value = true

                        val snapshot = dbOrders.document(uid).collection("User preferences").document("Date quantity").get().await()

                        var snapshotLong: Int? = snapshot.getLong("OrderNumber")?.toInt()

                        if (snapshot.exists())
                        {
                            if (snapshotLong != null)
                            {
                                if (snapshotLong <= 5 && !update)
                                {
                                    snapshotLong += 1
                                }
                            }
                            else
                            {
                                println("SnapshotLong does not exist!")

                                snapshotLong = 1
                            }
                        }
                        else
                        {
                            snapshotLong = 1

                            dbOrders.document(uid).collection("User preferences").document("Date quantity").set(dateSetter(snapshotLong)).await()

                            dbOrders.document(uid).collection("User preferences").document("Dates placed").set(hashMapOf<String, Any>()).await()
                        }

                        if (snapshotLong <= 5)
                        {
                            if (!update)
                            {
                                dbOrders.document(uid).collection(serviceDate).document("${serviceDate}_${snapshotLong}").set(inputOrderData).await()

                                dbOrders.document(uid).collection("User preferences").document("Date quantity").set(dateSetter(snapshotLong)).await()

                                dbOrders.document(uid).collection("User preferences").document("Dates placed").update("Date Items", FieldValue.arrayUnion(serviceDate)).await()

                                _orderState.update { currentState -> currentState.copy(instanceInd = snapshotLong) }

                                _orderState.update { currentState -> currentState.copy(orderSuccess = true) }

                                if (snapshotLong == 5)
                                {
                                    _orderState.update { currentState -> currentState.copy(orderListFull = true) }
                                }

                                println("Added order to Firestore, yaaaay!")
                            }
                            else
                            {
                                dbOrders.document(uid).collection(serviceDate).document(dateUpdate).set(inputOrderData).await()

                                _orderState.update { currentState -> currentState.copy(updateSuccess = true) }

                                println("Updated Firestore order, yaaaaay!")
                            }
                        }
                        else
                        {
                            println("Order slots full, crap!")
                        }

                        _isLoading.value = false

                    }
                    else
                    {
                        _maintenanceLoading.value = true

                        val maintenanceSnapshot = dbMaintenance.document(uid).collection("User preferences").document("Date quantity").get().await()

                        var maintenanceSnapshotLong: Int? = maintenanceSnapshot.getLong("OrderNumber")?.toInt()

                        if(maintenanceSnapshot.exists())
                        {
                            if (maintenanceSnapshotLong != null)
                            {
                                if (maintenanceSnapshotLong <= 5 && !update)
                                {
                                    maintenanceSnapshotLong += 1
                                }
                            }
                            else
                            {
                                Log.d("addDataToFirestore-Maint", "SnapshotLong does not exist!")

                                maintenanceSnapshotLong = 1
                            }
                        }
                        else
                        {
                            maintenanceSnapshotLong = 1

                            dbMaintenance.document(uid).collection("User preferences").document("Date quantity").set(dateSetter(maintenanceSnapshotLong)).await()

                            dbMaintenance.document(uid).collection("User preferences").document("Dates placed").set(hashMapOf<String, Any>()).await()

                        }
                        if(maintenanceSnapshotLong <= 5)
                        {
                            if (!update)
                            {
                                dbMaintenance.document(uid).collection(serviceDate).document("${serviceDate}_${maintenanceSnapshotLong}").set(inputMaintenanceData).await()

                                dbMaintenance.document(uid).collection("User preferences").document("Date quantity").set(dateSetter(maintenanceSnapshotLong)).await()

                                dbMaintenance.document(uid).collection("User preferences").document("Dates placed").update("Date Items", FieldValue.arrayUnion(serviceDate)).await()

                                _orderState.update { currentState -> currentState.copy(maintenanceInstance = maintenanceSnapshotLong) }

                                _orderState.update { currentState -> currentState.copy(maintenanceSuccess = true) }

                                Log.d("addDataToFirestore", "Added maintenance to Firestore, yaaaay!")

                                if (maintenanceSnapshotLong == 5)
                                {
                                    _orderState.update { currentState -> currentState.copy(maintenanceFull = true) }
                                }
                            }
                            else
                            {
                                _maintenanceLoading.value = true

                                dbMaintenance.document(uid).collection(serviceDate).document(dateUpdate).set(inputMaintenanceData).await()

                                _orderState.update {currentState -> currentState.copy(maintenanceSuccess = true)}

                                Log.d("addDataToFirestore", "Maintenance updated!")
                            }
                        }

                        _maintenanceLoading.value = false

                    }
                }
                catch (e: Exception)
                {
                    if (serviceOption)
                    {
                        _orderState.update { currentState -> currentState.copy(maintenanceFail = true) }

                        println("Upload error message: ${e.message}")

                        _maintenanceLoading.value = false
                    }
                    else
                    {
                        _orderState.update { currentState -> currentState.copy(orderFail = true) }

                        _isLoading.value = false
                    }
                    println("Failed to add data, crap! ${e.message}")
                }
            }
        }
    }

    fun readOrderFromFirebase(input: String?, input2: Boolean = false, specificOrderParam: String = "", serviceOption: Boolean = false)
    {
        if(currentUser != null && uid != null)
        {
            viewModelScope.launch {
                try
                {
                    if(!serviceOption)
                    {
                        if (!input2)
                        {
                            val dateSnapshot = dbOrders.document(uid).collection(input.toString()).get().await()

                            fetchedOrderList = dateSnapshot.documents.map { document -> document.id }

                            for (i in 0 until fetchedOrderList.size)
                            {
                                println("Date $i: ${fetchedOrderList[i]}")
                            }

                            _orderFetchLoad.value = true
                        }
                        else
                        {
                            if (specificOrderParam != "")
                            {
                                val orderSnapshot = dbOrders.document(uid).collection(input.toString())
                                    .document(specificOrderParam).get().await()

                                if (orderSnapshot.exists())
                                {
                                    _orderSpecs.update { orderSnapshot.toObject(FetchedOrderData::class.java)!! }

                                    _specificFetchedOrder.value = true

                                    Log.d("readOrderFromFirebase", "${orderSpecs.value}")
                                }
                            }
                        }
                    }
                    else
                    {
                        if (!input2)
                        {
                            val dateSnapshot = dbMaintenance .document(uid).collection(input.toString()).get().await()

                            fetchedMaintenanceList = dateSnapshot.documents.map { document -> document.id }

                            for (i in 0 until fetchedMaintenanceList.size)
                            {
                                println("Date $i: ${fetchedMaintenanceList[i]}")
                            }

                            _maintenanceFetchLoad.value = true
                        }
                        else
                        {
                            if (specificOrderParam != "")
                            {
                                val maintenanceSnapshot = dbMaintenance.document(uid).collection(input.toString()).document(specificOrderParam).get().await()

                                if (maintenanceSnapshot.exists())
                                {
                                    _maintenanceSpecs.value = maintenanceSnapshot.data

                                    _specificFetchedMaintenance.value = true

                                    Log.d("readOrderFromFirebase", "${_maintenanceSpecs.value}")
                                }
                            }
                        }
                    }
                }
                catch (e: Exception)
                {
                    println("No fetched dates, crap!")
                }
            }
        }
    }

    fun orderDelete(serviceDate: String, dateToDelete: String, maintenance: Boolean = false)
    {
        viewModelScope.launch {
            try
            {
                if(currentUser != null && uid != null)
                {
                    if(!maintenance)
                    {
                        _isLoading.value = true

                        val countSnapshot = dbOrders.document(uid).collection(serviceDate).count().get(AggregateSource.SERVER).await()

                        val dateCount = countSnapshot.count.toInt()

                        Log.d("orderDelete", "${dateCount}")

                        val snapShot = dbOrders.document(uid).collection("User preferences").document("Date quantity").get().await()

                        var snapShotLong: Int? = snapShot.getLong("OrderNumber")?.toInt()

                        if (snapShot.exists()) {
                            if (snapShotLong != null) {
                                if (snapShotLong > 0)
                                {
                                    snapShotLong -= 1

                                    if (dateCount == 1)
                                    {
                                        dbOrders.document(uid).collection("User preferences").document("Dates placed").update("Date Items", FieldValue.arrayRemove(serviceDate)).await()
                                    }

                                    dbOrders.document(uid).collection("User preferences").document("Date quantity").set(dateSetter(snapShotLong)).await()
                                }
                            }
                        }

                        dbOrders.document(uid).collection(serviceDate).document(dateToDelete).delete().await()

                        _isLoading.value = false

                        _orderState.update { currentState -> currentState.copy(orderDelete = true) }
                    }
                    else
                    {
                        _maintenanceLoading.value = true

                        val countSnapshot = dbMaintenance.document(uid).collection(serviceDate).count().get(AggregateSource.SERVER).await()

                        val dateCount = countSnapshot.count.toInt()

                        Log.d("orderDelete", "${dateCount}")

                        val snapShot = dbMaintenance.document(uid).collection("User preferences").document("Date quantity").get().await()

                        var snapShotLong: Int? = snapShot.getLong("OrderNumber")?.toInt()

                        if (snapShot.exists())
                        {
                            if (snapShotLong != null)
                            {
                                if (snapShotLong > 0)
                                {
                                    snapShotLong -= 1

                                    if (dateCount == 1)
                                    {
                                        dbMaintenance.document(uid).collection("User preferences").document("Dates placed").update("Date Items", FieldValue.arrayRemove(serviceDate)).await()
                                    }

                                    dbMaintenance.document(uid).collection("User preferences").document("Date quantity").set(dateSetter(snapShotLong)).await()
                                }
                            }
                        }

                        dbMaintenance.document(uid).collection(serviceDate).document(dateToDelete).delete().await()

                        _maintenanceLoading.value = false

                        _orderState.update { currentState -> currentState.copy(maintenanceDelete = true) }

                    }
                }
            }
            catch (e: Exception)
            {
                _orderState.update {currentState -> currentState.copy(orderDeleteFail = true)}
            }
        }
    }

}