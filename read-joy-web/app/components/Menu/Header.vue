<script lang="ts" setup>
import { useUserStore } from "~/composables/sotre/useUserStore";

defineProps({
  search: { type: Boolean, default: true }, // 是否显示搜索框，默认显示
});
const user = useUserStore();
// 搜索
const isShowSearch = ref<boolean>(false);
const searchInputRef = ref();
const searchWord = ref<string>("");
function showSearch() {
  searchInputRef.value.inputRef?.focus?.();
}
</script>

<template>
  <header class="rounded-0 border-default-b bg-color dark:shadow-sm">
    <div class="flex-row-bt-c layout-default">
      <NuxtLink to="/" class="flex-row-c-c">
        <CardLogo />
      </NuxtLink>
      <ClientOnly>
        <!-- 搜索框 -->
        <transition name="fade" :duration="200">
          <div
            v-if="search"
            v-show="isShowSearch"
            class="fixed left-0 top-0 z-2 h-100vh w-full bg-[rgba(222,222,222,0.9)] backdrop-blur-10px dark:bg-[rgba(20,20,20,0.9)] sm:bg-[rgba(10,10,10,0.2)]"
            @click="isShowSearch = false"
          />
        </transition>
        <div
          v-if="search"
          class="z-2 translate-y-0 transition-300 transition-ease-in-out absolute-center"
          :class="{ 'translate-y-20vh scale-120': isShowSearch }"
        >
          <LazyInputSearch ref="searchInputRef" v-model="searchWord" @open="isShowSearch = true" @close="isShowSearch = false" />
          <h2
            class="scale-10 text-center text-lg text-light font-500 transition-300 absolute-center -translate-y-5em"
            :class="{ '-translate-y-5em scale-100 drop-shadow-2xl': isShowSearch }"
          >
            <NuxtImg src="logo-text-light.png" class="block h-30px select-none dark:hidden" />
            <NuxtImg src="logo-text-dark.png" class="hidden h-30px select-none dark:block" />
          </h2>
        </div>
        <div flex items-center gap-2 text-sm sm:gap-4>
          <PopperUserInfo v-if="user.isLogin">
            <CardElImage :default-src="user.userInfo.imgUrl" class="h-32px w-32px rounded-full rounded-full btn-primary-border border-default bg-color-br">
              <template #error>
                <div h-full w-full flex-row-c-c text-lg>
                  {{ user?.userInfo?.loginName?.[0] || "未设置" }}
                </div>
              </template>
            </CardElImage>
          </PopperUserInfo>
          <template v-else>
            <button
              btn-primary-text
              title="注册"
              class="border-r-2px pr-4 border-default-r"
              @click="user.showRegisterForm = true"
            >
              注册
            </button>
            <button btn-primary-text title="登录" @click="user.showLoginForm = true">
              登录
            </button>
          </template>
          <BtnTool @show-search="showSearch" />
        </div>
      </ClientOnly>
    </div>
    <ClientOnly>
      <LazyFormLoginDialog />
    </ClientOnly>
  </header>
</template>
