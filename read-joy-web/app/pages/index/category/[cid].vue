<script setup lang="ts">
import type { SelectBookPageDTO } from "~/composables/api/book";

const route = useRoute();
// 图书
// @ts-expect-error
const initCid = String(route?.params?.cid || "all") === "all" ? undefined : +route.params?.cid;
const pageDTO = reactive<SelectBookPageDTO>({
  page: 1,
  size: 20,
  categoryId: initCid,
  keyword: undefined,
});
// 搜索
const inputRef = ref();
</script>

<template>
  <div>
    <ListBookList
      :show-load="true"
      :show-more-text="true"
      :auto-stop="false"
      :debounce="300"
      :ssr="true"
      animated="auto"
      :dto="pageDTO"
    >
      <template #header="{ pageInfo }">
        <div class="sticky left-0 top-0 z-2 mb-4 flex flex-col items-center justify-between gap-4 pb-2 sm:flex-row bg-color-layout">
          <h2 class="text-lg font-bold">
            {{ $route.query?.ct || '' }}
            <span v-if="!!pageInfo.current" font-500 text-small>
              共{{ pageInfo?.total }}本图书
            </span>
          </h2>
          <div class="group relative z-9 flex items-center" @click="inputRef?.focus">
            <ElInput
              ref="inputRef"
              v-model.trim="pageDTO.keyword"
              type="text"
              :prefix-icon="ElIconSearch"
              placeholder="搜索"
              class="search mx-1 hidden rounded-2rem sm:block border-default"
              @keyup.esc="() => {
                inputRef?.blur()
              }"
            />
            <el-button
              type="primary"
              class="btn absolute right-0 group-hover:(scale-100 opacity-100)"
              round
              style="font-size: 0.5em;height: 60%;margin: 0 0.8rem;box-shadow: none;"
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
$input-width: min(50vw, 16rem);
$scle-input-width: min(60vw, 22rem);
.search {
  :deep(.el-input__wrapper) {
    width: $input-width;
    transition: width $transition-delay;
    transition-timing-function: $animate-cubic-bount;
    border-radius: 20px;
    height: 2rem;
    border: 0;
    background-color: #fff;
    box-shadow: inset 0 0 2px 1px #f8f8f8, inset 0 0 2px 1px #f8f8f8;
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
      width: $scle-input-width;
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
