<script setup lang="ts">
import type { BookDetailVO } from "~/composables/api/book";
import {
  Drawer,
  DrawerContent,
} from "@/components/ui/drawer";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";

const { open, book = {} } = defineProps<{
  open: boolean
  book?: Partial<BookDetailVO>
}>();
const emit = defineEmits<{
  (e: "update:open", open: boolean): void
}>();
const store = useDefaultStore();
const show = computed({
  get() {
    return open;
  },
  set(open) {
    emit("update:open", open);
  },
});

function onNewTaget(book: Partial<BookDetailVO>) {
  if (book.bookId) {
    store.openBookDetail(book.bookId, true);
  }
}
</script>

<template>
  <Drawer v-model:open="show">
    <DrawerContent class="bg-color-layout">
      <section class="relative w-full">
        <menu class="bg-linear flex-row-bt-c py-4 layout-default">
          <div class="group ml-a">
            <i i-solar:circle-top-up-linear mr-4 p-2.4 op-60 transition-opacity btn-info title="新标签页打开" group-hover:op-100 @click="onNewTaget(book)" />
            <i i-solar:close-circle-outline p-3 op-60 transition-opacity btn-danger group-hover:op-100 title="关闭" @click="show = false" />
          </div>
        </menu>
        <el-scrollbar height="84vh" view-class="layout-default">
          <BookDetailView :book-detial="book" />
        </el-scrollbar>
      </section>
    </DrawerContent>
  </Drawer>
</template>

<style lang="scss" scoped>
.bg-linear {
  position: relative;
  // 渐变色从上到下
  ::after {
    content: "";
    background: #dadada;
  }
}
</style>
