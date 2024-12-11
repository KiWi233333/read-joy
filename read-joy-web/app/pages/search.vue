<script lang="ts" setup>
import type { SelectBookPageDTO } from "~/composables/api/book";
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { useUserStore } from "~/composables/sotre/useUserStore";

const emit = defineEmits(["update:modelValue", "open", "close"]);
definePageMeta({
  layout: "default",
});
const setting = useSettingStore();
const user = useUserStore();
const route = useRoute();
// 搜索
const [autoAnimateRef, enable] = useAutoAnimate({});
const inputRef = ref();
const dto = ref<SelectBookPageDTO>({
  page: 1,
  size: 10,
  keyword: String(route.query.keyword || "") as string,
  categoryId: (Number(route.query.categoryId || 0) || undefined) as number,
  sortType: undefined,
  sortOrder: undefined,
  startDate: undefined,
  endDate: undefined,
});
const dtoTemp = ref<SelectBookPageDTO>(JSON.parse(JSON.stringify(dto.value)));
const MAX_HISTORY = 6;
function onSearch() {
  const len = dto.value.keyword?.trim()?.length || 0;
  if (!len) {
    return;
  }
  if (len > 50) {
    ElMessage.warning("搜索关键字不能超过50个字符");
    return;
  }
  // 搜索
  dtoTemp.value = JSON.parse(JSON.stringify(dto.value));
  // 添加搜索历史
  if (dto.value.keyword) {
    user.searchHistory = [dto.value.keyword, ...user.searchHistory.filter(item => item !== dto.value.keyword)].slice(0, MAX_HISTORY);
  }
}

// 点击搜索历史
function clickHistory(keyword: string) {
  dto.value.keyword = keyword;
  inputRef.value.focus();
  onSearch();
}

onMounted(() => {
  enable(!setting.isCloseAllTransition);
});
</script>

<template>
  <main class="py-0 layout-default">
    <header class="flex-row-c-c flex-col py-10 sm:(py-10)">
      <NuxtLink to="/" class="hidden sm:block">
        <NuxtImg src="logo-text-light.png" class="block h-9 select-none dark:hidden" />
        <NuxtImg src="logo-text-dark.png" class="hidden h-9 select-none dark:block" />
      </NuxtLink>
      <div class="relative my-8 flex-row-c-c">
        <ElInput
          ref="inputRef"
          v-model.trim="dto.keyword"
          type="search"
          size="large"
          :clearable="false"
          :prefix-icon="ElIconSearch"
          placeholder="搜索"
          class="search hidden sm:block"
          @keyup.enter="onSearch"
          @focus="$emit('open')"
          @keyup.esc="() => {
            inputRef?.blur()
            $emit('close')
          }"
        />
        <el-button
          v-show="dto.keyword !== ''"
          type="primary"
          class="btn absolute right-0 group-hover:(scale-100 opacity-100) md:(scale-0 opacity-0) border-default"
          :class="{ 'sm:(!scale-100 !opacity-100)': dto.keyword }"
          round
          size="large"
          style="height: 60%;margin: 0 0.6rem;box-shadow: none;"
          @click.prevent="onSearch()"
        >
          搜 索
        </el-button>
      </div>
      <ClientOnly>
        <template #fallback>
          <div class="flex flex-wrap gap-2 sm:gap-4">
            <div class="sky-loading max-w-10em min-w-4em cursor-pointer truncate border border-1px border-color-transparent rounded-full px-2 py-2.2 transition-border sm:max-w-14em hover:border-default card-default-br text-small" />
            <div class="sky-loading max-w-10em min-w-2em cursor-pointer truncate border border-1px border-color-transparent rounded-full px-2 py-2.2 transition-border sm:max-w-14em hover:border-default card-default-br text-small" />
            <div class="sky-loading max-w-10em min-w-10em cursor-pointer truncate border border-1px border-color-transparent rounded-full px-2 py-2.2 transition-border sm:max-w-14em hover:border-default card-default-br text-small" />
            <div class="sky-loading max-w-10em min-w-7em cursor-pointer truncate border border-1px border-color-transparent rounded-full px-2 py-2.2 transition-border sm:max-w-14em hover:border-default card-default-br text-small" />
          </div>
        </template>
        <div ref="autoAnimateRef" class="flex items-start gap-2 sm:gap-4">
          <div v-for="item in user.searchHistory" :key="item" :title="item" class="max-w-10em cursor-pointer truncate border border-1px border-color-transparent rounded-full px-2 transition-border sm:max-w-14em hover:border-default card-default-br text-small" @click="clickHistory(item)">
            {{ item }}
          </div>
        </div>
      </ClientOnly>
    </header>
    <div mt-6>
      <ListBookList
        class="recommendListDom"
        :show-load="true"
        :show-more-text="true"
        :auto-stop="false"
        :debounce="300"
        :ssr="true"
        :dto="dtoTemp"
        :animated="true"
      />
    </div>
  </main>
</template>


<style lang="scss" scoped>
$input-width: min(80vw, 48rem);
$scle-input-width: min(85vw, 56rem);
.search {
  :deep(.el-input__wrapper) {
    width: $input-width;
    transition-duration: $transition-delay;
    transition-timing-function: $animate-cubic-bount;
    transition-property: width, box-shadow;
    transition-timing-function: $animate-cubic-bount;
    border-radius: 3rem;
    font-weight: 500;
    border: 0;
    background-color: #ffffff;
    box-shadow: rgba(0, 0, 0, 0.02) 0px 1px 3px 0px, rgba(27, 31, 35, 0.15) 0px 0px 0px 1px;
    input::placeholder {
      letter-spacing: 0.3em;
      color: var(--el-text-color-placeholder);
    }
    input {
      padding-right: 20px !important;
    }
    input:focus::placeholder {
      color: var(--el-color-primary-light-5);
    }
    input:focus,
    input:valid,
    &.is-focus {
      width: $scle-input-width;
    }
    &.is-focus {
      box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
    }
    .el-input__suffix {
      position: absolute;
      right: 3%;
    }
  }
}
.dark .search :deep(.el-input__wrapper) {
  background-color: rgb(40, 40, 40);
  box-shadow: rgba(68, 68, 68, 0.02) 0px 1px 3px 0px, rgba(170, 170, 170, 0.15) 0px 0px 0px 1px;
  input::placeholder {
    color: #ffffffb3;
  }
  &.is-focus {
    box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
  }
}
</style>

