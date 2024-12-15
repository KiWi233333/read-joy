<script setup lang="ts">
import type { BookDetailVO, ResourceVO } from "~/composables/api/book";
import { useAddResourceLikeApi } from "~/composables/api/resorce";
import { ResultStatus } from "~/composables/api/types/result";
import { useDefaultStore } from "~/composables/sotre/useDefaultStore";
import { useUserStore } from "~/composables/sotre/useUserStore";
import { BaseUrlFile } from "~/composables/utils/useBaseUrl";
import { downloadFile } from "~/composables/utils/useFile";
import { FILE_TYPE_ICON_DEFAULT, FILE_TYPE_ICON_MAP, formatFileSize, useCopyText } from "~/composables/utils/useUtils";
import Progress from "../ui/progress/Progress.vue";

const {
  bookDetial = {},
} = defineProps<{
  bookDetial?: Partial<BookDetailVO>
}>();
const user = useUserStore();
const store = useDefaultStore();
const likeAllCount = computed(() => bookDetial?.resourceList?.reduce((pre, cur) => pre + cur.likeCount, 0) || 0);
const downAllCount = computed(() => bookDetial?.resourceList?.reduce((pre, cur) => pre + cur.downloadCount, 0) || 0);
// tabs栏
const activeName = ref("read");
function downloadResource(resource: ResourceVO) {
  if (!user.isLogin) {
    ElMessage.error("没有权限，请先登录！");
    user.toLogin(); // 登录后再下载
    return;
  }
  downloadFile(BaseUrlFile + resource.url, resource.title, {
    Authorization: user.getToken,
  }, () => {
    ElMessage.success("正在下载中，请稍后查看");
  });
}

// 点赞
const actionResorceMap = useSessionStorage(`${user.userId}_actionResorceMap`, {

}) as any;
async function likeResource(item: ResourceVO) {
  // if (actionResorceMap.value[item.resourceId]) {
  //   ElMessage.warning("一天只能点赞一次哟！");
  //   return;
  // }
  const res = await useAddResourceLikeApi(item.resourceId, user.token);
  if (res.code !== ResultStatus.SUCCESS) {
    return;
  }
  ElMessage.success("点赞成功！");
  item.likeCount++;
  actionResorceMap.value[item.resourceId] = true;
}
</script>

