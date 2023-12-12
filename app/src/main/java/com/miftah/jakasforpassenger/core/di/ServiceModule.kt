package com.miftah.jakasforpassenger.core.di

import com.miftah.jakasforpassenger.core.data.source.remote.socket.SocketHandlerImpl
import com.miftah.jakasforpassenger.core.data.source.remote.socket.SocketHandlerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun provideSocketHandlerService() : SocketHandlerService = SocketHandlerImpl()
}