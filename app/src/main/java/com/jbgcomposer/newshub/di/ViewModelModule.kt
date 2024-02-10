package com.jbgcomposer.newshub.di

import android.content.Context
import android.net.ConnectivityManager
import com.jbgcomposer.newshub.utils.NetworkStatusChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun providesNetworkStatusChecker(@ApplicationContext context: Context) : NetworkStatusChecker {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return NetworkStatusChecker(connectivityManager)
    }
}