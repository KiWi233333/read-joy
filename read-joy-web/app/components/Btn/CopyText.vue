<script lang="ts" setup>
const { text, pretext, icon } = defineProps<{
  text?: string | number | boolean
  pretext?: string
  icon?: string
}>();
</script>

<template>
  <span class="cursor-pointer transition-300 hover:text-[var(--el-color-info)]">
    <div inline>
      {{ pretext }}
    </div>
    <div
      v-copying.toast="text" ml-a
      inline
    >
      {{ text }}
    </div>
    <i
      v-if="icon"
      class="ml-1 p-0.5em opacity-60"
      :class="icon"
    />
  </span>
</template>

<style scoped lang="scss"></style>
