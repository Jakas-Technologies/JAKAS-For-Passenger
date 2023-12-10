package com.miftah.jakasforpassenger.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.utils.MapObjective
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    private var _pointDestination = MutableLiveData<LatLng?>()
    val pointDestination: LiveData<LatLng?> = _pointDestination

    private var _pointPosition = MutableLiveData<LatLng?>()
    val pointPosition: LiveData<LatLng?> = _pointPosition

    var canFindDestination: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        var pointDestinationValue: Boolean? = null
        var pointPositionValue: Boolean? = null

        fun updateResult() {
            value = pointDestinationValue == true && pointPositionValue == true
        }

        addSource(pointDestination) { data ->
            pointDestinationValue = data != null
            updateResult()
        }

        addSource(pointPosition) { data ->
            pointPositionValue = data != null
            updateResult()
        }
    }

    fun updatePoint(pointType: MapObjective, newValue: LatLng) {
        when (pointType) {
            MapObjective.DESTINATION -> _pointDestination.value = newValue
            MapObjective.POSITION -> _pointPosition.value = newValue
        }
    }

}