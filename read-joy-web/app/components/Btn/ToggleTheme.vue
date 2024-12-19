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
  <ElButton
    id="toggle-theme-btn"
    circle
    @click="toggleFn"
  >
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
  </ElButton>
</template>

<style lang="scss" scoped>
</style>
