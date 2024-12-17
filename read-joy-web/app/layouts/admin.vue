
<script setup lang="ts">
import { useAdminStore } from "~/composables/sotre/useAdminStore";

const admin = useAdminStore();
</script>

<template>
  <div class="relative h-100dvh h-100vh flex">
    <AdminSystemMenu />
    <div flex flex-1 flex-col>
      <AdminSystemHeader />
      <slot />
    </div>
    <ClientOnly>
      <LazyFormPwdForm v-model="admin.showEditForm" @submit="admin.onTogglePwd" />
    </ClientOnly>
  </div>
</template>
