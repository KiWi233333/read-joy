<script lang="ts" setup>
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { appName } from "~/constants";

const user = useUserStore();
const setting = useSettingStore();

const [autoAnimateRef, enable] = useAutoAnimate({});

onMounted(() => {
  enable(!setting.isCloseAllTransition);
});
onUnmounted(() => {
  user.showLoginForm = false;
  user.showRegisterForm = false;
});
const showDialog = computed({
  get() {
    return user.showLoginForm || user.showRegisterForm;
  },
  set(val) {
    if (val === false) {
      user.showLoginForm = false;
      user.showRegisterForm = false;
    }
  },
});
</script>

<template>
  <el-dialog
    v-model="showDialog"
    style="width: fit-content;box-shadow: none;"
    class="border-default"
    :show-close="false"
    center
    destroy-on-close
    trap-focus
  >
    <div class="forms">
      <div key="login-bg" class="mt-4 flex-row-c-c gap-3 px-3 sm:(relative left-a top-a px-4)">
        <img
          alt="logo"
          src="/logo.png" class="h-8 w-8 object-cover"
        >
        <h3 class="text-lg font-bold tracking-0.2em">
          {{ appName }}
        </h3>
      </div>
      <!-- 表单 -->
      <div class="w-80vw px-3 text-1rem sm:(w-22rem px-4)">
        <div ref="autoAnimateRef" class="relative mt-6 sm:mt-8">
          <!-- 登录 -->
          <FormLoginForm
            v-if="user.showLoginForm"
            key="login-form"
          />
          <!-- 注册 -->
          <FormRegisterForm
            v-else-if="user.showRegisterForm"
            key="register-form"
          />
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<style lang="scss" scoped>
</style>
