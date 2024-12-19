<script lang="ts" setup>
import { type Result, ResultStatus } from "~/composables/api/types/result";
import { useLoginApi, type UserLoginVO } from "~/composables/api/user";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { BaseUrlImg } from "~/composables/utils/useBaseUrl";
import { appName } from "~/constants";

enum LoginType {
  USER = "user",
  ADMIN = "admin",
}

const user = useUserStore();
const loginType = useLocalStorage<LoginType>("loginType", LoginType.USER);
const isLoading = ref<boolean>(false);
const autoLogin = ref<boolean>(true);
// 表单
const userForm = useLocalStorage("userForm", {
  loginName: "",
  password: "",
  code: "", // 验证码
  phone: "", // 手机登录
});
onMounted(() => {
  userForm.value.code = "";
});
const rules = reactive({
  loginName: [
    { required: true, message: "用户名不能为空！", trigger: "blur" },
    { min: 6, max: 30, message: "长度在6-30个字符！", trigger: "blur" },
  ],
  password: [
    { required: true, message: "密码不能为空！", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度6-20位！", trigger: "blur" },
  ],
  code: [
    {
      required: true,
      message: "验证码6位组成！",
      trigger: "change",
    },
  ],
  email: [
    { required: true, message: "邮箱不能为空！", trigger: "blur" },
    {
      pattern:
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\])|(([a-z\-0-9]+\.)+[a-z]{2,}))$/i,
      message: "邮箱格式不正确！",
      trigger: "blur",
    },
  ],
  phone: [
    { required: true, message: "手机号不能为空！", trigger: "blur" },
    {
      pattern:
        /^(?:(?:\+|00)86)?1(?:3\d|4[5-79]|5[0-35-9]|6[5-7]|7[0-8]|8\d|9[1589])\d{8}$/,
      message: "手机号格式不正确！",
      trigger: "change",
    },
  ],
});

const store = useUserStore();
function toRegister() {
  store.showLoginForm = false;
  store.showRegisterForm = true;
}

const formRef = ref();
function done() {
  isLoading.value = false;
  user.isOnLogining = false;
}
/**
 * 登录
 * @param formEl 表单示例
 */
async function onLogin(formEl: any | undefined) {
  if (!formEl || isLoading.value)
    return;
  formEl.validate(async (valid: boolean) => {
    if (!valid)
      return;
    isLoading.value = true;
    user.isOnLogining = true;
    let res: Result<Partial<UserLoginVO>> = { code: 20001, data: {}, message: "登录失败！" };
    try {
      switch (loginType.value) {
        case LoginType.ADMIN:
          res = await useLoginApi({
            loginName: userForm.value.loginName,
            password: userForm.value.password,
          });
          break;
        case LoginType.USER:
          res = await useLoginApi({
            loginName: userForm.value.loginName,
            password: userForm.value.password,
          });
          break;
      }
    }
    catch (error) {
      done();
    }
    if (res.code === ResultStatus.SUCCESS) {
      // 登录成功
      if (res.data && res.data.token) {
        await store.onUserLogin(res.data.token, autoLogin.value);
        done();
        ElMessage.success({
          message: "登录成功 🎉",
          duration: 1000,
        });
        setTimeout(() => {
          user.showLoginForm = false;
        }, 1000);
      }
      // 登录失败
      else {
        ElMessage.error({
          message: res.message,
          duration: 3000,
        });
        // store
        store.$patch({
          token: "",
          isLogin: false,
        });
        done();
      }
    }
    else {
      done();
    }
  });
}
</script>

<template>
  <!-- 登录 -->
  <el-form
    ref="formRef"
    :disabled="isLoading"
    label-position="top"
    hide-required-asterisk
    :rules="rules"
    :model="userForm"
    style="border: none;"
    class="form"
  >
    <template v-if="!user.isLogin">
      <h4 mb-4 font-500 tracking-0.2em>
        欢迎来到{{ appName }} ✨
      </h4>
      <p
        mb-4 text-0.8em tracking-0.1em op-70
      >
        还没有账户？
        <span
          cursor-pointer color-emerald transition-300 hover:font-700
          @click="toRegister"
        >
          立即注册
        </span>
      </p>
      <!-- 密码登录 -->
      <el-form-item
        v-if="loginType === LoginType.USER || loginType === LoginType.ADMIN "
        label=""
        prop="loginName"
      >
        <el-input
          v-model.trim="userForm.loginName"
          autocomplete="off"
          :prefix-icon="ElIconUser"
          size="large"
          placeholder="请输入用户名"
          @keyup.enter="onLogin(formRef)"
        />
      </el-form-item>
      <el-form-item
        v-if="loginType === LoginType.USER || loginType === LoginType.ADMIN"
        type="password"
        show-password
        label=""
        prop="password"
        class="animated"
      >
        <el-input
          v-model.trim="userForm.password"
          :prefix-icon="ElIconLock"
          autocomplete="off"
          size="large"
          placeholder="请输入密码"
          show-password
          type="password"
          @keyup.enter="onLogin(formRef)"
        />
      </el-form-item>
      <el-form-item mt-5>
        <el-button
          type="primary"
          class="submit w-full tracking-0.2em shadow"
          style="padding: 20px"
          :loading="user.isOnLogining"
          @keyup.enter="onLogin(formRef)"
          @click="onLogin(formRef)"
        >
          登录
        </el-button>
      </el-form-item>
    </template>
    <template v-else>
      <div data-fades class="relative flex-row-c-c flex-col gap-4 pb-2 sm:gap-6">
        <CardNuxtImg :src="user.userInfo.imgUrl ? BaseUrlImg + user.userInfo.imgUrl : ''" class="h-6rem w-6rem border-default card-default">
          <template #error>
            <div h-full w-full flex-row-c-c text-lg>
              {{ user?.userInfo?.loginName?.[0] || "未设置" }}
            </div>
          </template>
        </CardNuxtImg>
        <div text-center>
          <span>
            {{ user.userInfo.loginName || "未登录" }}
          </span>
          <br>
          <small el-color-info op-80>（{{ user.userInfo.loginName ? "已登录" : "请登录" }}）</small>
        </div>
        <div flex-row-bt-c px-4>
          <BtnElButton
            type="primary"
            transition-icon class="w-8em"
            :loading="user.isOnLogining"
            icon-class="i-solar-alt-arrow-left-bold"
            @click="() => {
              user.showLoginForm = false
              user.showRegisterForm = false
            }"
          >
            {{ user.isOnLogining ? "登录中..." : "前往" }}
          </BtnElButton>
          <BtnElButton
            type="danger"
            class="w-8em"
            plain
            transition-icon
            icon-class="i-solar:logout-3-broken"
            @click="user.exitLogin"
          >
            注销
          </BtnElButton>
        </div>
      </div>
    </template>
  </el-form>
</template>

<style scoped lang="scss">
.form {
  display: block;
  overflow: hidden;
  animation-delay: 0.1s;

  :deep(.el-input__wrapper) {
    padding: 0.3em 1em;
  }

  // 报错信息
  :deep(.el-form-item) {
    padding: 0.3em 0.1em;

    .el-form-item__error {
      padding-top: 0.4em;
    }
  }
}

:deep(.el-button) {
  padding: 0.3em 1em;
}

// label总体
:deep(.el-form-item) {
  margin-bottom: 14px;
}
</style>
