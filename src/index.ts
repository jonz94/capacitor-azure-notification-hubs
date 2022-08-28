import { registerPlugin } from '@capacitor/core';

import type { AzureNotificationHubsPlugin } from './definitions';

const AzureNotificationHubs = registerPlugin<AzureNotificationHubsPlugin>('AzureNotificationHubs', {});

export * from './definitions';
export { AzureNotificationHubs };
