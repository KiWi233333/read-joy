<script lang="ts" setup>
import { BaseUrlImg } from "~/composables/utils/useBaseUrl";

const {
  defaultSrc,
  src,
  quality = 80,
} = defineProps<{
  src?: string
  defaultSrc?: string
  loadClass?: string
  loadRootClass?: string
  errorRootClass?: string
  errorClass?: string
  quality?: number
}>();
const img = useImage();
const url = computed(() => defaultSrc ? `${BaseUrlImg}${defaultSrc}` : (src || ""));
</script>

<template>
  <NuxtImg
    v-if="url"
    fit="cover"
    :src="url"
    alt="图片"
    :quality="quality"
    loading="lazy"
    class="object-cover"
    :placeholder="img(url, { f: 'png', blur: 2, q: 40 })"
    v-bind="$attrs"
  />
  <div
    v-else class="icon"
    v-bind="$attrs"
  >
    <slot name="error">
      <div bg-color />
    </slot>
  </div>
</template>

<style lang="scss">
</style>
