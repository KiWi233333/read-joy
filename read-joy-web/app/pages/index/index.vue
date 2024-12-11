<script setup lang="ts">
import { BookSortOrder, BookSortType, type SelectBookPageDTO } from "~/composables/api/book";

// 大家都在看
const recommendListRef = ref();
const recommendListDto = reactive<SelectBookPageDTO>({
  page: 1,
  size: 6,
});
function reloadRecommon() {
  if (!recommendListRef.value?.reload) {
    return;
  }
  const pageInfo = recommendListRef.value.pageInfo;
  if (pageInfo) {
    recommendListDto.page = pageInfo.pages > recommendListDto.page ? pageInfo.current + 1 : 1;
  }
  recommendListRef.value.reload?.();
}
const recommendListRefHeight = ref(0);
onMounted(() => {
  setTimeout(async () => {
    await nextTick();
    const dom = document.querySelector(".recommendListDom");
    if (dom) {
      recommendListRefHeight.value = dom.clientHeight;
    }
  }, 300);
});


interface BookListParams {
  headerTitle: string;
  showLoad: boolean;
  showMoreText?: boolean;
  ssr?: boolean;
  imgSrc?: string;
  dto: {
    page: number;
    size: number;
    sortType: BookSortType;
    sortOrder: BookSortOrder;
  };
  animated?: boolean;
  limit?: number;
}
const bookListCards: BookListParams[] = [
  {
    headerTitle: "总榜",
    showLoad: false,
    showMoreText: false,
    ssr: true,
    imgSrc: "/images/allbook_top.png",
    dto: {
      page: 1,
      size: 6,
      sortType: BookSortType.PUBLISH_TIME,
      sortOrder: BookSortOrder.DESC,
    },
    animated: false,
    limit: 6,
  },
  {
    headerTitle: "最新出版",
    showLoad: false,
    showMoreText: false,
    ssr: true,
    imgSrc: "/images/newbook_top.png",
    dto: {
      page: 1,
      size: 6,
      sortType: BookSortType.PUBLISH_TIME,
      sortOrder: BookSortOrder.DESC,
    },
    animated: false,
    limit: 6,
  },
];
</script>

<template>
  <div data-fades style="--lv: 0;">
    <!-- 大家都在看 -->
    <h2 class="title">
      大家都在看
      <BtnElButton
        transition-icon
        style="border:none;background: transparent;"
        icon-class="i-solar:refresh-outline"
        class="group text-small" @click="reloadRecommon"
      >
        换一批
      </BtnElButton>
    </h2>
    <ListBookList
      ref="recommendListRef"
      class="recommendListDom rounded"
      :style="{ minHeight: `${recommendListRefHeight}px` }"
      :show-load="false"
      :show-more-text="false"
      :ssr="true"
      :dto="recommendListDto"
      :animated="false"
      :limit="recommendListDto.size"
    />
    <!-- 榜单 -->
    <h2 class="title">
      榜单
    </h2>
    <div grid cols-1 gap-4 sm:cols-2 sm:gap-8>
      <div v-for="(item, index) in bookListCards" :key="index" class="p-4 shadow-sm card-default sm:(px-6 py-6 pb-4)">
        <ListSortBookList
          :ssr="true"
          :show-load="item.showLoad"
          :show-more-text="item.showMoreText"
          :dto="item.dto"
          :animated="item.animated"
          :limit="item.limit"
        >
          <template #header>
            <NuxtImg :src="item.imgSrc" class="mb-4 h-8" />
          </template>
          <template #footer>
            <NuxtLink
              v-if="item.dto"
              class="group flex items-center justify-end btn-primary-text text-small"
              :to="{
                path: '/search',
                query: { ...item.dto || {} },
              }"
            >
              查看更多
              <i i-solar:alt-arrow-right-outline inline-block h-4 w-4 class="transition-transform group-hover:(translate-x-1/3 text-color-[var(--color-primary)])" text-mini />
            </NuxtLink>
          </template>
        </ListSortBookList>
      </div>
    </div>
    <!-- 分类 -->
    <h2 class="title">
      分类
      <NuxtLink to="/category/all" class="float-right btn-primary-text text-small">
        更多
      </NuxtLink>
    </h2>
    <ListCategoryList />
  </div>
</template>

<style lang="scss" scoped>
.title {
  --at-apply: "mb-4 mt-6 text-size-lg font-600";
}
</style>
