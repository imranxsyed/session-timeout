package com.photo.compare.dailylife.common

import androidx.lifecycle.*

interface IAppInactivitySessionTimeOut : DefaultLifecycleObserver {

    fun cancelPreviousInactivityTimeoutSession()

    fun startAppInactivityTimeoutSession(timeout: Long)
}