<script setup lang="ts">
definePageMeta({
  layout: "home",
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
