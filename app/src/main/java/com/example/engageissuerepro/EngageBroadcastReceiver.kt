package com.example.engageissuerepro

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.google.android.engage.service.Intents

class EngageBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        EngageWorker.startWorker(context)
    }

    companion object {
        @SuppressLint("UnspecifiedRegisterReceiverFlag")
        @JvmStatic
        fun register(context: Context) {
            val intentFilter = IntentFilter().apply {
                addAction(Intents.ACTION_PUBLISH_RECOMMENDATION)
                addAction(Intents.ACTION_PUBLISH_FEATURED)
                addAction(Intents.ACTION_PUBLISH_CONTINUATION)
            }
            if (Build.VERSION.SDK_INT >= 33) {
                context.registerReceiver(
                    EngageBroadcastReceiver(),
                    intentFilter,
                    Context.RECEIVER_EXPORTED
                )
            } else {
                context.registerReceiver(EngageBroadcastReceiver(), intentFilter)
            }
        }
    }
}