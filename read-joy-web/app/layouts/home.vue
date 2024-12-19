
<script setup lang="ts">
</script>

<template>
  <el-scrollbar wrap-class="main-scrollbar-warp relative" height="100vh">
    <MenuHeader />
    <div class="pt-4rem layout-default">
      <slot />
    </div>
    <ClientOnly>
      <el-backtop target=".main-scrollbar-warp" :bottom="40" class="shadow-lg border-default card-default">
        <i i-solar:alt-arrow-up-linear />
      </el-backtop>
    </ClientOnly>
    <!-- 页脚 -->
    <MenuFooter />
  </el-scrollbar>
</template>
