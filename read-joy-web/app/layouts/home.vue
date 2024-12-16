
<script setup lang="ts">
const ignoreRoute: Record<string, boolean> = {
  // "index-book-bid": true,
};
</script>

<template>
  <el-scrollbar wrap-class="main-scrollbar-warp" height="100vh" relative>
    <MenuHeader v-if="!ignoreRoute[$route.name]" />
    <div class="pt-4rem layout-default">
      <slot />
    </div>
    <ClientOnly>
      <el-backtop target=".main-scrollbar-warp" :bottom="90" class="shadow-lg border-default card-default">
        <i i-solar:alt-arrow-up-linear />
      </el-backtop>
      <el-backtop target=".main-scrollbar-warp" :bottom="40" class="shadow-lg border-default card-default">
        <i i-solar:alt-arrow-up-linear />
      </el-backtop>
    </ClientOnly>
    <!-- 页脚 -->
    <MenuFooter />
  </el-scrollbar>
</template>
