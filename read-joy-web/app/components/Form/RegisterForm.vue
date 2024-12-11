<script lang="ts" setup>
import type { FormInstance, FormRules } from "element-plus";
import { type Result, ResultStatus } from "~/composables/api/types/result";
import { useLoginApi, useRegisterApi, type UserRegisterVO } from "~/composables/api/user";
import { useUserStore } from "~/composables/sotre/useUserStore";

// 请求加载
const isLoading = ref<boolean>(false);
const loadingText = ref<string>("");
const formRef = ref();
// 表单
const formUser = reactive({
  username: "", // 用户名
  phone: "", // 手机号 0
  code: "", // 验证码
  password: "", // 密码
  secondPassword: "", // 密码
});
const rules = reactive<FormRules>({
  username: [
    { required: true, message: "用户名不能为空！", trigger: "blur" },
    {
      pattern: /^[a-z]\w*$/i,
      message: "英文开头、字母数字下划线组成",
      trigger: "change",
    },
    { min: 6, max: 30, message: "长度在6-30个字符！", trigger: "change" },
    {
      asyncValidator: async () => {
        return await checkUsername();
      },
      message: "该用户名已被使用！",
      trigger: "change",
    },
  ],
  password: [
    { required: true, message: "密码不能为空！", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度为6-20字符！", trigger: "change" },
    {
      pattern: /^\w{6,20}$/,
      message: "密码字母数字下划线组成",
      trigger: "change",
    },
    {
      validator(rule: any, value: string, callback: any) {
        if (value !== formUser.password?.trim())
          callback(new Error("两次密码不一致"));
        else
          callback();
      },
    },
  ],
  secondPassword: [
    { required: true, message: "密码不能为空！", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度为6-20字符！", trigger: "change" },
    {
      pattern: /^\w{6,20}$/,
      message: "密码字母数字下划线组成",
      trigger: "change",
    },
    {
      validator(rule: any, value: string, callback: any) {
        if (value !== formUser.password?.trim())
          callback(new Error("两次密码不一致"));
        else
          callback();
      },
    },
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
      trigger: ["blur", "change"],
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

/**
 *
 * @param timer 本地定时器
 * @param num 计数对象
 * @param target 开始秒数
 * @param step 自增自减
 * @param fn 回调
 */
function useInterval(timer: any, num: Ref<number>, target?: number, step: number = -1, fn?: () => void) {
  num.value = target || timer.value;
  timer.value = setInterval(() => {
    num.value += step;
    // 清除定时器
    if (num.value <= 0) {
      num.value = -1;
      timer.value = -1;
      clearInterval(timer.value);
      fn && fn();
    }
  }, 1000);
}
const store = useUserStore();
/**
 * 注册
 * @param formEl 表单实例
 */
async function onRegister(formEl: FormInstance) {
  if (!formEl)
    return;
  await formEl.validate((valid) => {
    isLoading.value = true;

    if (valid)
      onRegisterHandle();
    else
      isLoading.value = false;
  });
}
async function onRegisterHandle() {
  let res: Result<UserRegisterVO> = { code: 20001, message: "注册失败，请稍后重试！", data: {} } as any;
  try {
    // 两个密码校验
    if (formUser.password !== formUser.secondPassword) {
      ElMessage.error("两次密码不一致！");
      return;
    }
    res = await useRegisterApi({
      loginName: formUser.username,
      loginPassword: formUser.password,
    });
  }
  catch (error) {
    isLoading.value = false;
  }

  if (res.code === ResultStatus.SUCCESS) {
    // 注册成功
    if (res.data) {
      const token = res.data.token;
      ElMessage.success({
        message: "恭喜，注册成功 🎉",
        duration: 3000,
      });
      // 登录
      let count = 3;
      const timers = setInterval(() => {
        isLoading.value = true;
        loadingText.value = `${count}s后自动登录...`;
        if (count <= 0) {
          toLogin(token);
          // 清除
          clearInterval(timers);
        }
        count--;
      }, 1000);
    }
  }
  else {
    ElMessage.closeAll("error");
    ElMessage.error({
      message: res.message || "抱歉，注册出了点问题！",
      duration: 3000,
    });
    isLoading.value = false;
    // store
    store.$patch({
      token: "",
      isLogin: false,
    });
  }
}

async function toLogin(token?: string) {
  if (token) {
    store.$patch({
      token,
      showLoginForm: false,
      showRegisterForm: false,
      isLogin: true,
    });
    // 登录
    store.onUserLogin(token, true);
    ElMessage.success({
      message: "登录成功！",
    });
    return;
  }
  const data = await useLoginApi({
    loginName: formUser.username,
    password: formUser.password,
  });
  // 自动登录成功
  store.$patch({
    token: data.data.token,
    showLoginForm: false,
    showRegisterForm: false,
    isLogin: true,
  });
  ElMessage.success({
    message: "登录成功！",
  });
  isLoading.value = false;
}

/**
 * 验证是否存在该用户
 */
async function checkUsername() {
  if (formUser.username.trim() === "")
    return Promise.reject();
  // const data = await  checkUsernameExists(formUser.username);
  // if (data.code === 20000)
  return Promise.resolve();

  return Promise.reject("该用户名已被使用！");
}

function toLoginForm() {
  store.$patch({
    showRegisterForm: false,
    showLoginForm: true,
  });
}
</script>

<template>
  <!-- 注册 -->
  <el-form
    ref="formRef"
    :disabled="isLoading"
    label-position="top"
    style="border: none;"
    hide-required-asterisk :rules="rules" :model="formUser" class="form relative"
  >
    <small v-if="isLoading" class="z-999 absolute-center-center">{{ loadingText }}</small>
    <h4 mb-4 tracking-0.2em op-80>
      开启你的专属圈子✨
    </h4>
    <p mb-4 text-0.8em tracking-0.1em op-70>
      已有账户？
      <span cursor-pointer color-emerald transition-300 hover:font-700 @click="toLoginForm">
        立即登录
      </span>
    </p>
    <!-- 验证码注册(客户端 ) -->
    <!-- 用户名 -->
    <el-form-item label="" prop="username" class="animated">
      <el-input v-model.lazy="formUser.username" :prefix-icon="ElIconUser" size="large" placeholder="请输入用户名" />
    </el-form-item>
    <!-- 密 码 -->
    <el-form-item
      type="password" show-password label="" prop="password" class="animated"
    >
      <el-input
        v-model.trim="formUser.password" :prefix-icon="ElIconLock" size="large" placeholder="请输入密码（6-20位）" show-password
        type="password"
      />
    </el-form-item>
    <!-- 确认密码 -->
    <el-form-item
      type="password" show-password label="" prop="secondPassword" class="animated"
    >
      <el-input
        v-model.trim="formUser.secondPassword" :prefix-icon="ElIconLock" size="large" placeholder="再一次输入密码" show-password
        type="password"
      />
    </el-form-item>
    <el-form-item mt-1em>
      <BtnElButton type="primary" class="submit w-full tracking-0.2em shadow" style="padding: 20px" @click="onRegister(formRef)">
        立即注册
      </BtnElButton>
    </el-form-item>
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
