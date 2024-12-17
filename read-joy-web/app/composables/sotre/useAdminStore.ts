import { acceptHMRUpdate, defineStore } from "pinia";

import { type AdminInfoVO, type AdminUpdatePwdDTO, useGetAdminInfoApi, useUpdateAdminPwdApi } from "../api/admin/admin";
import { ResultStatus } from "../api/types/result";
import { useDefaultStore } from "./useDefaultStore";
// @unocss-include
// https://pinia.web3doc.top/ssr/nuxt.html#%E5%AE%89%E8%A3%85
export const useAdminStore = defineStore(
  "admin",
  () => {
    // token
    const token = ref<string>("");
    // 是否登录
    const isLogin = ref<boolean>(false);
    const showEditForm = ref<boolean>(false);
    const isOnLogining = ref<boolean>(false);
    // 个人信息
    const adminInfo = ref<Partial<AdminInfoVO>>({
      id: undefined,
      username: undefined,
    });
    // 用户id
    const userId = computed(() => adminInfo.value.id);
    const getToken = computed({
      get() {
        if (!isLogin.value || !token.value) {
          navigateTo("/admin/login");
          return "";
        }
        else {
          return token.value;
        }
      },
      set(value) {
        token.value = value;
      },
    });

    function getTokenFn() {
      return token.value?.trim();
    }

    /**
     * 用户登录
     * @param t token
     */
    const onUserLogin = async (t: string, saveLocal?: boolean, redirectTo?: string, callback?: (data: AdminInfoVO) => void) => {
      const res = await useGetAdminInfoApi(t);
      if (res.code && res.code === ResultStatus.SUCCESS) {
        adminInfo.value = res.data;
        isLogin.value = true;
        token.value = t;
        callback && callback(res.data);
        if (redirectTo)
          await navigateTo(redirectTo);
      }
      else {
        callbackUserExit(t);
      }
    };

    // 退出登录
    function exitLogin(toast: boolean = false) {
      ElMessageBox.confirm("是否确认退出登录？", "提示", {
        confirmButtonText: "确认退出",
        confirmButtonClass: "el-button--danger",
        lockScroll: false,
        center: true,
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          await callbackUserExit(token.value);
          if (toast) {
            await navigateTo("/admin/login");
            ElMessage.success("退出成功");
          }
        })
        .catch(() => { });
    }

    /**
     * 用户确认状态
     */
    const onCheckLogin = () => {
      if (token.value)
        return onUserLogin(token.value);
      else
        return false;
    };
    /**
     * 退出登录
     * @param t token
     */
    async function callbackUserExit(t?: string) {
      // 退出登录
      clearStore();
      await nextTick();
    }

    function toLogin(closebookdetail: boolean = true) {
      if (closebookdetail) {
        useDefaultStore().showBookDetail = false;
      }
      navigateTo("/admin/login");
    }


    /**
     * 修改密码
     * @param dto 修改密码dto
     */
    async function onTogglePwd(dto: AdminUpdatePwdDTO) {
      const res = await useUpdateAdminPwdApi(dto, getToken.value);

      if (res && res.code === ResultStatus.SUCCESS) {
        // 修改成功
        ElMessage.success({
          message: "修改成功，下次登录请用新密码！",
          duration: 2000,
        });
        // 修改成功后关闭弹窗
        showEditForm.value = false;
      }
    }

    /**
     * 清空store缓存
     */
    function clearStore() {
      token.value = "";
      isLogin.value = false;
      adminInfo.value = {
        id: undefined,
        username: undefined,
      };
    }

    return {
      // state
      userId,
      adminInfo,
      token,
      isLogin,
      isOnLogining,
      showEditForm,
      // actions
      onUserLogin,
      toLogin,
      onCheckLogin,
      onTogglePwd,
      callbackUserExit,
      exitLogin,
      clearStore,
      // getter
      getToken,
      getTokenFn,
    };
  },
  {
    // https://prazdevs.github.io/pinia-plugin-persistedstate/frameworks/nuxt-3.html
    persist: {
      storage: piniaPluginPersistedstate.cookies(),
    },
  },
);
if (import.meta.hot)
  import.meta.hot.accept(acceptHMRUpdate(useAdminStore, import.meta.hot));
