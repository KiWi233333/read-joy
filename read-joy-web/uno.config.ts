import {
  defineConfig,
  presetAttributify,
  presetIcons,
  presetTypography,
  presetUno,
  presetWebFonts,
  transformerDirectives,
  transformerVariantGroup,
} from "unocss";

export default defineConfig({
  shortcuts: [
    ["card-default", "bg-[#ffffff93] dark:(bg-dark-5) rounded-1.5"],
    ["card-default-br", "bg-[#ffffff93] dark:(bg-dark-5 bg-op-60) rounded-1.5 backdrop-blur-12px backdrop-saturate-180"],
    ["blur-card", "backdrop-blur-4 bg-[#75757528] rounded-4px"],
    // 颜色
    ["text-color", "text-dark dark:text-white"],
    ["bg-color", "bg-white dark:bg-dark"],
    ["bg-color-br", "bg-[#ffffff93] dark:(bg-dark-5 bg-op-60) backdrop-blur-12px"],
    ["bg-menu-color", "bg-white  dark:bg-dark-8"], // 控件菜单背景色
    ["bg-color-second", "bg-light-5 dark:bg-dark"],
    // 边框
    ["border-default", "border-1px border-solid border-gray-200 dark:border-dark-300"],
    ["border-default-r", "border-solid border-gray-200 border-0 border-r-1px  dark:border-dark-300"],
    ["border-default-l", "border-solid border-gray-200 border-0 border-l-1px  dark:border-dark-300"],
    ["border-default-t", "border-solid border-gray-200 border-0 border-t-1px  dark:border-dark-300"],
    ["border-default-b", "border-solid border-gray-200 border-0 border-b-1px  dark:border-dark-300"],
    ["border-default-dashed", "hover:shadow-sm border-2px  border-default border-dashed"],
    ["border-default-hover", "transition-200 hover:shadow hover:border-solid hover:border-[var(--el-color-primary)]  border-default-dashed"],
    // 布局
    ["flex-row-c-c", "flex flex-row flex-justify-center flex-items-center"],
    ["flex-row-bt-c", "flex flex-row flex-justify-between flex-items-center"],
    ["layout-default", "mx-a sm:px-2rem py-4 w-94vw md:w-1400px"],
    ["layout-default-md", "mx-a sm:px-2rem py-4 w-94vw md:w-1400px"],
    ["layout-default-xm", "mx-a sm:px-2rem py-4 w-94vw md:w-1200px"],
    ["layout-default-se", "mx-a sm:px-2rem py-4 w-94vw md:w-1000px"],
    ["absolute-center", "absolute left-1/2 -translate-x-1/2"],
    ["absolute-center-center", "absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2"],
    ["absolute-center-x", "absolute left-1/2 -translate-x-1/2"],
  ],
  presets: [
    presetUno(),
    presetAttributify(),
    presetIcons({
      scale: 1.2,
    }),
    presetTypography(),
    presetWebFonts(),
  ],
  transformers: [
    transformerDirectives(),
    transformerVariantGroup(),
  ],
});
