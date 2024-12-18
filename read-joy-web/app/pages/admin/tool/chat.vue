<script lang="ts" setup>
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { useModeToggle } from "~/composables/utils/useToggleThemeAnima";

const setting = useSettingStore();
const iframeUrl = ref("");
const iframeRef = ref();
onMounted(() => {
  iframeUrl.value = "https://chat.jiwu.kiwi233.top";
  // 监听iframe消息
  window.addEventListener("message", (event) => {
    const { data } = event;
    if (data.type === "toggle-theme") {
      const { theme, isAnimated = false } = data.message || {};
      if (["dark", "light", "auto"].includes(theme))
        useModeToggle(theme);
    }
  }, false);

  // iframe发送消息
  onChangeTheme(setting.theme);
});
watch(() => setting.theme, (theme) => {
  // iframe发送消息
  onChangeTheme(theme);
});
function onChangeTheme(theme: string) {
  if (iframeRef.value && theme) {
    iframeRef.value?.contentWindow?.postMessage({
      type: "toggle-theme",
      message: {
        theme,
        isAnimated: false,
      },
    }, "*");
  }
}
</script>

<template>
  <div>
    <TableLayout
      :header="{ title: `聊天工具` }"
    >
      <template #default>
        <div class="flex-row-c-c">
          <iframe v-if="iframeUrl" ref="iframeRef" :src="iframeUrl" class="h-88vh w-90vw border-2 border-none sm:w-70vw card-default" />
        </div>
      </template>
    </TableLayout>
  </div>
</template>

<style scoped lang="scss">
:deep(.el-table ){
  .el-table__cell .cell {
    display:flex;
  }
}
</style>
