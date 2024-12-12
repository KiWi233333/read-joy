<script lang="ts" setup>
import Button from "~/components/ui/button/Button.vue";
import { BookSortOrder, BookSortType, type SelectBookPageDTO } from "~/composables/api/book";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { DATE_SELECTOR_OPTIONS } from "~/composables/utils/useUtils";

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
const store = useDefaultStore();
const dtoTemp = ref<SelectBookPageDTO>(JSON.parse(JSON.stringify(dto.value)));
const MAX_HISTORY = 6;
function onSearch() {
  const len = dto.value.keyword?.trim()?.length || 0;
  if (!len && Object.values(dto.value).filter(p => p).length === 0) {
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

const showFilter = useLocalStorage("searcj_show_filter", false);
// 排序
const sortGroupModel = computed({
  get() {
    if (dtoTemp.value.sortType === BookSortType.PRICE && dtoTemp.value.sortOrder !== undefined) {
      return "price";
    }
    else if (dtoTemp.value.sortType === BookSortType.PUBLISH_TIME && dtoTemp.value.sortOrder !== undefined) {
      return "new";
    }
    else {
      return undefined;
    }
  },
  set(val: "new" | "price" | undefined) {
    if (!val) {
      dto.value.sortType = undefined;
      dto.value.sortOrder = undefined;
    }
    else {
      dto.value.sortType = val === "price" ? BookSortType.PRICE : BookSortType.PUBLISH_TIME;
      dto.value.sortOrder = val !== sortGroupModel.value
        ? BookSortOrder.DESC // 切换选项后默认降序
        : (dto.value.sortOrder === BookSortOrder.DESC ? BookSortOrder.ASC : BookSortOrder.DESC);
    }
    onSearch();
  },
});
// @unocss-inlude
const sortOptions = [
  {
    label: "出版排序",
    value: "new",
    icon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    ascIcon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    descIcon: "i-solar:sort-from-top-to-bottom-bold-duotone",
  },
  {
    label: sortGroupModel.value === "price" ? (dtoTemp.value?.sortOrder === BookSortOrder.DESC ? "从高到低" : "从低到高") : "价格排序",
    value: "price",
    icon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    ascIcon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    descIcon: "i-solar:sort-from-top-to-bottom-bold-duotone",
  },
];
// 日期筛选
const dateGroupModel = computed({
  get() {
    if (dtoTemp.value.startDate && dtoTemp.value.endDate) {
      return [dtoTemp.value.startDate, dtoTemp.value.endDate];
    }
    else {
      return undefined;
    }
  },
  set(val: string[] | undefined) {
    if (!val) {
      dto.value.startDate = undefined;
      dto.value.endDate = undefined;
    }
    else {
      dto.value.startDate = val[0];
      dto.value.endDate = val[1];
    }
    onSearch();
  },
});

onMounted(() => {
  enable(!setting.isCloseAllTransition);
});
</script>

<template>
  <main class="py-0 layout-default">
    <header class="flex-row-c-c flex-col py-10 sm:(py-12)">
      <NuxtLink to="/" class="hidden sm:block">
        <NuxtImg src="logo-text-light.png" class="block h-10 select-none dark:hidden" />
        <NuxtImg src="logo-text-dark.png" class="hidden h-10 select-none dark:block" />
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
    <ListBookList
      :show-load="true"
      :show-more-text="true"
      :auto-stop="false"
      :debounce="300"
      :ssr="true"
      books-class="data-fades relative grid cols-2 w-full items-center gap-4 md:cols-5 sm:cols-4 md:gap-4"
      :dto="dtoTemp"
      book-class="mx-a"
      :animated="false"
    >
      <template #header="{ pageInfo }">
        <!-- 搜索区域 -->
        <div class="group flex-row-bt-c py-4 sm:(pb-8 pt-4)">
          <h2 class="text-lg font-bold">
            {{ dtoTemp?.categoryId ? store.categoryMap?.[dtoTemp?.categoryId]?.categoryName || '全部' : "全部" }}
            <span v-if="!!pageInfo.current" font-500 text-small>
              共{{ pageInfo?.total }}本图书
            </span>
          </h2>
          <div class="w-fit flex flex-wrap items-center op-50 transition-opacity group-hover:op-100">
            <ClientOnly>
              <div v-show="showFilter" data-fade flex flex-wrap items-center style="--anima: latter-slice-left;">
                <!-- 分类select -->
                <el-segmented
                  :model-value="sortGroupModel"
                  :options="sortOptions"
                  class="ml-2"
                  size="small"
                >
                  <template #default="{ item }">
                    <!-- @vue-expect-error -->
                    <div class="flex gap-2" @click="sortGroupModel = item.value">
                      <!-- @vue-expect-error -->
                      <i p-2 :class="sortGroupModel === item.value ? (dtoTemp.sortOrder === BookSortOrder.DESC ? item.descIcon : item.ascIcon) : item.icon" />
                      <!-- @vue-expect-error -->
                      <div>{{ item?.label || "" }}</div>
                    </div>
                  </template>
                </el-segmented>
                <el-date-picker
                  v-model="dateGroupModel"
                  type="daterange"
                  unlink-panels
                  class="ml-2"
                  style="width: 14rem;"
                  range-separator="至"
                  start-placeholder="起始"
                  end-placeholder="截至"
                  :shortcuts="DATE_SELECTOR_OPTIONS"
                  size="small"
                />
                <el-select
                  v-model="dto.categoryId"
                  size="small"
                  class="ml-2"
                  style="width: 9rem;box-shadow: none;border: none; border-radius: 10px;"
                  placeholder="分类"
                  filterable
                  @change="onSearch"
                >
                  <el-option v-for="item in store.categoryList" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId">
                    <span>{{ item.categoryName }}</span>
                  </el-option>
                </el-select>
              </div>
            </ClientOnly>
            <el-button size="small" class="ml-2" @click="showFilter = !showFilter">
              筛选
            </el-button>
          </div>
        </div>
      </template>
      <template #empty>
        <div data-fade class="flex flex-col items-center justify-center gap-2">
          <span class="text-color-secondary text-small">没有找到相关书籍</span>
        </div>
      </template>
    </ListBookList>
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

