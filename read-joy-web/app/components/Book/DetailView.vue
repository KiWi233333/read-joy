<script setup lang="ts">
import type { BookDetailVO, ResourceVO } from "~/composables/api/book";
import { CardBookInfoSe } from "#components";
import { useAddResourceLikeApi } from "~/composables/api/resource";
import { ResultStatus } from "~/composables/api/types/result";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { useSettingStore } from "~/composables/sotre/useSettingStore";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { BaseUrlFile, BaseUrlImg } from "~/composables/utils/useBaseUrl";
import { downloadFile } from "~/composables/utils/useFile";
import { FILE_TYPE_ICON_DEFAULT, FILE_TYPE_ICON_MAP, formatFileSize, useCopyText } from "~/composables/utils/useUtils";
import Progress from "../ui/progress/Progress.vue";

const {
  bookDetial = {},
} = defineProps<{
  bookDetial?: Partial<BookDetailVO>
}>();
const setting = useSettingStore();
const user = useUserStore();
const store = useDefaultStore();
const likeAllCount = computed(() => bookDetial?.resourceList?.reduce((pre, cur) => pre + cur.likeCount, 0) || 0);
const downAllCount = computed(() => bookDetial?.resourceList?.reduce((pre, cur) => pre + cur.downloadCount, 0) || 0);
const bookListDto = computed(() => store.theBookDetail.categoryId === bookDetial?.categoryId ? { categoryId: store.theBookDetail.categoryId } : {});
// tabs栏
const activeName = ref("read");
function downloadResource(resource: ResourceVO) {
  if (!user.isLogin) {
    ElMessage.error("没有权限，请先登录！");
    user.toLogin(); // 登录后再下载
    return;
  }
  ElMessage.warning("正在下载中，请稍后查看");
  downloadFile(BaseUrlFile + resource.url, resource.title, {
    Authorization: user.getToken,
  }, () => {
    ElMessage.closeAll("warning");
    ElMessage.success("下载完成！");
  });
}

// 点赞
const actionResourceMap = useSessionStorage(`${user.userId}_actionResourceMap`, {
}) as any;
async function likeResource(item: ResourceVO) {
  const res = await useAddResourceLikeApi(item.resourceId, user.token);
  if (res.code !== ResultStatus.SUCCESS) {
    return;
  }
  ElMessage.success("点赞成功！");
  item.likeCount++;
  actionResourceMap.value[item.resourceId] = true;
}
</script>

