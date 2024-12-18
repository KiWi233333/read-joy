<script lang="ts" setup>
import type { AdminHomeStatisticsVO } from "~/composables/api/admin/res";

const {
  data: total,
} = defineProps<{ data: Partial<AdminHomeStatisticsVO> }>();

// 首页数据统计 图书总量、用户数量、总下载量、资源总数、总评论数
function getPercent(num = 0): number {
  if (num > 100) {
    num /= 10;
    return getPercent(num);
  }
  return num;
}
</script>

<template>
  <section class="group relative grid grid-(cols-2 gap-4) lg:grid-(cols-4 gap-6) md:grid-(cols-3 gap-8)">
    <!-- 图书总量/本 -->
    <NuxtLink to="/admin/book" title="前往图书管理" class="grid-card relative flex-col justify-around">
      <small tracking-0.2em>图书总量/本</small>
      <div class="mt-2 text-1.7rem text-[var(--el-color-info)] font-800">
        {{ total?.bookCount || 0 }}
        <i class="animate-icon i-solar:book-bookmark-outline" />
      </div>
      <small class="flex items-baseline text-0.8em tracking-0.2em">
        覆盖<span class="text-info">{{ total?.bookCategoryCount || 0 }}</span>种类别
      </small>
    </NuxtLink>
    <!-- 用户总数 -->
    <NuxtLink to="/admin/user" title="前往用户管理" class="grid-card">
      <AdminTotalProgress
        color="var(--el-color-success)" title="用户总数" :percentage="getPercent(total?.userCount)"
        :count="total?.userCount || 0" fix-count="人"
      >
        <i class="animate-icon i-solar:user-outline text-success" />
      </AdminTotalProgress>
    </NuxtLink>
    <!-- 待审核评论 -->
    <NuxtLink to="/admin/comment" title="前往评论管理" class="grid-card">
      <AdminTotalProgress
        color="var(--el-color-danger)"
        title-class="!text-danger"
        title="待审评论"
        :percentage="+((total?.pendingCommentCount || 0) / (total?.totalCommentCount || 1) * 100).toFixed(2)"
      >
        <i class="animate-icon i-solar:chat-line-broken text-danger" />
        <template #count>
          {{ total?.pendingCommentCount }}<small text-small>/{{ total?.totalCommentCount }}</small>
        </template>
        <template #tip>
          {{ total?.rejectionCommentCount || 0 }}已驳回
        </template>
      </AdminTotalProgress>
    </NuxtLink>
    <!-- 资源总数 -->
    <NuxtLink to="/admin/resource" title="前往资源管理" class="grid-card">
      <AdminTotalProgress
        color="var(--el-color-warning)" title="资源总数"
        :percentage="getPercent(total?.resourceCount)" :count="total?.resourceCount || 0" fix-count="件"
      >
        <i class="animate-icon i-solar:cloud-file-outline text-warning" />
        <template #tip>
          <i i-solar:download-minimalistic-outline mr-2 p-2 />{{ total?.totalDownloadCount || 0 }}次
        </template>
      </AdminTotalProgress>
    </NuxtLink>
  </section>
</template>

<style lang="scss" scoped>
.grid-card {
  --at-apply: "flex relative card-default truncate border-default-hover select-none rounded-12px p-4 ";

  .animate-icon {
    --at-apply: "scale-120 -rotate-45 translate-x-1/3 translate-y-1/3  absolute right-0 bottom-0 p-6 op-40 transition-all";
  }
  &:hover {
    .animate-icon {
      --at-apply: "scale-100 rotate-0 translate-x-0 translate-y-0 absolute right-4 bottom-8 op-100";
    }
  }
}

@keyframes rotate-anim {
  from {
    opacity: 0;
    transform: translateX(100%) scale(0.5) rotate(-180deg);
    transform-origin: right center;
  }

  to {
    opacity: 1;
    transform: translateX(0%) scale(1) rotate(-0);
  }
}
</style>
