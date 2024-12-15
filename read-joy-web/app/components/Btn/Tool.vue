<script lang="ts" setup>
const emit = defineEmits(["showSearch"]);
</script>

<template>
  <div class="block sm:hidden">
    <el-button :icon="ElIconSearch" circle @click="emit('showSearch')" />
  </div>
  <BtnToggleTheme />
</template>

<style lang="scss" scoped>
</style>
