<script setup lang="ts">
definePageMeta({
  layout: "admin",
});
</script>

<template>
  <div>
    <NuxtPage
      :transition="{
        name: 'fade-lr',
        mode: 'out-in',
        duration: 300,
      }"
    />
  </div>
</template>
