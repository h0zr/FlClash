package com.follow.clash

import android.app.Application
import android.content.ComponentName
import android.content.Context
import com.follow.clash.common.GlobalState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlClashApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalState.launch(Dispatchers.Main.immediate) {
            ServiceState.runState.collect {
                runCatching {
                    android.service.quicksettings.TileService.requestListeningState(
                        this@FlClashApplication,
                        ComponentName(this@FlClashApplication, TileService::class.java),
                    )
                }.onFailure { error ->
                    GlobalState.log("Unable to request tile refresh: $error")
                }
            }
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        GlobalState.init(this)
    }
}
