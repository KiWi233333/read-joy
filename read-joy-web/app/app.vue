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

// 初始化
onMounted(() => {
  store.init();
});
</script>

<template>
  <VitePwaManifest />
  <main vaul-drawer-wrapper class="min-h-100vh w-full bg-color-layout">
    <NuxtLayout>
      <NuxtPage
        :transition="{
          name: 'page',
          duration: 200,
        }"
      />
    </NuxtLayout>
  </main>
  <BookDetailDrawer v-model:open="store.showBookDetail" :book="store.theBookDetail" />
  <ClientOnly>
    <LazyFormPwdForm v-model="user.showEditForm" @submit="user.onTogglePwd" />
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
  /* filter: blur(10px); */
}
.page-enter-active,
.page-leave-active {
  transition: opacity 0.3s;
}
.page-enter-from,
.page-leave-to {
  /* filter: blur(10px); */
  opacity: 0;
}

/* 图书详情 */
/* [data-nuxt-img] {
  transform: scale(1);
  transition: transform 0.2s;
} */
/* .book-detail-page-enter-active,
.book-detail-page-leave-active {
    transition: opacity 0.2s;
}
.book-detail-page-enter-from,
.book-detail-page-leave-to {
  opacity: 0;
} */
</style>
