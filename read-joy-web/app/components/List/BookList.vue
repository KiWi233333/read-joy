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
  debounce = 0,
  bookAttrs = undefined,
} = defineProps<{
  dto?: Partial<SelectBookPageDTO>
  autoStop?: boolean
  showLoad?: boolean
  showMoreText?: boolean
  ssr?: boolean
  animated?: boolean
  limit?: number
  debounce?: number
  bookAttrs?: string[]
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
  setTimeout(() => {
    isLoading.value = false;
  }, debounce);
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

const unWatch = watchDebounced(
  () => dto,
  () => {
    reload();
  },
  { debounce },
);

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
onDeactivated(() => {
  unWatch();
});

// 路由跳转
function resolveRouteDetail(bookId?: number) {
  if (bookId === undefined)
    return;
  if (setting.isNewTabOpenBook) {
    navigateTo(`/book/${bookId}`);
  }
  else {
    store.setBook(bookId);
  }
}
</script>

<template>
  <ListAutoIncre
    :immediate="!ssr"
    :auto-stop="limit !== undefined || autoStop"
    :no-more="noMore"
    :loading="isLoading"
    :loading-class="showLoad ? 'op-100' : 'op-0'"
    @load="loadData(dto)"
  >
    <div
      ref="autoAnimateRef" class="booklist relative grid cols-2 w-full items-start gap-4 md:cols-5 sm:cols-4 md:gap-10"
      :class="$attrs.booksClass"
      v-bind="$attrs"
    >
      <a
        v-for="book in pageInfo.records"
        :key="book.bookId"
        class="flex flex-col items-center justify-center leading-loose sm:w-36"
        :class="$attrs.bookClass"
        :href="`/book/${book.bookId}`"
        @click.prevent.stop="resolveRouteDetail(book?.bookId)"
      >
        <CardElImage
          class="book"
          :default-src="book.coverImageUrl"
        >
          <template #error>
            <small class="h-full w-full flex flex-row items-center justify-center">
              无图片
            </small>
          </template>
        </CardElImage>
        <label>
          {{ book.title }}
        </label>
        <small class="text-mini">
          {{ book.author }}
        </small>
      </a>
    </div>
    <template #done>
      <div
        v-if="showMoreText && !isLoading"
        class="transtion-400 mb-6 mt-4 w-full animate-[fade-in_0.3s_ease-out] text-center text-small"
      >
        暂无更多
      </div>
    </template>
  </ListAutoIncre>
</template>

<style lang="scss" scoped>
.book {
  --at-apply: "h-50 w-36 border-default card-default";
}
.booklist {
  grid-template-columns: repeat(auto-fill, minmax(min(30vw, 9rem), 1fr));
}
</style>
