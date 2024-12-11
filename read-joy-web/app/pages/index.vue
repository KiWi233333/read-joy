<script setup lang="ts">
definePageMeta({
  layout: "home",
});
</script>

<template>
  <div>
    <NuxtPage
      keeplive
      :transition="{
        name: 'fade',
        mode: 'out-in',
      }"
    />
  </div>
</template>
