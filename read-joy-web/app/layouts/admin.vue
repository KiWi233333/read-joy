
<script setup lang="ts">
import { useAdminDefaultStore } from "~/composables/sotre/useAdminDefaultStore";
import { useAdminStore } from "~/composables/sotre/useAdminStore";

const admin = useAdminStore();
const store = useAdminDefaultStore();
watch(() => admin.isLogin, (newVal) => {
  if (newVal) {
    store.init(); // 初始化
  }
}, { immediate: true });
</script>

<template>
  <div class="relative min-h-100dvh min-h-100vh flex">
    <AdminSystemMenu />
    <div flex flex-1 flex-col>
      <AdminSystemHeader class="sticky top-0 z-999 shrink-0" />
      <div class="flex flex-1 flex-col p-4 md:p-8 sm:p-6">
        <slot />
      </div>
    </div>
    <ClientOnly>
      <LazyFormPwdForm v-model="admin.showEditForm" @submit="admin.onTogglePwd" />
    </ClientOnly>
  </div>
</template>
