import { useDefaultStore } from "../sotre/useDefaultStore";
import { useSettingStore } from "../sotre/useSettingStore";
import { useUserStore } from "../sotre/useUserStore";
import { useModeToggle } from "./useToggleThemeAnima";

export function useInit() {
  const setting = useSettingStore();
  const store = useDefaultStore();
  const user = useUserStore();
  user.showRegisterForm = false;
  user.showLoginForm = false;
  onMounted(() => {
    // 1、用户信息获取
    if (user.token) {
      user.onCheckLogin();
    }
    // 1、模式切换
    window.addEventListener("keydown", keyToggleTheme);
    // 2、监听窗口大小变化
    setting.isMobileSize = window?.innerWidth <= 768; // 判断是否为移动端
    let timer: NodeJS.Timeout | null = null;
    window.addEventListener("resize", resizeWind);
    function resizeWind() {
      if (timer)
        clearTimeout(timer); // 清除之前的定时器，避免重复触发
      const app = document.documentElement;
      if (app)
        app.classList.add("stop-transition");
      timer = setTimeout(() => {
        if (app)
          app.classList.remove("stop-transition");
        setting.isMobileSize = window?.innerWidth <= 768; // 判断是否为移动端
        timer = null;
      }, 200);
    }
  });


  // 2、监听书籍id变化
  watch(() => store.theBookDetail.bookId, (newVal, oldVal) => {
    if (newVal && newVal !== oldVal) {
      store.setBook(newVal);
    }
  });


  return () => {
    user.showRegisterForm = false;
    user.showLoginForm = false;
    window.removeEventListener("keydown", keyToggleTheme);
  };
}

// 快捷键切换
function keyToggleTheme(e: KeyboardEvent) {
  if (e?.altKey && e?.key && e?.key === "k") {
    const setting = useSettingStore();
    // 获取
    const dom = document.querySelector("#toggle-theme-btn");
    if (setting.isThemeChangeLoad) {
      return;
    }
    // 计算屏幕中心坐标
    const centerY = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight);
    const xy = dom?.getBoundingClientRect();
    const colorMode = useColorMode();
    const mode = colorMode.value === "dark" ? "light" : "dark";
    // 持久化
    setting.theme = mode;
    useModeToggle(mode, (dom && xy
      ? {
          // 按钮 x y 坐标、
          clientX: xy.x + 10,
          clientY: xy.y + 10,
        }
      : {
          clientX: window.innerWidth - 10,
          clientY: 10,
        }) as MouseEvent);
  }
}
