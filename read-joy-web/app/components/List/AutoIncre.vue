<script lang="ts" setup>
const {
  noMore = false,
  immediate = true,
  loading = false,
  autoStop = true,
  delay = 400,
  offset = 10,
  loadingClass = "mx-a my-0.6em h-1.2em w-1.2em animate-[spin_2s_infinite_linear] rounded-6px bg-[var(--el-color-primary)]",
  appendLoadingClass = "",
} = defineProps<{
  noMore?: boolean
  immediate?: boolean
  delay?: number
  loading?: boolean
  loadingClass?: string
  appendLoadingClass?: string
  ssr?: boolean
  autoStop?: boolean
  offset?: number
}>();
const emit = defineEmits(["load"]);
// 停止加载
const loadMoreRef = ref();
const isSee = ref(immediate);
// 首次执行
onMounted(() => {
  if (immediate)
    emit("load");
});
// 定时器
let timer: any = null;
// 刷新
const { stop, isSupported } = useIntersectionObserver(
  loadMoreRef,
  ([obj]) => {
    isSee.value = !!obj?.isIntersecting;
    callBack();
  },
);

// 监听
watch(isSee, (val) => {
  if (!val)
    return clearTimeout(timer);
  callBack();
});


function callBack() {
  if (loading && isSee.value) {
    emit("load");
    timer = setTimeout(callBack, delay);
  }
  else if (noMore && autoStop) {
    stop && stop();
    clearTimeout(timer);
  }
}

onUnmounted(() => {
  clearTimeout(timer);
  stop();
  timer = null;
});
defineExpose({
  stop,
  loadMoreRef,
});
</script>

<template>
  <slot name="default" />
  <!-- 加载 -->
  <div
    v-if="loading"
    ref="loadMoreRef"
    key="loadMoreRef"
    class="z-9 min-h-1em"
  >
    <slot name="load">
      <div key="load" w-full flex-row-c-c py-2 text-center text-bluegray>
        <div
          :class="`${loadingClass} ${appendLoadingClass}`"
        />
      </div>
    </slot>
  </div>
  <!-- 完成 -->
  <div v-else-if="!loading" :style="{ minHeight: `${offset}px` }">
    <slot name="done">
      <div key="done" class="animate-fade-in" w-full text-center text-bluegray @click="!isSupported && $emit('load')" />
    </slot>
  </div>
</template>
