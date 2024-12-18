
<script lang="ts" setup>
import { type AdminHomeStatisticsVO, selectHomeStatistics } from "~/composables/api/admin/res";
import { CommentSortOrder } from "~/composables/api/comment";
import { ResultStatus } from "~/composables/api/types/result";
import { useAdminStore } from "~/composables/sotre/useAdminStore";

const homeStatistics = ref<Partial<AdminHomeStatisticsVO>>({

});
const admin = useAdminStore();
const isLoding = ref(false);
async function loadData() {
  isLoding.value = true;
  const res = await selectHomeStatistics({}, admin.getToken);
  if (res.code === ResultStatus.SUCCESS) {
    homeStatistics.value = res.data;
  }
  isLoding.value = false;
}
loadData();
</script>

<template>
  <div
    flex
    flex-col
    class="tracking-0.1em"
  >
    <AdminCardWelcomeCard
      :total-data="homeStatistics"
      class="mb-4 p-4 shadow-sm sm:mb-8 card-default"
    />
    <!-- 主内容 -->
    <div
      grid="~ cols-1 gap-8 items-start"
      lg:grid="~ cols-[2fr_1fr]  gap-8 "
      mb-4 md:grid-cols-1
      class="content"
    >
      <!-- 左侧 -->
      <div
        class="left"
        grid="~ cols-1 gap-8"
      >
        <!-- 数据统计本月 -->
        <AdminTotalDesTotal :data="homeStatistics" class="box-content" />
        <!-- 图书发表统计 -->
        <AdminTotalBookPublishTable
          title="出版统计"
          :data="homeStatistics" class="box-content h-40vh sm:h-400px border-default-hover"
        />
      </div>
      <!-- 右侧 -->
      <div
        grid="~ cols-1 gap-8 "
        class="right"
      >
        <AdminTotalBooksPie
          chart-class="w-300px h-400px flex-row-c-c"
          :data="homeStatistics"
          class="flex-row-c-c pb-10 card-default border-default-hover"
        />
        <!-- 最新评论 -->
        <el-scrollbar height="500px" wrap-class="p-4 sm:p-6" class="box-content card-default border-default-hover">
          <AdminListCommentList
            :dto="{
              page: 1,
              size: 15,
              sortOrder: CommentSortOrder.DESC,
            }"
            :limit="15"
            :show-load="false"
            :show-more-text="true"
            :ssr="true"
            :animated="false"
          >
            <template #header>
              <h3 class="mb-4 text-1rem font-bold">
                最新评论
              </h3>
            </template>
          </AdminListCommentList>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.box-content {
  --at-apply: "";
}
.left :deep(.el-scrollbar__thumb) {
  display: none;
}
</style>
