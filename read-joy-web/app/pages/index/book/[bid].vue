<script setup lang="ts">
import { getBookDetailApi } from "~/composables/api/book";
import { ResultStatus } from "~/composables/api/types/result";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { appName } from "~/constants";

const user = useUserStore();
const store = useDefaultStore();
const route = useRoute();
// @ts-expect-error
const bookId = (route.params?.bid || undefined) as number;

// 请求
await loadData();

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
useHead({
  title: () => `${store.theBookDetail?.title} - ${appName}`,
  meta: [
    {
      name: "description",
      content: () => `${store.theBookDetail?.title} - ${appName}`,
    },
  ],
});
</script>

<template>
  <div relative>
    <BookDetailView class="data-fades mt-10 sm:mt-10vh" :book-detial="store.theBookDetail" />
  </div>
</template>

<style lang="scss" scoped>

</style>
