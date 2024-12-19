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
export const rawRoutes: MenuItem[] = [
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
  /** ------------------ 评论模块 ---------------- */
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
  /** ------------------ 资源模块 ---------------- */
  {
    path: "resource",
    name: "admin:resource",
    meta: {
      url: "/admin/resource",
      title: "资源",
      icon: "i-solar:library-line-duotone",
      onIcon: "i-solar:library-bold-duotone",
    },
  },
  /** ------------------ 其他模块 ---------------- */
  // {
  //   path: "tool",
  //   name: "admin:tool",
  //   meta: {
  //     title: "工具",
  //     url: "/admin/tool",
  //     icon: "i-solar:inbox-archive-linear",
  //     onIcon: "i-solar:inbox-archive-bold-duotone",
  //   },
  //   children: [
  //     {
  //       path: "chat",
  //       name: "admin:tool:chat",
  //       meta: {
  //         url: "/admin/tool/chat",
  //         title: "聊天",
  //         icon: "i-solar:chat-line-broken",
  //         onIcon: "i-solar:chat-line-bold-duotone",
  //       },
  //     },
  //   ],
  // },
];

export function getRouteTitle(path: string) {
  for (const item of rawRoutes) {
    if (item.path === path) {
      return item.meta.title;
    }
    if (item.children) {
      for (const child of item.children) {
        if (child.path === path) {
          return child.meta.title;
        }
      }
    }
  }
  return "";
}


export interface MenuButtonType {
  title: string
  icon?: string | object | Array<string | object>
  type?: "" | "default" | "info" | "text" | "success" | "warning" | "primary" | "danger"
  plain?: boolean
  show?: boolean
  methods?: () => any
}
