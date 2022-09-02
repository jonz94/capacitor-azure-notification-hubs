package dev.jonz94.capacitorjs.plugins.azurenotificationhubs

import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.google.firebase.messaging.FirebaseMessaging
import com.microsoft.windowsazure.messaging.notificationhubs.NotificationHub

@CapacitorPlugin(name = "AzureNotificationHubs")
class AzureNotificationHubsPlugin : Plugin() {
    companion object {
        private const val EVENT_TOKEN_CHANGE = "registration"
        private const val EVENT_TOKEN_ERROR = "registrationError"
    }

    @PluginMethod
    fun register(call: PluginCall) {
        val notificationHubName = call.getString("notificationHubName")
        notificationHubName ?: run {
            call.reject("Must provide notificationHubName")
            return
        }
        val connectionString = call.getString("connectionString")
        connectionString ?: run {
            call.reject("Must provide connectionString")
            return
        }
        val deviceTag = call.getString("deviceTag")
        deviceTag ?: run {
            call.reject("Must provide deviceTag")
            return
        }

        NotificationHub.start(activity.application, notificationHubName, connectionString)
        NotificationHub.addTag(deviceTag)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging
            .getInstance()
            .token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    sendError(task.exception?.localizedMessage)
                    return@addOnCompleteListener
                }
                sendToken(task.result)
            }
        call.resolve()
    }

    private fun sendToken(token: String) {
        val data = JSObject()
        data.put("value", token)
        notifyListeners(EVENT_TOKEN_CHANGE, data, true)
    }

    private fun sendError(error: String?) {
        val data = JSObject()
        data.put("error", error)
        notifyListeners(EVENT_TOKEN_ERROR, data, true)
    }
}
