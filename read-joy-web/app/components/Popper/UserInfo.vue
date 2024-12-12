<script setup lang="ts">
import { useUserStore } from "~/composables/sotre/useUserStore";

const user = useUserStore();
const isVisible = ref(false);

defineExpose({
  isVisible,
});
</script>

<template>
  <el-popover
    placement="bottom"
    width="fit-content"
    trigger="click"
    :offset="10"
    transition="popper-fade"
    popper-style="padding: 0;border-radius: 8px;border:none; backgroud:transparent;shadow: none; z-index: 1005;"
  >
    <template #reference>
      <slot />
    </template>
    <div class="w-60vw flex flex-col overflow-hidden border rounded-8px sm:w-15rem">
      <div class="h-18 w-full bg-[var(--el-color-primary)]" />
      <CardNuxtImg :default-src="user.userInfo.imgUrl" class="mx-auto h-16 w-16 border-2px border-white rounded-full rounded-full transition-shadow -mt-8 dark:border-gray bg-color-br hover:shadow">
        <template #error>
          <div h-full w-full flex-row-c-c text-lg>
            {{ user?.userInfo?.loginName?.[0] || "未设置" }}
          </div>
        </template>
      </CardNuxtImg>
      <h4 mt-4 truncate text-center font-500>
        {{ user.userInfo.loginName }}
      </h4>
      <div class="mt-4 flex justify-between gap-2 p-2 border-default-t">
        <BtnElButton
          plain
          transition-icon
          icon-class="i-solar:logout-3-broken"
          class="w-full op-60" @click="user.exitLogin"
        >
          退出登录
        </BtnElButton>
        <BtnElButton
          transition-icon
          icon-class="i-solar:home-2-outline"
          type="primary" class="w-full shadow" @click="ElMessage.warning('功能还未上线')"
        >
          我的主页
        </BtnElButton>
      </div>
    </div>
  </el-popover>
</template>
