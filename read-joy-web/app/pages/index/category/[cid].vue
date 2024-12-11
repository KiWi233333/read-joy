<script setup lang="ts">
const route = useRoute();
// @ts-expect-error
const categoryId = ref(String(route.params?.cid as string || "all"));
const options = [
  { label: "All", value: "all" },
  { label: "Category 1", value: "1" },
  { label: "Category 2", value: "2" },
  { label: "Category 3", value: "3" },
];
</script>

<template>
  <div>
    <NuxtLink to="/">
      {{ categoryId }}
    </NuxtLink>
    <div class="grid">
      <el-segmented v-model="categoryId" :options="options" />
    </div>
  </div>
</template>
