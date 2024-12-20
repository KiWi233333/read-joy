
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
      <MenuAichatBar class="fixed bottom-100px right-40px z-998" />
      <el-backtop :bottom="40" :right="40" title="回到顶部" style="width: 3rem; height: 3rem; line-height: 3rem; text-align: center;" class="shadow border-default card-default">
        <i i-solar:alt-arrow-up-linear />
      </el-backtop>
    </ClientOnly>
    <!-- 页脚 -->
    <MenuFooter />
  </div>
</template>
