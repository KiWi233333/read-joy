<script setup lang="ts">
import { useUserStore } from "~/composables/sotre/useUserStore";
import { AiAppId, AiBaseUrl } from "~/composables/utils/useBaseUrl";
import { appName } from "~/constants";
import { type ChatMessageVO, MessageType, WsStatusEnum } from "./Chat/chat";

const isShow = ref(false);

const INIT_MSG: ChatMessageVO = {
  fromUser: {
    userId: "1234567890",
    avatar: "/maskable-icon.png",
    nickName: "悦读Zero",
  },
  message: {
    id: 1,
    sendTime: `${new Date().getTime()}`,
    content: `你好！欢迎光临${appName}，有什么可以帮您的吗？`,
    type: MessageType.AI_CHAT,
  },
};

const user = useUserStore();
const form = ref({
  role: "user",
  content: "",
});
const status = ref<WsStatusEnum>(WsStatusEnum.CLOSE);

const body = ref({
  ws: null as WebSocket | null,
});

const dto = ref({
  header: {
    app_id: AiAppId,
    uid: String(user.userInfo.id),
  },
  parameter: {
    chat: {
      domain: "general",
      temperature: 0.8,
      top_k: 4,
      max_tokens: 2048,
    },
  },
  payload: {
    message: {
      text: [{
        role: "user",
        content: "",
      }],
    },
  },
});
// 是否在返回数据
const isChat = ref(false);


const formRef = ref();
function onSubmit() {
  if (status.value === WsStatusEnum.OPEN)
    return;

  formRef.value?.validate((action: boolean) => {
    if (!action)
      return;
    if (!user.userInfo.id || !user.isLogin) {
      user.showLoginForm = true;
      ElMessage.error("请先登录！");
      return;
    }
    isChat.value = true;
    senMsg(form.value.content, user.userInfo.id);
    scrollBottom();
    form.value.content = "";
  });
}
// 消息列表
const msgList = useLocalStorage<ChatMessageVO[]>(`ai_chat_history_${user.userInfo.id}`, [INIT_MSG]);


// dom
const scollRef = ref();
const inputRef = ref();

/**
 * 发送消息
 * @param msg 消息
 * @param id 用户id
 */
function senMsg(msg: string, id: number) {
  if (body.value.ws && body.value.ws.OPEN === 1)
    return;

  // 发起请求
  body.value.ws = new WebSocket(AiBaseUrl);
  status.value = WsStatusEnum.OPEN;
  body.value.ws.onopen = (e) => {
    // 截取最后10条消息
    const last10 = msgList.value.slice(-10).map(item => ({ content: item.message.content as string, role: String(item.fromUser.userId) === String(user.userInfo.id) ? "user" : "assistant" }));
    // 用户
    last10.push({
      content: msg,
      role: "user",
    });
    // 追加到消息列表中
    msgList.value.push({
      fromUser: {
        userId: String(user.userInfo.id)!,
        avatar: user.userInfo.imgUrl,
        nickName: user.userInfo?.loginName || user.userInfo?.trueName || "游客",
      },
      message: {
        id: Date.now(),
        sendTime: new Date().toDateString(),
        content: msg,
        type: MessageType.TEXT,
      },
    });
    // 构建请求体
    dto.value.payload.message.text = last10;
    dto.value.header.uid = String(id)!;
    body.value.ws?.send(JSON.stringify(dto.value));
    status.value = WsStatusEnum.OPEN;
  };
  body.value.ws.onclose = () => {
    status.value = WsStatusEnum.SAFE_CLOSE;
    body.value.ws = null;
  };

  body.value.ws.onerror = () => {
    status.value = WsStatusEnum.CLOSE;
    body.value.ws = null;
  };
  body.value.ws.onmessage = (e) => {
    const data = JSON.parse(e.data);
    if (data) {
      status.value = data.header.code as WsStatusEnum;
      const text = data?.payload?.choices?.text || [];
      text.value = "";
      text.forEach((p: any) => {
        if (p && p.role === "assistant")
          text.value += p.content;
      });
      const theMsg = msgList.value.find(p => p.message.id === data?.header?.sid);
      if (theMsg) {
        theMsg.message.content += text.value;
        return;
      }
      msgList.value.push({
        fromUser: {
          userId: "1234567890",
          avatar: "/maskable-icon.png",
          nickName: "悦读Zero",
        },
        message: {
          id: data.header.sid,
          sendTime: new Date().toDateString(),
          content: text.value,
          type: MessageType.AI_CHAT,
        },
      });
      ;
    }
    status.value = WsStatusEnum.CLOSE;
    nextTick(() => {
      scrollBottom();
    });
  };
}

