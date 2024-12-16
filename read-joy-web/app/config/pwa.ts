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
    globPatterns: ["**/*.{js,css,html,txt,png,ico,svg}"],
    navigateFallbackDenylist: [/^\/api\//],
    navigateFallback: "/",
    cleanupOutdatedCaches: true,
    runtimeCaching: [
    ],
  },
  registerWebManifestInRouteRules: true,
  writePlugin: true,
  devOptions: {
    enabled: process.env.VITE_PLUGIN_PWA === "true",
    navigateFallback: scope,
  },
};
