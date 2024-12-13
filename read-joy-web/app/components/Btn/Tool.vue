<script lang="ts" setup>
const emit = defineEmits(["showSearch"]);
</script>

<template>
  <BtnToggleTheme />
  <div class="block sm:hidden">
    <el-button :icon="ElIconSearch" circle @click="emit('showSearch')" />
  </div>
</template>

<style lang="scss" scoped>
</style>
