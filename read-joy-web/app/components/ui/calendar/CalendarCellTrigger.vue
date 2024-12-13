<script lang="ts" setup>
import { buttonVariants } from '@/components/ui/button'
import { cn } from '@/lib/utils'
import { CalendarCellTrigger, type CalendarCellTriggerProps, useForwardProps } from 'radix-vue'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<CalendarCellTriggerProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <CalendarCellTrigger
    :class="cn(
      buttonVariants({ variant: 'ghost' }),
      'tw-h-9 tw-w-9 tw-p-0 tw-font-normal',
      '[&[data-today]:not([data-selected])]:tw-bg-accent [&[data-today]:not([data-selected])]:tw-text-accent-foreground',
      // Selected
      'data-[selected]:tw-bg-primary data-[selected]:tw-text-primary-foreground data-[selected]:tw-opacity-100 data-[selected]:hover:tw-bg-primary data-[selected]:hover:tw-text-primary-foreground data-[selected]:focus:tw-bg-primary data-[selected]:focus:tw-text-primary-foreground',
      // Disabled
      'data-[disabled]:tw-text-muted-foreground data-[disabled]:tw-opacity-50',
      // Unavailable
      'data-[unavailable]:tw-text-destructive-foreground data-[unavailable]:tw-line-through',
      // Outside months
      'data-[outside-view]:tw-text-muted-foreground data-[outside-view]:tw-opacity-50 [&[data-outside-view][data-selected]]:tw-bg-accent/50 [&[data-outside-view][data-selected]]:tw-text-muted-foreground [&[data-outside-view][data-selected]]:tw-opacity-30',
      props.class,
    )"
    v-bind="forwardedProps"
  >
    <slot />
  </CalendarCellTrigger>
</template>
