
<script setup lang="ts">
</script>

<template>
  <div class="main-scrollbar-warp relative">
    <MenuHeader />
    <div class="pt-4rem layout-default">
      <slot />
    </div>
    <ClientOnly>
      <el-backtop :bottom="80" :right="15" :visibility-height="0" style="width: 3rem; height: 3rem; line-height: 3rem; text-align: center;">
        <MenuAichatBar />
      </el-backtop>
      <el-backtop :bottom="20" :right="15" :visibility-height="100" title="回到顶部" style="width: 3rem; height: 3rem; line-height: 3rem; text-align: center;" class="shadow border-default bg-color-br">
        <i i-solar:alt-arrow-up-linear />
      </el-backtop>
    </ClientOnly>
    <!-- 页脚 -->
    <MenuFooter mt-a />
  </div>
</template>
