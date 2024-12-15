<script lang="ts" setup>
import type { IPage } from "~/composables/api/types";
import { CommentStatusEnum, type CommentVO, getCommentPageByDTOApi, type SelectCommentPageDTO } from "~/composables/api/comment";

import { ResultStatus } from "~/composables/api/types/result";
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { formatDate } from "~/composables/utils/useUtils";

const {
  showLoad = true,
  showMoreText = true,
  limit = undefined,
  ssr = true,
  bookId = undefined,
  dto,
  debounce = 300,
  itemsClass = "relative grid gap-4 cols-1",
  itemClass,
} = defineProps<{
  bookId?: number
  dto?: Partial<SelectCommentPageDTO>
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

const setting = useSettingStore();
interface PageType extends Partial<IPage<CommentVO>> {
  page: number
}
// const [autoAnimateRef, enable] = useAutoAnimate();
const isLoading = ref<boolean>(false);
const pageInfo = ref <PageType> ({
  page: 0,
  size: limit || 20,
  current: undefined,
  total: undefined,
  pages: undefined,
  records: [] as CommentVO[],
});
const noMore = computed(() => !!(pageInfo.value.current !== undefined && pageInfo.value.pages !== undefined && pageInfo.value.current >= pageInfo.value.pages));
const user = useUserStore();
/**
 * 加载图书列表
 */
async function loadData(appendDTO?: Partial<SelectCommentPageDTO>) {
  if (isLoading.value || noMore.value || !bookId)
    return;
  if (!user.getToken && !user.showLoginForm) {
    user.showLoginForm = true;
    return;
  }
  if (limit !== undefined && pageInfo.value.records && +pageInfo?.value?.records.length >= limit) {
    isLoading.value = false;
    return;
  }
  isLoading.value = true;
  pageInfo.value.page++;
  const res = await getCommentPageByDTOApi(bookId, {
    ...dto,
    size: limit || dto?.size || 20,
    page: pageInfo.value.page,
    ...(appendDTO || {}),
  }, user.getToken);
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

async function reload(appendDTO?: Partial<SelectCommentPageDTO>) {
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

onDeactivated(() => {
  unWatch();
});

const BadgeTypeMap: Record<CommentStatusEnum, any> = {
  [CommentStatusEnum.PENDING]: "warning",
  [CommentStatusEnum.APPROVED]: "info",
  [CommentStatusEnum.REJECTED]: "error",
};

function addComment(vo: CommentVO) {
  if (vo.bookId === bookId) {
    if (!pageInfo.value.records) {
      reload(dto);
      return;
    }
    pageInfo.value.records.unshift(vo);
  }
}


defineExpose({
  pageInfo,
  noMore,
  reload,
  loadData,
  addComment,
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
      :class="itemsClass"
      v-bind="$attrs"
    >
      <slot v-for="item in pageInfo.records" name="item" :item="item">
        <div :key="item.id" class="flex gap-4 card-rounded-df p-2 sm:p-4 border-default-hover" :class="itemClass">
          <CardNuxtImg :default-src="item.commentatorAvatar" class="h-12 w-12 flex-shrink-0 rounded-full border-default card-default">
            <template #error>
              <div h-full w-full flex-row-c-c text-lg>
                {{ item?.commentatorName?.[0] || "未设置" }}
              </div>
            </template>
          </CardNuxtImg>
          <div flex-1>
            <div font-500>
              {{ item.commentatorName }}
              <span v-if="item.createTime" float-right flex-row-c-c text-right text-mini>
                <span>{{ formatDate(new Date(item.createTime.replace(/-/g, '/'))) }}</span>
                <el-badge v-if="item.commentator === user.userId" class="ml-2" :type="item.commentStatus ? BadgeTypeMap[item.commentStatus] : 'info'" is-dot>
                  {{ item.commentStatusText }}
                  {{ item.commentStatus === CommentStatusEnum.PENDING ? `（自己可见）` : '' }}
                </el-badge>
              </span>
            </div>
            <p text-small>
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
</style>
