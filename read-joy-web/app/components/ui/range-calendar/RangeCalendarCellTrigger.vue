<script lang="ts" setup>
import { buttonVariants } from '@/components/ui/button'
import { cn } from '@/lib/utils'
import { RangeCalendarCellTrigger, type RangeCalendarCellTriggerProps, useForwardProps } from 'radix-vue'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<RangeCalendarCellTriggerProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <RangeCalendarCellTrigger
    :class="cn(
      buttonVariants({ variant: 'ghost' }),
      'tw-h-9 tw-w-9 tw-p-0 tw-font-normal data-[selected]:tw-opacity-100',
      '[&[data-today]:not([data-selected])]:tw-bg-accent [&[data-today]:not([data-selected])]:tw-text-accent-foreground',
      // Selection Start
      'data-[selection-start]:tw-bg-primary data-[selection-start]:tw-text-primary-foreground data-[selection-start]:hover:tw-bg-primary data-[selection-start]:hover:tw-text-primary-foreground data-[selection-start]:focus:tw-bg-primary data-[selection-start]:focus:tw-text-primary-foreground',
      // Selection End
      'data-[selection-end]:tw-bg-primary data-[selection-end]:tw-text-primary-foreground data-[selection-end]:hover:tw-bg-primary data-[selection-end]:hover:tw-text-primary-foreground data-[selection-end]:focus:tw-bg-primary data-[selection-end]:focus:tw-text-primary-foreground',
      // Outside months
      'data-[outside-view]:tw-text-muted-foreground data-[outside-view]:tw-opacity-50 [&[data-outside-view][data-selected]]:tw-bg-accent/50 [&[data-outside-view][data-selected]]:tw-text-muted-foreground [&[data-outside-view][data-selected]]:tw-opacity-30',
      // Disabled
      'data-[disabled]:tw-text-muted-foreground data-[disabled]:tw-opacity-50',
      // Unavailable
      'data-[unavailable]:tw-text-destructive-foreground data-[unavailable]:tw-line-through',
      props.class,
    )"
    v-bind="forwardedProps"
  >
    <slot />
  </RangeCalendarCellTrigger>
</template>
