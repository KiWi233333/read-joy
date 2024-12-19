<script lang="ts" setup>
import type { pl } from "element-plus/es/locale/index.mjs";
import type { IPage } from "~/composables/api/types";

import { useDateFormat } from "@vueuse/core";
import { type AdminCommentVO, type AdminSelectCommentPageDTO, useAdminBatchDeleteCommentApi, useAdminCommentPageByDTOApi, useAdminDeleteCommentApi, useAdminUpdateCommentStatusApi } from "~/composables/api/admin/comment";
import { CommentStatusEnum } from "~/composables/api/comment";
import { BoolEnum, DefaultOrderSort, ResultStatus } from "~/composables/api/types/result";
import { useAdminStore } from "~/composables/sotre/useAdminStore";
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { compareObjects, DATE_FORMAT, DATE_SELECTOR_OPTIONS, DATE_TIME_FORMAT, randomISBN } from "~/composables/utils/useUtils";
import { appName } from "~/constants";

const setting = useSettingStore();

const admin = useAdminStore();
useSeoMeta({
  title: `评论管理 - ${appName}`,
});

// 数据
const pageInfo = ref<IPage<AdminCommentVO>>({
  records: [],
  total: 0,
  pages: 0,
  size: 0,
  current: 0,
});
// 参数
const page = ref<number>(1);
const size = ref<number>(10);
// 数据
const updateTime = ref<string>();
const formRef = ref();

// 功能 （展开）
const isEdit = ref<boolean>(false); // 是否编辑
const isAdd = ref<boolean>(false); // 是否添加
const isShowForm = ref<boolean>(false);
const isLoading = ref<boolean>(false);
const theRowInfo = ref<Partial<AdminCommentVO>>({});

// 多选
const selectList = ref<AdminCommentVO[]>([]);
function onSelectChange(list: AdminCommentVO[]) {
  // 添加
  selectList.value = list?.length ? list : [];
}

// 表单参数
const form = ref<Partial<AdminCommentVO>>({
  id: undefined,
  bookId: undefined,
  commentator: undefined,
  commentatorAvatar: undefined,
  commentatorName: undefined,
  commentBody: undefined,
  commentStatus: undefined,
  commentStatusText: undefined,
  createTime: undefined,
  isDeleted: undefined,
});
// 查询参数
const isShowSearch = ref<boolean>(true);
const searchDTO = ref<AdminSelectCommentPageDTO>({
  page: 1,
  size: 10,
  bookId: undefined,
  commentator: undefined,
  endDateTime: undefined,
  isDeleted: undefined,
  keyword: undefined,
  sortOrder: undefined,
  startDateTime: undefined,
  status: undefined,
});
// 监听参数变化
watch([
  () => searchDTO.value.status,
  () => searchDTO.value.bookId,
  () => searchDTO.value.sortOrder,
  () => searchDTO.value.startDateTime && searchDTO.value.endDateTime,
  () => searchDTO.value.isDeleted,
  size,
  page,

], (val) => {
  loadData();
}, {
  deep: true,
  immediate: true,
});

// 日期筛选
const dateRangeModel = computed({
  get() {
    if (searchDTO.value.startDateTime && searchDTO.value.endDateTime) {
      return [searchDTO.value.startDateTime, searchDTO.value.endDateTime];
    }
    else {
      return undefined;
    }
  },
  set(val: string[] | undefined) {
    if (!val) {
      searchDTO.value.startDateTime = undefined;
      searchDTO.value.endDateTime = undefined;
    }
    else {
      searchDTO.value.startDateTime = val[0];
      searchDTO.value.endDateTime = val[1];
    }
  },
});
// 排序
const sortGroupModel = computed({
  get() {
    if (searchDTO.value.sortOrder !== undefined) {
      return "sortOrder";
    }
    else {
      return undefined;
    }
  },
  set(val: "sortOrder" | undefined) {
    if (!val) {
      searchDTO.value.sortOrder = undefined;
    }
    else {
      searchDTO.value.sortOrder = val !== sortGroupModel.value
        ? DefaultOrderSort.DESC // 切换选项后默认降序
        : (searchDTO.value.sortOrder === DefaultOrderSort.DESC ? DefaultOrderSort.ASC : DefaultOrderSort.DESC);
    }
  },
});
// @unocss-inlude
const sortOptions = [
  {
    label: "创建排序",
    value: "sortOrder",
    icon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    ascIcon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    descIcon: "i-solar:sort-from-top-to-bottom-bold-duotone",
    descLabel: "创建降序",
    ascLabel: "创建升序",
  },
];
/**
 * 加载数据
 */
