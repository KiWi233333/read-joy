<script setup lang="ts">
import type { UploadProps } from "element-plus";
import { type AdminInfoVO, useUpdateAdminPwdApi } from "~/composables/api/admin/admin";
import { type Result, ResultStatus } from "~/composables/api/types/result";
import { useAdminStore } from "~/composables/sotre/useAdminStore";
import { BaseUrl } from "~/composables/utils/useBaseUrl";

const admin = useAdminStore();
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
  return false;
};

function reloadCache() {
  location.reload();
}
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
        drag
        :action="`${BaseUrl}/admin`"
        :headers="{ Authorization: admin.token }"
        method="PUT"
        name="cover"
        :limit="1"
        accept="image/*"
        :multiple="false"
        :auto-upload="true"
        :show-file-list="false"
        list-type="picture"
        disabled
        :before-upload="beforeUpload"
      >
        <div order-0 h-2.2rem h-full w-2.2rem w-full flex-row-c-c rounded-full text-lg btn-primary-border border-default bg-color-br>
          {{ admin?.adminInfo?.username?.[0] || "A" }}
        </div>
      </el-upload>
      <h4 mt-3 truncate pb-3 text-center text-1rem font-500 border-default-b>
        {{ admin.adminInfo.username || "未填写" }}
      </h4>
      <div class="mt-3 flex justify-between gap-2 p-3">
        <BtnElButton
          plain
          transition-icon
          icon-class="i-solar:logout-3-broken"
          class="w-full op-60" @click="admin.exitLogin"
        >
          退出登录
        </BtnElButton>
        <BtnElButton
          type="danger"
          transition-icon
          icon-class="i-solar:refresh-bold"
          class="w-full op-60" @click="reloadCache"
        >
          刷新缓存
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
