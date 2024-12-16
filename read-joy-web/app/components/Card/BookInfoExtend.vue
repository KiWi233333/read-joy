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
    class="flex flex-col items-center justify-center sm:w-36"
    :href="`/book/${book.bookId}`"
    @click.prevent.stop="store.setBook(book.bookId)"
  >
    <div class="group book relative h-fit">
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
      <div class="absolute bottom-0 left-0 w-full truncate rounded-b-2 p-1 text-xs op-0 transition-opacity bg-color-br group-hover:(op-100)">
        {{ book?.publisher || "暂无出版社" }}
        {{ book?.publishionDate ? `(${book?.publishionDate})` : "" }}
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
