<script setup lang="ts">
import type { UploadProps } from "element-plus";
import type { UserInfoVO } from "~/composables/api/user";
import { type Result, ResultStatus } from "~/composables/api/types/result";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { BaseUrl, BaseUrlImg } from "~/composables/utils/useBaseUrl";

const user = useUserStore();
const isVisible = ref(false);

defineExpose({
  isVisible,
});
const showEditForm = ref(false);

// 头像
const avatatRef = ref();
const formData = new FormData();

/**
 * 上传之前验证类型
 */
const beforeUpload: UploadProps["beforeUpload"] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("头像需要小于2MB!");
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
</script>

<template>
  <el-popover
    placement="bottom"
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
        drag
        :action="`${BaseUrl}/user`"
        :headers="{ Authorization: user.token }"
        method="PUT"
        name="cover"
        :limit="1"
        accept="image/*"
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

      <h4 mt-2 truncate pb-2 text-center text-1rem font-500 border-default-b>
        {{ user.userInfo.loginName || "未填写" }}
      </h4>
      <div mt-2 truncate px-4 font-500>
        登录账号：{{ user.userInfo.loginName || "-" }}
      </div>
      <div mt-2 truncate px-4 font-500>
        真实姓名：{{ user.userInfo.trueName || "-" }}
      </div>
      <div mt-2 truncate px-4 font-500>
        电话：{{ user.userInfo.telephone || "-" }}
      </div>
      <div class="mt-2 flex justify-between gap-2 p-2 border-default-t">
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
          icon-class="i-solar:user-outline"
          type="primary" class="w-full shadow"
          @click="showEditForm = true"
        >
          修改信息
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
