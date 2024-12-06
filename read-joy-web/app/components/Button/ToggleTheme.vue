<script lang="ts" setup>
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { useModeToggle } from "~/composables/utils/useToggleThemeAnima";

const setting = useSettingStore();
// 切换动画
const colormode = useColorMode();
onMounted(() => {
  colormode.preference = setting.theme;
});
function toggleFn(e: MouseEvent) {
  const mode = colormode.value === "dark" ? "light" : "dark";
  useModeToggle(mode, e);
  setting.theme = mode;
}
</script>

<template>
  <button
    class="flex cursor-pointer gap-2 rounded-4 p-1 border-default card-default hover:shadow"
    @click="toggleFn"
  >
    <ClientOnly>
      <template #fallback>
        <div class="h-5 w-5" />
      </template>
      <img
        v-if="$colorMode.value === 'light'"
        class="h-5 w-5"
        src="/images/icon/sun.svg"
        alt="sun 日间模式"
      >
      <img
        v-else
        src="/images/icon/moon.svg"
        class="h-5 w-5 fill-light-5"
        alt="sun 日间模式"
      >
    </ClientOnly>
  </button>
</template>

<style lang="scss" scoped>
</style>
