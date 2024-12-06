<script lang="ts" setup>
import type { HtmlHTMLAttributes } from 'vue'
import { cn } from '@/lib/utils'

const props = defineProps<{
  class?: HtmlHTMLAttributes['class']
}>()
</script>

<template>
  <div :class="cn('tw-mt-auto tw-flex tw-flex-col tw-gap-2 tw-p-4', props.class)">
    <slot />
  </div>
</template>
