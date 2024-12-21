<script lang="ts" setup>
import type { UploadInstance, UploadProps, UploadRawFile, UploadUserFile } from "element-plus";
import type { IPage } from "~/composables/api/types";
import { useDateFormat } from "@vueuse/core";

import { type AdminCategoryVO, type AdminSelectCategoryPageDTO, type InsertCategoryDTO, type UpdateCategoryDTO, useAdminBatchDeleteCategoryApi, useAdminCategoryPageByDTOApi, useAdminDeleteCategoryApi, useAdminInsertCategoryApi, useAdminUpdateCategoryApi } from "~/composables/api/admin/category";
import { DefaultOrderSort, ResultStatus } from "~/composables/api/types/result";
import { useAdminDefaultStore } from "~/composables/sotre/useAdminDefaultStore";
import { useAdminStore } from "~/composables/sotre/useAdminStore";
import { BaseUrlImg } from "~/composables/utils/useBaseUrl";
import { compareObjects, DATE_FORMAT, DATE_SELECTOR_OPTIONS, randomISBN } from "~/composables/utils/useUtils";
import { appName } from "~/constants";

const store = useAdminDefaultStore();
const admin = useAdminStore();
useSeoMeta({
  title: `分类管理 - ${appName}`,
});

// 数据
const pageInfo = ref<IPage<AdminCategoryVO>>({
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
const tableRef = ref();

// 功能 （展开）
const isEdit = ref<boolean>(false); // 是否编辑
const isAdd = ref<boolean>(false); // 是否添加
const isShowForm = ref<boolean>(false);
const isLoading = ref<boolean>(false);
const theRowInfo = ref<Partial<AdminCategoryVO>>({});

// 多选
const selectList = ref<AdminCategoryVO[]>([]);
function onSelectChange(list: AdminCategoryVO[]) {
  // 添加
  selectList.value = list?.length ? list : [];
}

// 表单参数
const form = ref<Partial<AdminCategoryVO & UpdateCategoryDTO & InsertCategoryDTO>>({
  categoryId: undefined,
  categoryName: undefined,
});
const formRules = computed<any>(() => ({
  categoryId: [
    { required: isAdd.value, message: "分类不能为空", trigger: "blur" },
    { type: "number", message: "分类必须为数字", trigger: "blur" },
  ],
  categoryName: [
    { required: isAdd.value, message: "请输入分类名称", trigger: "blur" },
    { min: 1, max: 20, message: "分类名称长度必须在1到20之间", trigger: "blur" },
  ],
}));

// 查询参数
const isShowSearch = ref<boolean>(true);
const searchDTO = ref<AdminSelectCategoryPageDTO>({
  page: 1,
  size: 10,
  idSort: undefined,
  keyword: undefined,
});
// 排序
const sortGroupModel = computed({
  get() {
    if (searchDTO.value.idSort !== undefined) {
      return "idSort";
    }
    else {
      return undefined;
    }
  },
  set(val: "idSort" | undefined) {
    if (!val) {
      searchDTO.value.idSort = undefined;
    }
    else {
      searchDTO.value.idSort = val !== sortGroupModel.value
        ? DefaultOrderSort.DESC // 切换选项后默认降序
        : (searchDTO.value.idSort === DefaultOrderSort.DESC ? DefaultOrderSort.ASC : DefaultOrderSort.DESC);
    }
    loadData();
  },
});
// 检查表单是否更新
const isUpdate = computed(() => {
  return Object.keys(compareObjects(theRowInfo.value, form.value as AdminCategoryVO)).length > 0;
});
// @unocss-inlude
const sortOptions = [
  {
    label: "创建排序",
    value: "idSort",
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
  const res = await useAdminCategoryPageByDTOApi({
    ...searchDTO.value,
    page: page.value,
    size: size.value,
  }, admin.getToken);
  if (res.code === ResultStatus.SUCCESS)
    pageInfo.value = res.data;
  updateTime.value = useDateFormat(new Date(), "YYYY-MM-DD HH:mm:ss").value;
  isLoading.value = false;
}

watch([searchDTO, page, size], () => {
  loadData();
}, { immediate: true });

// 打开添加弹窗
function onAddItem() {
  clearForm();
  isAdd.value = true;
  isShowForm.value = true;
}
function onEditItem(row: AdminCategoryVO) {
  isEdit.value = true;
  isAdd.value = false;
  onShowInfoDetail(row);
}

/**
 * 分类操作
 * @param type 类型
 */
async function onSubmit(type: "insert" | "update" | "delete" | "batchDel", data: any) {
  const tip = {
    class: "el-button--primary",
    title: "操作",
  };
  if (type === "insert") {
    tip.class = "el-button--success";
    tip.title = "添加分类";
  }
  else if (type === "update") {
    tip.class = "el-button--warning";
    tip.title = "修改分类";
  }
  else if (type === "delete") {
    tip.class = "el-button--danger";
    tip.title = "删除该分类";
  }
  else if (type === "batchDel") {
    tip.class = "el-button--danger";
    tip.title = "批量删除分类";
  }
  else {
    return;
  }
  if (isLoading.value)
    return;
  // 二步确认
  checkForm(type, () => {
    ElMessageBox.confirm(`是否确认${tip.title}？`, "操作提醒", {
      confirmButtonText: "确认",
      confirmButtonClass: tip.class,
      cancelButtonText: "取消",
      type: "warning",
      center: true,
      async callback(a: string) {
        if (a === "confirm") {
          isLoading.value = true;
          let res; // 请求结果
          try {
            if (type === "insert") {
              if (!data && form.value.categoryName)
                return;
              res = await useAdminInsertCategoryApi({
                categoryName: form.value.categoryName!,
              }, admin.getToken);
            }
            else if (type === "update") {
              if (!data || !data.categoryId)
                return;
              res = await useAdminUpdateCategoryApi(data.categoryId, {
                categoryName: form.value.categoryName!,
              }, admin.getToken);
            }
            else if (type === "delete") {
              if (typeof data !== "number") {
                ElMessage.warning("参数错误！");
                return;
              }
              res = await useAdminDeleteCategoryApi(data!, admin.getToken);
            }
            else if (type === "batchDel") {
              if (!data || !Array.isArray(data))
                return;
              res = await useAdminBatchDeleteCategoryApi(data as number[], admin.getToken);
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
  });
}

/**
 * 验证校验
 */
function checkForm(type: "insert" | "update" | "delete" | "batchDel", call: () => any) {
  if (type === "delete" || type === "batchDel") {
    call && call();
    return;
  }
  formRef.value?.validate(async (valid: boolean) => {
    if (!valid)
      return;
    call && call();
  });
}

/**
 * 关闭弹窗 之前
 * @param done 是否完成
 * @param doneBack 关闭后的回调
 */
function onBeforeCloseDialog(done: () => void, doneBack?: () => any, cancelBack?: () => any, flag?: boolean) {
  if (flag) {
    ElMessageBox.confirm("是否放弃修改？", "操作提醒", {
      confirmButtonText: "放弃",
      confirmButtonClass: "el-button--danger",
      cancelButtonText: "取消",
      center: true,
    })
      .then(() => {
        isAdd.value = false;
        isEdit.value = false;
        // 确认退出
        doneBack && doneBack();
        done();
      })
      .catch(() => {
        // 取消
        cancelBack && cancelBack();
      });
  }
  else {
    isAdd.value = false;
    isEdit.value = false;
    done();
  }
}

// 清除表单
function clearForm(call?: () => void) {
  form.value = {
    categoryId: undefined,
    categoryName: undefined,
  };
  call && call();
}

/**
 * 显示弹窗
 * @param row table选中的行
 */
async function onShowInfoDetail(row?: AdminCategoryVO, call?: () => any) {
  if (row?.categoryId) {
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
  tableRef.value?.clearSort?.();
  loadData();
}
</script>

<template>
  <div>
    <AdminTableDefaultTable
      :header="{ title: `分类管理`, updateTime }"
    >
      <!-- 菜单 -->
      <template #menu>
        <div
          mr-4
          :class="!isShowSearch ? 'scale-x-90  opacity-0' : ' scale-x-100 opacity-100'"
          class="top-menu flex-shrink-0 transform-origin-left-center overflow-hidden transition-300 transition-all"
          flex="~ gap-2 md:gap-4 items-center"
        >
          <small sticky left-0 flex-shrink-0 opacity-70>筛选：</small>
          <!-- 分类名 -->
          <el-input v-model.lazy.trim="searchDTO.keyword" class="w-12rem sm:w-14rem" :prefix-icon="ElIconSearch" placeholder="关键字搜索" @keyup.enter="loadData" />
          <!-- 排序 -->
          <el-segmented
            :model-value="sortGroupModel"
            :options="sortOptions"
            class="border-default card-default !bg-color"
          >
            <template #default="{ item }">
              <div class="flex gap-2" @click="sortGroupModel = item.value">
                <i p-2 :class="sortGroupModel === item.value ? (searchDTO.idSort === DefaultOrderSort.DESC ? item.descIcon : item.ascIcon) : item.icon" />
                <div>{{ sortGroupModel === item.value ? (searchDTO.idSort === DefaultOrderSort.DESC ? item.descLabel : item.ascLabel) : item.label }}</div>
              </div>
            </template>
          </el-segmented>
          <el-button
            plain
            type="danger"
            @click="resetSearchOption"
          >
            重置
          </el-button>
        </div>
        <div ml-a flex>
          <!-- 按钮 -->
          <AdminTableDefaultTableBtns
            :menu="[
              { show: true, title: '刷新', methods: loadData, icon: 'i-solar:refresh-outline', type: 'info' },
              {
                show: selectList.length > 0,
                title: '批量',
                methods: () => onSubmit('batchDel', selectList.map((p: AdminCategoryVO) => p.categoryId) as number[] || []),
                icon: 'i-solar:trash-bin-minimalistic-broken ',
                class: 'animate-[fade-in_0.15s_ease-in-out]',
                type: 'danger',
                btnProps: {
                  disabled: selectList.length === 0,
                },
              },
              { show: true, title: isShowSearch ? '收起' : '筛选', methods: () => isShowSearch = !isShowSearch, icon: 'i-solar:sort-from-top-to-bottom-line-duotone ', type: 'default' },
              { show: true, plain: false, title: '添加', methods: onAddItem, icon: 'i-solar:widget-add-line-duotone mr-2', type: 'info' },
            ]"
          />
        </div>
      </template>
      <template #default>
        <div class="overflow-hidden border-default card-default">
          <el-table
            ref="tableRef"
            v-loading="isLoading"
            :header-cell-style="{
              padding: '1rem 0',
              fontSize: '1em',
            }"
            row-class-name="group h-4.16rem"
            header-cell-class-name="!bg-color"
            :disabled="isEdit"
            class-name="w-full"
            :data="pageInfo.records"
            stripe
            select-on-indeterminate
            empty-text="暂无数据"
            row-key="categoryId"
            height="75vh"
            @row-click="(row: AdminCategoryVO) => {
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
            <!-- 分类ID -->
            <el-table-column
              label="分类ID"
              property="categoryId"
              column-key="categoryId"
              align="center"
            >
              <template #default="{ row }">
                <BtnCopyText :text="row.categoryId" />
              </template>
            </el-table-column>
            <!-- 分类名称 -->
            <el-table-column
              column-key="categoryName"
              prop="categoryName"
              label="分类名称"
              align="center"
              class="font-bold"
            >
              <template #default="{ row }">
                {{ row?.categoryName || '-' }}
              </template>
            </el-table-column>
            <!-- 图书数量 -->
            <el-table-column
              column-key="bookCount"
              prop="bookCount"
              label="图书数量"
              sortable
              align="center"
            >
              <template #default="{ row }">
                {{ row?.bookCount || '' }}
              </template>
            </el-table-column>
            <!-- 动作+弹窗 -->
            <el-table-column
              fixed="right"
              label="操作"
              width="300%"
            >
              <template #default="{ row }">
                <div class="flex opacity-0 transition-200 group-hover:opacity-100">
                  <!-- 图书列表 -->
                  <BtnElButton
                    :plain="false"
                    style="padding: 0rem 0.6rem"
                    class="btns"
                    @click.stop="navigateTo(`/admin/book?categoryId=${row.categoryId}`)"
                  >
                    <i
                      i-solar:book-line-duotone
                      p-2
                    />
                    <span w-0 overflow-hidden transition-200 transition-all class="btns-hover">&nbsp;图书</span>
                  </BtnElButton>
                  <!-- 编辑 -->
                  <BtnElButton
                    :plain="false"
                    style="padding: 0rem 0.6rem"
                    type="primary"
                    class="btns"
                    @click.stop="() => onEditItem(row)"
                  >
                    <i
                      i-solar:pen-2-broken
                      p-2
                    />
                    <span w-0 overflow-hidden transition-200 transition-all class="btns-hover">&nbsp;编辑</span>
                  </BtnElButton>
                  <!-- 删除分类 -->
                  <BtnElButton
                    type="danger"
                    :plain="false"
                    style="padding: 0rem 0.6rem"
                    class="btns"
                    @click="onSubmit('delete', row.categoryId)"
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
              名分类
            </small>
          </div>
        </footer>
      </template>
    </AdminTableDefaultTable>
    <!-- 分类信息查看 -->
    <el-dialog
      v-model="isShowForm"
      class="view-dialog border-default"
      align-center
      destroy-on-close
      width="fit-content"
      :before-close="(done:()=>void) => (isShowForm && isEdit) ? onBeforeCloseDialog(done, clearForm, undefined, isUpdate) : done()"
    >
      <template #header>
        <h3 text-center>
          {{ isEdit ? '编辑分类信息' : isAdd ? '添加分类信息' : '分类详情' }}
        </h3>
      </template>
      <div relative w-80vw md:w-400px>
        <el-form
          ref="formRef"
          :rules="formRules"
          :model="form"
          :disabled="isLoading || (!isEdit && !isAdd)"
          label-width="6em"
          label-position="left"
          class="px-4 pt-8"
          hide-required-asterisk
          @submit.prevent.stop="() => {}"
        >
          <el-form-item v-if="form.categoryId" label="出版日期" prop="categoryId">
            <BtnCopyText :text="form.categoryId" />
          </el-form-item>
          <el-form-item label="分类名称" prop="categoryName">
            <el-input v-model.trim="form.categoryName" autofocus placeholder="请输入分类名称" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="flex-row-c-c">
          <BtnElButton class="w-5rem border-default" plain @click="isShowForm = false && clearForm()">
            取消
          </BtnElButton>
          <!-- 添加操作按钮 -->
          <BtnElButton v-if="isAdd || isEdit" class="w-5rem border-default" type="primary" @click="onSubmit(isEdit ? 'update' : 'insert', form)">
            {{ isEdit ? '保存' : '添加' }}
          </BtnElButton>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
// .btns:hover .btns-hover {
//   width: 2.8em;
// }
.btns-hover {
  width: auto;
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
