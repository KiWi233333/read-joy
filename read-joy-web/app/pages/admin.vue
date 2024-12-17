<script setup lang="ts">
definePageMeta({
  layout: "admin",
});
</script>

<template>
  <div>
    <NuxtPage
      keepalive
      :transition="{
        name: 'page',
        duration: 200,
      }"
    />
  </div>
</template>
