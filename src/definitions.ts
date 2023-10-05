import type { PluginListenerHandle } from '@capacitor/core';

export interface AzureNotificationHubsPlugin {
  /**
   * Register the app to receive push notifications.
   *
   * This method will trigger the `'registration'` event with the push token or
   * `'registrationError'` if there was a problem. It does not prompt the user for
   * notification permissions, use `PushNotifications.requestPermissions()` from
   * `@capacitor/push-notifications` first.
   *
   * @since 1.0.0
   */
  register(info: RegisterInfo): Promise<void>;

  /**
   * Called when the push notification registration finishes without problems.
   *
   * Provides the push notification token.
   *
   * @since 1.0.0
   */
  addListener(
    eventName: 'registration',
    listenerFunc: (token: Token) => void,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;

  /**
   * Called when the push notification registration finished with problems.
   *
   * Provides an error with the registration problem.
   *
   * @since 1.0.0
   */
  addListener(
    eventName: 'registrationError',
    listenerFunc: (error: RegistrationError) => void,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;

  /**
   * Remove all native listeners for this plugin.
   *
   * @since 1.0.0
   */
  removeAllListeners(): Promise<void>;
}

export interface RegisterInfo {
  /**
   * The notification hub name.
   *
   * @since 1.0.0
   */
  notificationHubName: string;

  /**
   * The connection string.
   *
   * @since 1.0.0
   */
  connectionString: string;

  /**
   * The device tag.
   *
   * @since 1.0.0
   */
  deviceTag: string;
}

export interface Token {
  /**
   * On iOS it contains the APNS token.
   * On Android it contains the FCM token.
   *
   * @since 1.0.0
   */
  value: string;
}

export interface RegistrationError {
  /**
   * Error message describing the registration failure.
   *
   * @since 1.0.0
   */
  error: string;
}
