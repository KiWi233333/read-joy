<script setup lang="ts">
import type { InsertCommentDTO, SelectCommentPageDTO } from "~/composables/api/comment";
import { addCommentApi, CommentSortOrder } from "~/composables/api/comment";
import { ResultStatus } from "~/composables/api/types/result";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { DATE_FORMAT, DATE_TIME_FORMAT } from "~/composables/utils/useUtils";

const {
  bookId,
} = defineProps<{
  bookId?: number
}>();
const user = useUserStore();
const form = ref<Partial<InsertCommentDTO>>({
  bookId,
  commentBody: "",
});
const isSending = ref(false);
const listCommentListRef = ref();
const formRef = ref();

function onSubmit() {
  if (isSending.value)
    return;
  formRef.value?.validate(async (action: boolean) => {
    if (!action)
      return;
    isSending.value = true;
    const res = await addCommentApi(form.value as InsertCommentDTO, user.getToken);
    if (res.code !== ResultStatus.SUCCESS) {
      return;
    }
    form.value.commentBody = "";
    isSending.value = false;
    if (listCommentListRef?.value?.addComment) {
      listCommentListRef.value.addComment(res.data);
    }
  });
}

const commentDTO = reactive<Partial<SelectCommentPageDTO>>({
  sortOrder: CommentSortOrder.DESC,
  size: 10,
  onlyMine: undefined,
  startDateTime: undefined,
  endDateTime: undefined,
});

const commentTimeRange = computed({
  get: () => {
    if (commentDTO.startDateTime && commentDTO.endDateTime) {
      return [commentDTO.startDateTime, commentDTO.endDateTime];
    }
    return [];
  },
  set: (val: string[] = []) => {
    if (val.length === 2) {
      commentDTO.startDateTime = val?.[0];
      commentDTO.endDateTime = val?.[1];
    }
    else if (val.length === 0 || !val) {
      commentDTO.startDateTime = undefined;
      commentDTO.endDateTime = undefined;
    }
  },
});
</script>

<template>
  <div class="relative">
    <el-form
      ref="formRef" :model="form" class="mb-2 w-full flex items-start gap-4 pb-4 !relative"
      :disabled="false"
      @submit.prevent="onSubmit"
    >
      <CardNuxtImg :default-src="user.userInfo.imgUrl" class="h-10 w-10 shrink-0 rounded-full border-default bg-color">
        <template #error>
          <div h-full w-full flex-row-c-c text-lg>
            {{ user?.userInfo?.loginName?.[0] || " " }}
          </div>
        </template>
      </CardNuxtImg>
      <el-form-item
        prop="commentBody"
        class="comment-form flex-row-c-c flex-1" style="margin: 0;" :rules="[{
          required: true,
          message: '评论内容不能为空！',
          trigger: 'blur',
        }, {
          min: 1,
          max: 500,
          message: '评论内容长度在 1 到 200 个字符之间！',
          trigger: 'blur',
        }]"
      >
        <el-input
          v-model.lazy.trim="form.commentBody"
          type="textarea"
          :rows="4"
          :maxlength="200"
          :autosize="false"
          :minlength="1"
          :disabled="isSending || !user.isLogin"
          placeholder="说点什么吧" @keydown.enter.prevent="onSubmit"
        />
      </el-form-item>
      <BtnElButton
        class="group bottom-20px right-1 z-1 !absolute"
        icon-class="i-solar:chat-dots-bold mr-1"
        style="padding: 0 1.2rem;"
        transition-icon
        size="small"
        type="primary"
        :disabled="!user.isLogin"
        @click="onSubmit()"
      >
        评论
      </BtnElButton>
    </el-form>
    <ListCommentList
      v-if="user.isLogin"
      ref="listCommentListRef"
      :book-id="bookId"
      :dto="commentDTO"
      :show-load="false"
      :show-more-text="false"
      :ssr="true"
      :animated="false"
    >
      <template #header="{ pageInfo }">
        <div class="mb-4 flex-row-bt-c text-sm">
          <span>评论（{{ pageInfo?.total || 0 }}）</span>
          <FormDatePicker
            v-model="commentTimeRange"
            size="sm"
            :format="DATE_TIME_FORMAT"
            :format-preview="DATE_FORMAT"
            :disabled="isSending"
            :btn-props="{
              class: 'text-left flex  w-14rem bg-transparent ',
            }"
            placeholder="选择时间范围"
            :clearable="true"
          />
        </div>
      </template>
    </ListCommentList>
    <li
      v-else
      data-fade style="--anima: latter-slice-blur-top;"
      class="flex items-center p-3 border-default card-default text-small"
    >
      暂无权限，请先<span text-info btn-info @click="user.showLoginForm = true">登录</span>
    </li>
  </div>
</template>

<style lang="scss" scoped>
.comment-form {
  :deep(.el-textarea__inner) {
    background-color: transparent;
  }
}
</style>
