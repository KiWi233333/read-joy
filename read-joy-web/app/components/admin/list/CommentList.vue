<script lang="ts" setup>
import type { IPage } from "~/composables/api/types";
import { type AdminCommentVO, type AdminSelectCommentPageDTO, useAdminCommentPageByDTOApi } from "~/composables/api/admin/comment";
import { CommentStatusEnum } from "~/composables/api/comment";
import { ResultStatus } from "~/composables/api/types/result";
import { useAdminStore } from "~/composables/sotre/useAdminStore";
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { formatDate } from "~/composables/utils/useUtils";

const {
  showLoad = true,
  showMoreText = true,
  limit = undefined,
  ssr = true,
  dto,
  animated = "auto",
  debounce = 300,
  itemsClass = "relative grid gap-4 cols-1",
  itemClass,
} = defineProps<{
  dto?: Partial<AdminSelectCommentPageDTO>
  autoStop?: boolean
  showLoad?: boolean
  showMoreText?: boolean
  ssr?: boolean
  animated?: "auto" | boolean
  limit?: number
  itemsClass?: string
  itemClass?: string
  debounce?: number
}>();

const [autoAnimateRef, enable] = useAutoAnimate({});

interface PageType extends Partial<IPage<AdminCommentVO>> {
  page: number
}
const isLoading = ref<boolean>(false);
const pageInfo = ref <PageType> ({
  page: 0,
  size: limit || 20,
  current: undefined,
  total: undefined,
  pages: undefined,
  records: [] as AdminCommentVO[],
});
const noMore = computed(() => !!(pageInfo.value.current !== undefined && pageInfo.value.pages !== undefined && pageInfo.value.current >= pageInfo.value.pages));
const admin = useAdminStore();
const setting = useSettingStore();
/**
 * 加载图书列表
 */
async function loadData(appendDTO?: Partial<AdminSelectCommentPageDTO>) {
  if (isLoading.value || noMore.value)
    return;
  if (limit !== undefined && pageInfo.value.records && +pageInfo?.value?.records.length >= limit) {
    isLoading.value = false;
    return;
  }
  isLoading.value = true;
  pageInfo.value.page++;
  const res = await useAdminCommentPageByDTOApi({
    ...dto,
    size: limit || dto?.size || 20,
    page: pageInfo.value.page,
    ...(appendDTO || {}),
  }, admin.getToken);
  if (res.code === ResultStatus.SUCCESS) {
    pageInfo.value = {
      page: res.data.current,
      current: res.data.current,
      size: res.data.size,
      total: res.data.total,
      pages: res.data.pages,
      records: [...(pageInfo.value.records || []), ...res.data.records],
    };
  }
  setTimeout(() => {
    isLoading.value = false;
  }, debounce);
}

async function reload(appendDTO?: Partial<AdminSelectCommentPageDTO>) {
  pageInfo.value = {
    page: 0,
    current: undefined,
    size: limit || 20,
    total: undefined,
    pages: undefined,
    records: [],
  };
  await loadData(appendDTO);
}


const unWatch = watchDebounced(
  () => dto,
  () => {
    reload();
  },
  {
    debounce,
    deep: true,
  },
);

const BadgeTypeMap: Record<CommentStatusEnum, any> = {
  [CommentStatusEnum.PENDING]: "warning",
  [CommentStatusEnum.APPROVED]: "info",
  [CommentStatusEnum.REJECTED]: "danger",
};


watch(() => animated, (val) => {
  if (val === "auto" || val) {
    enable(!setting.isCloseAllTransition);
  }
  else {
    enable(false);
  }
});
onMounted(() => {
  enable(animated === false ? false : !setting.isCloseAllTransition);
});
onActivated(() => {
  enable(animated === false ? false : !setting.isCloseAllTransition);
});
onDeactivated(() => {
  unWatch();
  unWatch();
});

defineExpose({
  pageInfo,
  noMore,
  reload,
  loadData,
});
// 初始化
await reload();
</script>

<template>
  <slot name="header" :page-info="pageInfo" />
  <ListAutoIncre
    :immediate="!ssr"
    :loading-parent-class="limit !== undefined ? 'hidden' : undefined"
    :auto-stop="limit !== undefined || autoStop"
    :no-more="noMore"
    :offset="limit !== undefined ? 0 : 20"
    :loading="isLoading"
    :loading-class="showLoad ? 'op-100 load-chaotic-orbit' : 'op-0'"
    @load="loadData()"
  >
    <div
      ref="autoAnimateRef"
      :class="itemsClass"
      v-bind="$attrs"
    >
      <slot v-for="item in pageInfo.records" name="item" :item="item">
        <div :key="item.id" class="flex gap-4 py-4 border-default-b" :class="itemClass">
          <CardNuxtImg :default-src="item.commentatorAvatar" class="h-10 w-10 flex-shrink-0 rounded-full border-default card-default">
            <template #error>
              <div h-full w-full flex-row-c-c text-lg>
                {{ item?.commentatorName?.[0] || " " }}
              </div>
            </template>
          </CardNuxtImg>

          <div flex-1>
            <div font-500>
              <div>
                {{ item.commentatorName }}
                <span v-if="item.createTime" class="float-right ml-a text-small">{{ formatDate(new Date(item.createTime.replace(/-/g, '/'))) }}</span>
              </div>
              <span v-if="item.createTime" float-right mt-2 flex-row-c-c text-right text-mini>
                <el-badge class="ml-2" :type="item.commentStatus ? BadgeTypeMap[item.commentStatus] : 'info'" is-dot>
                  {{ item.commentStatusText }}
                  {{ item.commentStatus === CommentStatusEnum.PENDING ? `（自己可见）` : '' }}
                </el-badge>
              </span>
            </div>
            <p class="msg-popper text-overflow-3">
              {{ item.commentBody }}
            </p>
          </div>
        </div>
      </slot>
    </div>
    <template #done>
      <div
        v-if="showMoreText && noMore && pageInfo.total !== 0"
        class="w-full pb-6 pt-4 text-center text-small"
      >
        暂无更多评论
      </div>
    </template>
    <div v-if="pageInfo.total === 0">
      <slot name="empty">
        <div class="mt-4 flex flex-col items-center justify-center gap-2">
          <span class="text-color-secondary text-small">没有相关评论</span>
        </div>
      </slot>
    </div>
  </ListAutoIncre>
</template>

<style lang="scss" scoped>
.msg-popper {
  --at-apply: "w-fit mt-2 max-w-20em md:max-w-full p-2 px-3 leading-1.2em bg-color-second rounded-1.5";
  box-shadow:
    rgba(0, 0, 0, 0.1) 0px 1px 3px 0px,
    rgba(0, 0, 0, 0.06) 0px 1px 2px 0px;
  border-radius: 4px 1rem 1rem 1rem;
}
</style>
