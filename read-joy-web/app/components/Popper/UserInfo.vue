<script setup lang="ts">
import type { UploadProps } from "element-plus";
import { type Result, ResultStatus } from "~/composables/api/types/result";
import { updateUserInfoApi, type UserInfoVO } from "~/composables/api/user";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { BaseUrl } from "~/composables/utils/useBaseUrl";

const user = useUserStore();
const isVisible = ref(false);

defineExpose({
  isVisible,
});
// 头像
const avatatRef = ref();
const formData = new FormData();

/**
 * 上传之前验证类型
 */
const beforeUpload: UploadProps["beforeUpload"] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 5) {
    ElMessage.error("头像需要小于5MB!");
    return false;
  }
  // check success
  formData.append("file", rawFile);
  return true;
};
/**
 * 更新
 */
const onUpdateSuccess: UploadProps["onSuccess"] = async (data: Result<UserInfoVO>) => {
  avatatRef.value?.clearFiles();
  if (data.code === ResultStatus.SUCCESS) {
    user.userInfo = data.data;
    ElMessage.success("更换头像成功！");
  }
  else {
    ElMessage.error(data.message);
  }
};

const editFiled = ref<"loginName" | "trueName" | "telephone">();
watch(editFiled, (val) => {
  if (!val)
    return;
  const map: Record<("loginName" | "trueName" | "telephone"), any> = {
    loginName: {
      desc: "登录账号",
      inputType: "text",
      inputValue: user.userInfo.loginName,
      center: true,
      inputPattern: /^[\w-]{6,20}$/,
      inputPlaceholder: "请输入新的登录账号",
      inputErrorMessage: "登录账号格式错误",
    },
    trueName: {
      desc: "真实姓名",
      inputType: "text",
      inputValue: user.userInfo.trueName,
      center: true,
      inputPattern: /^.{1,20}$/,
      inputPlaceholder: "请输入新的真实姓名",
      inputErrorMessage: "真实姓名格式错误",
    },
    telephone: {
      desc: "联系电话",
      inputType: "text",
      inputValue: user.userInfo.telephone,
      center: true,
      inputPattern: /^1[3-9]\d{9}$/,
      inputPlaceholder: "请输入新的联系电话",
      inputErrorMessage: "联系电话格式错误",
    },
  };
  ElMessageBox.prompt(`修改${map[val].desc}`, {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    ...map[val],
    center: true,
  }).then(async ({ value }) => {
    if (value) {
      const res = await updateUserInfoApi({ [`${val}`]: value }, user.token);
      if (res.code === ResultStatus.SUCCESS) {
        user.userInfo = res.data;
        ElMessage.success("修改成功！");
      }
      else {
        setTimeout(() => {
          editFiled.value = val;
        }, 300);
      }
    }
    editFiled.value = undefined;
  });
});
</script>

<template>
  <el-popover
    placement="bottom"
    v-bind="$attrs"
    width="fit-content"
    trigger="click"
    :offset="10"
    transition="popper-fade"
    popper-style="padding: 0;border-radius: 8px;border:none; backgroud:transparent;shadow: none; z-index: 1005;"
  >
    <template #reference>
      <slot />
    </template>
    <div class="w-60vw flex flex-col overflow-hidden border rounded-8px sm:w-15rem">
      <div class="h-18 w-full bg-[var(--el-color-primary)]" />
      <el-upload
        ref="avatatRef"
        class="avatar-uploader -mt-2.5rem"
        :action="`${BaseUrl}/user`"
        :headers="{ Authorization: user.token }"
        method="PUT"
        name="cover"
        :limit="1"
        accept="image/*"
        drag
        :multiple="false"
        :auto-upload="true"
        :show-file-list="false"
        list-type="picture"
        :before-upload="beforeUpload"
        :on-success="onUpdateSuccess"
      >
        <CardNuxtImg
          v-if="user?.userInfo?.imgUrl"
          :default-src="user.userInfo.imgUrl"
          class="h-full w-full overflow-hidden rounded-full"
        >
          <template #error>
            <div h-full w-full flex-row-c-c text-lg>
              {{ user?.userInfo?.loginName?.[0] || "未设置" }}
            </div>
          </template>
        </CardNuxtImg>
        <ElIconPlus
          v-else
          size="1.5rem"
        />
      </el-upload>
      <h4 mt-3 truncate pb-3 text-center text-1rem font-500 border-default-b>
        {{ user.userInfo.loginName || "未填写" }}
      </h4>
      <div class="group mt-3 truncate px-4 font-500">
        登录账号：<BtnCopyText :text="user.userInfo.loginName || '-'" text-small />
        <span class="float-right op-0 btn-info-text text-mini group-hover:op-100" @click="editFiled = 'loginName'">修改</span>
      </div>
      <div class="group mt-3 truncate px-4 font-500">
        真实姓名：<span text-small>{{ user.userInfo.trueName || "-" }}</span>
        <span class="float-right op-0 btn-info-text text-mini group-hover:op-100" @click="editFiled = 'trueName'">修改</span>
      </div>
      <div class="group mt-3 truncate px-4 font-500">
        联系电话：<span text-small>{{ user.userInfo.telephone ? user.userInfo.telephone.replace(/(\d{3})\d{4}(\d{4})/, "$1****$2") : "-" }}</span>
        <span class="float-right op-0 btn-info-text text-mini group-hover:op-100" @click="editFiled = 'telephone'">修改</span>
      </div>
      <div class="mt-3 flex justify-between gap-2 p-3 border-default-t">
        <BtnElButton
          plain
          transition-icon
          icon-class="i-solar:logout-3-broken"
          class="w-full op-60" @click="user.exitLogin"
        >
          退出登录
        </BtnElButton>
        <BtnElButton
          transition-icon
          icon-class="i-solar:lock-outline"
          type="danger" class="w-full shadow"
          @click="user.showEditForm = true"
        >
          修改密码
        </BtnElButton>
      </div>
    </div>
  </el-popover>
</template>

<style lang="scss" scoped>
.avatar-uploader {

  :deep(.el-upload--picture) {
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 50%;
  }

  :deep(.el-upload-dragger) {
    border-radius: 50%;
    width: 5rem;
    height: 5rem;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: var(--el-bg-color);
    opacity: 0.95;
    padding: 0;
  }
}
</style>
