<script lang="ts" setup>
import type { DefineComponent, VNode } from "vue";
import type { IPage } from "~/composables/api/types";
import { CardBookInfo } from "#components";
import { type BookDetailVO, getBookPageByDTOApi, type SelectBookPageDTO } from "~/composables/api/book";
import { ResultStatus } from "~/composables/api/types/result";
import { useSettingStore } from "~/composables/sotre/useSettingStore";

const {
  showLoad = true,
  showMoreText = true,
  animated = "auto",
  limit = undefined,
  ssr = true,
  dto = undefined,
  debounce = 300,
  bookNode = CardBookInfo,
  booksClass = "relative grid cols-2 w-full items-start gap-4 md:cols-5 sm:cols-4 md:gap-10 cols-[repeat(auto-fill,_minmax(min(30vw,_10rem),_1fr))] ",
  bookClass,
} = defineProps<{
  dto?: Partial<SelectBookPageDTO>
  autoStop?: boolean
  showLoad?: boolean
  showMoreText?: boolean
  ssr?: boolean
  animated?: "auto" | boolean
  limit?: number
  booksClass?: string
  bookClass?: string
  debounce?: number
  bookNode?: VNode | DefineComponent | any
}>();

const setting = useSettingStore();
interface PageType extends Partial<IPage<BookDetailVO>> {
  page: number
}
const [autoAnimateRef, enable] = useAutoAnimate();
const isLoading = ref<boolean>(false);
const pageInfo = ref <PageType> ({
  page: 0,
  size: limit || 20,
  current: undefined,
  total: undefined,
  pages: undefined,
  records: [] as BookDetailVO[],
});
const noMore = computed(() => !!(pageInfo.value.current !== undefined && pageInfo.value.pages !== undefined && pageInfo.value.current >= pageInfo.value.pages));

/**
 * 加载图书列表
 */
async function loadData(appendDTO?: Partial<SelectBookPageDTO>) {
  if (isLoading.value || noMore.value)
    return;
  if (limit !== undefined && pageInfo.value.records && +pageInfo?.value?.records.length >= limit) {
    isLoading.value = false;
    return;
  }
  isLoading.value = true;
  pageInfo.value.page++;
  const res = await getBookPageByDTOApi({
    ...dto,
    size: limit || dto?.size || 20,
    page: pageInfo.value.page,
    ...(appendDTO || {}),
  });
  if (res.code === ResultStatus.SUCCESS) {
    pageInfo.value = {
      page: res.data.current,
      current: res.data.current,
      size: res.data.size,
      total: res.data.total,
      pages: res.data.pages,
      records: [...(pageInfo.value.records || []), ...res.data.records],
    };
  }
  setTimeout(() => {
    isLoading.value = false;
  }, debounce);
}

async function reload(appendDTO?: Partial<SelectBookPageDTO>) {
  pageInfo.value = {
    page: 0,
    current: undefined,
    size: limit || 20,
    total: undefined,
    pages: undefined,
    records: [],
  };
  await loadData(appendDTO);
}


const unWatch = watchDebounced(
  () => dto,
  () => {
    reload();
  },
  {
    debounce,
    deep: true,
  },
);


watch(() => animated, (val) => {
  if (val === "auto" || val) {
    enable(!setting.isCloseAllTransition);
  }
  else {
    enable(false);
  }
});
onMounted(() => {
  enable(animated === false ? false : !setting.isCloseAllTransition);
});
onActivated(() => {
  enable(animated === false ? false : !setting.isCloseAllTransition);
});
onDeactivated(() => {
  unWatch();
});


defineExpose({
  pageInfo,
  noMore,
  reload,
  loadData,
});

// 初始化
await reload();
</script>

<template>
  <slot name="header" :page-info="pageInfo" />
  <ListAutoIncre
    :immediate="!ssr"
    :loading-parent-class="limit !== undefined ? 'hidden' : undefined"
    :auto-stop="limit !== undefined || autoStop"
    :no-more="noMore"
    :offset="limit !== undefined ? 0 : 20"
    :loading="isLoading"
    :loading-class="showLoad ? 'op-100' : 'op-0'"
    @load="loadData()"
  >
    <div
      ref="autoAnimateRef"
      :class="booksClass"
      v-bind="$attrs"
    >
      <slot v-for="book in pageInfo.records" name="book" :book="book">
        <component
          :is="bookNode"
          :key="book.bookId" :book="book"
          :class="bookClass"
        />
      </slot>
    </div>
    <template #done>
      <div
        v-if="showMoreText && noMore && pageInfo.total !== 0"
        class="w-full animate-[fade-in_0.4s_ease-out] pb-6 pt-4 text-center text-small"
      >
        暂无更多
      </div>
    </template>
    <div v-if="pageInfo.total === 0">
      <slot name="empty">
        <div
          class="w-full animate-[fade-in_0.4s_ease-out] pb-6 pt-4 text-center text-small"
        >
          暂无数据
        </div>
      </slot>
    </div>
  </ListAutoIncre>
</template>

<style lang="scss" scoped>
</style>
