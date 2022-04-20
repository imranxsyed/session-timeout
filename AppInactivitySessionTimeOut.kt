package com.photo.compare.dailylife.common

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import diet.viewmodel.FireBaseNetwork
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.lang.ClassCastException
import java.util.concurrent.CancellationException

class AppInactivitySessionTimeOut(val activity: Activity)  : IAppInactivitySessionTimeOut{

    init {
        if ((activity is AppInactivityTimeoutSessionListener).not())
            throw ClassCastException("Activity must implement onAppInactivitySessionTimeout")
    }

    var inActivityTimeoutSession : Job? = null

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d(FireBaseNetwork.TAG, "onResume: ")
    }
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d(FireBaseNetwork.TAG, "activityStopped: onActivity stoppped called")
        inActivityTimeoutSession?.cancel(CancellationException("Activity Stopped. Therefore this session timer is no longer needed"))
    }

    override fun cancelPreviousInactivityTimeoutSession() {
        Log.d(FireBaseNetwork.TAG, "cancelPreviousInactivityTimeoutSession: ")
        inActivityTimeoutSession?.cancel()
    }

    override fun startAppInactivityTimeoutSession(timeout: Long) {
        cancelPreviousInactivityTimeoutSession()
        inActivityTimeoutSession = ProcessLifecycleOwner.get().lifecycleScope.launchWhenCreated {
            Log.d(FireBaseNetwork.TAG, "startInActivityTimeoutSession: Corourtine launched")
            delay(timeout)
            if (isActive.not()){
                Log.d(FireBaseNetwork.TAG, "startInActivityTimeoutSession: Corourtine Cancelled")
                return@launchWhenCreated
            }
            Log.d(FireBaseNetwork.TAG, "startInActivityTimeoutSession: Lock Activity Due to InActivity")
            (activity as AppInactivityTimeoutSessionListener).onAppInactivitySessionTimeout()

        }

    }

}