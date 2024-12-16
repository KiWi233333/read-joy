
<script setup lang="ts">
const ignoreRoute: Record<string, boolean> = {
  // "index-book-bid": true,
};
</script>

<template>
  <el-scrollbar height="100vh" relative>
    <MenuHeader v-if="!ignoreRoute[$route.name]" />
    <div class="pt-4rem layout-default">
      <slot />
    </div>
    <ClientOnly>
      <el-backtop :bottom="40" class="border-default card-default-br">
        <i i-solar:alt-arrow-up-linear />
      </el-backtop>
    </ClientOnly>
    <!-- 页脚 -->
    <MenuFooter />
  </el-scrollbar>
</template>
