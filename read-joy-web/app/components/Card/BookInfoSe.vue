<script lang="ts" setup>
import type { BookVO } from "~/composables/api/book";
import { NuxtLink } from "#components";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";

const {
  book,
  targetSelf = false,
} = defineProps<{
  book: BookVO
  targetSelf?: boolean
}>();
const store = useDefaultStore();
</script>

<template>
  <component
    :is="targetSelf ? 'div' : NuxtLink"
    :key="book.bookId"
    class="group h-fit flex flex-col"
    :href="`/book/${book.bookId}`"
    @click.prevent.stop="store.setBook(book.bookId)"
  >
    <div class="book relative">
      <CardNuxtImg
        class="img"
        :default-src="book.coverImageUrl"
      >
        <template #error>
          <small class="h-full w-full flex flex-row items-center justify-center">
            暂无图片
          </small>
        </template>
      </CardNuxtImg>
      <div class="absolute bottom-0 left-0 w-full truncate rounded-b-2 bg-(light op-50) p-1 text-right text-xs font-500 op-0 backdrop-blur transition-opacity dark:bg-(dark op-20) group-hover:(op-100)">
        {{ book?.publisher || "暂无出版社" }}
        <div v-if="book?.publishionDate">
          {{ book?.publishionDate }}
        </div>
      </div>
    </div>
    <label class="text-overflow-3 my-1">
      {{ book.title }}
    </label>
    <span class="max-w-full truncate text-mini">
      {{ book.author }}
    </span>
    <div class="inline max-w-full truncate !text-danger text-small">
      <small>售价:</small><span>￥{{ book?.price === 0 ? '免费' : book?.price || '-' }}</span>
    </div>
  </component>
</template>

<style lang="scss" scoped>
.book {
  --at-apply: "border-default card-rounded-df";
  .img {
    aspect-ratio: 1/1.4;
    --at-apply: "max-h-44 h-full w-full  card-default card-rounded-df";
  }
}
</style>
