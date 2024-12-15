<script lang="ts" setup>
import type { FormInstance } from "element-plus/es/components/form";
import { useUpdatePwdApi } from "~/composables/api/admin/admin";
import { ResultStatus } from "~/composables/api/types/result";
import { useUserStore } from "~/composables/sotre/useUserStore";

const user = useUserStore();
enum CheckTypeEnum {
  OLD_PASSWORD = 2,
}
const isLoading = ref<boolean>(false);
const chooseType = ref<CheckTypeEnum | undefined>(CheckTypeEnum.OLD_PASSWORD);

// 表单
const userForm = reactive({
  oldPassword: "",
  newPassword: "", // 密码
});
const rules = {
  oldPassword: [
    { required: true, message: "旧密码不能为空！", trigger: "blur" },
    {
      pattern: /^\w{6,20}$/,
      message: "密码字母数字下划线组成",
      trigger: ["blur", "change"],
    },
    { min: 6, max: 20, message: "旧密码长度6-20字符！", trigger: "blur" },
    {
      validator: () => userForm.oldPassword !== userForm.newPassword,
      message: "新旧密码相同！",
      trigger: ["blur", "change"],
    },
  ],
  newPassword: [
    { required: true, message: "新密码不能为空！", trigger: "blur" },
    {
      pattern: /^\w{6,20}$/,
      message: "密码字母数字下划线组成",
      trigger: ["blur", "change"],
    },
    { min: 6, max: 20, message: "新密码长度6-20字符！", trigger: "blur" },
    {
      validator: () => userForm.oldPassword !== userForm.newPassword,
      message: "新旧密码相同！",
      trigger: ["blur", "change"],
    },
  ],
};
const userFormRef = ref();

/**
 * 修改密码
 */
async function onUpdatePwd(formEl: FormInstance | undefined) {
  if (!formEl || isLoading.value)
    return;
  await formEl.validate(async (valid) => {
    if (valid) {
      isLoading.value = true;
      try {
        const action = await ElMessageBox.confirm("是否确认修改密码?", "修改密码", {
          confirmButtonText: "确认修改",
          cancelButtonText: "取消",
        });
        if (action === "confirm")
          await toUpdate();
      }
      finally {
        setTimeout(() => {
          isLoading.value = false;
        }, 300);
      }
    }
  });
}

async function toUpdate() {
  if (chooseType.value === undefined)
    return;
  const res = await useUpdatePwdApi(
    { oldPassword: userForm.oldPassword, newPassword: userForm.newPassword },
    user.getToken,
  );

  if (res && res.code === ResultStatus.SUCCESS) {
    // 修改成功
    ElMessage.success({
      message: "修改成功，下次登录请用新密码！",
      duration: 2000,
    });
    // 修改成功后关闭弹窗
    user.showEditForm = false;
  }
  return true;
}
</script>

<template>
  <el-dialog
    v-model="user.showEditForm"
    width="fit-content"
    :append-to-body="true"
    :show-close="false"
    :overflow="false"
  >
    <template #header>
      <h3 text-center text-lg>
        修改密码
      </h3>
    </template>
    <el-form
      ref="userFormRef"
      :disabled="isLoading"
      label-position="top"
      hide-required-asterisk
      :rules="rules"
      :model="userForm"
      class="w-18rem"
    >
      <!-- 新旧密码 -->
      <el-form-item type="password" label="旧密码" prop="oldPassword" class="animated">
        <el-input
          v-model.trim="userForm.oldPassword"
          :prefix-icon="ElIconUnlock"
          size="large"
          placeholder="请输入旧密码"
          required
          show-password
          type="password"
          @keyup.enter="onUpdatePwd(userFormRef)"
        />
      </el-form-item>
      <el-form-item
        type="password" label="新密码" prop="newPassword" class="animated"
      >
        <el-input
          v-model.trim="userForm.newPassword"
          :prefix-icon="ElIconUnlock"
          size="large"
          placeholder="请输入新密码"
          required
          show-password
          type="password"
          @keyup.enter="onUpdatePwd(userFormRef)"
        />
      </el-form-item>
      <el-button
        type="danger"
        class="w-full"
        style="padding: 1em 0"
        @keyup.enter="onUpdatePwd(userFormRef)"
        @click="onUpdatePwd(userFormRef)"
      >
        修 改
      </el-button>
    </el-form>
  </el-dialog>
</template>

<style scoped lang="scss">

</style>
