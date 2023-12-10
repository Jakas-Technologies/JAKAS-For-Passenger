package com.miftah.jakasforpassenger.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.utils.Constants.MapObjective
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    private var _pointDestination = MutableLiveData<LatLng?>()
    val pointDestination: LiveData<LatLng?> = _pointDestination

    private var _pointPosition = MutableLiveData<LatLng?>()
    val pointPosition: LiveData<LatLng?> = _pointPosition

    fun updatePoint(pointType: MapObjective, newValue: LatLng) {
        when (pointType) {
            MapObjective.DESTINATION -> _pointDestination.value = newValue
            MapObjective.POSITION -> _pointPosition.value = newValue
        }
    }

}