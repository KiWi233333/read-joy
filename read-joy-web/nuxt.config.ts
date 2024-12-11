import { pwa } from "./app/config/pwa";
import { appDescription } from "./app/constants/index";

// 配置
export default defineNuxtConfig({
  modules: [
    "@vueuse/nuxt",
    "@unocss/nuxt",
    "@pinia/nuxt",
    "@nuxtjs/color-mode",
    "@vite-pwa/nuxt",
    "@nuxt/eslint",
    "@pinia/nuxt",
    "pinia-plugin-persistedstate/nuxt",
    "shadcn-nuxt",
    "@nuxtjs/tailwindcss",
    "@nuxt/image",
    "@element-plus/nuxt",
    "@formkit/auto-animate",
  ],
  ssr: true,
  devtools: {
    enabled: false,
  },
  app: {
    head: {
      viewport: "width=device-width,initial-scale=1",
      link: [
        { rel: "icon", href: "/favicon.ico", sizes: "any" },
        { rel: "apple-touch-icon", href: "/apple-touch-icon.png" },
      ],
      meta: [
        { name: "viewport", content: "width=device-width, initial-scale=1" },
        { name: "description", content: appDescription },
        { name: "apple-mobile-web-app-status-bar-style", content: "black-translucent" },
        { name: "theme-color", media: "(prefers-color-scheme: light)", content: "white" },
        { name: "theme-color", media: "(prefers-color-scheme: dark)", content: "#222222" },
      ],
    },
  },
  css: [
    "@unocss/reset/tailwind.css",
    "./app/assets/style/ui.css",
    "./app/assets/style/init.scss",
    "./app/assets/style/animate.scss",
  ],
  colorMode: {
    classSuffix: "",
  },
  routeRules: {
    "/": { prerender: true },
  },
  future: {
    compatibilityVersion: 4,
  },

  // 实验性
  experimental: {
    // when using generate, payload js assets included in sw precache manifest
    // but missing on offline, disabling extraction it until fixed
    payloadExtraction: false,
    renderJsonPayloads: true,
    typedPages: true,
    viewTransition: true,
    crossOriginPrefetch: true, // 使用 Speculation Rules API 启用跨源预取。
  },
  compatibilityDate: "2024-08-14",
  // nitro服务器
  nitro: {
    esbuild: {
      options: {
        target: "esnext",
      },
    },
    prerender: {
      crawlLinks: false,
      routes: ["/"],
    },
  },
  vite: {
    envPrefix: ["VITE_", "TAURI_"],

    css: {
      preprocessorOptions: {
        scss: {
          additionalData: `
          @use "@/assets/style/element/index.scss" as element;
          @use "@/assets/style/element/dark.scss" as dark;
          @use "@/assets/style/var.scss" as *;
          `,
        },
      },
    },
  },
  elementPlus: {
    icon: "ElIcon",
    importStyle: "scss",
    themes: ["dark"],
    defaultLocale: "zh-cn",
  },
  // eslint
  eslint: {
    config: {
      standalone: false,
      nuxt: {
        sortConfigKeys: true,
      },
    },
  },
  pwa,
  shadcn: {
    componentDir: "./app/components/ui",
  },
});
