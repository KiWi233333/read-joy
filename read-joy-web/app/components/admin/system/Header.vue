<script lang="ts" setup>
import { useAdminStore } from "~/composables/sotre/useAdminStore";
import { useSettingStore } from "~/composables/sotre/useSettingStore";

defineProps({
  search: { type: Boolean, default: true }, // 是否显示搜索框，默认显示
});
const admin = useAdminStore();
const setting = useSettingStore();
// 搜索
const searchInputRef = ref();
function showSearch() {
  searchInputRef.value.inputRef?.focus?.();
}

const MenuTitleMap: Record<any, string> = {
  "/admin": "首页",
  "/admin/book": "图书管理",
  "/admin/book/category": "分类管理",
  "/admin/resource": "资源管理",
  "/admin/comment": "评论管理",
  "/admin/user": "用户管理",
  "/admin/tool/chat": "聊天",
};
</script>

<template>
  <header class="h-15 flex-row-bt-c shrink-0 px-2 -mt-1px border-default-b dark:border-color-[#1e1e1e] !rounded-0 card-default sm:px-4">
    <!-- 面包 -->
    <div class="flex items-center">
      <i
        class="mr-4 p-2 btn-primary"
        :title="setting.isMenuCollapse ? '展开菜单' : '收起菜单'"
        :class="setting.isMenuCollapse ? 'i-solar:list-down-minimalistic-linear' : 'i-solar:sidebar-minimalistic-outline'"
        @click="setting.isMenuCollapse = !setting.isMenuCollapse"
      />
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/admin' }">
          <strong>首页</strong>
        </el-breadcrumb-item>
        <el-breadcrumb-item v-if="$route.path !== '/admin'" :to="{ path: $route.path }">
          {{ MenuTitleMap[$route.path] || $route.name }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <ClientOnly>
      <div flex items-center gap-2 text-sm sm:gap-4>
        <LazyPopperAdminInfo v-if="admin.isLogin">
          <div order-0 h-2.2rem w-2.2rem flex-row-c-c rounded-full text-lg btn-primary-border border-default bg-color-br>
            {{ admin?.adminInfo?.username?.[0] || "A" }}
          </div>
        </LazyPopperAdminInfo>
        <template v-else>
          <NuxtLink to="/admin/login" class="btn-primary-circle">
            <i class="i-solar:admin-bold p-2" />
          </NuxtLink>
        </template>
        <BtnToggleTheme />
        <div class="block sm:hidden">
          <el-button :icon="ElIconSearch" circle @click="showSearch" />
        </div>
      </div>
    </ClientOnly>
  </header>
</template>

<style lang="scss" scoped>
</style>
