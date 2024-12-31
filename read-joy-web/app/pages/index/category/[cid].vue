<script setup lang="ts">
import { BookSortOrder, BookSortType, type SelectBookPageDTO } from "~/composables/api/book";
import { appName } from "~/constants";

const route = useRoute();
// 图书
// @ts-expect-error
const initCid = String(route?.params?.cid || "all") === "all" ? undefined : +route.params?.cid;
const pageDTO = ref<SelectBookPageDTO>({
  page: 1,
  size: 20,
  categoryId: initCid,
  keyword: undefined,
});
// 搜索
const inputRef = ref();
const tempDto = ref(JSON.parse(JSON.stringify(pageDTO.value)));
useHead({
  title: () => `分类 - ${appName}`,
  meta: [
    {
      name: "description",
      content: () => `分类 - ${appName}`,
    },
  ],
});
</script>

<template>
  <div class="w-full">
    <ListBookList
      :show-load="true"
      :show-more-text="true"
      :auto-stop="false"
      :debounce="300"
      :ssr="true"
      animated="auto"
      :dto="tempDto"
      book-class="max-w-full"
      books-class="relative grid cols-2 w-full items-start gap-2 md:cols-5 sm:cols-4 md:gap-10 pr-2px"
    >
      <template #header="{ pageInfo }">
        <div class="sticky left-0 top-4rem z-2 mb-4 max-w-full flex flex-col items-center justify-between gap-4 pb-4 pt-10 sm:flex-row bg-color-layout">
          <h2 class="flex items-end text-lg font-bold">
            <strong>{{ $route.query?.ct || '' }}</strong>
            <span class="ml-2 min-w-6em" py-1 font-500 text-small>
              共{{ pageInfo?.total }}本图书
            </span>
          </h2>
          <div class="group relative z-9 w-full flex items-center sm:w-fit" @click="inputRef?.focus">
            <ElInput
              ref="inputRef"
              v-model.trim="pageDTO.keyword"
              type="text"
              :prefix-icon="ElIconSearch"
              placeholder="搜索"
              class="search mx-1 rounded-2rem sm:block border-default"
              @keyup.esc="() => {
                inputRef?.blur()
              }"
              @keydown.prevent.enter="() => {
                tempDto = JSON.parse(JSON.stringify(pageDTO))
              }"
            />
            <el-button
              type="primary"
              class="btn absolute right-0 group-hover:(scale-100 opacity-100)"
              round
              style="font-size: 0.5em;height: 60%;margin: 0 0.8rem;box-shadow: none;"
              @click="() => {
                tempDto = JSON.parse(JSON.stringify(pageDTO))
              }"
            >
              搜索
            </el-button>
          </div>
        </div>
      </template>
    </ListBookList>
  </div>
</template>

<style lang="scss" scoped>
// $input-width: min(100%, 24rem);
// $scle-input-width: min(100%, 28rem);
.search {
  width: 100%;
  :deep(.el-input__wrapper) {
    --at-apply: "w-full sm:w-22rem";
    transition: width $transition-delay;
    transition-timing-function: $animate-cubic-bount;
    border-radius: 20px;
    height: 2.2rem;
    border: 0;
    background-color: #fff;
    box-shadow: none;
    input::placeholder {
      letter-spacing: 0.3em;
      transition: width $transition-delay;
      color: var(--el-text-color-placeholder);
    }
    input:focus::placeholder {
      color: var(--el-color-primary-light-5);
    }
    input:focus,
    input:valid,
    &.is-focus {
      --at-apply: "w-full sm:w-26rem";
    }
    .el-input__suffix {
      position: absolute;
      right: 3%;
    }
  }
}
.dark .search :deep(.el-input__wrapper) {
  background-color: rgb(15, 15, 15);
  box-shadow: inset 0 0 2px 1px rgb(15, 15, 15), inset 0 0 2px 1px rgb(15, 15, 15);
  input::placeholder {
    color: #ffffffb3;
  }
}
</style>
