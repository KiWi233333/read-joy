<script setup lang="ts">
import type { BookDetailVO } from "~/composables/api/book";
import {
  Drawer,
  DrawerContent,
} from "@/components/ui/drawer";

const { open } = defineProps<{
  book: BookDetailVO
  open: boolean
}>();

const emit = defineEmits<{
  (e: "update:open", open: boolean): void
}>();

const show = computed({
  get() {
    return open;
  },
  set(open) {
    emit("update:open", open);
  },
});
</script>

<template>
  <div>
    <Drawer v-model:open="show">
      <DrawerContent class="group">
        <section class="min-h-80vh w-full layout-default">
          <menu flex-row-bt-c py-4>
            <div />
            <h2 class="flex-1 truncate text-1.5rem font-600 absolute-center">
              {{ book?.title }}
            </h2>
            <i i-solar:close-circle-outline p-3 op-60 transition-opacity btn-danger group-hover:op-100 @click="show = false" />
          </menu>
          <div>
            图书详情
            <Button>在新标签打开</Button>
          </div>
        </section>
      </DrawerContent>
    </Drawer>
  </div>
</template>
