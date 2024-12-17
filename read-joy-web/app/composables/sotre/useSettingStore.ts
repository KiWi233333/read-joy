
import { acceptHMRUpdate, defineStore } from "pinia";
import { downloadFile } from "../utils/useFile";
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
    // 是否是移动尺寸
    const isMobileSize = ref(false);

    // 管理员相关
    const isMenuFold = ref(false);
    const isMenuCollapse = ref(false);


    function onDownloadFile(url: string, name: string, token: string, callback?: () => void) {
      downloadFile(url, name, {
        Authorization: token,
      }, callback);
    }

    return {
      isMobileSize,
      isCloseAllTransition,
      isNewTabOpenBook,
      isThemeChangeLoad,
      theme,

      // 管理员
      isMenuFold,
      isMenuCollapse,

      onDownloadFile,
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
