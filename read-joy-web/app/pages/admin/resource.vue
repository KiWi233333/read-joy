<script lang="ts" setup>
import type { UploadFile, UploadFiles, UploadProps, UploadRawFile, UploadUserFile } from "element-plus";
import type { IPage } from "~/composables/api/types";

import { useDateFormat } from "@vueuse/core";
import { AdminResourceSortType, type AdminResourceVO, type AdminSelectResourcePageDTO, type InsertResourceDTO, type UpdateResourceDTO, useAdminAddResourceApi, useAdminBatchDeleteResourceApi, useAdminDeleteResourceApi, useAdminResourceDetailApi, useAdminResourcePageByDTOApi, useAdminUpdateResourceApi } from "~/composables/api/admin/resource";
import { BoolEnum, DefaultOrderSort, ResultStatus } from "~/composables/api/types/result";
import { useAdminStore } from "~/composables/sotre/useAdminStore";
import { BaseUrlFile } from "~/composables/utils/useBaseUrl";
import { compareObjects, DATE_FORMAT, DATE_SELECTOR_OPTIONS, downloadResource, FILE_TYPE_ICON_DEFAULT, FILE_TYPE_ICON_MAP, formatFileSize } from "~/composables/utils/useUtils";
import { appName } from "~/constants";

const MAX_FILE_SIZE_MB = 100;
const admin = useAdminStore();
useSeoMeta({
  title: `资源管理 - ${appName}`,
});

