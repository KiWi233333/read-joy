
import { acceptHMRUpdate, defineStore } from "pinia";
// @unocss-include
// https://pinia.web3doc.top/ssr/nuxt.html#%E5%AE%89%E8%A3%85
export const useSettingStore = defineStore(
  "setting",
  () => {
    // 主题
    const isThemeChangeLoad = ref(false);
    const theme = ref("system");
    // 是否关闭所有动画
    const isCloseAllTransition = ref(false);
    // 是否通过新标签页打开书籍
    const isNewTabOpenBook = ref(false);
    const isMobileSize = ref(false);

    return {
      isMobileSize,
      isCloseAllTransition,
      isNewTabOpenBook,
      isThemeChangeLoad,
      theme,
    };
  },
  {
    // https://prazdevs.github.io/pinia-plugin-persistedstate/frameworks/nuxt-3.html
    persist: {
      storage: piniaPluginPersistedstate.cookies(),
    },
  },
);
if (import.meta.hot)
  import.meta.hot.accept(acceptHMRUpdate(useSettingStore, import.meta.hot));
