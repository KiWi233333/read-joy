<script lang="ts" setup>
import { appName } from "~/constants";

definePageMeta({
  layout: "default",
  title: `管理员登录 - ${appName}`,
});
</script>

<template>
  <div
    relative h-100vh w-full flex-row-c-c overflow-hidden
  >
    <!-- 背景 -->
    <div class="bg h-80vw w-80vw md:h-30vw md:w-30vw" />
    <!-- 内容 -->
    <div
      data-fade
      style="--lv: 2;--anima: latter-slice-blur-top;"
      class="relative z-1 grid w-9/10 lg:(cols-2 w-800px) md:(cols-2 w-700px) border-default card-default"
    >
      <section hidden md:block class="img-bg h-full w-full card-rounded-df rounded-r-0 shadow-lg shadow-inset border-default-r" />
      <section class="group relative flex-row-bt-c px-4 py-8 md:(px-8 py-24)">
        <!-- logo+nav -->
        <div class="absolute left-0 top-0 min-h-3em w-full flex-row-bt-c p-4 tracking-0.2em md:px-8">
          <div class="flex-row-c-c">
            <CardLogo />
          </div>
          <BtnToggleTheme class="opacity-0 group-hover:opacity-70" />
        </div>
        <!-- 表单 -->
        <AdminSystemLoginForm class="w-full pt-6" />
      </section>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.img-bg {
  background: url('/images/login-bg.png') no-repeat center center;
}
.bg {
  position: absolute;
  background: radial-gradient(circle at top, rgb(65, 138, 255), rgba(255, 255, 255, 0.9));
  z-index: 0;
  border-radius: 67% 33% 70% 30% / 30% 54% 46% 70%;
  animation: border-anima 20s ease-out infinite;
}

@keyframes border-anima {
  0% {
    transform: scale(1);
    border-radius: 67% 33% 70% 30% / 30% 54% 46% 70%;
  }
  25% {
    transform: scale(1.02);
    border-radius: 55% 45% 54% 46% / 42% 61% 39% 58%;
  }
  50% {
    transform: scale(0.98);
    border-radius: 70% 30% 71% 29% / 53% 70% 30% 47%;
  }
  75% {
    transform: scale(1.02);
    border-radius: 67% 33% 70% 30% / 30% 54% 46% 70%;
  }
  100% {
    transform: scale(1);
    border-radius: 67% 33% 70% 30% / 30% 54% 46% 70%;
  }
}
</style>
