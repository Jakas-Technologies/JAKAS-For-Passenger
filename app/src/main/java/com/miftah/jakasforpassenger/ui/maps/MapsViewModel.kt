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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    private var _pointDestination = MutableLiveData<LatLng?>()
    val pointDestination: LiveData<LatLng?> = _pointDestination

    private var _pointPosition = MutableLiveData<LatLng?>()
    val pointPosition: LiveData<LatLng?> = _pointPosition

    private var _isPointFilled = MutableLiveData<Boolean>()
    val isPointFilled: LiveData<Boolean> = _isPointFilled

    private var _isOnPath = MutableLiveData<Boolean>()
    val isOnPath: LiveData<Boolean> = _isOnPath

    fun updatePoint(pointType: MapObjective, newValue: LatLng) {
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

    private fun checkPointIsFilled() {
        _isPointFilled.value = (_pointPosition.value != null) && (_pointDestination.value != null)
    }

    fun findAngkotBaseOnPositionAndDestination(
        position: String,
        destination: String
    ) = repository.findAngkotBaseOnPositionAndDestination(position, destination)

    fun isUserOnPath(
        userLocation: LatLng,
        positionLocation: LatLng,
        polyline: Polyline,
        tolerance: Double = 10.0
    ) {
//        val points = polyline.points
//        val path = PolyUtil.decode(polyline.id)

//        val result = PolyUtil.isLocationOnPath(userLocation, polyline, true, tolerance)
        val listLng = polylineToListLatLng(polyline)

        val resultUser =
            PolyUtil.isLocationOnPath(userLocation, listLng, false, tolerance)
        val resultDestination =
            PolyUtil.isLocationOnPath(positionLocation, listLng, false, tolerance)

        _isOnPath.postValue(resultUser && resultDestination)
    }
}