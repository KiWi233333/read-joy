<script lang="ts" setup>
import { BaseUrlImg } from "~/composables/utils/useBaseUrl";

const { defaultSrc, src } = defineProps<{
  src?: string
  defaultSrc?: string
  loadClass?: string
  loadRootClass?: string
  errorRootClass?: string
  errorClass?: string
  quality?: number
}>();
const url = computed(() => defaultSrc ? `${BaseUrlImg}${defaultSrc}` : (src || ""));
</script>

<template>
  <ClientOnly>
    <el-image
      :src="url"
      loading="lazy"
      fit="cover"
      hide-on-click-modal
      v-bind="$attrs"
    >
      <template #placeholder>
        <slot name="placeholder">
          <div :class="loadClass !== undefined ? loadClass : 'sky-loading h-full w-full'" />
        </slot>
      </template>
      <template #error>
        <slot name="error">
          <div class="h-full w-full flex-row-c-c" :class="errorRootClass">
            <i class="icon i-solar-gallery-remove-bold-duotone op-60" :class="errorClass" />
          </div>
        </slot>
      </template>
    </el-image>
  </ClientOnly>
</template>

<style scoped lang="scss">
.icon {
  --at-apply: "block max-w-4/5 min-h-5 min-w-5"
}
</style>