<template>
  <div class="relative py-4">
    <section class="grid cols-1 gap-4 py-4 sm:cols-[3fr_4fr] sm:gap-10">
      <!-- 左侧 -->
      <div w-full flex-row-c-c flex-col truncate>
        <CardNuxtImg
          class="h-64 w-45 shadow-sm border-default card-default-br"
          :default-src="bookDetial?.coverImageUrl"
        >
          <template #error>
            <small class="h-full w-full flex flex-row items-center justify-center">
              无图片
            </small>
          </template>
        </CardNuxtImg>
        <h1 mb-1 mt-2 max-w-full truncate text-center text-lg font-500>
          {{ bookDetial?.title }}
        </h1>
        <div max-w-full truncate text-center text-primary text-small>
          {{ bookDetial?.author }}
        </div>
        <!-- 指数卡片 -->
        <div class="mt-4 w-full overflow-hidden p-4 border-default dark:(border-default-t rounded-0 bg-transparent) card-default">
          <div text-small>
            推荐指数：{{ (likeAllCount + downAllCount) || "暂无" }}
          </div>
          <div mt-2 flex>
            <el-progress :width="60" type="dashboard" class="mr-2" :percentage="(likeAllCount + downAllCount) || 0">
              <template #default="{ percentage }">
                <span class="text-sm">{{ percentage }}</span>
              </template>
            </el-progress>
            <div class="right flex-1 text-mini">
              <div class="mt-2 flex items-center">
                <i i-solar:like-broken />
                <Progress :model-value="likeAllCount" :max="(likeAllCount + downAllCount) || 100" class="ml-2 h-2 flex-1" />
              </div>
              <div class="mt-2 flex items-center">
                <i i-solar:download-minimalistic-outline />
                <Progress :model-value="downAllCount" :max="(likeAllCount + downAllCount) || 100" class="ml-2 h-2 flex-1" />
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 描述 -->
      <div flex-row-c-c flex-col gap-2 text-center>
        <div truncate text-1.6rem font-500>
          {{ bookDetial?.title }}
        </div>
        <div mt-4 max-w-full truncate text-small-color>
          作者：{{ bookDetial?.author || "-" }}
        </div>
        <div mt-2 max-w-full truncate text-small-color>
          出版社：{{ bookDetial?.publisher || "-" }}
        </div>
        <div mt-2 max-w-full truncate text-small-color>
          ISBN：<span btn-primary @click="useCopyText(bookDetial?.isbn || '', true)">{{ bookDetial?.isbn || "-" }}</span>
        </div>
        <div mt-2 max-w-full truncate text-small>
          发表时间：{{ bookDetial?.publishionDate || "-" }}
        </div>
        <div mt-4 max-w-full truncate text-lg text-warning font-600>
          价格：<small>￥</small>{{ bookDetial?.price }}
        </div>
        <div class="mt-4 pt-4 border-default-t">
          <BtnElButton icon-class="i-solar:eye-outline mr-2" transition-icon @click="activeName = 'resorce'">
            下载资源
          </BtnElButton>
          <BtnElButton icon-class="i-solar:command-bold mr-2" transition-icon type="primary" class="border-default" @click="activeName = 'read'">
            查看摘要
          </BtnElButton>
        </div>
      </div>
    </section>
    <el-tabs
      v-model="activeName"
      class="mt-4 pb-10vh pb-12"
    >
      <el-tab-pane label="内容摘要" name="read" class="tab-pane">
        <div
          data-fade style="--anima: latter-slice-blur-top;"
          class="card-rounded-df p-4 border-default"
        >
          {{ bookDetial?.introduction }}
        </div>
      </el-tab-pane>
      <el-tab-pane lazy label="课程资源" name="resorce" class="tab-pane">
        <ul class="grid cols-2 gap-2 sm:cols-3 sm:gap-4" data-fade style="--anima: latter-slice-blur-top;">
          <li
            v-for="item in bookDetial?.resourceList"
            :key="item.resourceId"
            :title="`${item.title}- 点击下载`"
            class="flex cursor-pointer items-center card-rounded-df p-2 border-hover-primary card-default sm:p-3"
            @click="downloadResource(item)"
          >
            <CardNuxtImg
              class="mr-2 h-8 w-8"
              :src="FILE_TYPE_ICON_MAP[item.type] || FILE_TYPE_ICON_DEFAULT"
            >
              <template #error>
                <small class="h-full w-full flex flex-row items-center justify-center">
                  无图片
                </small>
              </template>
            </CardNuxtImg>
            <div class="w-full truncate text-sm">
              {{ item.title?.replace(/(.{16}).*(.{5})/, '$1...$2') }}
              <div mt-1 truncate text-mini>
                大小：{{ formatFileSize(item.size) }}
                下载：{{ item.downloadCount }}
                点赞：{{ item.likeCount }}
              </div>
            </div>
            <!-- 点赞 -->
            <i :title="actionResorceMap[`${item.resourceId}`] ? '已点赞' : '点赞'" :class="actionResorceMap[`${item.resourceId}`] ? 'i-solar:like-bold text-danger' : 'i-solar:like-broken'" class="block h-6 w-6 btn-danger" @click.stop.prevent.capture="likeResource(item)" />
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
      <el-tab-pane label="评论区" lazy name="comment" class="tab-pane">
        <BookCommentView
          :book-id="bookDetial?.bookId"
          data-fade style="--anima: latter-slice-blur-top;"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style lang="scss" scoped>
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
