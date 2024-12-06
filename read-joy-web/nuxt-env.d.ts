/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_ENV: "development" | "production"
  readonly VITE_ENV: string
  readonly VITE_API_BASE_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
