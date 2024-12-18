<script lang="ts" setup>
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { rawRoutes } from "~/composables/utils/useRoute";


const route = useRoute();
const setting = useSettingStore();
const activeMenu = computed({
  get() {
    return route.path;
  },
  set(val: string) {
    navigateTo(val);
  },
});
</script>

<template>
  <menu
    transition="all 300 cubic-bezier(0.61, 0.225, 0.195, 1.3)"
    class="fixed left-0 top-0 z-1000 sm:sticky" :class="setting.isMenuCollapse ? 'max-w-0 truncate' : 'max-w-12rem' "
  >
    <ClientOnly>
      <div
        v-show="!setting.isMenuCollapse"
        class="fixed left-0 top-0 z-999 block h-100vh w-full opacity-80 backdrop-blur-20px sm:hidden"
        @click="setting.isMenuCollapse = true"
      />
      <div
        class="group menu sticky left-0 top-0 z-1000 h-full px-2 pb-2 transition-all md:block border-default-r bg-color"
      >
        <el-scrollbar view-class="pb-10">
          <!-- 顶部 -->
          <div
            sticky top-0 z-2
            class="h-14 w-full flex-row-bt-c bg-color"
            @click="setting.isMenuFold = !setting.isMenuFold"
          >
            <NuxtLink to="/admin" :class="setting.isMenuFold ? 'op-0 w-0 ' : 'pl-2 w-6.6rem op-100' " class="truncate text-sm transition-all">
              <img alt="logo-text" src="/logo.png" class="mr-2 inline h-6 select-none object-cover">
              后台管理
            </NuxtLink>
            <span h-2.6rem w-2.6rem flex-row-c-c cursor-pointer>
              <i
                class="i-solar:hamburger-menu-outline"
                inline-block h-5 w-5
              />
            </span>
          </div>
          <el-divider
            style="margin: 1px 0"
            opacity-80
          />
          <!-- 菜单 -->
          <el-menu
            class="menu-first"
            :popper-offset="14"
            :router="true"
            unique-opened
            :default-active="activeMenu"
            :class="{ 'is-fold': setting.isMenuFold }"
            :collapse="setting.isMenuFold"
          >
            <!-- 递归生成菜单栏 -->
            <AdminSystemMenuLine
              v-for="data in rawRoutes"
              :key="data?.meta?.url"
              :data="data"
            />
          </el-menu>
        </el-scrollbar>
      </div>
    </ClientOnly>
  </menu>
</template>

<style lang="scss" scoped>
$rounded-default: 0.5rem;
$el-base-level-padding: 10px;
.menu {
  height: 100vh;
  height: 100dvh;
  user-select: none;
  :deep(.el-scrollbar__thumb) {
    width: 0;
  }
  :deep(.el-menu) {
    background-color: transparent;
    &.menu-first {
      border: none;
      > .el-menu-item {
        margin: 0.6rem 0;
      }
    }
    &.el-menu--popup {
      padding: 0.2rem;
      border-radius: $rounded-default;
    }
    // 二级
    .is-opened {
      border-radius: $rounded-default;
    }

    // 菜单单项
    .el-menu-item,
    .el-sub-menu_title {
      .is-active,
      &.is-active {
        border: 1px solid var(--el-color-primary);
        box-shadow: rgba(0, 0, 0, 0.12) 0px 1px 3px, rgba(0, 0, 0, 0.2) 0px 1px 2px;
      }
      border: 1px solid transparent;
      width: auto;
      height: 2.6rem;
      border-radius: $rounded-default;
      transition: 150ms;
      &:not(.is-active):hover {
        color: var(--el-color-primary);
        border-color: var(--el-color-primary);
      }
    }

    &.el-menu--inline {
      transition-property: all;
      border-radius: $rounded-default $rounded-default;
      background-color: #97979718;
      backdrop-filter: blur(12px);
      box-shadow: inset rgba(22, 22, 22, 0.1) 0px 1px 2px, inset rgba(22, 22, 22, 0.1) 0px 1px 2px;
      .el-menu-item {
        border-radius: $rounded-default;
        width: 100%;
        font-size: 0.8rem;
        margin: 0 auto;
      }
    }

    .el-menu-item,
    .el-sub-menu {
      border: 1px dashed transparent;
      border-radius: $rounded-default;
    }
  }
}


:deep(.el-menu-item.is-active){
  &,
  &:hover{
    color: #fff;
    background-color: var(--el-color-primary);
  }
}
</style>
