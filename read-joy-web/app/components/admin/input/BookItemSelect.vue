<script setup lang="ts">
import type { IPage } from "~/composables/api/types";
import { type AdminBookVO, type AdminSelectBookPageDTO, useAdminBookPageByDTOApi } from "~/composables/api/admin/book";
import { ResultStatus } from "~/composables/api/types/result";
import { useAdminStore } from "~/composables/sotre/useAdminStore";

// Props
const {
  bookId,
  limit = 10,
  debounce = 300,
  dto,
} = defineProps<{
  bookId?: number;
  limit?: number;
  debounce?: number;
  dto?: Partial<AdminSelectBookPageDTO>;
}>();
// Emits
const emit = defineEmits<{
  (event: "update:bookId", value: number | undefined, book?: AdminBookVO | undefined): void;
}>();

const admin = useAdminStore(); // 确保 store 正确导入
const isLoading = ref(false);

// 参数
const page = ref<number>(0);
const dtoTemp = ref<Partial<AdminSelectBookPageDTO>>({
  keyword: undefined,
  categoryId: undefined,
  endDate: undefined,
  startDate: undefined,
  sortType: undefined,
  sortOrder: undefined,
});

// 数据
const pageInfo = ref<IPage<AdminBookVO>>({
  records: [],
  total: 0,
  pages: 0,
  size: 0,
  current: 0,
});

const _bookId = computed({
  get() {
    return bookId;
  },
  set(val: number | undefined) {
    emit("update:bookId", val, val ? pageInfo.value?.records?.find(book => book.bookId === val) : undefined);
  },
});

const noMore = computed(() => page.value > 0 && pageInfo.value.current === pageInfo.value.pages); // 修改逻辑

async function fetchBooks(keyword?: string, loadMore: boolean = false) {
  if (isLoading.value)
    return;
  isLoading.value = true;
  page.value = loadMore ? (noMore.value ? pageInfo.value.pages : page.value + 1) : 1;
  const res = await useAdminBookPageByDTOApi({
    ...dto,
    keyword,
    size: limit,
    page: page.value,
  }, admin.token);

  if (res.code === ResultStatus.SUCCESS) {
    const records = (res?.data?.records || []) as AdminBookVO[];
    page.value = res.data.current;
    pageInfo.value.records = noMore.value ? records : [...(pageInfo.value.records || []), ...records];
    pageInfo.value.current = res.data.current;
    pageInfo.value.size = res.data.size;
    pageInfo.value.total = res.data.total;
    pageInfo.value.pages = res.data.pages;
    dtoTemp.value = {
      ...dtoTemp.value,
      keyword,
    };
  }
  setTimeout(() => {
    isLoading.value = false;
  }, debounce || 300); // 确保 debounce 有默认值
}
</script>

<template>
  <el-select
    v-bind="$attrs"
    v-model="_bookId"
    filterable
    show-arrow
    remote
    reserve-keyword
    :loading="false"
    clearable
    placeholder="选择图书"
    fit-input-width
    :remote-method="fetchBooks"
  >
    <slot name="prefix" />
    <slot v-for="book in pageInfo.records" name="option" :book="book">
      <el-option
        :key="book.bookId"
        :label="book.title"
        :value="book.bookId"
      />
    </slot>
    <p v-if="noMore" key="no-more" class="mt-2 text-center text-mini">
      没有更多了
    </p>
    <p v-else key="load-more" class="mt-2 text-center btn-info text-mini" @click="fetchBooks(dtoTemp?.keyword, true)">
      加载更多
    </p>
  </el-select>
</template>

<style lang="scss" scoped>
</style>
