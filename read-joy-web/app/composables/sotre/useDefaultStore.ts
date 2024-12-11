
import type { BookDetailVO } from "../api/book";
import { acceptHMRUpdate, defineStore } from "pinia";
import { getBookDetailApi } from "~/composables/api/book";
import { useSettingStore } from "./useSettingStore";
// @unocss-include
// https://pinia.web3doc.top/ssr/nuxt.html#%E5%AE%89%E8%A3%85
export const useDefaultStore = defineStore(
  "default",
  () => {
    // 图书相关
    const theBookDetail = ref<Partial<BookDetailVO>>({
      bookId: undefined,
    });
    const showBookDetail = ref(false);

    // 设置book
    async function setBook(bookId: number, callback = () => {
      showBookDetail.value = true;
    }) {
      theBookDetail.value.bookId = bookId;
      if (!bookId) {
        theBookDetail.value = {};
        return;
      }
      const res = await getBookDetailApi(bookId);
      theBookDetail.value = res.data;
      callback && callback();
    }

    // 打开图书详情
    async function openBookDetail(bookId: number) {
      if (!bookId)
        return;
      const setting = useSettingStore();
      if (setting.isNewTabOpenBook) {
        await navigateTo(`/book/${bookId}`);
      }
      else {
        await setBook(bookId);
      }
    }

    return {
      showBookDetail,
      theBookDetail,
      setBook,
      openBookDetail,
    };
  },
  {
    // https://prazdevs.github.io/pinia-plugin-persistedstate/frameworks/nuxt-3.html
    persist: {
      storage: piniaPluginPersistedstate.localStorage(),
    },
  },
);
if (import.meta.hot)
  import.meta.hot.accept(acceptHMRUpdate(useDefaultStore, import.meta.hot));
