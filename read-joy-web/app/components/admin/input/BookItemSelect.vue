<script setup lang="ts">
import type { IPage } from "~/composables/api/types";
import { type AdminBookVO, type AdminSelectBookPageDTO, useAdminBookPageByDTOApi } from "~/composables/api/admin/book";
import { ResultStatus } from "~/composables/api/types/result";
import { useAdminStore } from "~/composables/sotre/useAdminStore";


// Props
const {
  bookId,
  limit = 10,
  debounce,
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
const admin = useAdminStore();
const books = ref<AdminBookVO[]>([]);
const isLoading = ref(false);

const _bookId = computed({
  get() {
    return bookId;
  },
  set(val: number | undefined) {
    emit("update:bookId", val, val ? books.value.find(book => book.bookId === val) : undefined);
  },
});

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

const noMore = computed(() => !!(page.value && pageInfo.value.pages >= page.value));

async function fetchBooks(keyword?: string, loadMore: boolean = false) {
  if (isLoading.value)
    return;
  isLoading.value = true;
  page.value = loadMore && !noMore.value ? page.value + 1 : 1;
  const res = await useAdminBookPageByDTOApi({
    ...dto,
    keyword,
    size: limit,
    page: page.value,
  }, admin.token);

  if (res.code === ResultStatus.SUCCESS) {
    const records = res.data.records as AdminBookVO[];
    page.value = res.data.current;
    pageInfo.value = {
      current: res.data.current,
      size: res.data.size,
      total: res.data.total,
      pages: res.data.pages,
      records: [...(pageInfo.value.records || []), ...records],
    };
    dtoTemp.value = {
      ...dtoTemp.value,
      keyword,
    };
    books.value = pageInfo.value.records;
  }
  setTimeout(() => {
    isLoading.value = false;
  }, debounce);
}

function onSelectChange(value: number) {
  emit("update:bookId", value);
}

watch(() => bookId, (newVal) => {
  emit("update:bookId", newVal);
});
</script>

<template>
  <div>
    <el-select
      v-bind="$attrs"
      v-model="_bookId"
      filterable
      show-arrow
      remote
      reserve-keyword
      clearable
      placeholder="选择图书"
      :remote-method="fetchBooks"
      :loading="isLoading"
      @change="onSelectChange"
    >
      <slot name="prefix" />
      <slot v-for="book in books" name="option" :book="book">
        <el-option
          :key="book.bookId"
          :label="book.title"
          :value="book.bookId"
        />
      </slot>
      <p v-if="noMore" key="no-more" mt-2 text-center text-mini>
        没有更多了
      </p>
      <p v-else key="load-more" mt-2 text-center btn-info text-mini @click="fetchBooks(dtoTemp?.keyword, true)">
        加载更多
      </p>
    </el-select>
  </div>
</template>

<style lang="scss" scoped>
</style>
