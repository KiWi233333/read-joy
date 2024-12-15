import { useUserStore } from "~/composables/sotre/useUserStore";
import { useCopyText } from "~/composables/utils/useUtils";

// 引入icon
export default defineNuxtPlugin(async (nuxtApp) => {
  // 写入指令
  nuxtApp.vueApp.directive("auth-disabled", {
    mounted(el: HTMLElement, binding) {
      if (binding.value) {
        el.setAttribute("--v-auth-disabled-content", binding.value);
        el.classList.add("v-auth-disabled");
        // 加入
        el.addEventListener("click", (e) => {
          e.preventDefault();
        });
      }
    },
    updated(el: HTMLElement, binding) {
      if (binding.value) {
        el.classList.add("v-auth-disabled");
      }
      else {
        el.classList.remove("v-auth-disabled");
      }
    },
    unmounted(el: HTMLElement, binding) {
      if (binding.value) {
        el.classList.remove("v-auth-disabled");
      }
    },
  });

  /**
   * 长按事件
   */
  nuxtApp.vueApp.directive("longpress", {
    mounted(el, binding) {
      let timer: NodeJS.Timeout | number;
      const start = (e: Event) => {
        if (e.type === "click")
          return; // 点击停止
        if (timer === null) {
          timer = setTimeout(() => {
            binding.value(e);
          }, 600);
        }
      };
      const cancel = () => {
        if (timer !== null) {
          clearTimeout(timer);
          timer = 0;
        }
      };

      // 开始
      el.addEventListener("mousedown", start, { passive: true });
      el.addEventListener("touchstart", start, { passive: true });
      // 取消
      el.addEventListener("mouseout", cancel, { passive: true });
      el.addEventListener("touchend", cancel, { passive: true });
      el.addEventListener("click", cancel, { passive: true });
    },
  });


  /**
   * 窗口锁定
   */
  nuxtApp.vueApp.directive("window-lock", {
    mounted(el, binding) {
      if (binding.value) {
        const cWidth = document.body.clientWidth || document.documentElement.clientWidth;// 页面可视区域宽度
        const iWidth = window.innerWidth;// 浏览器窗口大小
        document.body.style.paddingRight = `${iWidth - cWidth}px`;
        document.body.style.overflow = "hidden";
      }
      else {
        document.body.style.overflow = "auto";
        document.body.style.paddingRight = "0px";
      }
    },
    updated(el, binding) {
      if (binding.value) {
        const cWidth = document.body.clientWidth || document.documentElement.clientWidth;// 页面可视区域宽度
        const iWidth = window.innerWidth;// 浏览器窗口大小
        document.body.style.paddingRight = `${iWidth - cWidth}px`;
        document.body.style.overflow = "hidden";
      }
      else {
        document.body.style.overflow = "auto";
        document.body.style.paddingRight = "0px";
      }
    },
    unmounted() {
      document.body.style.overflow = "auto";
      document.body.style.paddingRight = "0px";
    },
  });


  /**
   * 复制文本
   */
  nuxtApp.vueApp.directive("copying", {
    mounted(el, binding) {
      el.addEventListener("click", async (e: Event) => {
        e.stopPropagation();
        const res = await useAsyncCopyText(binding.value || el.innerHTML);
        if (res && binding.modifiers.toast) {
          ElMessage.success({
            message: "成功复制至剪贴板！",
            grouping: true,
          });
        }
      }, { passive: true });
    },
  });


  /**
   * 权限校验
   */
  nuxtApp.vueApp.directive("auth", {
    mounted(el, binding) {
      el.addEventListener("click", async (e: Event) => {
        e.stopPropagation();
        const user = useUserStore();
        if (binding.value !== undefined && Boolean(binding.value)) {
          user.showLoginForm = true;
          return;
        }
        user?.getTokenFn && user?.getTokenFn();
      }, { passive: true });
    },
  });
});

/**
 *
 * 复制文本（异步）
 * @param text 复制的文本
 * @returns promise
 */
function useAsyncCopyText(text: string): Promise<boolean> {
  // 新语法
  if (navigator.clipboard) {
    navigator.clipboard.writeText(text);
    return Promise.resolve(true);
  }
  else {
    // 兼容移动端版本
    return new Promise((resolve, reject) => {
      if (useCopyText(text))
        return resolve(true);
      else
        return resolve(false);
    });
  }
}