async function loadData() {
  if (isLoading.value)
    return;
  if (pageInfo.value.records.length)
    isLoading.value = true;
  // 请求
  const res = await useAdminCommentPageByDTOApi({
    ...searchDTO.value,
    page: page.value,
    size: size.value,
  }, admin.getToken);
  if (res.code === ResultStatus.SUCCESS)
    pageInfo.value = res.data;
  updateTime.value = useDateFormat(new Date(), "YYYY-MM-DD HH:mm:ss").value;
  isLoading.value = false;
}


/**
 * 评论操作
 * @param type 类型
 */
async function onSubmit(type: "updateStatus" | "delete" | "batchDel", data: any) {
  const tip = {
    class: "el-button--primary",
    title: "操作",
    btnText: "确认",
  };
  if (type === "updateStatus") {
    tip.class = data?.commentStatus === CommentStatusEnum.APPROVED ? "el-button--primary" : data?.commentStatus === CommentStatusEnum.REJECTED ? "el-button--danger" : "";
    tip.title = "审核评论";
    tip.btnText = data?.commentStatus === CommentStatusEnum.APPROVED ? "确认审核" : data?.commentStatus === CommentStatusEnum.REJECTED ? "确认驳回" : "";
  }
  else if (type === "delete") {
    tip.class = "el-button--danger";
    tip.title = "删除该评论";
  }
  else if (type === "batchDel") {
    tip.class = "el-button--danger";
    tip.title = "批量删除评论";
  }
  else {
    return;
  }
  if (isLoading.value)
    return;
  // 二步确认
  ElMessageBox.confirm(`是否确认${tip.title}？`, "操作提醒", {
    confirmButtonText: tip.btnText || "确认",
    confirmButtonClass: tip.class,
    cancelButtonText: "取消",
    type: "warning",
    center: true,
    async callback(a: string) {
      if (a === "confirm") {
        isLoading.value = true;
        let res; // 请求结果
        try {
          if (type === "updateStatus") {
            if (!data || !data.id)
              return;
            res = await useAdminUpdateCommentStatusApi(data.id, data.commentStatus, admin.getToken);
          }
          else if (type === "delete") {
            if (typeof data !== "number") {
              ElMessage.warning("参数错误！");
              return;
            }
            res = await useAdminDeleteCommentApi(data!, admin.getToken);
          }
          else if (type === "batchDel") {
            if (!data || !Array.isArray(data))
              return;
            res = await useAdminBatchDeleteCommentApi(data as number[], admin.getToken);
          }
          else {
            return;
          }
        }
        catch (error) {
          console.error(error);
          isLoading.value = false;
          ElMessage.error("操作失败，请稍后再试！");
          return;
        }
        isLoading.value = false;
        if (!res)
          return;
        if (res.code === ResultStatus.SUCCESS) {
          isShowForm.value = false;
          loadData();
          clearForm();
          ElNotification({
            title: `${tip.title}提示`,
            message: res.message || `${tip.title}成功！`,
            type: "success",
            duration: 2000,
          });
        }
        else {
          ElMessage.closeAll("error");
          ElMessage.error(res.message || `${tip.title}失败，请稍后再试！`);
        }
      }
    },
  });
}

// 清除表单
function clearForm(call?: () => void) {
  form.value = {
    id: undefined,
    bookId: undefined,
    commentator: undefined,
    commentatorAvatar: undefined,
    commentatorName: undefined,
    commentBody: undefined,
    commentStatus: undefined,
    commentStatusText: undefined,
    createTime: undefined,
    isDeleted: undefined,
  };
  call && call();
}

