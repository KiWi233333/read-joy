<script lang="ts" setup>
import { MdPreview } from "md-editor-v3";

import { useUserStore } from "~/composables/sotre/useUserStore";
import "md-editor-v3/lib/preview.css";
/**
 * ai消息
 */
defineProps<{
  data: any
  index: number
}>();
const user = useUserStore();
</script>

<template>
  <div
    v-bind="$attrs"
    :label="data.roomId"
    class="msg"
    :class="{
      self: data?.fromUser?.userId === user?.userInfo.id,
    }"
  >
    <!-- 头像 -->
    <CardElImage :src="data.fromUser.avatar" fit="cover" class="avatar h-2.4rem w-2.4rem flex-shrink-0 rounded-1/2 object-cover border-default" />
    <!-- 消息体 -->
    <div class="body">
      <!-- 昵称 -->
      <small class="nickname">
        {{ data.fromUser.nickName }}
      </small>
      <!-- 内容 -->
      <template v-if="+data.fromUser.userId === user.userInfo.id">
        <p class="msg-popper mt-2">
          {{ data.message?.content }}
        </p>
      </template>
      <MdPreview
        v-else
        language="zh-CN"
        :editor-id="data.id"
        show-code-row-number
        :theme="$colorMode.value === 'dark' ? 'dark' : 'light'"
        preview-theme="smart-blue"
        code-theme="a11y"
        :code-foldable="false"
        class="msg-popper markdown mt-2 sm:max-w-40rem"
        :model-value="data.message?.content || ''"
      />
    </div>
  </div>
</template>

<style lang="scss" scoped>
@use './msg.scss';

.markdown {
  :deep(.md-editor-preview-wrapper){
    padding: 0;
  }
  :deep(.md-editor-preview)  {
    padding: 0;
    font-size: 0.9rem;
    font-family: $font-family;
    img {
      border-radius: 0.25rem;
      overflow: hidden;
      max-width: 12rem !important;
      max-height: 12rem !important;
    }
    ol,
    ul {
      padding-left: 1.5em;
      margin-top: .5em;
    }
    .md-editor-code {
      .md-editor-code-block {
         line-height: 1.6em;
        & ~ span[rn-wrapper] >span {
         line-height: 1.6em;
        }
      }
      --at-apply: 'overflow-hidden leading-2em card-default border-default hover:shadow transition-all mb-2';
      code {
        border-radius: 0 0 8px 8px;
      }
    }

  }
}
</style>