// 数据
const pageInfo = ref<IPage<AdminResourceVO>>({
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
const theRowInfo = ref<Partial<AdminResourceVO>>({});

// 多选
const selectList = ref<AdminResourceVO[]>([]);
function onSelectChange(list: AdminResourceVO[]) {
  // 添加
  selectList.value = list?.length ? list : [];
}

// 表单参数
const form = ref<Partial<AdminResourceVO & InsertResourceDTO & UpdateResourceDTO>>({
  bookId: undefined,
  resourceFile: undefined,
  title: undefined,
  isDeleted: undefined,

  // 以下不需要传给后台
  resourceId: undefined,
  submitter: undefined,
  type: undefined,
  url: undefined,
  createTime: undefined,
  downloadCount: undefined,
  likeCount: undefined,
  size: undefined,
  bookTitle: undefined,

});
const fileList = ref<UploadUserFile[]>([]);
const formRules = computed<any>(() => ({
  bookId: [
    { required: isAdd.value, message: "请选择图书！", trigger: "blur" },
  ],
  resourceFile: [
    { required: isAdd.value, message: "请上传资源文件！", trigger: "blur" },
  ],
  title: [
    { required: isAdd.value, message: "请输入资源标题！", trigger: "blur" },
    { type: "string", min: 1, max: 100, message: "资源标题长度必须在 1 到 100 个字符之间！", trigger: "blur" },
  ],
}));
const tableRef = ref<any>();
// 查询参数
const isShowSearch = ref<boolean>(true);
const searchDTO = ref<AdminSelectResourcePageDTO>({
  page: 1,
  size: 10,
  isDeleted: BoolEnum.FALSE,
  bookId: undefined,
  endDate: undefined,
  keyword: undefined,
  sortOrder: undefined,
  sortType: undefined,
  startDate: undefined,
});
// 检查表单是否更新
const isUpdate = computed(() => {
  return Object.keys(compareObjects(theRowInfo.value, form.value as AdminResourceVO)).length > 0;
});

/**
 * 加载数据
 */
async function loadData() {
  if (isLoading.value)
    return;
  if (pageInfo.value.records.length)
    isLoading.value = true;
  // 请求
  const res = await useAdminResourcePageByDTOApi({
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
}, { immediate: true, deep: true });

// 打开添加弹窗
function onAddItem() {
  clearForm();
  isAdd.value = true;
  isShowForm.value = true;
}
function onEditItem(row: AdminResourceVO) {
  isEdit.value = true;
  isAdd.value = false;
  onShowInfoDetail(row);
}
/**
 * 资源操作
 * @param type 类型
 */
async function onSubmit(type: "insert" | "update" | "delete" | "batchDel" | "recover", data: any) {
  const tip = {
    class: "el-button--primary",
    title: "操作",
  };
  if (type === "insert") {
    tip.class = "el-button--success";
    tip.title = "添加资源";
  }
  else if (type === "update") {
    tip.class = "el-button--warning";
    tip.title = "修改资源";
  }
  else if (type === "delete") {
    tip.class = "el-button--danger";
    tip.title = "删除该资源";
  }
  else if (type === "batchDel") {
    tip.class = "el-button--danger";
    tip.title = "批量删除资源";
  }
  else if (type === "recover") {
    tip.class = "el-button--primary";
    tip.title = "恢复该资源";
  }
  else {
    return;
  }
  if (isLoading.value)
    return;
  const resourceFile = fileList.value[0];
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
              if (!data && typeof data === "object")
                return;
              const formData = new FormData() as any;
              form.value.bookId !== undefined && formData.append("bookId", form.value.bookId);
              form.value.title !== undefined && formData.append("title", form.value.title);
              form.value.isDeleted !== undefined && formData.append("isDeleted", form.value.isDeleted);
              resourceFile && resourceFile.raw && formData.append("resourceFile", resourceFile.raw, resourceFile.name || "default.txt");
              res = await useAdminAddResourceApi(formData, admin.getToken);
            }
            else if (type === "update") {
              if (!data || !data.resourceId)
                return;
              const formData = new FormData() as any;
              form.value.bookId !== undefined && formData.append("bookId", form.value.bookId);
              form.value.title !== undefined && formData.append("title", form.value.title);
              form.value.isDeleted !== undefined && formData.append("isDeleted", form.value.isDeleted);
              resourceFile && resourceFile.raw && formData.append("resourceFile", resourceFile.raw, resourceFile.name || "default.txt");
              res = await useAdminUpdateResourceApi(data.resourceId, formData, admin.getToken);
            }
            else if (type === "recover") { // 恢复
              if (typeof data !== "number") {
                ElMessage.warning("参数错误！");
                return;
              }
              const formData = new FormData() as any;
              form.value.bookId !== undefined && formData.append("bookId", form.value.bookId);
              formData.append("isDeleted", BoolEnum.FALSE);
              res = await useAdminUpdateResourceApi(data, formData, admin.getToken);
            }
            else if (type === "delete") {
              if (typeof data !== "number") {
                ElMessage.warning("参数错误！");
                return;
              }
              res = await useAdminDeleteResourceApi(data!, admin.getToken);
            }
            else if (type === "batchDel") {
              if (!data || !Array.isArray(data))
                return;
              res = await useAdminBatchDeleteResourceApi(data as number[], admin.getToken);
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
function checkForm(type: "insert" | "update" | "delete" | "batchDel" | "recover", call: () => any) {
  if (type === "delete" || type === "batchDel" || type === "recover") {
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

const uploadRef = ref();
// 文件上传
const beforeUpload: UploadProps["beforeUpload"] = (rawFile) => {
  if ((rawFile.size / 1024 / 1024) > MAX_FILE_SIZE_MB) {
    ElMessage.error(`图片大小不能超过${MAX_FILE_SIZE_MB}MB`);
    return false;
  }
  form.value.resourceFile = rawFile; // 赋值文件
  return true;
};
function handleFileChange(file: UploadFile, fList: UploadFiles) {
  if (file.raw && !beforeUpload(file.raw)) {
    fileList.value.splice(0, 1);
    return;
  };
  // 设置文件名
  form.value.title = file?.name || "";
  form.value.resourceFile = file?.raw;
}

// 当前文件信息
const theFile = computed(() => {
  return {
    title: fileList.value?.[0]?.name || form.value.title || undefined,
    type: fileList.value?.[0]?.raw?.type || form.value.type || undefined,
    size: fileList.value?.[0]?.size !== undefined ? fileList.value?.[0]?.size : (form.value.size || undefined),
  };
});

const handleExceed: UploadProps["onExceed"] = (files) => {
  uploadRef.value!.clearFiles();
  const file = files[0] as UploadRawFile;
  uploadRef.value!.handleStart(file);
};
// 清除表单
function clearForm(call?: () => void) {
  form.value = {
    resourceId: undefined,
    submitter: undefined,
    title: undefined,
    type: undefined,
    url: undefined,
    bookId: undefined,
    bookTitle: undefined,
    createTime: undefined,
    downloadCount: undefined,
    isDeleted: BoolEnum.FALSE,
    likeCount: undefined,
    size: undefined,

    resourceFile: undefined,
  };
  fileList.value = [];
  call && call();
}

/**
 * 显示弹窗
 * @param row table选中的行
 */
async function onShowInfoDetail(row?: AdminResourceVO, call?: () => any) {
  if (row?.resourceId) {
    // 预览
    theRowInfo.value = row;
    form.value = {
      ...row,
    };
    isShowForm.value = true;
    fileList.value = row?.url
      ? [{
          name: row?.url?.split?.("/")?.pop() || "img.png",
          url: BaseUrlFile + row.url,
        }]
      : [];

    // 补充详情
    const res = await useAdminResourceDetailApi(row.resourceId, admin.getToken);
    if (res.code === ResultStatus.SUCCESS) {
      theRowInfo.value = res.data;
      form.value = {
        ...res.data,
      };
    }
  }
  else {
    clearForm();
  }
  call && call();
  isShowForm.value = true;
}

// 排序
const sortGroupModel = computed({
  get() {
    if (searchDTO.value.sortType === AdminResourceSortType.CREATE_TIME && searchDTO.value.sortOrder !== undefined) {
      return "new";
    }
    else if (searchDTO.value.sortType === AdminResourceSortType.DOWNLOAD_COUNT && searchDTO.value.sortOrder !== undefined) {
      return "download";
    }
    else if (searchDTO.value.sortType === AdminResourceSortType.LIKE_COUNT && searchDTO.value.sortOrder !== undefined) {
      return "like";
    }
    else {
      return undefined;
    }
  },
  set(val: "new" | "download" | "like" | undefined) {
    if (!val) {
      searchDTO.value.sortType = undefined;
      searchDTO.value.sortOrder = undefined;
    }
    else {
      searchDTO.value.sortOrder = searchDTO.value.sortOrder === DefaultOrderSort.DESC ? DefaultOrderSort.ASC : DefaultOrderSort.DESC;
      if (val === "new") {
        searchDTO.value.sortType = AdminResourceSortType.CREATE_TIME;
      }
      else if (val === "download") {
        searchDTO.value.sortType = AdminResourceSortType.DOWNLOAD_COUNT;
      }
      else if (val === "like") {
        searchDTO.value.sortType = AdminResourceSortType.LIKE_COUNT;
      }
      else {
        searchDTO.value.sortType = undefined;
        searchDTO.value.sortOrder = undefined;
      }
    }
    loadData();
  },
});
// @unocss-inlude
const sortOptions = [
  {
    label: "创建排序",
    value: "new",
    icon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    ascIcon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    descIcon: "i-solar:sort-from-top-to-bottom-bold-duotone",
    descLabel: "创建降序",
    ascLabel: "创建升序",
  },
  {
    label: "下载排序",
    value: "download",
    icon: "i-solar:download-minimalistic-bold-duotone",
    ascIcon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    descIcon: "i-solar:sort-from-top-to-bottom-bold-duotone",
    descLabel: "下载降序",
    ascLabel: "下载升序",
  },
  {
    label: "点赞排序",
    value: "like",
    icon: "i-solar:like-line-duotone",
    ascIcon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    descIcon: "i-solar:sort-from-top-to-bottom-bold-duotone",
    descLabel: "点赞降序",
    ascLabel: "点赞升序",
  },
];
// 日期筛选
const dateGroupModel = computed({
  get() {
    if (searchDTO.value.startDate && searchDTO.value.endDate) {
      return [searchDTO.value.startDate, searchDTO.value.endDate];
    }
    else {
      return undefined;
    }
  },
  set(val: string[] | undefined) {
    if (!val) {
      searchDTO.value.startDate = undefined;
      searchDTO.value.endDate = undefined;
    }
    else {
      searchDTO.value.startDate = val[0];
      searchDTO.value.endDate = val[1];
    }
    loadData();
  },
});

/**
 * 重置筛选
 */
function resetSearchOption() {
  page.value = 1;
  searchDTO.value = {
    page: 1,
    size: 10,
    isDeleted: BoolEnum.FALSE,
    bookId: undefined,
    endDate: undefined,
    keyword: undefined,
    sortOrder: undefined,
    sortType: undefined,
    startDate: undefined,
  };
  tableRef.value?.clearSort?.();
  loadData();
}
</script>

<template>
  <div>
    <AdminTableDefaultTable
      :header="{ title: `资源管理`, updateTime }"
    >
      <!-- 菜单 -->
      <template #menu>
        <div
          mr-4
          :class="!isShowSearch ? 'scale-x-90  opacity-0' : ' scale-x-100 opacity-100'"
          class="top-menu flex-shrink-0 transform-origin-left-center overflow-hidden transition-300 transition-all"
          flex="~ gap-2 md:gap-4 items-center"
        >
          <small sticky left-0 flex-shrink-0>筛选：</small>
          <!-- 资源名 -->
          <el-input v-model.lazy.trim="searchDTO.keyword" class="w-12rem sm:w-14rem" :prefix-icon="ElIconSearch" placeholder="聚合搜索" label="资源名" @keyup.enter="loadData" />
          <!-- 分类select -->
          <el-segmented
            :model-value="sortGroupModel"
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
          <el-checkbox-button v-model="searchDTO.isDeleted" :true-value="BoolEnum.TRUE" :false-value="BoolEnum.FALSE" style="border: none;border-radius: 0;outline: none;box-shadow: none;" class="truncate !border-default !card-default" label="回收站" />
          <FormDatePicker
            v-model="dateGroupModel"
            :format="DATE_FORMAT"
            :preload-dates="DATE_SELECTOR_OPTIONS"
            :popover-props="{
              class: 'w-14rem',
            }"
            placeholder="日期筛选"
            size="default"
            :btn-props="{
              class: '!block bg-color  w-17rem hover:bg-color',
              style: 'display: block;',
            }"
          />
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
              { show: true, title: '刷新', methods: loadData, icon: 'i-solar:refresh-outline' },
              { show: true, title: isShowSearch ? '收起' : '筛选', methods: () => isShowSearch = !isShowSearch, icon: 'i-solar:sort-from-top-to-bottom-line-duotone ', type: 'default' },
              {
                show: selectList.length > 0,
                title: '批量',
                methods: () => onSubmit('batchDel', selectList.map((p: AdminResourceVO) => p.resourceId) as number[] || []),
                icon: 'i-solar:trash-bin-minimalistic-broken ',
                class: 'animate-[fade-in_0.15s_ease-in-out]',
                type: 'danger',
                btnProps: {
                  disabled: selectList.length === 0,
                },
              },
              { show: true, plain: false, title: '添加', methods: onAddItem, icon: 'i-solar:book-2-line-duotone  mr-2', type: 'primary' },
            ]"
          />
        </div>
      </template>
      <template #default>
        <div class="overflow-hidden border-default card-default">
          <el-table
            ref="tableRef"
            v-loading="isLoading"
            header-cell-class-name="!bg-color"
            :disabled="isEdit"
            class-name="w-full"
            :data="pageInfo.records"
            stripe
            empty-text="暂无数据"
            :header-cell-style="{
              padding: '1rem 0',
              fontSize: '1em',
            }"
            row-class-name="group h-4rem"
            row-key="resourceId"
            height="75vh"
            @row-click="(row: AdminResourceVO) => {
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
            <!-- 资源ID -->
            <el-table-column
              label="资源ID"
              property="resourceId"
              width="80%"
              column-key="resourceId"
            >
              <template #default="{ row }">
                <BtnCopyText :text="row.resourceId" />
              </template>
            </el-table-column>
            <!-- 归属图书 -->
            <el-table-column
              label="归属图书"
              property="bookTitle"
              min-width="100%"
              column-key="bookTitle"
            >
              <template #default="{ row }">
                <BtnCopyText title="复制图书" :text="row.bookTitle" />
              </template>
            </el-table-column>
            <!-- 文件名 -->
            <el-table-column
              column-key="title"
              prop="title"
              show-overflow-tooltip
              min-width="180%"
              label="文件名"
            >
              <template #default="{ row }">
                <BtnCopyText :text="row.title" />
              </template>
            </el-table-column>
            <!-- 资源类型 -->
            <el-table-column
              column-key="type"
              sortable
              prop="type"
              min-width="140%"
              show-overflow-tooltip
              label="资源类型"
            >
              <template #default="{ row }">
                <div class="flex items-center">
                  <CardElImage
                    class="mr-2 h-5 w-5 flex-shrink-0"
                    :src="FILE_TYPE_ICON_MAP[row.type || 'default' ] || FILE_TYPE_ICON_DEFAULT"
                  >
                    <template #error>
                      <small class="h-full w-full flex flex-row items-center justify-center">
                        暂无
                      </small>
                    </template>
                  </CardElImage>
                  <span class="max-w-full truncate">{{ row?.type || '-' }}</span>
                </div>
              </template>
            </el-table-column>
            <!-- 路径 -->
            <el-table-column
              column-key="url"
              prop="url"
              label="路径（点击预览）"
              min-width="120%"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <a
                  target="_blank" block max-w-12em truncate
                  :href="admin.token && row.url ? `${BaseUrlFile}${row.url}?Authorization=${admin.token}` : ''"
                >
                  {{ row.url }}
                </a>
              </template>
            </el-table-column>
            <!-- 文件大小 -->
            <el-table-column
              column-key="size"
              prop="size"
              label="文件大小"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{ formatFileSize(row?.size || 0) }}
              </template>
            </el-table-column>
            <!-- 下载量 -->
            <el-table-column
              column-key="downloadCount"
              prop="downloadCount"
              label="下载量"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <div class="text-info font-500">
                  <i class="i-solar:download-minimalistic-bold mr-1 p-2" />
                  {{ row?.downloadCount || 0 }}
                </div>
              </template>
            </el-table-column>
            <!-- 点赞量 -->
            <el-table-column
              column-key="likeCount"
              prop="likeCount"
              label="点赞量"
              align="center"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <div class="text-danger font-500">
                  <i class="i-solar:like-outline mr-1 p-2 text-danger" />
                  {{ row?.likeCount || 0 }}
                </div>
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
                <span v-else>否</span>
              </template>
            </el-table-column>
            <!-- 创建时间 -->
            <el-table-column
              column-key="createTime"
              prop="createTime"
              label="创建时间"
              sortable
              align="center"
              min-width="140%"
            />
            <!-- 动作+弹窗 -->
            <el-table-column
              fixed="right"
              label="操作"
              min-width="300%"
            >
              <template #default="{ row }">
                <div class="flex opacity-0 transition-200 group-hover:opacity-100">
                  <!-- 下载 -->
                  <BtnElButton
                    :plain="false"
                    style="padding: 0rem 0.6rem;margin: 0 0 0 0.2rem;"
                    class="btns"
                    @click.stop="downloadResource(row, admin.token, (status) => {
                      if (status === '403') {
                        ElMessage.error('您没有权限下载该资源！');
                        navigateTo('/admin/login');
                        return
                      }
                    })"
                  >
                    <i
                      i-solar:download-minimalistic-bold
                      p-2
                    />
                    <span w-0 overflow-hidden transition-200 transition-all class="btns-hover">&nbsp;下载</span>
                  </BtnElButton>
                  <!-- 详情 -->
                  <BtnElButton
                    :plain="false"
                    style="padding: 0rem 0.6rem"
                    class="btns"
                    @click.stop="() => {
                      isEdit = false;
                      isAdd = false;
                      onShowInfoDetail(row);
                    }"
                  >
                    <i
                      i-solar:eye-bold-duotone
                      p-2
                    />
                    <span w-0 overflow-hidden transition-200 transition-all class="btns-hover">&nbsp;详情</span>
                  </BtnElButton>
                  <!-- 编辑 -->
                  <BtnElButton
                    :plain="false"
                    style="padding: 0rem 0.6rem"
                    class="btns"
                    type="primary"
                    @click.stop="() => onEditItem(row)"
                  >
                    <i
                      i-solar:pen-2-broken
                      p-2
                    />
                    <span w-0 overflow-hidden transition-200 transition-all class="btns-hover">&nbsp;编辑</span>
                  </BtnElButton>
                  <!-- 删除资源 -->
                  <BtnElButton
                    :type="row.isDeleted ? 'default' : 'danger'"
                    :plain="false"
                    style="padding: 0rem 0.6rem"
                    class="btns"
                    @click="onSubmit(row.isDeleted === BoolEnum.TRUE ? 'recover' : 'delete', row.resourceId)"
                  >
                    <i
                      :class="row.isDeleted ? 'i-solar:refresh-outline' : 'i-solar:trash-bin-minimalistic-broken'"
                      p-2
                    />
                    <span w-0 overflow-hidden transition-200 transition-all class="btns-hover">&nbsp;
                      {{ row.isDeleted ? '恢复' : '删除' }}
                    </span>
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
              本资源
            </small>
          </div>
        </footer>
      </template>
    </AdminTableDefaultTable>
    <!-- 资源信息查看 -->
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
          {{ isEdit ? '编辑资源信息' : isAdd ? '添加资源信息' : '资源详情' }}
        </h3>
      </template>
      <div relative w-80vw md:w-400px>
        <el-form
          ref="formRef"
          :rules="formRules"
          :model="form"
          :disabled="isLoading || (!isEdit && !isAdd)"
          label-width="10em"
          label-position="left"
          class="px-4 pt-4"
          hide-required-asterisk
        >
          <el-form-item label="选择图书" prop="bookId">
            <AdminInputBookItemSelect v-model:book-id="form.bookId" :dto="{ keyword: isEdit ? theRowInfo.bookTitle : '' }" class="w-full" />
          </el-form-item>
          <el-form-item label="资源文件" prop="resourceFile" class="h-10rem w-full">
            <el-upload
              ref="uploadRef"
              v-model:file-list="fileList"
              size="small"
              name="resourceFile"
              :limit="1"
              drag
              :multiple="false"
              :auto-upload="false"
              :show-file-list="false"
              :before-upload="beforeUpload"
              list-type="text"
              class="h-8rem w-full"
              :on-exceed="handleExceed"
              @change="handleFileChange"
            >
              <div v-if="fileList.length">
                <CardElImage
                  class="mr-2 h-6 w-6"
                  :src="FILE_TYPE_ICON_MAP[theFile.type || 'default' ] || FILE_TYPE_ICON_DEFAULT"
                >
                  <template #error>
                    <small class="h-full w-full flex flex-row items-center justify-center">
                      暂无
                    </small>
                  </template>
                </CardElImage>
                <div class="w-full truncate text-sm">
                  {{ theFile?.title?.replace(/(.{16}).*(.{5})/, '$1...$2') }}
                  <div mt-1 truncate text-mini>
                    大小：{{ formatFileSize(theFile?.size || 0) }}
                    <span v-if="isEdit" ml-2>下载：{{ form?.downloadCount || 0 }}</span>
                    <span v-if="isEdit" ml-2>点赞：{{ form?.likeCount || 0 }}</span>
                  </div>
                </div>
              </div>
              <div v-else class="h-full w-full flex-row-c-c flex-col">
                <ElIconPlus
                  class="h-8 w-8"
                />
                <p class="mt-2 text-mini">
                  请选择小于{{ MAX_FILE_SIZE_MB }}M 的文件
                </p>
              </div>
            </el-upload>
          </el-form-item>
          <el-form-item label="资源名称（展示）" prop="title">
            <el-input v-model.trim="form.title" placeholder="请输入资源显示名称">
              <template #suffix>
                <span
                  v-if="isAdd || isEdit"
                  class="select-none text-sm text-info btn-info"
                  @click.stop="() => {
                    if (!theFile?.title) {
                      return ElMessage.error('请先上传资源文件！');
                    }
                    form.title = theFile?.title;
                  }"
                >
                  获取
                </span>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="是否隐藏" prop="isDeleted">
            <el-switch
              v-model="form.isDeleted"
              :true-value="BoolEnum.TRUE"
              :false-value="BoolEnum.FALSE"
              inline-prompt
              style="--el-switch-on-color: var(--el-color-danger); --el-switch-off-color: var(--el-color-primary);"
              inactive-text="显示"
              active-text="隐藏"
            />
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
  .el-checkbox-button__inner {
    border: none;
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

:deep(.el-upload) {
  height: 100%;
  width: 100%;
  .el-upload-dragger {
    height: 100%;
    width: 100%;
    padding:0;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
