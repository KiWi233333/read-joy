<script lang="ts" setup>
import type { AdminHomeStatisticsVO } from "~/composables/api/admin/res";
import { useAdminStore } from "~/composables/sotre/useAdminStore";

const { totalData } = defineProps<{ totalData?: Partial<AdminHomeStatisticsVO> }>();

const admin = useAdminStore();
const getNowStatus = computed(() => {
  return useNowDateText(new Date());
});

function useNowDateText(date: Date = new Date()): string {
  const hours = date.getHours();
  let res = "";
  if (hours > 0 && hours < 6)
    res = "😴";
  else if (hours < 12)
    res = "早上好 🏆";
  else if (hours === 12)
    res = "中午好 🍕";
  else if (hours > 12 && hours < 18)
    res = "下午好 ☕";
  else
    res = "晚上好 🌜";
  return res;
}
</script>

<template>
  <div flex items-center>
    <img alt="logo-text" src="/maskable-icon.png" class="mr-4 h-12 w-12 select-none object-cover card-default border-default-hover">
    <div leading-2rem>
      <h3>
        Hi {{ admin?.adminInfo?.username }},
      </h3>
      <h2>
        欢迎回来，{{ getNowStatus }}
      </h2>
    </div>
    <div ml-a min-w-6rem cursor-pointer px-4 py-2 card-default border-default-hover @click="totalData?.pendingCommentCount && navigateTo('/admin/comment')">
      <h1>待办</h1>
      <small :class="{ 'cursor-pointer text-danger': totalData?.pendingCommentCount }">
        {{ totalData?.pendingCommentCount ? `${totalData?.pendingCommentCount}条评论待审` : "暂无更多" }}
      </small>
    </div>
  </div>
</template>

<style scoped lang="scss">

</style>
