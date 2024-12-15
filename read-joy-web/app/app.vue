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
      :transition="true"
    />
  </NuxtLayout>
  <BookDetailDrawer v-model:open="store.showBookDetail" :book="store.theBookDetail" />
  <ClientOnly>
    <LazyFormPwdForm />
    <LazyFormLoginDialog />
  </ClientOnly>
</template>

<style>
/* .layout-enter-active,
.layout-leave-active {
  transition: opacity,filter 0.1s;
}
.layout-enter-from,
.layout-leave-to {
  opacity: 0;
  filter: blur(1rem);
} */
.page-enter-active,
.page-leave-active {
  transition: opacity,filter 0.1s;
}
.page-enter-from,
.page-leave-to {
  opacity: 0;
  filter: blur(1rem);
}
</style>
