
<script setup lang="ts">
</script>

<template>
  <div class="main-scrollbar-warp relative">
    <MenuHeader />
    <div class="pt-4rem layout-default">
      <slot />
    </div>
    <ClientOnly>
      <MenuAichatBar class="fixed bottom-80px right-15px z-998" />
      <el-backtop :bottom="20" :right="15" title="回到顶部" style="width: 3rem; height: 3rem; line-height: 3rem; text-align: center;" class="shadow border-default card-default">
        <i i-solar:alt-arrow-up-linear />
      </el-backtop>
    </ClientOnly>
    <!-- 页脚 -->
    <MenuFooter />
  </div>
</template>
