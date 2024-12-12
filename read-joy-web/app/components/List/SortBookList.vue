<script lang="ts" setup>
import type { IPage } from "~/composables/api/types";
import { type BookDetailVO, getBookPageByDTOApi, type SelectBookPageDTO } from "~/composables/api/book";
import { ResultStatus } from "~/composables/api/types/result";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { useSettingStore } from "~/composables/sotre/useSettingStore";

const {
  showLoad = true,
  showMoreText = true,
  animated = "auto",
  limit,
  ssr = true,
  dto,
  debounce = 0,
} = defineProps<{
  dto?: Partial<SelectBookPageDTO>
  autoStop?: boolean
  showLoad?: boolean
  showMoreText?: boolean
  ssr?: boolean
  title?: string
  bookClass?: string
  bookImgClass?: string
  animated?: "auto" | boolean
  limit?: number
  debounce?: number
}>();
const store = useDefaultStore();
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
const noMore = computed(() => !!(pageInfo.value.current !== undefined && pageInfo.value.pages !== undefined && pageInfo.value.current === pageInfo.value.pages));

/**
 * 加载图书列表
 */
async function loadData() {
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

async function reload() {
  pageInfo.value = {
    page: 0,
    current: undefined,
    size: limit || 20,
    total: undefined,
    pages: undefined,
    records: [],
  };
  await loadData();
}

defineExpose({
  pageInfo,
  noMore,
  reload,
});

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
// 初始化
await reload();
onMounted(() => {
  enable(animated === false ? false : animated === "auto" ? false : !setting.isCloseAllTransition);
});
onActivated(() => {
  enable(animated === false ? false : animated === "auto" ? false : !setting.isCloseAllTransition);
});
onDeactivated(() => {
  unWatch();
});

// 路由跳转
function resolveRouteDetail(bookId?: number) {
  if (bookId === undefined)
    return;
  store.openBookDetail(bookId);
}
</script>

<template>
  <ListAutoIncre
    :immediate="!ssr"
    :auto-stop="limit !== undefined || autoStop"
    :no-more="noMore"
    :loading-class="showLoad ? 'op-100' : 'op-0'"
    @load="loadData()"
  >
    <slot name="header">
      <h4 v-if="title" class="mb-4">
        {{ title }}
      </h4>
    </slot>
    <div
      ref="autoAnimateRef" class="booklist relative grid cols-2 gap-4"
      v-bind="$attrs"
    >
      <a
        v-for="book in pageInfo.records"
        :key="book.bookId"
        class="flex pb-4 border-default-b"
        :class="bookClass"
        :href="`/book/${book.bookId}`"
        @click.prevent.stop="resolveRouteDetail(book?.bookId)"
      >
        <CardNuxtImg
          class="book h-25 w-18 shrink-0 shadow card-default"
          :class="bookImgClass"
          :default-src="book.coverImageUrl"
        >
          <template #error>
            <small class="h-full w-full flex flex-row items-center justify-center">
              暂无图片
            </small>
          </template>
        </CardNuxtImg>
        <div flex flex-col px-4 leading-1.8em>
          <h4 class="max-w-6rem sm:max-w-9em" truncate>
            {{ book.title }}
            {{ book.title }}
          </h4>
          <p class="text-overflow-2 mt-1 max-w-8em text-mini">
            {{ book.author }}
          </p>
          <div class="mt-1 truncate text-mini">
            出版于:{{ book.publishionDate || "未填写" }}
          </div>
          <div class="!text-warning mt-a truncate text-small">
            <small>售价:</small><span>￥{{ book?.price === 0 ? '免费' : book?.price || '-' }}</span>
          </div>
        </div>
      </a>
    </div>
    <template #done>
      <div
        v-if="showMoreText"
        class="mb-6 mt-4 w-full text-center text-0.8rem opacity-60"
      >
        暂无更多
      </div>
    </template>
  </ListAutoIncre>
  <slot name="footer" />
</template>

<style lang="scss" scoped>
</style>
