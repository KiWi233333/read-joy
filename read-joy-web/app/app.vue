<script setup lang="ts">
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { appDescription, appName } from "~/constants";
import { useInit } from "./composables/utils/useInit";

const store = useDefaultStore();
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
</script>

<template>
  <VitePwaManifest />
  <NuxtLayout vaul-drawer-wrapper class="min-h-100vh w-full bg-color-layout">
    <NuxtPage :transition="true" />
  </NuxtLayout>
  <!-- 图书详情 -->
  <LazyBookDetailDrawer v-model:open="store.showBookDetail" :book="store.theBookDetail" />
</template>

<style>
.layout-enter-active,
.layout-leave-active {
  transition: all 0.4s;
}
.layout-enter-from,
.layout-leave-to {
  opacity: 0;
  filter: blur(1rem);
}
.page-enter-active,
.page-leave-active {
  transition: all 0.4s;
}
.page-enter-from,
.page-leave-to {
  opacity: 0;
  filter: blur(1rem);
}
</style>
