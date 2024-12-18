<script lang="ts" setup>
const props = defineProps<{
  color: string
  percentage: number
  whidth?: number
  strokeWidth?: number
  title?: string
  count?: string | number
  fixCount?: string
  titleClass?: string
}>();
</script>

<template>
  <div
    flex items-center leading-1.8em
  >
    <el-progress
      class="cursor-pointer transition-200 hover:scale-110"
      type="circle"
      :width="whidth || 70"
      :striped="true"
      :striped-flow="true"
      :stroke-width="strokeWidth || 8"
      :indeterminate="true"
      :color="color || 'var(--el-color-primary)'"
      :percentage="percentage || 0"
    />

    <div class="w-full flex flex-col gap-1 truncate pl-3">
      <small
        font-500 tracking-0.2em text-small
        :class="titleClass"
      >
        {{ title }}
      </small>
      <!-- 数量 -->
      <strong text-2xl font-600>
        <slot name="count" :count="count">
          {{ count }}
        </slot>
        <small
          v-show="fixCount"
          font-500 text-small
        >{{ fixCount }}</small>
        <slot />
      </strong>
      <small
        font-400 tracking-0.2em text-mini
      >
        <slot name="tip" />
      </small>
    </div>
  </div>
</template>

<style scoped lang="scss">
:deep(.el-progress__text) {
  font-size: 1em !important;
}
</style>
