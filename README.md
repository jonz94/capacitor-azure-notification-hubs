<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Azure Notification Hubs</h3>
<p align="center"><strong><code>@jonz94/capacitor-azure-notification-hubs</code></strong></p>
<p align="center">
  Capacitor plugin to register push notifications via Azure Notification Hub.
</p>

<p align="center">
  <a href="https://www.npmjs.com/package/@jonz94/capacitor-azure-notification-hubs"><img src="https://img.shields.io/npm/l/@jonz94/capacitor-azure-notification-hubs?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@jonz94/capacitor-azure-notification-hubs"><img src="https://img.shields.io/npm/v/@jonz94/capacitor-azure-notification-hubs?style=flat-square" /></a>
</p>

## Install

```shell
npm install @jonz94/capacitor-azure-notification-hubs @capacitor/push-notifications
npx cap sync
```

## iOS

On iOS you must enable the Push Notifications capability. See [Setting Capabilities](https://capacitorjs.com/docs/v3/ios/configuration#setting-capabilities) for instructions on how to enable the capability.

After enabling the Push Notifications capability, add the following to your app's `AppDelegate.swift`:

```swift
func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
  NotificationCenter.default.post(name: .capacitorDidRegisterForRemoteNotifications, object: deviceToken)
}

func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
  NotificationCenter.default.post(name: .capacitorDidFailToRegisterForRemoteNotifications, object: error)
}
```

## Android

The Push Notification API uses [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging) SDK for handling notifications.  See [Set up a Firebase Cloud Messaging client app on Android](https://firebase.google.com/docs/cloud-messaging/android/client) and follow the instructions for creating a Firebase project and registering your application.  There is no need to add the Firebase SDK to your app or edit your app manifest - the Push Notifications provides that for you.  All that is required is your Firebase project's `google-services.json` file added to the module (app-level) directory of your app.

### Variables

This plugin will use the following project variables (defined in your app's `variables.gradle` file):
- `$firebaseMessagingVersion` version of `com.google.firebase:firebase-messagin` (default: `23.0.5`)
- `$azureNotificationHubsVersion` version of `me.leolin:ShortcutBadger` (default: `1.1.4`)
- `$androidVolleyVersion` version of `com.android.volley:volley` (default: `1.2.1`)
- `$kotlinVersion` version of `org.jetbrains.kotlin:kotlin-stdlib-jdk7:` (default: `1.7.10`)
- `$coreKtx` version of `androidx.core:core-ktx` (default: `1.8.0`)

## Configuration

No configuration required for this plugin.

## Usage

```typescript
import { Device } from '@capacitor/device';
import { PushNotifications } from '@capacitor/push-notifications';
import { AzureNotificationHubs } from '@jonz94/capacitor-azure-notification-hubs';

const addListeners = async () => {
  await AzureNotificationHubs.addListener('registration', token => {
    console.info('Registration token: ', token.value);
  });

  await AzureNotificationHubs.addListener('registrationError', err => {
    console.error('Registration error: ', err.error);
  });
}

const registerNotifications = async () => {
  let permissionStatus = await PushNotifications.checkPermissions();

  if (permissionStatus.receive === 'prompt') {
    permissionStatus = await PushNotifications.requestPermissions();
  }

  if (permissionStatus.receive !== 'granted') {
    throw new Error('User denied permissions!');
  }

  const { uuid } = await Device.getId();

  const myDeviceTag = `${uuid}-${Date.now()}`

  await AzureNotificationHubs.register({
    notificationHubName: 'azure-notification-hub-name',
    connectionString: 'my-connection-string',
    deviceTag: myDeviceTag,
  });
}
```

## API

<docgen-index>

* [`register(...)`](#register)
* [`addListener('registration', ...)`](#addlistenerregistration)
* [`addListener('registrationError', ...)`](#addlistenerregistrationerror)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### register(...)

```typescript
register(info: RegisterInfo) => Promise<void>
```

Register the app to receive push notifications.

This method will trigger the `'registration'` event with the push token or
`'registrationError'` if there was a problem. It does not prompt the user for
notification permissions, use `requestPermissions()` first.

| Param      | Type                                                  |
| ---------- | ----------------------------------------------------- |
| **`info`** | <code><a href="#registerinfo">RegisterInfo</a></code> |

**Since:** 1.0.0

--------------------


### addListener('registration', ...)

```typescript
addListener(eventName: 'registration', listenerFunc: (token: Token) => void) => Promise<PluginListenerHandle> & PluginListenerHandle
```

Called when the push notification registration finishes without problems.

Provides the push notification token.

| Param              | Type                                                        |
| ------------------ | ----------------------------------------------------------- |
| **`eventName`**    | <code>'registration'</code>                                 |
| **`listenerFunc`** | <code>(token: <a href="#token">Token</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt; & <a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

**Since:** 1.0.0

--------------------


### addListener('registrationError', ...)

```typescript
addListener(eventName: 'registrationError', listenerFunc: (error: RegistrationError) => void) => Promise<PluginListenerHandle> & PluginListenerHandle
```

Called when the push notification registration finished with problems.

Provides an error with the registration problem.

| Param              | Type                                                                                |
| ------------------ | ----------------------------------------------------------------------------------- |
| **`eventName`**    | <code>'registrationError'</code>                                                    |
| **`listenerFunc`** | <code>(error: <a href="#registrationerror">RegistrationError</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt; & <a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

**Since:** 1.0.0

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

Remove all native listeners for this plugin.

**Since:** 1.0.0

--------------------


### Interfaces


#### RegisterInfo

| Prop                      | Type                | Description                |
| ------------------------- | ------------------- | -------------------------- |
| **`notificationHubName`** | <code>string</code> | The notification hub name. |
| **`connectionString`**    | <code>string</code> | The connection string.     |
| **`deviceTag`**           | <code>string</code> | The device tag.            |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


#### Token

| Prop        | Type                | Description                                                              | Since |
| ----------- | ------------------- | ------------------------------------------------------------------------ | ----- |
| **`value`** | <code>string</code> | On iOS it contains the APNS token. On Android it contains the FCM token. | 1.0.0 |


#### RegistrationError

| Prop        | Type                | Description                                        | Since |
| ----------- | ------------------- | -------------------------------------------------- | ----- |
| **`error`** | <code>string</code> | Error message describing the registration failure. | 1.0.0 |

</docgen-api>
