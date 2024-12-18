<script lang="ts" setup>
import type { MenuItem } from "./Menu.vue";
import { ElMenuItem, ElSubMenu } from "element-plus";
import { BaseUrlImg } from "~/composables/utils/useBaseUrl";

const { data } = defineProps<{
  data: MenuItem
}>();
</script>

<template>
  <!-- 一级组件 -->
  <component
    :is="data?.children?.length ? ElSubMenu : ElMenuItem"
    :index="data.meta?.url || data?.meta?.icon"
  >
    <!-- 无儿子 -->
    <i
      v-if="data.meta.icon && !data.children?.length"
      :class="$route.path === data.meta.url ? data.meta?.onIcon : data.meta.icon"
      filter-drop-shadows p-0.6em
    />
    <el-image
      v-else-if="data.meta.image && !data.children?.length"
      :src="BaseUrlImg + data.meta.image"
      :alt="data.meta.title"
      fit="cover"
      style="border-radius: 6px; overflow: hidden"
    />
    <template #title>
      <div v-if="data.children?.length">
        <i
          v-if="data.meta.icon"
          :class="$route.path === data.meta.url ? data.meta?.onIcon : data.meta.icon"
          p-0.6em filter-drop-shadow
        />
        <el-image
          v-else-if="data.meta.image"
          :src="BaseUrlImg + data.meta.image"
          loading="lazy"
          :alt="data.meta.title"
          fit="cover"
          style="border-radius: 6px; overflow: hidden"
        />
      </div>
      <span class="min-w-6rem truncate text-center tracking-0.4em">{{ data.meta.title }}</span>
    </template>
    <MenuLine
      v-for="child in data?.children"
      :key="child.meta.url"
      :data="child"
    />
  </component>
</template>

<style scoped lang="scss">
 </style>
