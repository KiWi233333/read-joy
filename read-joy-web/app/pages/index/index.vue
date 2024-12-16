<script setup lang="ts">
import { BookSortOrder, BookSortType, type SelectBookPageDTO } from "~/composables/api/book";
import { useSettingStore } from "~/composables/sotre/useSettingStore";

const setting = useSettingStore();
// 大家都在看
const recommendListRef = ref();
const isLodaing = ref(false);
const recommendListPage = ref<number>(0);
async function reloadRecommon() {
  if (!recommendListRef.value?.reload) {
    return;
  }
  if (isLodaing.value) {
    return;
  }
  isLodaing.value = true;
  const pageInfo = recommendListRef.value.pageInfo;
  if (pageInfo) {
    const page = pageInfo.pages > pageInfo.current ? pageInfo.current + 1 : 1;
    await recommendListRef.value?.reload({
      page,
    });
    recommendListPage.value = page;
    await nextTick();
    isLodaing.value = false;
  }
}

// 定义最小高度
// const recommendListRefHeight = ref(0);
// onMounted(() => {
//   setTimeout(() => {
//     computedRecommendListRefHeight();
//   }, 300);
// });
// async function computedRecommendListRefHeight() {
//   await nextTick();
//   const dom = document.querySelector("#home-recommend-list");
//   if (dom) {
//     recommendListRefHeight.value = dom.clientHeight;
//   }
// }
// watch(() => setting.isMobileSize, () => {
//   computedRecommendListRefHeight();
// });


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
  animated?: boolean | "auto";
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
      sortType: BookSortType.PRICE,
      sortOrder: BookSortOrder.DESC,
    },
    animated: "auto",
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
    animated: "auto",
    limit: 6,
  },
];
</script>

<template>
  <div class="relative py-4 pb-20vh">
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
    <div
      id="home-recommend-list"
      class="h-35rem overflow-hidden md:h-17rem"
    >
      <ListBookList
        ref="recommendListRef"
        :show-load="false"
        :show-more-text="false"
        :ssr="true"
        :list-props="{
          'data-fades': !!recommendListPage,
        }"
        :animated="false"
        :limit="setting.isMobileSize ? 4 : 5"
      />
    </div>
    <!-- 榜单 -->
    <h2 class="title">
      榜单
    </h2>
    <div grid cols-1 gap-8 lg:cols-2>
      <div v-for="(item, index) in bookListCards" :key="index" class="p-4 border-default card-default sm:(px-6 py-6 pb-4)">
        <ListSortBookList
          :ssr="true"
          :show-load="item.showLoad"
          :show-more-text="item.showMoreText"
          :dto="item.dto"
          :limit="item.limit"
        >
          <template #header>
            <img :alt="item.headerTitle" :src="item.imgSrc" class="mb-4 h-8 object-cover">
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
    <ListCategoryList class="gap-4 lg:gap-4 md:gap-6" />
  </div>
</template>

<style lang="scss" scoped>
.title {
  --at-apply: "mb-4 mt-8 text-size-lg font-600";
}
</style>
