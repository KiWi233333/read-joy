<script lang="ts" setup>
import { BaseUrlImg } from "~/composables/utils/useBaseUrl";

const props = defineProps<{
  src?: string
  defaultSrc?: string
  loadClass?: string
  loadRootClass?: string
  errorRootClass?: string
  errorClass?: string
  quality?: number
}>();
const url = computed(() => props.defaultSrc ? `${BaseUrlImg}${props.defaultSrc}` : (props.src || ""));
const status = ref<"loading" | "loaded" | "error">("loading");
const imgRef = ref<HTMLImageElement>();
onMounted(() => {
  if (imgRef.value) {
    imgRef.value.onload = () => {
      status.value = "loaded";
    };
    imgRef.value.onerror = () => {
      status.value = "error";
    };
  }
});
</script>

<template>
  <img
    v-if="url && status !== 'error'"
    v-bind="$attrs"
    ref="imgRef"
    :src="url"
    fit="cover"
    loading="lazy"
    class="obj-cover"
    hide-on-click-modal
  >
  <div v-else v-bind="$attrs">
    <slot name="error">
      <div v-if="status === 'error' || !url" class="h-full w-full flex-row-c-c" :class="errorRootClass">
        <i class="icon i-solar-gallery-remove-bold-duotone op-60" :class="errorClass" />
      </div>
    </slot>
  </div>
</template>

<style scoped lang="scss">
.icon {
  --at-apply: "block max-w-4/5 min-h-5 min-w-5"
}
</style>
