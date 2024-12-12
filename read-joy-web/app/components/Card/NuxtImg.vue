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
    v-bind="$attrs"
    :src="url"
    :quality="quality"
    loading="lazy"
    :placeholder="img(url, { f: 'png', blur: 2, q: 40 })"
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
.icon {
  --at-apply: "bg-dark "
  &::after {
    content: "xxx";
    width: 100%;
    height: 100%;
    display: block;
  }
}
</style>
