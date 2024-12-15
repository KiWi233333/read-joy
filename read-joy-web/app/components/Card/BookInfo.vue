<script lang="ts" setup>
import type { BookVO } from "~/composables/api/book";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { useSettingStore } from "~/composables/sotre/useSettingStore";

const {
  book,
} = defineProps<{
  book: BookVO
}>();
const setting = useSettingStore();
// 路由跳转
function resolveRouteDetail(bookId?: number) {
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
    class="group flex flex-col items-center justify-center sm:w-36"
    :href="`/book/${book.bookId}`"
    @click.prevent.stop="resolveRouteDetail(book?.bookId)"
  >
    <div class="book relative h-fit">
      <CardNuxtImg
        class="img"
        :default-src="book.coverImageUrl"
      >
        <template #error>
          <small class="h-full w-full flex flex-row items-center justify-center">
            无图片
          </small>
        </template>
      </CardNuxtImg>
      <div class="absolute bottom-0 left-0 w-full truncate rounded-b-2 bg-(light op-50) p-1 text-right text-xs font-500 leading-1.6em op-0 backdrop-blur transition-opacity dark:bg-(dark op-20) group-hover:(op-100)">
        {{ book?.publisher || "暂无出版社" }}
        <div v-if="book?.publishionDate">{{ book?.publishionDate }}</div>
      </div>
    </div>
    <label class="text-overflow-3 my-1 text-center leading-1.5em">
      {{ book.title }}
    </label>
    <span class="max-w-full truncate text-mini">
      {{ book.author }}
    </span>
    <div class="inline max-w-full truncate text-right !text-danger text-small">
      <small>售价:</small><span>￥{{ book?.price === 0 ? '免费' : book?.price || '-' }}</span>
    </div>
  </a>
</template>

<style lang="scss" scoped>
.book {
  --at-apply: "h-50 w-36 border-default card-rounded-df";
  .img {
    --at-apply: "h-full w-full  card-default card-rounded-df";
  }
}
</style>
