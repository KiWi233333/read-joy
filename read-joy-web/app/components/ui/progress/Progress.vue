<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  ProgressIndicator,
  ProgressRoot,
  type ProgressRootProps,
} from 'radix-vue'
import { computed, type HTMLAttributes } from 'vue'

const props = withDefaults(
  defineProps<ProgressRootProps & { class?: HTMLAttributes['class'] }>(),
  {
    modelValue: 0,
  },
)

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})
</script>

<template>
  <ProgressRoot
    v-bind="delegatedProps"
    :class="
      cn(
        'tw-relative tw-h-4 tw-w-full tw-overflow-hidden tw-rounded-full tw-bg-secondary',
        props.class,
      )
    "
  >
    <ProgressIndicator
      class="tw-h-full tw-w-full tw-flex-1 tw-bg-primary tw-transition-all"
      :style="`transform: translateX(-${100 - (props.modelValue ?? 0)}%);`"
    />
  </ProgressRoot>
</template>
