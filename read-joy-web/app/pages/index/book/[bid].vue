<script setup lang="ts">
import { getBookDetailApi } from "~/composables/api/book";
import { ResultStatus } from "~/composables/api/types/result";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { appName } from "~/constants";

const user = useUserStore();
const store = useDefaultStore();
const setting = useSettingStore();
const route = useRoute();
// @ts-expect-error
const bookId = (route.params?.bid || undefined) as number;


async function loadData() {
  const res = await getBookDetailApi(bookId, user.token);
  if (res.code === ResultStatus.SUCCESS) {
    store.theBookDetail = res.data;
  }
  else {
    store.theBookDetail = {};
  }
  return true;
}

watch(() => user.isLogin, async (val) => {
  await loadData();
});
definePageMeta({
  // transition: {
  // name: "book-detail-page",
  // },
});
useHead({
  title: () => `${store.theBookDetail?.title} - ${appName}`,
  meta: [
    {
      name: "description",
      content: () => `${store.theBookDetail?.title} - ${appName}`,
    },
  ],
});
// 请求
await loadData();
</script>

<template>
  <div relative pb-10vh>
    <BookDetailView class="data-fades mt-10 sm:mt-6vh" :book-detial="store.theBookDetail" />
  </div>
</template>

<style lang="scss" scoped>
</style>
