import type { ModuleOptions } from "@vite-pwa/nuxt";
import process from "node:process";
import { appDescription, appName } from "../constants/index";

const scope = "/";

export const pwa: ModuleOptions = {
  registerType: "autoUpdate",
  scope,
  base: scope,
  manifest: {
    id: scope,
    scope,
    name: appName,
    short_name: appName,
    description: appDescription,
    theme_color: "#ffffff",
    icons: [
      {
        src: "pwa-192x192.png",
        sizes: "192x192",
        type: "image/png",
      },
      {
        src: "maskable-icon.png",
        sizes: "128x128",
        type: "image/png",
      },
      {
        src: "pwa-512x512.png",
        sizes: "512x512",
        type: "image/png",
      },
    ],
  },
  workbox: {
    cacheId: "read-joy-web-cache",
    globPatterns: [],
    // navigateFallbackDenylist: [/^\/api\//],
    navigateFallback: null,
    cleanupOutdatedCaches: true,
    runtimeCaching: [
      { // 图片等资源缓存
        urlPattern: /\.(?:png|jpg|jpeg|svg|gif|webp)$/,
        handler: "CacheFirst",
        options: {
          cacheName: "images",
          expiration: {
            maxEntries: 200,
            maxAgeSeconds: 14 * 24 * 60 * 60, // 14 days
          },
        },
      },
      { // JS/css/html缓存
        urlPattern: /\.(?:js|css|html)$/,
        handler: "NetworkFirst",
        options: {
          cacheName: "main-resources",
          networkTimeoutSeconds: 10,
          expiration: {
            maxEntries: 30,
            maxAgeSeconds: 1 * 24 * 60 * 60, // 1 days
          },
        },
      },

    ],
  },
  registerWebManifestInRouteRules: true,
  writePlugin: true,
  devOptions: {
    enabled: process.env.VITE_PLUGIN_PWA === "true",
    navigateFallback: scope,
  },
};
