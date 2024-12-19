import { useAdminStore } from "~/composables/sotre/useAdminStore";
import { useUserStore } from "~/composables/sotre/useUserStore";

// 路由中间件
export default defineNuxtRouteMiddleware((to, from) => {
  // 详情页需要权限
  // if (to.path.includes("")) {
  //   const user = useUserStore();
  //   // 检查用户是否登录
  //   if (!user.isLogin) {
  //     // 如果没有登录 跳转来到的页面
  //     user.showLoginForm = true;
  //     return abortNavigation("403 暂无权限，请登录！");
  //   }
  // }
  // 管理员 /admin 页面需要管理员权限
  if (/^\/admin\/*/.test(to.path)) {
    const admin = useAdminStore();
    // 检查用户是否登录
    if (!admin.isLogin && to.path !== "/admin/login") {
      return "/admin/login";
    }
    else if (admin.isLogin && to.path === "/admin/login") {
      return "/admin";
    }
  }
  // 其他页面不需要权限
  return true;
});
