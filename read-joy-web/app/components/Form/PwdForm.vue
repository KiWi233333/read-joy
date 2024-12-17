<script lang="ts" setup>
import type { FormInstance } from "element-plus/es/components/form";

const {
  modelValue,
} = defineProps<{
  modelValue: boolean
}>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "submit", value: { oldPassword: string; newPassword: string }): void;
}>();
enum CheckTypeEnum {
  PASSWORD = 1,
}
const isLoading = ref<boolean>(false);
const chooseType = ref<CheckTypeEnum | undefined>(CheckTypeEnum.PASSWORD);

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

// 去更新
async function toUpdate() {
  if (chooseType.value === undefined)
    return;

  emit("submit", {
    oldPassword: userForm.oldPassword,
    newPassword: userForm.newPassword,
  });
}
</script>

<template>
  <el-dialog
    width="fit-content"
    :append-to-body="true"
    :show-close="false"
    destroy-on-close
    :overflow="false"
    v-bind="$attrs"
    :model-value="modelValue"
    @update:model-value="(val: boolean) => emit('update:modelValue', val)"
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
      class="w-18rem p-2"
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
        size="large"
        class="w-full"
        style="font-size: 1rem;"
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
