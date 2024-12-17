<script lang="ts" setup>
import { useSettingStore } from "~/composables/sotre/useSettingStore";

const route = useRoute();
const router = useRouter();
const setting = useSettingStore();
const activeMenu = computed({
  get() {
    return route.path;
  },
  set(val: string) {
    router.push(val);
  },
});

export interface MenuItem {
  path: string;
  name: string;
  meta: {
    title: string;
    icon: string;
    onIcon: string;
    url?: string;
    first?: boolean;
    [key: string]: any;
  };
  children?: MenuItem[];
  [key: string]: any;
}

// @unocss-include
const rawRoutes: MenuItem[] = [
  {
    path: "home",
    name: "admin:index",
    meta: {
      title: "首页",
      icon: "i-solar:home-2-outline",
      onIcon: "i-solar:home-2-bold",
      url: "/admin",
      first: true,
    },
  },
  /** ------------------ 用户管理 ---------------- */
  {
    path: "user",
    name: "admin:user",
    meta: {
      url: "/admin/user",
      title: "用户",
      icon: "i-solar:user-broken",
      onIcon: "i-solar:user-bold",
    },
  },
  /** ------------------ 图书管理 ---------------- */
  {
    path: "book",
    name: "admin:book",
    meta: {
      title: "图书",
      icon: "i-solar:book-2-outline",
      onIcon: "i-solar:book-2-bold-duotone",
    },
    children: [
      {
        path: "book",
        name: "admin:book:index",
        meta: {
          url: "/admin/book",
          title: "图书列表",
          icon: "i-solar:book-bookmark-outline",
          onIcon: "i-solar:book-bookmark-bold-duotone",
        },
      },
      {
        path: "category",
        name: "admin:category",
        meta: {
          url: "/admin/book/category",
          title: "分类列表",
          icon: "i-solar:widget-2-broken",
          onIcon: "i-solar:widget-2-bold-duotone",
        },
      },
    ],
  },
  /** ------------------ 资源模块 ---------------- */
  {
    path: "resorce",
    name: "admin:resorce",
    meta: {
      url: "/admin/resorce",
      title: "资源",
      icon: "i-solar:library-line-duotone",
      onIcon: "i-solar:library-bold-duotone",
    },
  },
  {
    path: "comment",
    name: "admin:comment",
    meta: {
      url: "/admin/comment",
      title: "评论",
      icon: "i-solar:chat-line-broken",
      onIcon: "i-solar:chat-line-bold",
    },
  },
  /** ------------------ 其他模块 ---------------- */
  // {
  //   path: "tools",
  //   name: "admin:tools",
  //   meta: {
  //     title: "工具",
  //     url: "/admin/tools",
  //     icon: "i-solar:inbox-archive-linear",
  //     onIcon: "i-solar:inbox-archive-bold-duotone",
  //   },
  //   children: [
  //     {
  //       path: "chat",
  //       name: "admin:tools:chat",
  //       meta: {
  //         url: "/admin/tools/chat",
  //         title: "聊天",
  //         icon: "i-solar:chat-line-broken",
  //         onIcon: "i-solar:chat-line-bold-duotone",
  //       },
  //     },
  //   ],
  // },
];
</script>

<template>
  <menu :class="{ 'w-0 -translate-x-full': setting.isMenuCollapse }" class="transition-200">
    <ClientOnly>
      <div
        v-if="!setting.isMenuCollapse"
        class="v-card fixed left-0 top-0 z-99 block h-100vh w-full opacity-80 backdrop-blur-20px sm:hidden"
        @click="setting.isMenuCollapse = true"
      />
      <div
        transition="all 300  cubic-bezier(0.61, 0.225, 0.195, 1.3)"
        class="group menu fixed z-999 h-full px-2 pb-2 md:sticky md:block border-default-r bg-color"
        :class="{ 'w-0 truncate -translate-x-full': setting.isMenuCollapse }"
      >
        <el-scrollbar height="calc(100vh - 60px)" view-class="pb-10">
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
  // height: calc(100vh - $top-nav-height);
  // top: $top-nav-height;
  top: 0;
  left: 0;
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
    .el-sub-menu__title {
      &.is-active {
        border: 1px solid var(--el-color-primary);
        box-shadow: rgba(0, 0, 0, 0.12) 0px 1px 3px, rgba(0, 0, 0, 0.2) 0px 1px 2px;
      }
      border: 1px solid transparent;
      width: auto;
      height: 2.6rem;
      border-radius: $rounded-default;
      transition: 150ms;
      &:hover {
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


:deep(.el-menu){
  .el-menu-item.is-active {
    &,
    &:hover{
      color: #fff;
      background-color: var(--el-color-primary);
    }
  }
}
</style>
