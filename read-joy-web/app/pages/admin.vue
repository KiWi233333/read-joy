<script setup lang="ts">
definePageMeta({
  layout: "admin",
});
</script>

<template>
  <div>
    <NuxtPage
      :transition="{
        name: 'page',
        duration: 200,
      }"
    />
  </div>
</template>