/**
 * 显示弹窗
 * @param row table选中的行
 */
async function onShowInfoDetail(row?: AdminCommentVO, call?: () => any) {
  if (row?.id) {
    // 预览
    theRowInfo.value = row;
    form.value = {
      ...row,
    };
    isShowForm.value = true;
  }
  else {
    clearForm();
  }
  call && call();
  isShowForm.value = true;
}

/**
 * 重置筛选
 */
function resetSearchOption() {
  page.value = 1;
  searchDTO.value = {
    page: 1,
    size: 10,
    keyword: undefined,
  };
  loadData();
}
const searchFiledSize = "default";
// const searchFiledSize = computed(() => {
//   return setting.isMobileSize ? "small" : "default";
// });
</script>

<template>
  <div>
    <AdminTableDefaultTable
      :header="{ title: `评论管理`, updateTime }"
    >
      <!-- 菜单 -->
      <template #menu>
        <div
          mr-4
          :class="!isShowSearch ? 'scale-x-90  opacity-0' : ' scale-x-100 opacity-100'"
          class="top-menu flex-shrink-0 transform-origin-left-center overflow-hidden transition-300 transition-all"
          flex="~ gap-2 items-center"
        >
          <small sticky left-0 flex-shrink-0 opacity-70>筛选：</small>
          <!-- 评论名 -->
          <el-input v-model.lazy.trim="searchDTO.keyword" :size="searchFiledSize" class="w-10rem sm:w-12rem" :prefix-icon="ElIconSearch" placeholder="关键字搜索" @keyup.enter="loadData" />
          <!-- 图书筛选 -->
          <div class="w-10rem sm:w-12rem">
            <LazyAdminInputBookItemSelect v-model:book-id="searchDTO.bookId" :size="searchFiledSize" />
          </div>
          <!-- 状态筛选 -->
          <div class="w-6rem sm:w-8rem">
            <el-select v-model="searchDTO.status" :size="searchFiledSize" placeholder="状态筛选" clearable>
              <el-option
                v-for="item in [{ label: '全部', value: '' }, { label: '待审核', value: CommentStatusEnum.PENDING }, { label: '已通过', value: CommentStatusEnum.APPROVED }, { label: '已驳回', value: CommentStatusEnum.REJECTED }]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </div>
          <!-- 回收站 -->
          <!-- <el-checkbox-button v-model="searchDTO.isDeleted" :size="searchFiledSize" :true-value="BoolEnum.TRUE" :false-value="BoolEnum.FALSE" label="回收站" /> -->
          <!-- 时间筛选 -->
          <FormDatePicker
            v-model="dateRangeModel"
            :format="DATE_TIME_FORMAT"
            :preload-dates="DATE_SELECTOR_OPTIONS"
            :popover-props="{
              class: 'w-12rem',
            }"
            :size="searchFiledSize"
            placeholder="日期筛选"
            :btn-props="{
              class: '!block bg-color  w-15rem hover:bg-color',
              style: 'display: block;',
            }"
          />
          <!-- 排序 -->
          <el-segmented
            :model-value="sortGroupModel"
            :size="searchFiledSize"
            :options="sortOptions"
            class="border-default card-default !bg-color"
          >
            <template #default="{ item }">
              <div class="flex gap-2" @click="sortGroupModel = item.value">
                <i p-2 :class="sortGroupModel === item.value ? (searchDTO.sortOrder === DefaultOrderSort.DESC ? item.descIcon : item.ascIcon) : item.icon" />
                <div>{{ sortGroupModel === item.value ? (searchDTO.sortOrder === DefaultOrderSort.DESC ? item.descLabel : item.ascLabel) : item.label }}</div>
              </div>
            </template>
          </el-segmented>
          <el-button
            plain
            type="danger" :size="searchFiledSize"
            @click="resetSearchOption"
          >
            重置
          </el-button>
        </div>
        <div ml-a flex>
          <!-- 按钮 -->
          <AdminTableDefaultTableBtns
            :menu="[
              { show: true,
                title: '刷新',
                methods: loadData,
                btnProps: {
                  size: searchFiledSize,
                },
                icon: 'i-solar:refresh-outline',
                type: 'info' },
              {
                show: selectList.length > 0,
                title: '批量',
                methods: () => onSubmit('batchDel', selectList.map((p: AdminCommentVO) => p.id) as number[] || []),
                icon: 'i-solar:trash-bin-minimalistic-broken ',
                class: 'animate-[fade-in_0.15s_ease-in-out]',
                type: 'danger',
                btnProps: {
                  size: searchFiledSize,
                  disabled: selectList.length === 0,
                },
              },
              { show: true,
                title: isShowSearch ? '收起' : '筛选',
                methods: () => isShowSearch = !isShowSearch,
                btnProps: {
                  size: searchFiledSize,

                },
                icon: 'i-solar:sort-from-top-to-bottom-line-duotone ',
                type: 'default' },
            ]"
          />
        </div>
      </template>
      <template #default>
        <div class="overflow-hidden border-default card-default">
          <el-table
            v-loading="isLoading"
            :header-cell-style="{
              padding: '1rem 0',
              fontSize: '1em',
            }"
            row-class-name="group h-4.16rem"
            :disabled="isEdit"
            class-name="w-full"
            :data="pageInfo.records"
            stripe
            select-on-indeterminate
            empty-text="暂无数据"
            row-key="id"
            height="75vh"
            @row-click="(row: AdminCommentVO) => {
              theRowInfo = row;
              form = { ...row }
            }"
            @selection-change="onSelectChange"
          >
            <!-- 选择 -->
            <el-table-column
              fixed
              type="selection"
            />
            <!-- 评论ID -->
            <el-table-column
              label="评论ID"
              property="id"
              column-key="id"
            >
              <template #default="{ row }">
                <BtnCopyText :text="row.id" />
              </template>
            </el-table-column>
            <!-- 评论人 -->
            <el-table-column
              column-key="commentatorName"
              prop="commentatorName"
              label="评论人"
              min-width="120%"
              class="font-bold"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <div class="flex items-center">
                  <CardNuxtImg
                    :default-src="row?.commentatorAvatar"
                    class="mr-2 inline-block h-8 w-8 overflow-hidden rounded-full text-center leading-8 border-default-hover"
                  >
                    <template #error="attr">
                      <div v-bind="attr">
                        {{ row?.commentatorName?.[0] || "" }}
                      </div>
                    </template>
                  </CardNuxtImg>
                  {{ row?.commentatorName || '-' }}
                </div>
              </template>
            </el-table-column>
            <!-- 评论图书 -->
            <el-table-column
              column-key="bookTitle"
              prop="bookTitle"
              label="评论图书"
              min-width="100%"
              class="font-bold"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{ row?.bookTitle || '-' }}
              </template>
            </el-table-column>
            <!-- 评论内容 -->
            <el-table-column
              column-key="commentBody"
              prop="commentBody"
              label="评论内容"
              min-width="200%"
              class="font-bold"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{ row?.commentBody || '-' }}
              </template>
            </el-table-column>
            <!-- 发布时间 -->
            <el-table-column
              column-key="createTime"
              prop="createTime"
              label="发布时间"
              min-width="120%"
              class="font-bold"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{ row?.createTime || '-' }}
              </template>
            </el-table-column>
            <!-- 是否删除 -->
            <el-table-column
              column-key="isDeleted"
              prop="isDeleted"
              label="是否删除"
              align="center"
            >
              <template #default="{ row }">
                <el-tag
                  v-if="row.isDeleted"
                  effect="dark" type="danger"
                >
                  已删除
                </el-tag>
                <span v-else>正常</span>
              </template>
            </el-table-column>
            <!-- 审核状态 -->
            <el-table-column
              column-key="commentStatusText"
              prop="commentStatusText"
              label="审核状态"
              align="center"
            >
              <template #default="{ row }">
                <el-tag :effect="row.commentStatus === CommentStatusEnum.APPROVED ? 'plain' : 'dark'" :type="row.commentStatus === CommentStatusEnum.APPROVED ? 'success' : (row.commentStatus === CommentStatusEnum.REJECTED ? 'warning' : 'danger')">
                  {{ row?.commentStatusText || '-' }}
                </el-tag>
              </template>
            </el-table-column>
            <!-- 动作+弹窗 -->
            <el-table-column
              fixed="right"
              label="操作"
              width="240%"
            >
              <template #default="{ row }">
                <div class="flex opacity-0 transition-200 group-hover:opacity-100">
                  <!-- 审核通过 -->
                  <BtnElButton
                    v-if="row.commentStatus === CommentStatusEnum.PENDING || row.commentStatus === CommentStatusEnum.REJECTED"
                    type="primary"
                    :plain="false"
                    style="padding: 0rem 0.6rem"
                    class="btns"
                    @click="onSubmit('updateStatus', { id: row.id, commentStatus: CommentStatusEnum.APPROVED })"
                  >
                    <i
                      i-solar:check-read-broken
                      p-2
                    />
                    <span w-0 overflow-hidden transition-200 transition-all class="btns-hover">&nbsp;通过</span>
                  </BtnElButton>
                  <!-- 驳回评论 -->
                  <BtnElButton
                    v-if="row.commentStatus === CommentStatusEnum.PENDING"
                    type="warning"
                    plain
                    style="padding: 0rem 0.6rem"
                    class="btns"
                    @click="onSubmit('updateStatus', { id: row.id, commentStatus: CommentStatusEnum.REJECTED })"
                  >
                    <i
                      i-solar:adhesive-plaster-broken
                      p-2
                    />
                    <span w-0 overflow-hidden transition-200 transition-all class="btns-hover">&nbsp;驳回</span>
                  </BtnElButton>

                  <!-- 删除评论 -->
                  <BtnElButton
                    type="danger"
                    :plain="false"
                    style="padding: 0rem 0.6rem"
                    class="btns"
                    @click="onSubmit('delete', row.id)"
                  >
                    <i
                      i-solar:trash-bin-trash-line-duotone
                      p-2
                    />
                    <span w-0 overflow-hidden transition-200 transition-all class="btns-hover">&nbsp;删除</span>
                  </BtnElButton>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </template>
      <template #footer>
        <footer class="group w-full flex-row-bt-c">
          <el-pagination
            v-model:current-page="page"
            background
            layout="prev, pager, next"
            :page-count="pageInfo.pages"
          />
          <div ml-a flex>
            <div mr-4 hidden w-10rem opacity-0 transition-200 transition-opacity sm:inline-block group-hover:opacity-100>
              <AdminPagesizeSelect v-model="size" />
            </div>
            <small class="ml-a">
              共
              <strong class="text-1rem text-[var(--el-color-primary)]">
                {{ pageInfo.total }}
              </strong>
              名评论
            </small>
          </div>
        </footer>
      </template>
    </AdminTableDefaultTable>
  </div>
