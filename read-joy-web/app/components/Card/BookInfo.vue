<script lang="ts" setup>
import type { BookVO } from "~/composables/api/book";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";

const {
  book,
} = defineProps<{
  book: BookVO
}>();
const store = useDefaultStore();
</script>

<template>
  <a
    :key="book.bookId"
    class="group flex flex-col items-center justify-center sm:w-36"
    :href="`/book/${book.bookId}`"
    v-bind="$attrs"
    @click.prevent.stop="store.setBook(book.bookId)"
  >
    <div class="book relative h-fit max-w-full">
      <CardNuxtImg
        :default-src="book.coverImageUrl"
        class="img"
      >
        <template #error>
          <small class="h-full w-full flex flex-row items-center justify-center">
            暂无图片
          </small>
        </template>
      </CardNuxtImg>
      <div class="absolute bottom-0 left-0 w-full truncate rounded-b-2 bg-(light op-50) p-1 text-right text-xs font-500 leading-1.6em op-0 backdrop-blur transition-opacity dark:bg-(dark op-20) group-hover:(op-100)">
        {{ book?.publisher || "暂无出版社" }}
        <div v-if="book?.publishionDate">{{ book?.publishionDate }}</div>
      </div>
    </div>
    <label :title="book.title" class="text-overflow-1 my-1 text-center leading-1.5em">
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
  --at-apply: "w-36 aspect-ratio-10/14 max-h-50 border-default card-rounded-df";
  .img {
    --at-apply: "h-full w-full card-rounded-df card-default";
  }
}
</style>
