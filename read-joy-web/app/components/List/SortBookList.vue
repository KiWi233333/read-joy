<script lang="ts" setup>
import type { IPage } from "~/composables/api/types";
import { type BookDetailVO, getBookPageByDTOApi, type SelectBookPageDTO } from "~/composables/api/book";
import { ResultStatus } from "~/composables/api/types/result";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { useSettingStore } from "~/composables/sotre/useSettingStore";

const {
  showLoad = true,
  showMoreText = true,
  animated = false,
  limit = undefined,
  ssr = true,
  dto = undefined,
} = defineProps<{
  dto?: Partial<SelectBookPageDTO>
  autoStop?: boolean
  showLoad?: boolean
  showMoreText?: boolean
  ssr?: boolean
  animated?: boolean
  limit?: number
  title?: string
  bookClass?: string
  bookImgClass?: string
}>();

const setting = useSettingStore();
const store = useDefaultStore();
interface PageType extends Partial< IPage<BookDetailVO>> {
  page: number
}
const [autoAnimateRef, enable] = useAutoAnimate();
const isLoading = ref<boolean>(false);
const pageInfo = ref <PageType> ({
  size: limit || 20,
  page: 1,
  current: 1,
  total: undefined,
  pages: undefined,
  records: [] as BookDetailVO[],
});
const noMore = computed(() => !!(pageInfo.value.current && pageInfo.value.current >= (pageInfo.value?.pages || 0)));

/**
 * 加载图书列表
 */
async function loadData(dto?: Partial<SelectBookPageDTO>) {
  if (isLoading.value || pageInfo.value.page > 0 && pageInfo.value?.current && pageInfo.value?.current >= (pageInfo.value?.pages || 0))
    return;
  if (limit !== undefined && pageInfo.value.records && +pageInfo?.value?.records.length >= limit) {
    isLoading.value = false;
    return;
  }
  isLoading.value = true;
  const res = await getBookPageByDTOApi({
    page: pageInfo.value.page || 1,
    size: pageInfo.value.size || limit || 20,
    ...dto,
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
  pageInfo.value.page++;
  isLoading.value = false;
}

async function reload() {
  pageInfo.value = {
    page: 1,
    current: 0,
    size: limit || 20,
    total: 0,
    pages: 0,
    records: [],
  };
  await loadData(dto);
}

defineExpose({
  pageInfo,
  noMore,
  reload,
});
// 初始化
await reload();
onMounted(() => {
  enable(animated === false ? false : !setting.isCloseAllTransition);
});
onActivated(() => {
  enable(animated === false ? false : !setting.isCloseAllTransition);
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
    @load="loadData(dto)"
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
        <CardElImage
          class="book h-25 w-18 shadow card-default"
          :class="bookImgClass"
          :default-src="book.coverImageUrl"
        >
          <template #error>
            <small class="h-full w-full flex flex-row items-center justify-center">
              暂无图片
            </small>
          </template>
        </CardElImage>
        <div truncate px-4>
          <h4 truncate>
            {{ book.title }}
          </h4>
          <small class="truncate text-mini">
            {{ book.author }}
          </small>
          <div class="truncate text-mini">
            出版于：{{ book.publishionDate || "未填写" }}
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
