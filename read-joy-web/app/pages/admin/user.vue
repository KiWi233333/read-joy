<script lang="ts" setup>
import type { IPage } from "~/composables/api/types";
import { useDateFormat } from "@vueuse/core";
import { type AdminSelectUserPageDTO, type AdminUserVO, useAdminUserBatchDeleteApi, useAdminUserCheckedApi, useAdminUserPageByDTOApi } from "~/composables/api/admin/user";
import { BoolEnum, DefaultOrderSort, type Result, ResultStatus } from "~/composables/api/types/result";

import { useAdminStore } from "~/composables/sotre/useAdminStore";
import { BaseUrlImg } from "~/composables/utils/useBaseUrl";
import { compareObjects, markPhone } from "~/composables/utils/useUtils";
import { appName } from "~/constants";

const admin = useAdminStore();

useSeoMeta({
  title: `用户管理 - ${appName}`,
});
// 数据
const pageInfo = ref<IPage<AdminUserVO>>({
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
const theRowInfo = ref<Partial<AdminUserVO>>({});

// 多选
const selectList = ref<AdminUserVO[]>([]);
function onSelectChange(list: AdminUserVO[]) {
  // 添加
  selectList.value = list?.length ? list : [];
}

// 表单参数
const form = ref<Partial<AdminUserVO>>({
  id: undefined,
  loginName: undefined,
  imgUrl: undefined,
  createTime: undefined,
  trueName: undefined,
  telephone: undefined,
  userType: undefined,
  isChecked: undefined,
});
const formRules = reactive({
  trueName: [
    { required: true, message: "用户名不能为空！", trigger: "blur" },
    { min: 1, max: 40, message: "长度在1-40个字符！", trigger: "blur" },
  ],
  loginName: [
    { required: true, message: "用户名不能为空！", trigger: "blur" },
    { min: 6, max: 30, message: "长度在6-30个字符！", trigger: "blur" },
  ],
});

// 查询参数
const isShowSearch = ref<boolean>(true);
const searchDTO = ref<AdminSelectUserPageDTO>({
  page: 1,
  size: 10,
  keyword: undefined,
  createOrder: undefined,
  isChecked: undefined,
});
// 检查表单是否更新
const isUpdate = computed(() => {
  return Object.keys(compareObjects(theRowInfo.value, form.value as AdminUserVO)).length > 0;
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
  const res = await useAdminUserPageByDTOApi({
    ...searchDTO.value,
    page: page.value,
    size: size.value,
  }, admin.getToken);
  if (res.code === ResultStatus.SUCCESS)
    pageInfo.value = res.data;
  updateTime.value = useDateFormat(new Date(), "YYYY-MM-DD HH:mm:ss").value;
  setTimeout(() => {
    isLoading.value = false;
  }, 300);
}

watch([searchDTO, page, size], () => {
  loadData();
}, { immediate: true });

/**
 * 用户操作
 * @param type 类型
 */
async function onSubmit(type: "delete" | "batchDel", data: number | number[], rawData?: Partial<AdminUserVO>) {
  const tip = {
    class: "el-button--primary",
    title: "操作",
  };
  if (type === "delete") {
    tip.class = "el-button--danger";
    tip.title = "删除该用户";
  }
  else if (type === "batchDel") {
    tip.class = "el-button--danger";
    tip.title = "批量删除用户";
  }
  else {
    return;
  }
  if (!rawData || !rawData?.id)
    rawData = theRowInfo.value;
  // 二步确认
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
        if (type === "delete") {
          if (!rawData || !rawData.id || typeof data !== "number") {
            ElMessage.warning("参数错误！");
            return;
          }
          res = await useAdminUserBatchDeleteApi([data]!, admin.getToken);
        }
        else if (type === "batchDel") {
          if (!data || !Array.isArray(data))
            return;
          res = await useAdminUserBatchDeleteApi(data as number[], admin.getToken);
        }
        else {
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

/**
 * 验证校验
 */
function checkForm(call: () => any) {
  formRef.value?.validate(async (valid: boolean) => {
    if (!valid)
      return;
    call && call();
  });
}

/**
 * 用户状态切换
 */
async function onToggleChange(id: number, toggle: number, alertText: string, callback?: () => void) {
  if (!id || toggle === undefined)
    return false;
  // 请求
  const data = await ElMessageBox.confirm(`确定用户${alertText}吗？`, "操作提醒", {
    confirmButtonText: alertText,
    confirmButtonClass: toggle ? "el-button--danger" : "el-button--info",
    cancelButtonText: "取消",
    type: "warning",
    center: true,
  });
  if (data === "confirm") {
    const res = await useAdminUserCheckedApi(id, toggle ? BoolEnum.FALSE : BoolEnum.TRUE, admin.getToken);
    if (res.code === ResultStatus.SUCCESS) {
      ElMessage.success(`${alertText}成功！`);
      callback && callback();
      return true;
    }
    else {
      ElMessage.error(res.message);
      return false;
    }
  }
  else {
    return false;
  }
}

/**
 * 关闭弹窗 之前
 * @param done 是否完成
 * @param doneBack 关闭后的回调
 */
function onBeforeCloseDialog(done: () => void, doneBack?: () => any, cancelBack?: () => any, flag?: boolean) {
  isAdd.value = false;
  isEdit.value = false;
  if (flag) {
    ElMessageBox.confirm("是否放弃修改？", "操作提醒", {
      confirmButtonText: "放弃",
      confirmButtonClass: "el-button--danger",
      cancelButtonText: "取消",
      center: true,
    })
      .then(() => {
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
    done();
  }
}

// 清除表单
function clearForm(call?: () => void) {
  form.value = {
    id: undefined,
    loginName: undefined,
    imgUrl: undefined,
    createTime: undefined,
    trueName: undefined,
    telephone: undefined,
    userType: undefined,
    isChecked: undefined,
  };
  call && call();
}

/**
 * 显示弹窗
 * @param row table选中的行
 */
function onShowInfoDetail(row?: AdminUserVO, call?: () => any) {
  if (row) {
    theRowInfo.value = row;
    form.value = {
      ...row,
    };
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
    createOrder: undefined,
    isChecked: undefined,
  };
  tableRef.value?.clearSort?.();
  loadData();
}
</script>

<template>
  <div>
    <AdminTableDefaultTable
      :header="{ title: `用户管理`, updateTime }"
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
          <!-- 用户名 -->
          <el-input v-model.lazy.trim="searchDTO.keyword" class="w-12rem sm:w-14rem" :prefix-icon="ElIconSearch" placeholder="关键字搜索" label="用户名" @keyup.enter="loadData" />
          <!-- 用户状态 -->
          <div
            class="w-8rem"
          >
            <el-select
              v-model="searchDTO.isChecked"
              placeholder="用户状态"
              @change="loadData"
            >
              <el-option
                label="全部"
                value=""
              />
              <el-option
                label="正常"
                :value="1"
              />
              <el-option
                label="禁用"
                :value="0"
              />
            </el-select>
          </div>
          <!-- 时间排序 -->
          <div
            class="w-8rem"
          >
            <el-select
              v-model="searchDTO.createOrder"
              placeholder="时间排序"
              @change="loadData"
            >
              <el-option
                label="全部"
                value=""
              />
              <el-option
                label="降序"
                :value="DefaultOrderSort.DESC"
              />
              <el-option
                label="升序"
                :value="DefaultOrderSort.ASC"
              />
            </el-select>
          </div>
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
              { show: true, title: isShowSearch ? '取消' : '筛选', methods: () => isShowSearch = !isShowSearch, icon: 'i-solar:sort-from-top-to-bottom-line-duotone ', type: 'default' },
              {
                show: true,
                title: '批量',
                methods: () => {
                  onSubmit('batchDel', selectList.map((p: AdminUserVO) => p.id) as number[] || [], theRowInfo)
                },
                icon: 'i-solar:trash-bin-minimalistic-broken ',
                class: 'animate-[fade-in_0.15s_ease-in-out]',
                type: 'danger',
                btnProps: {
                  disabled: selectList.length === 0,
                },
              },
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
            stripe
            empty-text="暂无数据"
            :header-cell-style="{
              padding: '1rem 0',
              fontSize: '1em',
            }"
            header-cell-class-name="!bg-color"
            row-class-name="group h-4.16rem items-center"
            row-key="id"
            height="75vh"
            @row-click="(row: AdminUserVO) => {
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
            <!-- 用户ID -->
            <el-table-column
              label="用户ID"
              property="id"
              width="80%"
              column-key="id"
            >
              <template #default="{ row }">
                <BtnCopyText :text="row.id" />
              </template>
            </el-table-column>
            <!-- 用户头像 -->
            <el-table-column
              column-key="name"
              prop="name"
              align="center"
              label="用户头像"
            >
              <template
                #default="{ row }"
              >
                <el-image
                  :preview-src-list="[BaseUrlImg + row.imgUrl]"
                  :preview-teleported="true"
                  class="mt-2 h-12 w-12 flex-shrink-0 card-default border-default-hover"
                  fit="cover"
                  loading="lazy"
                  :src="`${BaseUrlImg + row.imgUrl}`"
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
            <!-- 用户名 -->
            <el-table-column
              column-key="loginName"
              prop="loginName"
              show-overflow-tooltip
              label="用户名"
            >
              <template #default="{ row }">
                {{ row?.loginName || '-' }}
              </template>
            </el-table-column>
            <!-- 真实姓名 -->
            <el-table-column
              column-key="trueName"
              prop="trueName"
              label="真实姓名"
              align="center"
            >
              <template #default="{ row }">
                {{ row?.trueName || '-' }}
              </template>
            </el-table-column>
            <!-- 手机号 -->
            <el-table-column
              column-key="telephone"
              prop="telephone"
              label="手机号"
              align="center"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{ row?.telephone ? markPhone(row?.telephone) : '-' }}
              </template>
            </el-table-column>
            <!-- 用户类别 -->
            <el-table-column
              label="用户类别"
              column-key="userType"
              prop="userType"
              align="center"
            >
              <template #default="{ row }">
                <el-tag op-70 :type="row?.userType ? 'info' : 'danger'">
                  {{ row?.userType === 1 ? '普通用户' : '' }}
                </el-tag>
              </template>
            </el-table-column>
            <!-- 用户状态 -->
            <el-table-column
              label="用户状态"
              align="center"
              column-key="isChecked"
              prop="isChecked"
            >
              <template #default="{ row }">
                <el-switch
                  v-model="row.isChecked"
                  class="mx-a transition-200 active:scale-90"
                  style="--el-switch-on-color:var(--el-color-success);
               --el-switch-off-color:var(--el-color-error)"
                  inline-prompt
                  active-text="正常"
                  :active-value="1"
                  inactive-text="封禁"
                  :before-change="onToggleChange.bind(null, row.id, row.isChecked, row.isChecked ? '封禁用户' : '设为正常')"
                  :inactive-value="0"
                  @click.stop="() => {}"
                />
              </template>
            </el-table-column>
            <!-- 创建时间 -->
            <el-table-column
              column-key="createTime"
              prop="createTime"
              label="创建时间"
              sortable
              align="center"
              width="180%"
              min-width="180%"
            />
            <!-- 动作+弹窗 -->
            <el-table-column
              fixed="right"
              label="操作"
              min-width="100%"
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
                  <!-- 注销用户 -->
                  <BtnElButton
                    type="danger"
                    :plain="false"
                    style="padding: 0rem 0.6rem"
                    class="btns"
                    @click="onSubmit('delete', row.id, row)"
                  >
                    <i
                      i-solar:trash-bin-trash-line-duotone
                      p-2
                    />
                    <span w-0 overflow-hidden transition-200 transition-all class="btns-hover">&nbsp;注销</span>
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
              名用户
            </small>
          </div>
        </footer>
      </template>
    </AdminTableDefaultTable>
    <!-- 用户信息查看 -->
    <el-dialog
      v-model="isShowForm"
      class="view-dialog"
      align-center
      width="fit-content"
      :before-close="(done:()=>void) => (isShowForm && isEdit) ? onBeforeCloseDialog(done, clearForm, undefined, isUpdate) : done()"
    >
      <template #header>
        <h3 text-center>
          用户详情
        </h3>
      </template>
      <div relative w-80vw md:w-350px>
        <el-form
          ref="formRef"
          :rules="formRules"
          :model="form"
          :disabled="isLoading || !isEdit"
          label-width="6em"
          label-position="left"
          class="p-4 md:p-6"
          hide-required-asterisk
        >
          <div class="w-full flex-row-c-c">
            <CardNuxtImg
              :default-src="form.imgUrl"
              class="mb-4 h-6rem w-6rem overflow-hidden rounded-full"
            >
              <template #error>
                <div mb-4 h-6rem w-6rem flex-row-c-c text-lg>
                  {{ form?.loginName?.[0] || "未上传" }}
                </div>
              </template>
            </CardNuxtImg>
          </div>
          <el-form-item
            v-if="form.id"
            prop="id"
            label="用户ID"
          >
            <BtnCopyText :text="form.id" />
          </el-form-item>
          <el-form-item
            prop="telephone"
            label="手机号"
          >
            <BtnCopyText :text="form.telephone" />
          </el-form-item>
          <el-form-item
            prop="userType"
            label="用户类型"
          >
            <el-tag
              :type="form.userType ? 'info' : 'danger'"
            >
              {{ form.userType === 1 ? '普通用户' : '' }}
            </el-tag>
          </el-form-item>
          <!-- 用户名 -->
          <el-form-item
            prop="loginName"
            label="用户名"
          >
            <el-input v-model="form.loginName" placeholder="请填写用户名（6-30字符）" />
          </el-form-item>
          <!-- 真实姓名 -->
          <el-form-item
            prop="trueName" label="真实姓名"
          >
            <el-input v-model="form.trueName" placeholder="请填写用户昵称（1-40字符）" />
          </el-form-item>
          <div flex items-center gap-2>
            <!-- 状态 -->
            <el-form-item
              prop="isChecked" label="状态"
            >
              <el-switch
                v-model="form.isChecked"
                class="mx-a transition-200 active:scale-90"
                style="--el-switch-on-color:var(--el-color-info);
               --el-switch-off-color:var(--el-color-error)"
                inline-prompt
                active-text="正常"
                :active-value="1"
                inactive-text="封禁"
                :inactive-value="0"
              />
            </el-form-item>
          </div>
        </el-form>
      </div>
      <template #footer>
        <el-button type="danger" plain @click="isShowForm = false && clearForm()">
          关闭
        </el-button>
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
</style>
