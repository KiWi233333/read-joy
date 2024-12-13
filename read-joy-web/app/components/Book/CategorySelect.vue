<script setup lang="ts">
import { BookSortOrder, BookSortType } from "~/composables/api/book";

const { sortOrder, sortType } = defineProps<{
  sortOrder: BookSortOrder | undefined;
  sortType: BookSortType | undefined;
}>();

const emit = defineEmits<{
  (e: "update:sortOrder", value: BookSortOrder | undefined): void;
  (e: "update:sortType", value: BookSortType | undefined): void;
  (e: "submit", value: BookSortOrder | undefined): void;
}>();

const dto = computed({
  get() {
    return {
      sortType,
      sortOrder,
    };
  },
  set(value: CategorySelectDTO) {
    emit("update:sortOrder", value.sortOrder);
    emit("update:sortType", value.sortType);
  },
});


export interface CategorySelectDTO {
  sortType?: BookSortType;
  sortOrder?: BookSortOrder;
}
// 排序
const sortGroupModel = computed({
  get() {
    if (dto.value.sortType === BookSortType.PRICE && dto.value.sortOrder !== undefined) {
      return "price";
    }
    else if (dto.value.sortType === BookSortType.PUBLISH_TIME && dto.value.sortOrder !== undefined) {
      return "new";
    }
    else {
      return undefined;
    }
  },
  set(val: "new" | "price" | undefined) {
    if (!val) {
      dto.value.sortType = undefined;
      dto.value.sortOrder = undefined;
    }
    else {
      dto.value.sortType = val === "price" ? BookSortType.PRICE : BookSortType.PUBLISH_TIME;
      dto.value.sortOrder = val !== sortGroupModel.value
        ? BookSortOrder.DESC // 切换选项后默认降序
        : (dto.value.sortOrder === BookSortOrder.DESC ? BookSortOrder.ASC : BookSortOrder.DESC);
    }
    emit("submit", dto.value.sortOrder);
  },
});
// @unocss-inlude
const sortOptions = [
  {
    label: "出版排序",
    value: "new",
    icon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    ascIcon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    descIcon: "i-solar:sort-from-top-to-bottom-bold-duotone",
  },
  {
    label: sortGroupModel.value === "price" ? (dto.value?.sortOrder === BookSortOrder.DESC ? "从高到低" : "从低到高") : "价格排序",
    value: "price",
    icon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    ascIcon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    descIcon: "i-solar:sort-from-top-to-bottom-bold-duotone",
  },
];
</script>

<template>
  <el-segmented
    :model-value="sortGroupModel"
    :options="sortOptions"
    class="border-default card-default !bg-color"
    v-bind="$attrs"
  >
    <template #default="{ item }">
      <div class="flex gap-2" @click="sortGroupModel = item.value">
        <i p-2 :class="sortGroupModel === item.value ? (dto.sortOrder === BookSortOrder.DESC ? item.descIcon : item.ascIcon) : item.icon" />
        <div>{{ item.label }}</div>
      </div>
    </template>
  </el-segmented>
</template>
