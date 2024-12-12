<script lang="ts" setup>
const {
  noMore = false,
  immediate = true,
  loading = false,
  autoStop = true,
  delay = 400,
  offset = 10,
  loadingParentClass = "min-h-4",
  loadingClass = "mx-a my-0.6em h-1.4rem w-1.4rem animate-spin animate-[spin_2s_infinite_linear] rounded-6px bg-[var(--el-color-primary)]",
  appendLoadingClass = "",
} = defineProps<{
  noMore?: boolean
  immediate?: boolean
  delay?: number
  loading?: boolean
  loadingClass?: string
  loadingParentClass?: string
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
  if (!loading && isSee.value) {
    emit("load");
    timer = setTimeout(callBack, delay);
  }
  else if (noMore && autoStop) {
    stop && stop();
    clearTimeout(timer);
  }
}

const showLoad = computed(() => !noMore && !loading);
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
    v-show="showLoad"
    ref="loadMoreRef"
    key="load-more-dom"
    :class="loadingParentClass"
  >
    <slot name="load">
      <div key="load" w-full flex-row-c-c text-center text-bluegray>
        <div
          :class="`${loadingClass} ${appendLoadingClass}`"
        />
      </div>
    </slot>
  </div>
  <!-- 完成 -->
  <div v-if="noMore" key="load" :style="{ minHeight: `${offset}px` }">
    <slot name="done">
      <div key="done" class="animate-fade-in" w-full text-center text-bluegray @click="!isSupported && $emit('load')" />
    </slot>
  </div>
</template>
