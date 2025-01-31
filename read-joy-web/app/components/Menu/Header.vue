<script lang="ts" setup>
import { useUserStore } from "~/composables/sotre/useUserStore";

const user = useUserStore();
// 搜索
const isShowSearch = ref<boolean>(false);
const searchInputRef = ref();
const searchWord = ref<string>("");
function showSearch() {
  isShowSearch.value = true;
  searchInputRef.value.inputRef?.focus?.();
}
</script>

<template>
  <div class="fixed top-0 z-999 h-16 w-full">
    <ClientOnly>
      <template #fallback>
        <div class="top-4 z-4 hidden h-2.1rem w-22rem translate-y-0 cursor-pointer truncate border border-1px border-color-transparent rounded-full transition-300 transition-border transition-ease-in-out absolute-center sm:block hover:border-default card-default-br text-small" />
      </template>
      <!-- 搜索框 -->
      <div
        class="absolute top-4 z-3 translate-y-0 animate-[fade-in_0.2s] transition-300 transition-ease-in-out absolute-center"
        :class="{ 'translate-y-20vh scale-120': isShowSearch }"
      >
        <LazyInputSearch ref="searchInputRef" v-model="searchWord" @open="isShowSearch = true" @close="isShowSearch = false" />
        <h2
          class="scale-0 truncate text-center text-lg text-light font-500 transition-300 absolute-center -translate-y-5em"
          :class="{ '-translate-y-5em scale-100 drop-shadow-2xl': isShowSearch }"
        >
          <img src="/logo-text-light.png" alt="logo" class="block h-30px select-none object-cover dark:hidden">
          <img src="/logo-text-dark.png" alt="logo" class="hidden h-30px select-none object-cover dark:block">
        </h2>
      </div>
      <transition name="fade" :duration="200">
        <div
          v-show="isShowSearch"
          key="search"
          class="fixed left-0 top-0 z-2 h-100vh w-full bg-[rgba(240,240,240,0.9)] backdrop-blur-12px dark:bg-[rgba(20,20,20,0.92)] sm-dark:bg-[rgba(10,10,10,0.4)]"
          @click="isShowSearch = false"
        />
      </transition>
    </ClientOnly>
    <menu class="h-full rounded-0 border-default-b bg-color-br dark:shadow-sm">
      <div class="h-full flex-row-bt-c layout-default">
        <NuxtLink to="/" class="flex-row-c-c">
          <CardLogo />
        </NuxtLink>
        <ClientOnly>
          <template #fallback>
            <div flex items-center gap-2 text-sm sm:gap-4>
              <div class="h-2.1rem w-2.1rem cursor-pointer truncate border border-1px border-color-transparent rounded-full px-2 py-2.2 transition-border sm:max-w-14em hover:border-default card-default-br text-small" />
              <div class="h-2.1rem w-2.1rem cursor-pointer truncate border border-1px border-color-transparent rounded-full px-2 py-2.2 transition-border sm:max-w-14em hover:border-default card-default-br text-small" />
              <div class="h-2.1rem w-2.1rem cursor-pointer truncate border border-1px border-color-transparent rounded-full px-2 py-2.2 transition-border sm:max-w-14em hover:border-default card-default-br text-small" />
            </div>
          </template>
          <ClientOnly>
            <div flex items-center gap-2 text-sm sm:gap-4>
              <LazyPopperUserInfo v-if="user.isLogin">
                <CardNuxtImg :default-src="user.userInfo.imgUrl" class="order-0 h-2rem w-2rem rounded-full rounded-full btn-primary-border border-default bg-color-br">
                  <template #error>
                    <div h-full w-full flex-row-c-c text-lg>
                      {{ user?.userInfo?.loginName?.[0] || " " }}
                    </div>
                  </template>
                </CardNuxtImg>
              </LazyPopperUserInfo>
              <el-tooltip content="GitHub开源仓库 - ReadJoy" placement="bottom">
                <a
                  href="https://github.com/KiWi233333/read-joy"
                  target="_blank"
                  class="h-2rem w-2rem flex-row-c-c rounded-full shadow-sm border-default card-default"
                  v-bind="$attrs"
                >
                  <svg height="24" width="24" aria-hidden="true" viewBox="0 0 24 24" version="1.1" data-view-component="true" class="fill-dark dark:fill-light">
                    <path d="M12.5.75C6.146.75 1 5.896 1 12.25c0 5.089 3.292 9.387 7.863 10.91.575.101.79-.244.79-.546 0-.273-.014-1.178-.014-2.142-2.889.532-3.636-.704-3.866-1.35-.13-.331-.69-1.352-1.18-1.625-.402-.216-.977-.748-.014-.762.906-.014 1.553.834 1.769 1.179 1.035 1.74 2.688 1.25 3.349.948.1-.747.402-1.25.733-1.538-2.559-.287-5.232-1.279-5.232-5.678 0-1.25.445-2.285 1.178-3.09-.115-.288-.517-1.467.115-3.048 0 0 .963-.302 3.163 1.179.92-.259 1.897-.388 2.875-.388.977 0 1.955.13 2.875.388 2.2-1.495 3.162-1.179 3.162-1.179.633 1.581.23 2.76.115 3.048.733.805 1.179 1.825 1.179 3.09 0 4.413-2.688 5.39-5.247 5.678.417.36.776 1.05.776 2.128 0 1.538-.014 2.774-.014 3.162 0 .302.216.662.79.547C20.709 21.637 24 17.324 24 12.25 24 5.896 18.854.75 12.5.75Z" />
                  </svg>
                </a>
              </el-tooltip>
              <el-tooltip content="Gitee开源仓库 - ReadJoy" placement="bottom">
                <a
                  href="https://gitee.com/KiWi233333/read-joy"
                  target="_blank"
                  class="h-2rem w-2rem flex-row-c-c rounded-full shadow-sm border-default card-default"
                  v-bind="$attrs"
                >
                  <svg height="24" width="24" t="1738348712309" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4168"><path d="M512 512m-494.933333 0a494.933333 494.933333 0 1 0 989.866666 0 494.933333 494.933333 0 1 0-989.866666 0Z" fill="#C71D23" p-id="4169" /><path d="M762.538667 457.045333h-281.088a24.4736 24.4736 0 0 0-24.439467 24.405334v61.098666c-0.034133 13.5168 10.922667 24.439467 24.405333 24.439467h171.1104c13.5168 0 24.439467 10.922667 24.439467 24.439467v12.219733a73.3184 73.3184 0 0 1-73.3184 73.3184h-232.209067a24.439467 24.439467 0 0 1-24.439466-24.439467v-232.174933a73.3184 73.3184 0 0 1 73.3184-73.3184h342.152533c13.482667 0 24.405333-10.922667 24.439467-24.439467l0.034133-61.098666a24.405333 24.405333 0 0 0-24.405333-24.439467H420.352a183.296 183.296 0 0 0-183.296 183.296V762.538667c0 13.482667 10.922667 24.439467 24.405333 24.439466h360.516267a164.9664 164.9664 0 0 0 165.000533-165.000533v-140.526933a24.439467 24.439467 0 0 0-24.439466-24.439467z" fill="#FFFFFF" p-id="4170" /></svg>
                </a>
              </el-tooltip>
              <div v-if="!user.isLogin" hidden sm:block>
                <button
                  btn-primary-text
                  title="注册"
                  class="mr-2 border-r-2px pr-2 shadow-sm sm:(mr-4 pr-4) border-default-r"
                  @click="user.showRegisterForm = true"
                >
                  注册
                </button>
                <button shadow-sm btn-primary-text title="登录" @click="user.showLoginForm = true">
                  登录
                </button>
              </div>
              <div v-if="!user.isLogin" class="block sm:hidden btn-primary-circle" @click="user.showLoginForm = true">
                <i class="i-solar:user-bold p-2" />
              </div>
              <BtnToggleTheme />
              <ClientOnly>
                <!-- pwa安装 -->
                <BtnPwaInstall />
              </ClientOnly>
              <div class="block sm:hidden">
                <el-button :icon="ElIconSearch" circle @click="showSearch" />
              </div>
            </div>
          </ClientOnly>
        </ClientOnly>
      </div>
    </menu>
  </div>
</template>

<style lang="scss" scoped>
</style>
