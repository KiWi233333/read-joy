<script lang="ts" setup>
import { MenuChatAiMsg, MenuChatText } from "#components";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { MessageType } from "./chat";


/**
 * 消息适配器
 */
const props = defineProps<{
  data: any
  index: number
}>();
const map: MsgComType = {
  [MessageType.TEXT]: MenuChatText,
  [MessageType.AI_CHAT]: MenuChatAiMsg,
};
interface MsgComType {
  [property: number]: any
}

const user = useUserStore();
</script>

<template>
  <component
    :is="data.message?.type !== undefined ? map[data.message?.type] : MenuChatText "
    :index="index"
    :data="data" v-bind="$attrs"
  />
</template>

<style lang="scss" scoped>
@use './msg.scss';
</style>
