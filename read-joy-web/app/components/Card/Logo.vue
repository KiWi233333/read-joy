<script lang="ts" setup>
</script>

<template>
  <div class="hidden sm:block" v-bind="$attrs">
    <img alt="logo-text" src="/logo-text-light.png" class="block h-8 select-none object-cover dark:hidden">
    <img alt="logo-dark" src="/logo-text-dark.png" class="hidden h-8 select-none object-cover dark:block">
  </div>
  <div class="block sm:hidden">
    <img alt="logo-text" src="/logo.png" class="h-8 w-8 select-none object-cover">
  </div>
</template>

<style scoped lang="scss">
</style>
