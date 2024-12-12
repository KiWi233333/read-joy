<script lang="ts" setup>
import type { BookVO } from "~/composables/api/book";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { useSettingStore } from "~/composables/sotre/useSettingStore";

const {
  book,
} = defineProps<{
  book: BookVO
}>();

// 路由跳转
function resolveRouteDetail(bookId?: number) {
  const setting = useSettingStore();
  const store = useDefaultStore();

  if (bookId === undefined)
    return;
  if (setting.isNewTabOpenBook) {
    navigateTo(`/book/${bookId}`);
  }
  else {
    store.setBook(bookId);
  }
}
</script>

<template>
  <a
    :key="book.bookId"
    class="flex flex-col items-center justify-center leading-loose sm:w-36"
    :href="`/book/${book.bookId}`"
    @click.prevent.stop="resolveRouteDetail(book?.bookId)"
  >
    <CardNuxtImg
      class="book"
      :default-src="book.coverImageUrl"
    >
      <template #error>
        <small class="h-full w-full flex flex-row items-center justify-center">
          无图片
        </small>
      </template>
    </CardNuxtImg>
    <label>
      {{ book.title }}
    </label>
    <small class="text-mini">
      {{ book.author }}
    </small>
  </a>
</template>

<style lang="scss" scoped>
.book {
  --at-apply: "h-50 w-36 card-default card-rounded-df border-default";
}
</style>
