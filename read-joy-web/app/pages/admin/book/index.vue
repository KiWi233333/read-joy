<script lang="ts" setup>
import type { UploadFile, UploadFiles, UploadInstance, UploadProps, UploadRawFile, UploadUserFile } from "element-plus";
import type { IPage } from "~/composables/api/types";
import { ElCollapseItem } from "#components";

import { useDateFormat } from "@vueuse/core";
import { type AdminBookDetailVO, AdminBookSortType, type AdminBookVO, type AdminSelectBookPageDTO, type InsertBookDTO, type UpdateBookDTO, useAdminAddBookApi, useAdminBatchDeleteBookApi, useAdminBookDetailApi, useAdminBookDetailPageByDTOApi, useAdminBookPageByDTOApi, useAdminDeleteBookApi, useAdminUpdateBookApi } from "~/composables/api/admin/book";
import { DefaultOrderSort, ResultStatus } from "~/composables/api/types/result";
import { useAdminDefaultStore } from "~/composables/sotre/useAdminDefaultStore";
import { useAdminStore } from "~/composables/sotre/useAdminStore";
import { BaseUrlFile, BaseUrlImg } from "~/composables/utils/useBaseUrl";
import { compareObjects, DATE_FORMAT, DATE_SELECTOR_OPTIONS, FILE_TYPE_ICON_DEFAULT, FILE_TYPE_ICON_MAP, formatFileSize, randomISBN } from "~/composables/utils/useUtils";
import { appName } from "~/constants";


