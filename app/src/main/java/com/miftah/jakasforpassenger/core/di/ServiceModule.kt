package com.miftah.jakasforpassenger.core.di

import com.miftah.jakasforpassenger.core.data.source.preference.UserPreference
import com.miftah.jakasforpassenger.core.data.source.remote.socket.SocketUserPositionHandlerImpl
import com.miftah.jakasforpassenger.core.data.source.remote.socket.SocketUserPositionHandlerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Named

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    @Named("TOKEN")
    fun provideAuth(userPreference: UserPreference): String {
        return runBlocking {
            userPreference.getSession().first().token
        }
    }

    @Provides
    @ServiceScoped
    fun provideSocketUserPositionHandlerService(@Named("TOKEN") token: String): SocketUserPositionHandlerService =
        SocketUserPositionHandlerImpl(token)

}