<template>
  <div class="relative py-4">
    <section class="main-content">
      <!-- 左侧 -->
      <div w-full flex-row-c-c flex-col truncate class="group">
        <CardElImage
          :preview-src-list="[BaseUrlImg + bookDetial?.coverImageUrl]"
          preview-teleported
          infinite
          class="h-64 w-45 shadow-md border-default card-default-br"
          :default-src="bookDetial?.coverImageUrl"
        >
          <template #error>
            <small class="h-full w-full flex flex-row items-center justify-center">
              暂无封面
            </small>
          </template>
        </CardElImage>
        <h1 mb-1 mt-2 max-w-full truncate text-center text-lg font-500>
          {{ bookDetial?.title }}
        </h1>
        <div max-w-full truncate text-center text-primary text-small>
          {{ bookDetial?.author }}
        </div>
        <!-- 指数卡片 -->
        <div class="mt-2 w-4/5 overflow-hidden rounded-0 bg-transparent py-4 sm:w-2/3 border-default-t">
          <div flex-row-bt-c>
            <span text-small>
              推荐指数：{{ (likeAllCount + downAllCount) || "暂无" }}
            </span>
            <el-switch
              v-model="setting.isNewTabOpenBook"
              class="ml-a op-0 transition-opacity group-hover:op-100"
              title="设置默认新标签页打开"
              inline-prompt active-text="新标签"
              inactive-text="抽屉式"
              style="--el-switch-on-color: var(--el-color-primary);"
            />
          </div>
          <div mt-4 flex>
            <el-progress style="--el-fill-color-light: var(--el-color-primary-light-9);" :width="60" type="circle" class="mr-4" :percentage="(likeAllCount + downAllCount) || 0">
              <template #default="{ percentage }">
                <span class="text-sm">{{ percentage }}</span>
              </template>
            </el-progress>
            <div class="right flex-1 text-mini">
              <div class="mt-2 flex items-center">
                <i i-solar:like-broken />
                <Progress :model-value="likeAllCount" :max="(likeAllCount + downAllCount) || 100" style="height: 0.5rem;" class="ml-2 flex-1" />
              </div>
              <div class="mt-2 flex items-center">
                <i i-solar:download-minimalistic-outline />
                <Progress :model-value="downAllCount" :max="(likeAllCount + downAllCount) || 100" class="ml-2 flex-1" style="height: 0.5rem;" />
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 描述 -->
      <div class="h-full w-full flex-row-c-c py-6 sm:(block p-0)">
        <div>
          <div truncate text-1.6rem font-600>
            {{ bookDetial?.title }}
            <small ml-1 text-small>{{ bookDetial?.categoryName }}</small>
          </div>
          <div mt-6 max-w-full truncate text-small-color>
            作者：{{ bookDetial?.author || "-" }}
          </div>
          <div mt-3 max-w-full truncate text-sm text-small-color>
            出版社：{{ bookDetial?.publisher || "-" }}
          </div>
          <div mt-3 max-w-full truncate text-sm text-small-color>
            ISBN：<span btn-primary @click="useCopyText(bookDetial?.isbn || '', true)">{{ bookDetial?.isbn || "-" }}</span>
          </div>
          <div mt-3 max-w-full truncate text-sm text-small>
            发表时间：{{ bookDetial?.publishionDate || "-" }}
          </div>
          <div mt-6 max-w-full truncate text-xl text-warning font-500>
            价格：<small>￥</small>{{ bookDetial?.price }}
          </div>
          <div class="mt-6 pt-6 border-default-t">
            <BtnElButton icon-class="i-solar:download-outline mr-2" @click="activeName = 'resource'">
              下载资源
            </BtnElButton>
            <BtnElButton icon-class="i-solar:eye-outline mr-2" type="primary" class="border-default" @click="activeName = 'read'">
              查看摘要
            </BtnElButton>
          </div>
        </div>
      </div>
    </section>
    <div class="main-content relative">
      <el-tabs
        v-model="activeName"
        class="mt-4"
      >
        <el-tab-pane label="内容摘要" name="read" class="tab-pane">
          <div
            data-fade style="--anima: latter-slice-blur-top;"
            class="card-rounded-df p-4 text-sm border-default"
          >
            {{ bookDetial?.introduction }}
          </div>
        </el-tab-pane>
        <el-tab-pane lazy label="课程资源" name="resource" class="tab-pane">
          <ul class="grid cols-1 gap-2 sm:cols-2 sm:gap-4" data-fade style="--anima: latter-slice-blur-top;">
            <li
              v-for="item in bookDetial?.resourceList"
              :key="item.resourceId"
              :title="`${item.title}- 点击下载`"
              class="flex cursor-pointer items-center card-rounded-df p-2 border-hover-primary card-default sm:p-3"
              @click="downloadResource(item)"
            >
              <CardElImage
                class="mr-2 h-8 w-8"
                :src="FILE_TYPE_ICON_MAP[item.type] || FILE_TYPE_ICON_DEFAULT"
              >
                <template #error>
                  <small class="h-full w-full flex flex-row items-center justify-center" />
                </template>
              </CardElImage>
              <div class="w-full truncate text-sm">
                {{ item.title?.replace(/(.{16}).*(.{5})/, '$1...$2') }}
                <div mt-1 truncate text-mini>
                  大小：{{ formatFileSize(item.size) }}
                  下载：{{ item.downloadCount }}
                  点赞：{{ item.likeCount }}
                </div>
              </div>
              <!-- 点赞 -->
              <ClientOnly>
                <a
                  v-if="user.isLogin && item.url"
                  target="_blank"
                  :href="user.token && item.url ? `${BaseUrlFile}${item.url}?Authorization=${user.token}` : ''"
                  @click.stop="() => {}"
                >
                  <i title="预览文件" class="i-solar:eye-outline mr-2 block h-5 w-5 transition-all btn-primary" />
                </a>
                <i :title="actionResourceMap[`${item.resourceId}`] ? '已点赞' : '点赞'" :class="actionResourceMap[`${item.resourceId}`] ? 'i-solar:like-bold text-danger' : 'i-solar:like-broken'" class="block h-6 w-6 btn-danger" @click.stop.prevent.capture="likeResource(item)" />
              </ClientOnly>
            </li>
            <li
              v-if="bookDetial?.resourceList === null || bookDetial?.resourceList === undefined"
              class="flex items-center p-4 border-default card-default text-small"
            >
              暂无权限，去<span text-info btn-info @click="user.toLogin()">登录</span>
            </li>
          </ul>
          <li
            v-if="bookDetial?.resourceList?.length === 0"
            data-fade style="--anima: latter-slice-blur-top;"
            class="flex items-center card-rounded-df p-4 border-default text-small"
          >
            暂无资源
          </li>
        </el-tab-pane>
        <el-tab-pane lazy label="评论区" name="comment" class="tab-pane">
          <BookCommentView
            :book-id="bookDetial?.bookId"
            data-fade
            style="--anima: latter-slice-blur-top;"
            class="max-w-full"
          />
        </el-tab-pane>
      </el-tabs>
      <div>
        <ListBookList
          v-if="bookListDto?.categoryId"
          :show-load="true"
          books-class="relative grid cols-3 sm:cols-2 gap-6 sm:gap-8"
          :book-node="CardBookInfoSe"
          immediate
          auto-stop
          :limit="8"
          :ssr="true"
          :dto="bookListDto"
          :animated="false"
          :ignore-ids="bookDetial?.bookId ? [bookDetial.bookId] : undefined"
        >
          <template #header>
            <h2 class="py-4">
              相关推荐
            </h2>
          </template>
        </ListBookList>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.main-content {
  --at-apply: "grid cols-1 gap-4 py-4 md:cols-[5fr_2fr] sm:gap-10";
}
:deep(.el-tabs) {
  .el-tabs__nav-wrap::after,
  .el-tabs__active-bar {
    height: 1px;
  }
}
.tab-pane {
  --at-apply: "mt-2 min-h-20vh";
}
</style>
