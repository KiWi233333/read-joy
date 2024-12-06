<script setup lang="ts">
</script>

<template>
  <main p="x4 y10" text="center dark:gray-200">
    <div text-4xl>
      <div i-carbon-warning inline-block />
    </div>
    <div>404 页面不存在！</div>
    <div>
      <button text-sm btn m="3 t8" @click="$router.back()">
        返回
      </button>
    </div>
  </main>
</template>