</template>

<style scoped lang="scss">
.btns:hover .btns-hover {
  width: 2.8em;
}
:deep(.el-dialog__body) {
  padding-top: 10px;
  padding-bottom: 10px;
  .el-select__popper {
    max-width:100%;
    width:100%;

    .el-select-dropdown__item.selected{
      background-color: var(--el-color-primary);
      color:#fff;
    }
  }

}
.top-menu {
  :deep(.scrollbar__bar) {
    display: none;
    width: 0;
    opacity: 0;
    .el-scrollbar__thumb{
      display: none;
      width: 0;
      opacity: 0;
    }
  }
  :deep(.el-checkbox) {
    margin:0;
    .el-checkbox__input {
      display: none;
    }
  }
}
:deep(.el-dialog__header.show-close) {
  padding: 0 1rem;
}
:deep(.el-dialog__header .el-dialog__headerbtn) {
  width: 62px;
  height: 62px;
}
:deep(.el-table) {
  .el-table__empty-text {
    font-size: 1rem;
    letter-spacing: .2rem;
  }
  .el-pagination {
    .number {
      transition: $transition-delay;
    }
  }
}


:deep(.el-upload.el-upload--picture-card) {
  width: 100%;
  height: 100%;
  .el-upload-dragger {
    width: 100%;
    height: 100%;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>