const route = useRoute();
const initCategoryId = route.query?.categoryId ? +route?.query?.categoryId?.toString() : undefined;
const MAX_FILE_SIZE_MB = 12;
const store = useAdminDefaultStore();
const admin = useAdminStore();
useSeoMeta({
  title: `图书管理 - ${appName}`,
});
// 只展开单个
const tableExpand = ref([]);
// 数据
const pageInfo = ref<IPage<AdminBookVO>>({
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
const tableRef = ref<any>();

// 功能 （展开）
const isEdit = ref<boolean>(false); // 是否编辑
const isAdd = ref<boolean>(false); // 是否添加
const isShowForm = ref<boolean>(false);
const isLoading = ref<boolean>(false);
const theRowInfo = ref<Partial<AdminBookVO>>({});

// 多选
const selectList = ref<AdminBookVO[]>([]);
function onSelectChange(list: AdminBookVO[]) {
  selectList.value = list?.length ? list : [];
}

// 折叠
const expandList = ref<string[]>([]);
function onExpand(row: AdminBookVO) {
  if (!tableRef.value || !row)
    return;

  tableRef.value?.toggleRowExpansion(row, !expandList.value.includes(String(row.bookId)));
  if (expandList.value.includes(String(row.bookId)))
    expandList.value.splice(expandList.value.indexOf(String(row.bookId)), 1);
  else
    expandList.value.push(String(row.bookId));
}

// 表单参数
const form = ref<Partial<AdminBookDetailVO & AdminBookVO & InsertBookDTO & UpdateBookDTO>>({
  bookId: undefined,
  isbn: undefined,
  title: undefined,
  categoryId: undefined,
  categoryName: undefined,
  author: undefined,
  introduction: undefined,
  price: undefined,
  publisher: undefined,
  coverImageUrl: undefined,
  publishionDate: undefined,
  // 文件
  coverImage: undefined, // file类型
});
const coverList = ref<UploadUserFile[]>([]);
const formRules = computed<any>(() => ({
  isbn: [
    { required: isAdd.value, message: "请输入国际标准书号", trigger: "blur" },
    { min: 1, max: 20, message: "国际标准书号长度必须在1到20之间", trigger: "blur" },
  ],
  title: [
    { required: isAdd.value, message: "请输入书名", trigger: "blur" },
    { min: 1, max: 100, message: "书名长度必须在1到100之间", trigger: "blur" },
  ],
  categoryId: [
    { required: isAdd.value, message: "分类不能为空", trigger: "blur" },
    { type: "number", message: "分类必须为数字", trigger: "blur" },
  ],
  author: [
    { required: isAdd.value, message: "请输入作者", trigger: "blur" },
    { min: 1, max: 100, message: "作者长度必须在1到100之间", trigger: "blur" },
  ],
  price: [
    { required: isAdd.value, message: "请输入价格", trigger: "blur" },
    { type: "number", min: 0, message: "价格输入错误", trigger: "blur" },
  ],
  publisher: [
    { required: isAdd.value, message: "请输入出版社", trigger: "blur" },
    { min: 1, max: 50, message: "出版社长度必须在1到50之间", trigger: "blur" },
  ],
  publishionDate: [
    { required: isAdd.value, message: "请输入出版日期", trigger: "blur" },
  ],
  coverImage: [
    { required: isAdd.value, message: "请上传封面图", trigger: "blur" },
  ],
  introduction: [
    { max: 1000, message: "书籍介绍长度不超过1000字", trigger: "blur" },
  ],
}));
// 查询参数
const isShowSearch = ref<boolean>(true);
const searchDTO = ref<AdminSelectBookPageDTO>({
  page: 1,
  size: 10,
  categoryId: initCategoryId,
  endDate: undefined,
  startDate: undefined,
  keyword: undefined,
  sortType: AdminBookSortType.PUBLISH_TIME,
  sortOrder: DefaultOrderSort.DESC,
});
// 检查表单是否更新
const isUpdate = computed(() => {
  return Object.keys(compareObjects(theRowInfo.value, form.value as AdminBookVO)).length > 0;
});
// 折叠面板
const collapseActionList = ref([]);

/**
 * 加载数据
 */
async function loadData() {
  if (isLoading.value)
    return;
  if (pageInfo.value.records.length)
    isLoading.value = true;
  // 请求
  const res = await useAdminBookPageByDTOApi({
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
  if (initCategoryId) {
    form.value.categoryId = initCategoryId;
  }
  isAdd.value = true;
  isShowForm.value = true;
}
function onEditItem(row: AdminBookVO) {
  isEdit.value = true;
  isAdd.value = false;
  onShowInfoDetail(row);
}

/**
 * 图书操作
 * @param type 类型
 */
async function onSubmit(type: "insert" | "update" | "delete" | "batchDel", data: any) {
  const tip = {
    class: "el-button--primary",
    title: "操作",
  };
  if (type === "insert") {
    tip.class = "el-button--success";
    tip.title = "添加图书";
  }
  else if (type === "update") {
    tip.class = "el-button--warning";
    tip.title = "修改图书";
  }
  else if (type === "delete") {
    tip.class = "el-button--danger";
    tip.title = "删除该图书";
  }
  else if (type === "batchDel") {
    tip.class = "el-button--danger";
    tip.title = "批量删除图书";
  }
  else {
    return;
  }
  if (isLoading.value)
    return;
  const coverImage = coverList.value[0];
  if (type === "insert" && (!coverImage || !coverImage.raw)) {
    ElMessage.error("请上传封面图!");
    return;
  }
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
              form.value.isbn !== undefined && formData.append("isbn", form.value.isbn);
              form.value.title !== undefined && formData.append("title", form.value.title);
              form.value.categoryId !== undefined && formData.append("categoryId", form.value.categoryId);
              form.value.author !== undefined && formData.append("author", form.value.author);
              form.value.introduction !== undefined && formData.append("introduction", form.value.introduction);
              form.value.price !== undefined && formData.append("price", form.value.price);
              form.value.publisher !== undefined && formData.append("publisher", form.value.publisher);
              form.value.publishionDate !== undefined && formData.append("publishionDate", useDateFormat(form.value.publishionDate, DATE_FORMAT).value);
              coverImage && coverImage.raw && formData.append("coverImage", coverImage.raw, coverImage.name || "image.png");
              res = await useAdminAddBookApi(formData, admin.getToken);
            }
            else if (type === "update") {
              if (!data || !data.bookId)
                return;
              const formData = new FormData() as any;
              form.value.title !== undefined && formData.append("title", form.value.title);
              form.value.categoryId !== undefined && formData.append("categoryId", form.value.categoryId);
              form.value.author !== undefined && formData.append("author", form.value.author);
              form.value.introduction !== undefined && formData.append("introduction", form.value.introduction);
              form.value.price !== undefined && formData.append("price", form.value.price);
              form.value.publisher !== undefined && formData.append("publisher", form.value.publisher);
              form.value.publishionDate !== undefined && formData.append("publishionDate", useDateFormat(form.value.publishionDate, DATE_FORMAT).value);
              coverImage && coverImage.raw && formData.append("coverImage", coverImage.raw, coverImage.name || "image.png");
              res = await useAdminUpdateBookApi(data.bookId, formData, admin.getToken);
            }
            else if (type === "delete") {
              if (typeof data !== "number") {
                ElMessage.warning("参数错误！");
                return;
              }
              res = await useAdminDeleteBookApi(data!, admin.getToken);
            }
            else if (type === "batchDel") {
              if (!data || !Array.isArray(data))
                return;
              res = await useAdminBatchDeleteBookApi(data as number[], admin.getToken);
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

const uploadRef = ref();
// 文件上传
const beforeUpload: UploadProps["beforeUpload"] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > MAX_FILE_SIZE_MB) {
    ElMessage.error(`图片大小不能超过${formatFileSize(MAX_FILE_SIZE_MB * 1024 * 1024)}！`);
    return false;
  }
  form.value.coverImage = rawFile; // 赋值文件
  return true;
};
const handleExceed: UploadProps["onExceed"] = (files) => {
  uploadRef.value!.clearFiles();
  const file = files[0] as UploadRawFile;
  uploadRef.value!.handleStart(file);
};
function handleUploadChange(file: UploadFile, fileList: UploadFiles) {
  if (file.raw && !beforeUpload(file.raw)) {
    coverList.value = [];
    return;
  }
  // 处理文件
  form.value.coverImage = file.raw;
}
// 清除表单
function clearForm(call?: () => void) {
  form.value = {
    bookId: undefined,
    isbn: undefined,
    title: undefined,
    categoryId: undefined,
    categoryName: undefined,
    author: undefined,
    introduction: undefined,
    price: undefined,
    publisher: undefined,
    coverImageUrl: undefined,
    publishionDate: undefined,
    // 文件
    coverImage: undefined, // file类型
  };
  coverList.value = [];
  call && call();
}

/**
 * 显示弹窗
 * @param row table选中的行
 */
async function onShowInfoDetail(row?: AdminBookVO, call?: () => any) {
  if (row?.bookId) {
    // 预览
    theRowInfo.value = row;
    form.value = {
      ...row,
    };
    isShowForm.value = true;
    coverList.value = row?.coverImageUrl
      ? [{
          name: row?.coverImageUrl?.split?.("/")?.pop() || "img.png",
          url: BaseUrlImg + row.coverImageUrl,
        }]
      : [];
    // 补充详情
    const res = await useAdminBookDetailApi(row.bookId, admin.getToken);
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


// 筛选
// 排序
const sortGroupModel = computed({
  get() {
    if (searchDTO.value.sortType === AdminBookSortType.PRICE && searchDTO.value.sortOrder !== undefined) {
      return "price";
    }
    else if (searchDTO.value.sortType === AdminBookSortType.PUBLISH_TIME && searchDTO.value.sortOrder !== undefined) {
      return "new";
    }
    else {
      return undefined;
    }
  },
  set(val: "new" | "price" | undefined) {
    if (!val) {
      searchDTO.value.sortType = undefined;
      searchDTO.value.sortOrder = undefined;
    }
    else {
      searchDTO.value.sortType = val === "price" ? AdminBookSortType.PRICE : AdminBookSortType.PUBLISH_TIME;
      searchDTO.value.sortOrder = val !== sortGroupModel.value
        ? DefaultOrderSort.DESC // 切换选项后默认降序
        : (searchDTO.value.sortOrder === DefaultOrderSort.DESC ? DefaultOrderSort.ASC : DefaultOrderSort.DESC);
    }
    loadData();
  },
});
// @unocss-inlude
const sortOptions = [
  {
    label: "出版排序",
    value: "new",
    icon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    ascIcon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    descIcon: "i-solar:sort-from-top-to-bottom-bold-duotone",
    descLabel: "出版降序",
    ascLabel: "出版升序",
  },
  {
    label: sortGroupModel.value === "price" ? (searchDTO.value?.sortOrder === DefaultOrderSort.DESC ? "从高到低" : "从低到高") : "价格排序",
    value: "price",
    icon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    ascIcon: "i-solar:sort-from-bottom-to-top-bold-duotone",
    descIcon: "i-solar:sort-from-top-to-bottom-bold-duotone",
    descLabel: "价格降序",
    ascLabel: "价格升序",
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
    categoryId: undefined,
    endDate: undefined,
    keyword: undefined,
    sortType: undefined,
    sortOrder: undefined,
    startDate: undefined,
  };
  tableRef.value?.clearSort?.();
  loadData();
}
</script>

<template>
  <div>
    <AdminTableDefaultTable
      :header="{ title: `图书管理`, updateTime }"
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
          <!-- 图书名 -->
          <el-input v-model.lazy.trim="searchDTO.keyword" class="w-12rem sm:w-14rem" :prefix-icon="ElIconSearch" placeholder="聚合搜索" label="图书名" @keyup.enter="loadData" />
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
          <FormDatePicker
            v-model="dateGroupModel"
            :format="DATE_FORMAT"
            :preload-dates="DATE_SELECTOR_OPTIONS"
            :popover-props="{
              class: 'w-14rem',
            }"
            placeholder="出版日期筛选"
            size="default"
            :btn-props="{
              class: '!block bg-color  w-14rem hover:bg-color',
              style: 'display: block;',
            }"
          />
          <el-select
            v-model="searchDTO.categoryId"
            class="bg-color"
            style="width: 9rem;box-shadow: none;border: none; border-radius: 10px;"
            placeholder="图书分类"
            filterable
            fit-input-width
            @change="loadData"
          >
            <el-option v-for="item in store.categoryList" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId">
              <span>{{ item.categoryName }}</span>
            </el-option>
          </el-select>
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
                methods: () => onSubmit('batchDel', selectList.map((p: AdminBookVO) => p.bookId) as number[] || []),
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
            :disabled="isEdit"
            class-name="w-full"
            :data="pageInfo.records"
            empty-text="暂无数据"
            :header-cell-style="{
              padding: '1rem 0',
              fontSize: '1em',
            }"
            header-cell-class-name="!bg-color"
            height="75vh"
            :tree-props="{ children: 'resourceList', hasChildren: 'isResource' }"
            row-class-name="group h-4.5rem"
            :expand="tableExpand"
            :expand-row-keys="expandList"
            row-key="bookId"
            @row-click.self="onExpand"
            @row-click="(row: AdminBookVO) => {
              theRowInfo = row;
              form = { ...row }
            }"
            @selection-change="onSelectChange"
          >
            <!-- 资源列表 table list -->
            <el-table-column
              fixed
              type="expand"
              label="资源列表"
              width="100%"
              column-key="bookId"
              style="padding: 0;"
            >
              <template #default="{ row }">
                <div class="p-2 pt-0 bg-color-layout sm:p-4">
                  <AdminTableResourceTable class="second-table truncate shadow-md shadow-inset border-default card-default" :book-id="row?.bookId!" :book-item="row" />
                </div>
              </template>
            </el-table-column>
            <!-- 选择 -->
            <el-table-column
              fixed
              type="selection"
            />
            <!-- 图书ID -->
            <el-table-column
              label="图书ID"
              property="bookId"
              width="100%"
              column-key="bookId"
            >
              <template #default="{ row }">
                <BtnCopyText :text="row.bookId" />
              </template>
            </el-table-column>
            <!-- ISBN -->
            <el-table-column
              label="ISBN"
              property="isbn"
              width="160px"
              column-key="isbn"
            >
              <template #default="{ row }">
                <BtnCopyText :text="row.isbn" />
              </template>
            </el-table-column>
            <!-- 图书封面 -->
            <el-table-column
              column-key="name"
              prop="name"
              label="图书封面"
            >
              <template
                #default="{ row }"
              >
                <el-image
                  title="点击查看大图"
                  :preview-src-list="[BaseUrlImg + row.coverImageUrl]"
                  :preview-teleported="true"
                  class="my-1 h-18 w-12 flex-shrink-0 rounded-1 shadow-sm border-default-hover"
                  fit="cover"
                  loading="lazy"
                  :src="`${BaseUrlImg + row.coverImageUrl}`"
                  @click.stop="() => {}"
                >
                  <template #error>
                    <small h-full w-full flex-row-c-c opacity-60 card-default>
                      未上传
                    </small>
                  </template>
                </el-image>
              </template>
            </el-table-column>
            <!-- 图书名 -->
            <el-table-column
              column-key="title"
              prop="title"
              show-overflow-tooltip
              min-width="180%"
              label="图书名"
            >
              <template #default="{ row }">
                {{ row?.title || '-' }}
              </template>
            </el-table-column>
            <!-- 分类名称 -->
            <el-table-column
              column-key="categoryName"
              prop="categoryName"
              label="分类名称"
              align="center"
            >
              <template #default="{ row }">
                {{ row?.categoryName || '-' }}
              </template>
            </el-table-column>
            <!-- 作者 -->
            <el-table-column
              column-key="author"
              prop="author"
              show-overflow-tooltip
              label="作者"
              min-width="180%"
              align="center"
            >
              <template #default="{ row }">
                {{ row?.author || '-' }}
              </template>
            </el-table-column>
            <!-- 出版社 -->
            <el-table-column
              column-key="publisher"
              prop="publisher"
              show-overflow-tooltip
              label="出版社"
              align="center"
            >
              <template #default="{ row }">
                {{ row?.publisher || '-' }}
              </template>
            </el-table-column>
            <!-- 价格 -->
            <el-table-column
              column-key="price"
              prop="price"
              label="价格"
              align="center"
            >
              <template #default="{ row }">
                {{ row?.price || '-' }}
              </template>
            </el-table-column>
            <!-- 出版时间 -->
            <el-table-column
              column-key="publishionDate"
              prop="publishionDate"
              label="出版时间"
              sortable
              align="center"
              width="120%"
              min-width="120%"
            />
            <!-- 动作+弹窗 -->
            <el-table-column
              fixed="right"
              label="操作"
              min-width="240%"
            >
              <template #default="{ row }">
                <div class="flex opacity-0 transition-200 group-hover:opacity-100">
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
                  <!-- 删除图书 -->
                  <BtnElButton
                    type="danger"
                    :plain="false"
                    style="padding: 0rem 0.6rem"
                    class="btns"
                    @click.stop="onSubmit('delete', row.bookId)"
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
              本图书
            </small>
          </div>
        </footer>
      </template>
    </AdminTableDefaultTable>
    <!-- 图书信息查看 -->
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
          {{ isEdit ? '编辑图书信息' : isAdd ? '添加图书信息' : '图书详情' }}
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
          class="px-4 pt-4"
          hide-required-asterisk
        >
          <el-collapse v-model="collapseActionList">
            <el-form-item label="封面" prop="coverImage" class="mt-4">
              <el-upload
                ref="uploadRef"
                v-model:file-list="coverList"
                action="#"
                name="coverImage"
                :limit="1"
                accept="image/*"
                drag
                :multiple="false"
                :auto-upload="false"
                :show-file-list="false"
                :before-upload="beforeUpload"
                list-type="picture-card"
                class="h-11rem w-8rem flex-row-c-c card-default"
                :on-exceed="handleExceed"
                @change="handleUploadChange"
              >
                <CardElImage
                  v-if="coverList.length"
                  :src="coverList?.[0]?.url"
                  class="h-full w-full card-default"
                >
                  <template #error>
                    <div h-full w-full flex-row-c-c text-lg>
                      加载失败
                    </div>
                  </template>
                </CardElImage>
                <ElIconPlus
                  v-else
                  size="2rem"
                />
              </el-upload>
            </el-form-item>
            <el-form-item label="ISBN" prop="isbn">
              <el-input v-model.trim="form.isbn" placeholder="请输入ISBN">
                <template #suffix>
                  <span
                    v-if="isAdd || isEdit"
                    class="select-none text-sm text-info btn-info"
                    @click.stop="() => {
                      form.isbn = randomISBN();
                    }"
                  >
                    生成
                  </span>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="书名" prop="title">
              <el-input v-model.trim="form.title" placeholder="请输入书名" />
            </el-form-item>
            <el-form-item label="分类ID" prop="categoryId">
              <el-select
                v-model="form.categoryId"
                placeholder="请选择图书分类"
                filterable
              >
                <el-option v-for="item in store.categoryList" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId">
                  <span>{{ item.categoryName }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="作者" prop="author">
              <el-input v-model.trim="form.author" placeholder="请输入作者" />
            </el-form-item>
            <el-form-item label="价格" prop="price">
              <el-input v-model.number="form.price" type="number" step="0.01" :min="0" placeholder="请输入价格" />
            </el-form-item>
            <component :is="!isAdd ? ElCollapseItem : 'div'" title="更多信息" name="more">
              <el-form-item label="出版社" prop="publisher">
                <el-input v-model.trim="form.publisher" placeholder="请输入出版社名称" />
              </el-form-item>
              <el-form-item label="出版日期" prop="publishionDate">
                <el-date-picker v-model="form.publishionDate" :clearable="false" type="date" style="width: 100%;" :format="DATE_FORMAT" placeholder="选择出版日期" />
              </el-form-item>
              <el-form-item label="书籍介绍" prop="introduction">
                <el-input
                  v-model.trim="form.introduction"
                  type="textarea"
                  placeholder="请输入书籍介绍"
                  :rows="5"
                />
              </el-form-item>
            </component>
            <component :is="!isAdd ? ElCollapseItem : 'div'" title="资源列表" name="resourceList">
              <ul v-if="!isEdit && !isAdd" class="max-h-20rem w-full flex flex-col gap-2 overflow-y-auto">
                <li
                  v-for="item in form?.resourceList"
                  :key="item.resourceId"
                  :title="`${item.title} - 点击预览`"
                  class="flex cursor-pointer items-center card-rounded-df p-2 border-hover-primary card-default sm:p-3"
                >
                  <CardElImage
                    class="mr-2 h-6 w-6"
                    :src="FILE_TYPE_ICON_MAP[item.type] || FILE_TYPE_ICON_DEFAULT"
                  >
                    <template #error>
                      <small class="h-full w-full flex flex-row items-center justify-center">
                        暂无图片
                      </small>
                    </template>
                  </CardElImage>
                  <div class="w-full truncate text-sm">
                    {{ item.title?.replace(/(.{12}).*(.{5})/, '$1...$2') }}
                    <div mt-1 truncate text-mini>
                      大小：{{ formatFileSize(item.size || 0) }}
                      下载：{{ item.downloadCount }}
                      点赞：{{ item.likeCount }}
                    </div>
                  </div>
                  <a
                    v-if="admin.isLogin && item.url"
                    target="_blank"
                    :href="admin.token && item.url ? `${BaseUrlFile}${item.url}?Authorization=${admin.token}` : ''"
                  >
                    <i title="预览文件" class="i-solar:eye-outline mr-2 block h-5 w-5 transition-all btn-primary" />
                  </a>
                </li>
              </ul>
              <div v-if="form.resourceList?.length === 0" text-center text-small>
                暂无资源
              </div>
            </component>
          </el-collapse>
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
