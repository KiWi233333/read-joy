<script setup lang="ts">
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { appDescription, appName } from "~/constants";
import { useUserStore } from "./composables/sotre/useUserStore";
import { useInit } from "./composables/utils/useInit";

const store = useDefaultStore();
const user = useUserStore();
useHead({
  title: appName,
  meta: [
    {
      name: "description",
      content: appDescription,
    },
  ],
});
const unMountedInit = useInit();
onBeforeUnmount(unMountedInit);

onMounted(() => {
  store.init();
});
</script>

<template>
  <VitePwaManifest />
  <NuxtLayout vaul-drawer-wrapper class="min-h-100vh w-full bg-color-layout">
    <NuxtPage
      keep-alive
      :transition="{
        name: 'page',
        duration: 200,
      }"
    />
  </NuxtLayout>
  <BookDetailDrawer v-model:open="store.showBookDetail" :book="store.theBookDetail" />
  <ClientOnly>
    <LazyFormPwdForm />
    <LazyFormLoginDialog />
  </ClientOnly>
</template>

<style>
.layout-enter-active,
.layout-leave-active {
  transition: opacity 0.3s;
}
.layout-enter-from,
.layout-leave-to {
  opacity: 0;
}
.page-enter-active,
.page-leave-active {
    transition: opacity 0.3s;
}
.page-enter-from,
.page-leave-to {
  opacity: 0;
}


[data-nuxt-img] {
  transform: scale(1);
  transition: transform 0.3s;
}
/* 图书详情 */
.book-detail-page-enter-active,
.book-detail-page-leave-active {
    transition: opacity 0.3s;
}
.book-detail-page-enter-from,
.book-detail-page-leave-to {
  opacity: 0;
  /* filter: blur(10px); */
  [data-nuxt-img] {
    transform: scale(2)
  }
}
</style>
