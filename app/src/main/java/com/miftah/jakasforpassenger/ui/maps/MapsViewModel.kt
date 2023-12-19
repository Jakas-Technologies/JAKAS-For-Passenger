package com.miftah.jakasforpassenger.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.maps.android.PolyUtil
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.utils.Constants.MapObjective
import com.miftah.jakasforpassenger.utils.MapsUtility.polylineToListLatLng
import com.miftah.jakasforpassenger.utils.SerializableDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    private var _pointDestination = MutableLiveData<SerializableDestination?>()
    val pointDestination: LiveData<SerializableDestination?> = _pointDestination

    private var _pointPosition = MutableLiveData<SerializableDestination?>()
    val pointPosition: LiveData<SerializableDestination?> = _pointPosition

    private var _isPointFilled = MutableLiveData<Boolean>()
    val isPointFilled: LiveData<Boolean> = _isPointFilled

    private var _isOnPath = MutableLiveData<Boolean>()
    val isOnPath: LiveData<Boolean> = _isOnPath

    private var _userPosition = MutableLiveData<LatLng>()
    val userPosition: LiveData<LatLng> = _userPosition

    private var _serviceLive = MutableLiveData<Boolean>()
    val serviceLive: LiveData<Boolean> = _serviceLive

    fun updatePoint(pointType: MapObjective, newValue: SerializableDestination) {
        when (pointType) {
            MapObjective.DESTINATION -> {
                _pointDestination.value = newValue
                checkPointIsFilled()
            }

            MapObjective.POSITION -> {
                _pointPosition.value = newValue
                checkPointIsFilled()
            }
        }
    }

    fun updateUserPosition(newValue: LatLng) {
        _userPosition.postValue(newValue)
    }

    fun updateServiceStatus(status: Boolean) {
        _serviceLive.postValue(status)
    }

    private fun checkPointIsFilled() {
        _isPointFilled.value = (_pointPosition.value != null) && (_pointDestination.value != null)
    }

    fun findAngkotBaseOnPositionAndDestination(
        position: SerializableDestination,
        destination: SerializableDestination
    ) = repository.findAngkotBaseOnPositionAndDestination(
        position.latLng, destination.latLng
    )

    fun isUserOnPath(
        userLocation: LatLng,
        polyline: Polyline,
        tolerance: Double = 10.0
    ) {
        val listLng = polylineToListLatLng(polyline)
        val resultUser = PolyUtil.isLocationOnPath(
            userLocation,
            listLng,
            false,
            tolerance
        )
        val lastData = _pointPosition.value
        lastData?.let {
            it.latLng = userLocation
            if (!resultUser) _pointPosition.postValue(lastData)
        }
        _isOnPath.postValue(resultUser)
    }
}