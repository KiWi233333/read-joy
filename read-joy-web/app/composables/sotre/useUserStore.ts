import { acceptHMRUpdate, defineStore } from "pinia";
import { ResultStatus } from "../api/types/result";
import { useGetUserInfoApi, type UserInfoVO, type UserUpdatePwdDTO, useUpdateUserPwdApi } from "../api/user";
import { useDefaultStore } from "./useDefaultStore";
// @unocss-include
// https://pinia.web3doc.top/ssr/nuxt.html#%E5%AE%89%E8%A3%85
export const useUserStore = defineStore(
  "user",
  () => {
    // token
    const token = ref<string>("");
    // 是否登录
    const isLogin = ref<boolean>(false);
    // 是否打开登录
    const showLoginForm = ref<boolean>(false);
    const showEditForm = ref<boolean>(false); // 是否打开修改个人信息
    const showRegisterForm = ref<boolean>(false);
    const showUpdatePwd = ref<boolean>(false);
    const isOnLogining = ref<boolean>(false);
    // 用户个人信息
    const userInfo = ref<UserInfoVO>({
      id: undefined,
      loginName: undefined,
      trueName: undefined,
      telephone: undefined,
      imgUrl: undefined,
      isChecked: undefined,
      createTime: undefined,
      userType: undefined,
    });
    // 用户id
    const userId = computed(() => userInfo.value.id);
    const getToken = computed({
      get() {
        if (!isLogin.value || !token.value) {
          showLoginForm.value = true;
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
    // 搜索记录
    const searchHistory = ref<string[]>([]);

    function getTokenFn() {
      return token.value?.trim();
    }

    /**
     * 用户登录
     * @param t token
     */
    const onUserLogin = async (t: string, saveLocal?: boolean, redirectTo?: string, callback?: (data: UserInfoVO) => void) => {
      try {
        // 用户信息
        const res = await useGetUserInfoApi(t);
        if (res.code && res.code === ResultStatus.SUCCESS) {
          userInfo.value = res.data as UserInfoVO;
          isLogin.value = true;
          token.value = t;
          callback && callback(res.data);
          if (redirectTo)
            await navigateTo(redirectTo);
        }
        else {
          callbackUserExit(t);
        }
      }
      catch (error) {
        console.error(error);
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
            ElMessage.success("退出成功 🎈");
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
      clearUserStore();
      await nextTick();
    }

    function toLogin(closebookdetail: boolean = true) {
      if (closebookdetail) {
        useDefaultStore().showBookDetail = false;
      }
      showLoginForm.value = true;
    }


    async function onTogglePwd(dto: UserUpdatePwdDTO) {
      const res = await useUpdateUserPwdApi(dto, getToken.value);

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
    function clearUserStore() {
      token.value = "";
      isLogin.value = false;
      userInfo.value = {
        id: undefined,
        loginName: undefined,
        trueName: undefined,
        telephone: undefined,
        imgUrl: undefined,
        isChecked: undefined,
        createTime: undefined,
        userType: undefined,
      };
      showLoginForm.value = false;
      showRegisterForm.value = false;
    }
    return {
      // state
      userId,
      userInfo,
      token,
      isLogin,
      isOnLogining,
      showUpdatePwd,
      showLoginForm,
      showEditForm,
      showRegisterForm,
      searchHistory,
      // actions
      onUserLogin,
      toLogin,
      onCheckLogin,
      callbackUserExit,
      exitLogin,
      clearUserStore,
      onTogglePwd,
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
  import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot));