watch(status, (newVal, oldVal) => {
  if (newVal === WsStatusEnum.OPEN) {
    scrollBottom();
    isChat.value = newVal === WsStatusEnum.OPEN;
  }
  else {
    inputRef.value?.focus();
    isChat.value = false;
    scrollBottom();
    body.value.ws = null;
  }
});

// 滚动到底部
function scrollBottom() {
  if (scollRef.value?.wrapRef?.scrollTo) {
    scollRef.value?.wrapRef?.scrollTo({
      top: scollRef?.value?.wrapRef?.scrollHeight + 20 || 0,
      behavior: "smooth",
    });
  }
  else {
    scollRef.value?.setScrollTop(scollRef?.value?.wrapRef?.scrollHeight + 20 || 0);
  }
}

function clearHistory() {
  msgList.value = [
    INIT_MSG,
  ];
}

// 初始化滚动到底部
onMounted(() => {
  nextTick(scrollBottom);
});
</script>

<template>
  <div>
    <ClientOnly>
      <el-popover
        v-if="user.isLogin"
        placement="top"
        width="fit-content"
        class="shadow-lg"
        :teleported="true"
        popper-class="popover"
        transition="popper-fade-up"
        trigger="click"
        append-to="body"
        :hide-after="0"
        @update:visible="(val) => isShow = val"
        @keyup.esc="isShow = false"
      >
        <template #reference>
          <!-- v-window-lock="isShow" -->
          <div
            :title="`${appName}助手`"
            data-fade
            class="shadow-bg h-12 w-12 flex-row-c-c rounded-1/2 bg-[var(--el-color-primary)] shadow border-default-hover"
          >
            <i class="i-solar:ghost-bold bg-light p-2" />
          </div>
        </template>
        <template #default>
          <section class="w-90vw text-1rem md:w-480px">
            <!-- header -->
            <p class="mb-2 text-[var(--el-color-primary)] font-600 tracking-0.2em">
              <i class="i-solar:ghost-bold mr-2 p-3" />
              {{ appName }}助手
            </p>
            <!-- 内容 -->
            <el-scrollbar
              v-if="isShow || msgList.length"
              ref="scollRef"
              view-class="p-2 md:p-4 h-58vh md:h-480px"
              wrap-class="shadow-inset shadow-sm "
              class="bg-light border-default card-default dark:bg-dark-9"
            >
              <div v-auto-animate relative flex flex-col>
                <!-- 消息适配器 -->
                <MenuChatMain
                  v-for="(msg, i) in msgList" :id="`chat-msg-${msg.message.id}`" :key="msg.message.id" :index="i"
                  :data="msg"
                />
              </div>
            </el-scrollbar>
            <el-form
              ref="formRef" v-auth :model="form" :disabled="!user?.isLogin || isChat"
              class="sticky bottom-0 left-0 mt-2 flex items-center gap-3" @submit.prevent="onSubmit"
            >
              <CardElImage
                :default-src="user.userInfo.imgUrl"
                class="h-2.4rem w-2.4rem shrink-0 rounded-1/2 border-default"
                @click.stop="clearHistory"
              />
              <el-form-item
                prop="content" class="w-full" :rules="[{
                  required: true,
                  message: '内容不能为空！',
                  trigger: 'change',
                }]"
              >
                <el-input
                  ref="inputRef"
                  v-model.lazy="form.content"
                  :disabled="!user.isLogin || isChat"
                  placeholder="快开始聊天吧 ✨"
                  class="content border-0 border-b-1px pt-4 border-default"
                />
              </el-form-item>
              <BtnElButton
                class="group ml-a" icon-class="i-solar:map-arrow-right-bold-duotone block mr-1" round
                :disabled="!user.isLogin || isChat"
                transition-icon
                type="info" @click=" onSubmit "
              >
                发送&nbsp;
              </BtnElButton>
            </el-form>
          </section>
        </template>
      </el-popover>
    </ClientOnly>
  </div>
</template>

<style lang="scss" scoped>
.content {
  :deep(.el-input__wrapper) {
    background-color: transparent;
    border: 1px solid transparent;
    border-radius: 2rem;
    padding: 0 1em;
    box-shadow: none !important;

    &.is-focus,
    &:hover,
    &.is-error {
      box-shadow: transparent !important;
      border-bottom: 1px solid #88888811;
    }
  }
}
.shadow-bg {
  background-color: var(--el-color-primary);
  border-radius: 50%;
  position: relative;
  z-index: 1;
}

.shadow-bg::after {
  content: "";
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: -1;
  border-radius: 50%;
  animation: GreenDot 2s ease 1s infinite;
  background-color:  var(--el-color-primary);
}
@keyframes GreenDot {
  0% {
      transform: scale(0.5);
      opacity: 1;
  }
  30% {
      opacity: 1;
  }
  100% {
      transform: scale(1.4);
      opacity: 0;
  }
}
</style>
