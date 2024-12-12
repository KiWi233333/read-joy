<script setup lang="ts">
import type { SelectBookPageDTO } from "~/composables/api/book";
import { type CategoryVO, getCategoryListByDTOApi, type SelectCategoryListDTO } from "~/composables/api/category";
import { ResultStatus } from "~/composables/api/types/result";

const route = useRoute();
// 分类
const categoryList = ref<Partial<CategoryVO>[]>([]);
const isLoading = ref(false);

// 图书
// @ts-expect-error
const initCid = String(route?.params?.cid || "all") === "all" ? undefined : +route.params?.cid;
const pageDTO = reactive<SelectBookPageDTO>({
  page: 1,
  size: 20,
  categoryId: initCid,
  keyword: undefined,
  ...route.query,
});
/**
 * 加载分类列表
 */
async function loadCategoryData(dto?: SelectCategoryListDTO) {
  if (isLoading.value)
    return;
  isLoading.value = true;
  const res = await getCategoryListByDTOApi({
    ...dto,
  });
  if (res.code === ResultStatus.SUCCESS) {
    categoryList.value = [
      {
        categoryId: undefined,
        categoryName: "全部",
        bookCount: undefined,
      },
      ...(res.data || []),
    ];
  }
  isLoading.value = false;
}
const categoryText = computed(() => categoryList.value.find(c => c.categoryId === pageDTO.categoryId || initCid)?.categoryName || "全部");
await loadCategoryData();
</script>

<template>
  <div
    pt-4 sm:pt-10
    class="relative grid cols-[1fr_4fr] min-h-86vh w-full gap-4 grid-items-start sm:(cols-[1fr_5fr] gap-6)"
  >
    <el-scrollbar height="86vh" class="sticky left-0 top-0">
      <ul class="categoryList">
        <NuxtLink
          v-for="p in categoryList" :key="p.categoryId"
          class="item block px-2 py-1 text-sm"
          prefetch
          :class="String(pageDTO.categoryId) === String(p.categoryId) ? 'is-selected' : ''"
          :to="{
            path: `/category/${p.categoryId || 'all'}`,
            query: {
              ...pageDTO,
              ct: p.categoryName || '全部',
              page: undefined,
              size: undefined,
            },
          }"
          @click.prevent="pageDTO.categoryId = p.categoryId"
        >
          {{ p.categoryName }}{{ p.bookCount ? `(${p.bookCount})` : undefined }}
        </NuxtLink>
      </ul>
      <div class="scroll-btm absolute bottom-0 left-0 z-2 h-8 w-full text-center leading-8 btn-primary-text">
        <i class="i-solar:double-alt-arrow-down-line-duotone p-3" />
      </div>
    </el-scrollbar>
    <el-scrollbar height="86vh" class="relative">
      <NuxtPage :key="pageDTO.categoryId" :category-text="categoryText" />
    </el-scrollbar>
  </div>
</template>

<style lang="scss" scoped>
.categoryList {
  display: flex;
  flex-direction: column;
  .item {
    --at-apply: "w-full h-10 mb-2 leading-8 px-4 truncate card-default-br";
    &.is-selected {
      background-color: var(--el-color-primary);
      color: var(--el-color-white);
    }
  }
}
.scroll-btm {
  // 渐变色
  background-image: linear-gradient(180deg, rgba(244, 245, 247, 0) 0%, rgba(244, 245, 247, 1) 90%);
}
.dark .scroll-btm {
  // 渐变色
  background-image: linear-gradient(180deg, rgba(20, 20, 20, 0) 0%, rgba(20, 20, 20, 1) 90%);
}
:deep(.el-scrollbar) {
  .el-scrollbar__thumb {
    background-color: transparent;
  }
}
</style>
