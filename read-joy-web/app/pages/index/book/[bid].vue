<script setup lang="ts">
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";

const route = useRoute();
// 图书
// @ts-expect-error
const bookId = String(route?.params?.bid || "");
const store = useDefaultStore();
</script>

<template>
  <div>{{ store.theBookDetail }}</div>
</template>

<style lang="scss" scoped>

</style>
