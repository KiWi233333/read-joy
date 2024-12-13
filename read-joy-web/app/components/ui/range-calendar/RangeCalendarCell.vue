<script lang="ts" setup>
import { cn } from '@/lib/utils'
import { RangeCalendarCell, type RangeCalendarCellProps, useForwardProps } from 'radix-vue'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<RangeCalendarCellProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <RangeCalendarCell
    :class="cn('tw-relative tw-h-9 tw-w-9 tw-p-0 tw-text-center tw-text-sm focus-within:tw-relative focus-within:tw-z-20 [&:has([data-selected])]:tw-bg-accent first:[&:has([data-selected])]:tw-rounded-l-md last:[&:has([data-selected])]:tw-rounded-r-md [&:has([data-selected][data-outside-view])]:tw-bg-accent/50 [&:has([data-selected][data-selection-end])]:tw-rounded-r-md [&:has([data-selected][data-selection-start])]:tw-rounded-l-md', props.class)"
    v-bind="forwardedProps"
  >
    <slot />
  </RangeCalendarCell>
</template>
