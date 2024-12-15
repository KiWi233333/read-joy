<script lang="ts" setup>
import { deleteOssFile, getOssErrorCode, getUploadToken, UploadFileType, uploadOssFile } from "@/api/res";
import { useColorMode } from "@vueuse/core";
import { MdEditor, MdPreview } from "md-editor-v3";
import { ResultStatus } from "~/composables/api/types/result";
import { useUserStore } from "~/composables/sotre/useUserStore";
import "md-editor-v3/lib/style.css";
import "md-editor-v3/lib/preview.css";


const {
  modelValue = "",
  isUploadImage = false,
  autoSave = false,
  disabled = false,
  placeholder = "请输入内容",
  previewOnly = false,
} = defineProps<{
  modelValue?: string
  pathList?: string[]
  isUploadImage?: boolean
  autoSave?: boolean
  disabled?: boolean
  previewOnly?: boolean
  placeholder?: string
}>();
const emit = defineEmits(["update:modelValue", "update:pathList", "submit"]);
const user = useUserStore();
const rawPathList = ref<string[]>([]);

const preIamgePath = "http://localhost:9090/res/";
/**
 * 详情信息-图片上传
 */
function onUploadDescImage(insertImage: (obj: { url: string; desc: string; width: string }) => void, files: File[]) {
  files.forEach(async (p) => {
    if (!p)
      return;
    // 1、获取凭证
    const res = await getUploadToken(UploadFileType.IMAGE, user.getToken);
    // 2、上传
    if (res.data.code === ResultStatus.SUCCESS) {
      // 文件上传
      // 3、插入markdown
      insertImage({
        url: `${preIamgePath}${res.data.data.key}`,
        desc: p.name,
        width: "100%",
      });
      // 添加上传文件
      rawPathList.value.push(res.data.data.key);

      // 自动保存
      if (autoSave)
        onSave();
      emit("update:pathList", rawPathList.value);
    }
    else {
      ElMessage.warning("网络错误，请稍后再试！");
    }
  });
}
// // 封装上传处理
// function qiniuUpload(file: File, key: string, token: string, callback: () => void) {
//   const observable = uploadOssFile(file, key, token);
//   observable.subscribe({
//     next() {
//     },
//     error(err) {
//       err = err as any;
//       if (err?.code)
//         ElMessage.error(getOssErrorCode(err?.code));

//       else ElMessage.error("上传失败，稍后再试！");
//     },
//     complete() {
//       callback && callback();
//     },
//   });
// }
// 加载
const isDescLoading = ref(false);
const valueModel = computed({
  get() {
    return modelValue;
  },
  set(val) {
    emit("update:modelValue", val);
  },
});


/**
 * 文件删除
 */
function onClickDescImage(images: string, currentIndex: number) {
  if (isDescLoading.value)
    return;

  if (images[currentIndex]) {
    ElMessageBox.confirm("是否删除该图片？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
      center: true,
      callback: async (res: string) => {
        if (res === "confirm") {
          const res = await deleteOssFile(images[currentIndex]?.replace(preIamgePath, ""), user.getToken);
          isDescLoading.value = true;
          if (res.data.code === ResultStatus.SUCCESS) {
            const regex = new RegExp(`!\\[.*?\\]\\((${images[currentIndex]})\\){{{width="100%"}}}`);
            // 删除输入框插入的图片
            rawPathList.value.splice(rawPathList.value.findIndex(p => p === images[currentIndex]), 1);
            emit("update:modelValue", modelValue.replace(regex, ""));
            emit("update:pathList", rawPathList.value);
            ElNotification.success({
              title: "删除提示",
              message: "删除成功！",
            });
          }
          else { ElMessage.error(res.data.message || "删除失败，请稍后再试!"); }
          setTimeout(() => {
            // false
            isDescLoading.value = false;
            // 自动保存
            if (autoSave)
              onSave();
          }, 300);
        }
      },
    });
  }
}

/**
 * 保存
 */
function onSave() {
  if (isDescLoading.value)
    return;

  isDescLoading.value = true;
  emit("submit", modelValue);
  setTimeout(() => {
    isDescLoading.value = false;
  }, 300);
}
const colorMode = useColorMode();
defineExpose({
  pathList: () => rawPathList.value,
});
</script>

<template>
  <div>
    <MdPreview
      v-if="previewOnly"
      v-bind="$props as any"
      v-model="valueModel"
      language="zh-CN"
      editor-id="editor-pre-md"
      show-code-row-number
      code-theme="a11y"
      preview-theme="smart-blue"
      :code-foldable="false"
      :theme="colorMode === 'dark' ? 'dark' : 'light'"
    />

    <MdEditor
      v-else
      v-model="valueModel"
      v-bind="$attrs"
      v-loading="isDescLoading"
      :disabled="disabled"
      language="zh-CN"
      :toolbars="[
        'bold',
        'underline',
        'italic',
        'strikeThrough',
        'title',
        'sub',
        'sup',
        'quote',
        'unorderedList',
        'orderedList',
        'codeRow',
        'code',
        'link',
        'image',
        'table',
        'revoke',
        'next',
        'save',
        'pageFullscreen',
        'fullscreen',
        'preview',
      ]"
      editor-id="editor-md"
      show-code-row-number
      code-theme="a11y"
      preview-theme="smart-blue"
      :code-foldable="false"
      :theme="colorMode === 'dark' ? 'dark' : 'light'"
      @on-upload-img="(files, callback) => onUploadDescImage((obj) => callback([obj.url]), files)"
    />
  </div>
</template>

<style scoped lang="scss">
:deep(.md-editor) {
  --md-bk-color: var(--el-bg-color) !important;
  // --md-bk-color-outstand: #111;
  // --md-bk-hover-color: #1b1a1a;
  // --md-border-color: #2d2d2d;
  // --md-border-hover-color: #636262;
  // --md-border-active-color: #777;
  // --md-modal-mask: #00000073;
  // --md-modal-shadow: 0px 6px 24px 2px #00000066;
  border-radius: 6px;
}
</style>
