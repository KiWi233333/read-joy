
import { acceptHMRUpdate, defineStore } from "pinia";
import { type CategoryVO, getCategoryListByDTOApi } from "../api/category";
import { ResultStatus } from "../api/types/result";
// @unocss-include
// https://pinia.web3doc.top/ssr/nuxt.html#%E5%AE%89%E8%A3%85
export const useAdminDefaultStore = defineStore(
  "admin-default",
  () => {
    // 分类
    const categoryList = ref<CategoryVO[]>([]);
    const categoryMap = shallowRef<Record<number, CategoryVO>>({});

    /**
     * 初始化
     */
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
      categoryList,
      categoryMap,
      init,
    };
  },
  {
    // https://prazdevs.github.io/pinia-plugin-persistedstate/frameworks/nuxt-3.html
    persist: false,
  },
);
if (import.meta.hot)
  import.meta.hot.accept(acceptHMRUpdate(useAdminDefaultStore, import.meta.hot));
