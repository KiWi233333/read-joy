<script lang="ts" setup>
import type { DialogContentEmits, DialogContentProps } from 'radix-vue'
import type { HtmlHTMLAttributes } from 'vue'
import { cn } from '@/lib/utils'
import { useForwardPropsEmits } from 'radix-vue'
import { DrawerContent, DrawerPortal } from 'vaul-vue'
import DrawerOverlay from './DrawerOverlay.vue'

const props = defineProps<DialogContentProps & { class?: HtmlHTMLAttributes['class'] }>()
const emits = defineEmits<DialogContentEmits>()

const forwarded = useForwardPropsEmits(props, emits)
</script>

<template>
  <DrawerPortal>
    <DrawerOverlay />
    <DrawerContent
      v-bind="forwarded" :class="cn(
        'tw-fixed tw-inset-x-0 tw-bottom-0 tw-z-50 tw-mt-24 tw-flex tw-h-auto tw-flex-col tw-rounded-t-[10px] tw-border tw-bg-background',
        props.class,
      )"
    >
      <div class="tw-mx-auto tw-mt-4 tw-h-2 tw-w-[100px] tw-rounded-full tw-bg-muted" />
      <slot />
    </DrawerContent>
  </DrawerPortal>
</template>
