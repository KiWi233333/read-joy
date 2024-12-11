<script lang="ts" setup>
import { type CategoryVO, getCategoryListByDTOApi, type SelectCategoryListDTO } from "~/composables/api/category";
import { ResultStatus } from "~/composables/api/types/result";

defineProps<{
  dto?: SelectCategoryListDTO
}>();
const isLoading = ref<boolean>(false);
const categoryList = ref<CategoryVO[]>([]);

/**
 * 加载图书列表
 */
async function loadData(dto?: SelectCategoryListDTO) {
  if (isLoading.value)
    return;
  isLoading.value = true;
  const res = await getCategoryListByDTOApi({
    ...dto,
  });
  if (res.code === ResultStatus.SUCCESS) {
    categoryList.value = res.data;
  }
  isLoading.value = false;
}

async function reload() {
  await loadData();
}

defineExpose({
  isLoading,
  categoryList,
  reload,
});
const backgroundColors = [
  "#2f7efd",
  "#E74C3C",
  "#F1C40F",
  "#2ECC71",
  "#E67E22",
  "#95A5A6",
  "#1ABC9C",
  "#ae1824",
  "#F39C12",
  "#16A085",
  "#000000",
  "#27AE60",
  "#D35400",
  "#7F8C8D",
];
// 初始化
await loadData();
</script>

<template>
  <div class="grid cols-3 gap-4 sm:(cols-6 gap-4)">
    <NuxtLink
      v-for="(p, i) in categoryList"
      :key="p.categoryId"
      :to="`/category/${p.categoryId}`"
      :title="p.categoryName"
      class="group relative max-w-full truncate p-3 btn-primary-bg border-default card-default sm:(p-4 p-4 hover:scale-106)"
      :style="`--el-color-primary: ${backgroundColors[i % backgroundColors.length]}`"
    >
      {{ p.categoryName }}
      <small>
        {{ p.bookCount ? `约${p.bookCount}本` : undefined }}
      </small>
      <i i-solar:bookmark-bold class="absolute right-2 p-3 op-0 sm:right-3 group-hover:(animate-[zoom-in-top_0.2s_ease-in] op-100)" />
    </NuxtLink>
  </div>
</template>

<style lang="scss" scoped>
</style>
