<script lang="ts" setup>
import { useAdminLoginApi } from "~/composables/api/admin/admin";
import { ResultStatus } from "~/composables/api/types/result";
import { useAdminStore } from "~/composables/sotre/useAdminStore";

enum LoginType {
  ADMIN = "admin",
}

const admin = useAdminStore();
const loginType = useLocalStorage<LoginType>("loginType", LoginType.ADMIN);
const isLoading = ref<boolean>(false);
const autoLogin = ref<boolean>(true);
// 表单
const userForm = useLocalStorage("userForm", {
  username: "",
  password: "",
});
const rules = reactive({
  username: [
    { required: true, message: "用户名不能为空！", trigger: "blur" },
    { min: 6, max: 20, message: "长度在6-20个字符！", trigger: "blur" },
  ],
  password: [
    { required: true, message: "密码不能为空！", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度6-20位！", trigger: "blur" },
  ],
});

const formRef = ref();
function done() {
  isLoading.value = false;
  admin.isOnLogining = false;
}
onMounted(() => {
  admin.isOnLogining = false;
});
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
    admin.isOnLogining = true;
    const res = await useAdminLoginApi({
      username: userForm.value.username,
      password: userForm.value.password,
    });
    if (res.code === ResultStatus.SUCCESS) {
      // 登录成功
      if (res.data && res.data.token) {
        await admin.onUserLogin(res.data.token, autoLogin.value);
        done();
        ElMessage.success({
          message: "登录成功 🎉",
          duration: 1000,
        });
        navigateTo("/admin");
        return;
      }
      // 登录失败
      ElMessage.error({
        message: res.message,
        duration: 3000,
      });
      // store
      admin.$patch({
        token: "",
        isLogin: false,
      });
      done();
    }
    else {
      done();
    }
  });
}
</script>

<template>
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
    <h4 mb-4 text-center font-500 tracking-0.2em>
      管理后台登录
    </h4>
    <!-- 密码登录 -->
    <el-form-item
      label=""
      prop="username"
    >
      <el-input
        v-model.trim="userForm.username"
        autocomplete="off"
        :prefix-icon="ElIconUser"
        size="large"
        placeholder="请输入账号"
        @keyup.enter="onLogin(formRef)"
      />
    </el-form-item>
    <el-form-item
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
        :loading="admin.isOnLogining"
        @keyup.enter="onLogin(formRef)"
        @click="onLogin(formRef)"
      >
        登录
      </el-button>
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
