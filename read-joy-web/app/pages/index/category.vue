<script setup lang="ts">
import type { ElScrollbar } from "#build/components";
import type { SelectBookPageDTO } from "~/composables/api/book";
import { type CategoryVO, getCategoryListByDTOApi, type SelectCategoryListDTO } from "~/composables/api/category";
import { ResultStatus } from "~/composables/api/types/result";
import { useSettingStore } from "~/composables/sotre/useSettingStore";

const route = useRoute();
const setting = useSettingStore();
// 分类
const categoryList = ref<Partial<CategoryVO>[]>([]);
const isLoading = ref(false);
const categoryRef = ref<InstanceType<typeof ElScrollbar>>();

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
function scrollBottom() {
  console.log(categoryRef.value);

  categoryRef.value?.scrollTo({
    top: categoryRef.value?.$el.scrollHeight,
    behavior: "smooth",
  });
}
await loadCategoryData();
</script>

<template>
  <div
    class="grid cols-[1fr_4fr] w-full gap-4 grid-items-start sm:(cols-[1fr_5fr] gap-6)"
    sm:pb-20vh
  >
    <div class="sticky left-0 top-4rem z-2 pt-10">
      <el-scrollbar ref="categoryRef" height="86vh" style="height: fit-content;">
        <ul class="categoryList pb-20">
          <NuxtLink
            v-for="p in categoryList" :key="p.categoryId"
            class="item"
            prefetch
            :title="p.categoryName"
            :class="String(pageDTO.categoryId) === String(p.categoryId) ? 'is-selected' : ''"
            :to="{
              path: `/category/${p.categoryId || 'all'}`,
              query: {
                ...pageDTO,
                ct: p.categoryName || '全部',
                categoryId: undefined,
                page: undefined,
                size: undefined,
              },
            }"
            @click.prevent="pageDTO.categoryId = p.categoryId"
          >
            {{ p.categoryName }}{{ p.bookCount ? `(${p.bookCount})` : undefined }}
          </NuxtLink>
        </ul>
        <div class="scroll-btm absolute bottom-0 left-0 z-2 h-8 w-full text-center leading-8 btn-primary-text" @click="scrollBottom">
          <i class="i-solar:double-alt-arrow-down-line-duotone p-3" />
        </div>
      </el-scrollbar>
    </div>
    <NuxtPage
      :key="pageDTO.categoryId" :category-text="categoryText"
      :transition="{
        name: 'page',
        duration: 200,
      }"
    />
  </div>
</template>

<style lang="scss" scoped>
.categoryList {
  display: flex;
  flex-direction: column;
  .item {
    --at-apply: " block text-sm px-2 w-full mb-2 leading-8 sm:(px-4 py-1) truncate card-default-br";
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
