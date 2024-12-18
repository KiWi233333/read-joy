<script lang="ts" setup generic="T extends {id: string | number }">
import type { MenuButtonType } from "~/composables/utils/useRoute";
import { useDateFormat } from "@vueuse/core";

// props
const {
  header = {
    title: "管理",
    updateTime: useDateFormat(new Date(), "YYYY-MM-DD HH:mm:ss").value,
  },
  menuClass = "w-full flex items-center",
} = defineProps<{
  header?: {
    title: string
    updateTime?: string
  }
  menuClass?: string
  menuRihght?: MenuButtonType[]
}>();
</script>

<template>
  <!-- 头部 -->
  <div grid="~ cols-1 gap-6" my-a>
    <slot name="header" :header="header" />
    <!-- 菜单 -->
    <el-scrollbar :view-class="menuClass">
      <slot name="menu" />
    </el-scrollbar>
    <!-- 表格 -->
    <slot name="default" />
    <!-- 底部 -->
    <slot name="footer" />
  </div>
</template>

<style scoped lang="scss">
  :deep(.el-scrollbar__bar) {
    display: none;
    width: 0;
    opacity: 0;
    .el-scrollbar__thumb{
      display: none;
      width: 0;
      opacity: 0;
    }
  }
</style>
