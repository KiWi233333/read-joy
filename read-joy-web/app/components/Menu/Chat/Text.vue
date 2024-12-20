<script lang="ts" setup>
import { useUserStore } from "~/composables/sotre/useUserStore";

/**
 * 文本消息
 */
const props = defineProps<{
  data: any
  index: number
}>();

const { data } = toRefs(props);
const user = useUserStore();
const nowDate = Date.now();

function getTime(time: string) {
  return (nowDate - +time) > 24 * 3600
    ? useDateFormat(time, "YYYY-MM-DD HH:mm:ss").value.toString()
    : useDateFormat(time, "HH:mm:ss").value.toString()
  ;
}


// 具体
const body: any | undefined = props.data.message?.body || {};
</script>

<template>
  <div
    v-bind="$attrs"
    :label="data.roomId"
    class="msg"
    :class="{
      self: +data?.fromUser?.userId === user?.userInfo.id,
    }"
  >
    <!-- 头像 -->
    <CardElImage :default-src="data.fromUser.avatar" fit="cover" class="avatar h-2.4rem w-2.4rem flex-shrink-0 rounded-1/2 object-cover border-default" />
    <!-- 消息体 -->
    <div class="body">
      <!-- 昵称 -->
      <small class="nickname">
        {{ data.fromUser.nickName }}
      </small>
      <!-- 内容 -->
      <p class="msg-popper">
        {{ data.message.content }}
      </p>
    </div>
  </div>
  <p v-if="index % 8 === 0" w-full py-2 text-center text-0.8em op-80>
    {{ getTime(data.message.sendTime) }}
  </p>
</template>

<style lang="scss" scoped>
@use './msg.scss';
</style>
