
<script setup lang="ts">
// const scrollRef = ref();
// const nuxtApp = useNuxtApp();
// nuxtApp.hook("page:finish", () => {
//   import.meta.client && scrollRef.value.scrollTo(0, 0);
//   // window.scrollTo(0, 0);
// });
</script>

<template>
  <div class="main-scrollbar-warp relative">
    <!-- <el-scrollbar ref="scrollRef" wrap-class="main-scrollbar-warp relative" height="100vh"> -->
    <MenuHeader />
    <div class="pt-4rem layout-default">
      <slot />
    </div>
    <ClientOnly>
      <el-backtop :bottom="40" class="shadow-lg card-default border-default-hover">
        <i i-solar:alt-arrow-up-linear />
      </el-backtop>
    </ClientOnly>
    <!-- 页脚 -->
    <MenuFooter />
  </div>
</template>
