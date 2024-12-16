
import type { BookDetailVO } from "../api/book";
import { acceptHMRUpdate, defineStore } from "pinia";
import { getBookDetailApi } from "~/composables/api/book";
import { type CategoryVO, getCategoryListByDTOApi } from "../api/category";
import { ResultStatus } from "../api/types/result";
import { useSettingStore } from "./useSettingStore";
import { useUserStore } from "./useUserStore";
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
    // 分类
    const categoryList = ref<CategoryVO[]>([]);
    const categoryMap = shallowRef<Record<number, CategoryVO>>({});

    // 设置book
    async function setBook(bookId: number, callback = () => {
      if (useRoute().name !== "index-book-bid") {
        showBookDetail.value = true;
      }
    }) {
      theBookDetail.value.bookId = bookId;
      if (!bookId) {
        theBookDetail.value = {};
        return;
      }
      const user = useUserStore();
      const res = await getBookDetailApi(bookId, user.token);
      theBookDetail.value = res.data;
      callback && callback();
      return res.data;
    }

    // 打开图书详情
    async function openBookDetail(bookId: number, newTab = false) {
      if (!bookId)
        return;
      const setting = useSettingStore();
      if (newTab || setting.isNewTabOpenBook) {
        showBookDetail.value = false;
        navigateTo(`/book/${bookId}`);
      }
      else {
        await setBook(bookId);
      }
    }

    async function init() {
      // 分类
      const res = await getCategoryListByDTOApi({});
      if (res.code === ResultStatus.SUCCESS) {
        categoryList.value = res.data;
        categoryList.value.forEach((item) => {
          categoryMap.value[item.categoryId] = item;
        });
      }
      else {
        categoryList.value = [];
      }
    }

    return {
      showBookDetail,
      theBookDetail,
      categoryList,
      categoryMap,
      init,
      setBook,
      openBookDetail,
    };
  },
  {
    // https://prazdevs.github.io/pinia-plugin-persistedstate/frameworks/nuxt-3.html
    persist: false,
  },
);
if (import.meta.hot)
  import.meta.hot.accept(acceptHMRUpdate(useDefaultStore, import.meta.hot));
