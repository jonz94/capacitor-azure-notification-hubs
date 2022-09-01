package dev.jonz94.capacitorjs.plugins.azurenotificationhubs;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.google.firebase.messaging.FirebaseMessaging;
import com.microsoft.windowsazure.messaging.notificationhubs.NotificationHub;

@CapacitorPlugin(name = "AzureNotificationHubs", permissions = @Permission(strings = {}, alias = "receive"))
public class AzureNotificationHubsPlugin extends Plugin {

    private static final String EVENT_TOKEN_CHANGE = "registration";
    private static final String EVENT_TOKEN_ERROR = "registrationError";

    @PluginMethod
    public void register(PluginCall call) {
        String notificationHubName = call.getString("notificationHubName");
        if (notificationHubName == null) {
            call.reject("Must provide notificationHubName");
            return;
        }

        String connectionString = call.getString("connectionString");
        if (connectionString == null) {
            call.reject("Must provide connectionString");
            return;
        }

        String deviceTag = call.getString("deviceTag");
        if (deviceTag == null) {
            call.reject("Must provide deviceTag");
            return;
        }

        NotificationHub.start(getActivity().getApplication(), notificationHubName, connectionString);
        NotificationHub.addTag(deviceTag);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging
            .getInstance()
            .getToken()
            .addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    sendError(task.getException().getLocalizedMessage());
                    return;
                }
                sendToken(task.getResult());
            });
        call.resolve();
    }

    public void sendToken(String token) {
        JSObject data = new JSObject();
        data.put("value", token);
        notifyListeners(EVENT_TOKEN_CHANGE, data, true);
    }

    public void sendError(String error) {
        JSObject data = new JSObject();
        data.put("error", error);
        notifyListeners(EVENT_TOKEN_ERROR, data, true);
    }
}
