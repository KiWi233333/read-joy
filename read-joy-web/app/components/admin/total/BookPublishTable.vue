<script lang="ts" setup>
import type { AdminHomeStatisticsVO, BookPublishTotal } from "~/composables/api/admin/res";

const {
  data,
  title = "出版统计",
} = defineProps<{
  title?: string;
  data: Partial<AdminHomeStatisticsVO>
}>();
// 主题
const colorMode = useColorMode();
provide(THEME_KEY, colorMode);
provide(INIT_OPTIONS_KEY, {
  renderer: "svg",
  ssr: false,
});
const options: Option[] = [
  { label: "按日", value: "day" },
  { label: "按月", value: "month" },
  { label: "按年", value: "year" },
];
const selectSortMode = ref("month");

interface Option {
  label: string;
  value: "day" | "month" | "year";
}

const sourceData = computed(() => {
  if (selectSortMode.value === "day")
    return data.bookPublishTotalByDayList || [];
  else if (selectSortMode.value === "month")
    return data.bookPublishTotalByMonthList || [];
  else if (selectSortMode.value === "year")
    return data.bookPublishTotalByYearList || [];
  return [];
}) as ComputedRef<BookPublishTotal[]>;

const isLoading = ref(false);
const timer: number | undefined = -1;

// 筛选select
function onChangeType(res: string) {
  isLoading.value = true;
  selectSortMode.value = res;
  isLoading.value = false;
}

// echarts配置 图表数据
const option = ref({
  color: ["#3583fd", "#11e08a", "#6e53bb", "#56c220"],
  title: {
    textStyle: {
      fontFamily: "Alimama",
    },
  },
  tooltip: { trigger: "axis" },
  toolbox: {
    feature: {
      saveAsImage: {},
    },
  },
  xAxis: {
    type: "category",
    data: computed(() => sourceData.value.map(item => item.date) || []),
  },
  yAxis: {
    name: "出版数量",
    type: "value",
    splitLine: {
      lineStyle: {
        color: "#8d8d8d",
        opacity: 0.2,
      },
    },
  },
  backgroundColor: "transparent",
  dataZoom: { type: "inside" },
  grid: {
    top: "15%",
    left: "30rem",
    right: "30rem",
    bottom: "8%",
    containLabel: false,
  },
  legend: {
    data: ["出版图书"],
    type: "scroll",
    zlevel: "1",
    itemStyle: {
      borderCap: "round",
      borderType: "dashed",
    },
    textStyle: {
      color: computed(() => colorMode.value === "dark" ? "#fff" : "#000"),
      fontFamily: "AliMama",
    },
  },
  series: [
    {
      name: "出版图书",
      data: computed(() => sourceData.value.map(item => item.total) || []),
      type: "line",
      smooth: true,
      color: "#3583fd",
      areaStyle: {
        color: {
          type: "linear",
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0,
            color: "rgba(47, 126, 253, 0.4)", // 0% 处的颜色
          }, {
            offset: 1,
            color: "rgba(47, 126, 253, 0)", // 100% 处的颜色
          }],
          global: false, // 缺省为 false
        },
      },
    },
  ],
});

onUnmounted(() => {
  timer && clearInterval(timer);
});
</script>

<template>
  <div
    class="group overflow-hidden p-4 card-default"
  >
    <div
      flex-row-c-c
      justify-between
    >
      <h3 class="text-1rem font-bold">
        {{ title }}
      </h3>
      <div class="w-6rem flex opacity-0 transition-200 group-hover:opacity-100">
        <el-select
          :model-value="selectSortMode"
          size="small"
          placeholder="筛选"
          class="flex-shrink-0 text-center"
          @change="onChangeType"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
            {{ item.label }}
          </el-option>
        </el-select>
      </div>
    </div>
    <section
      class="h-full flex-row-c-c pb-4"
    >
      <VChart
        v-if="sourceData.length"
        key="infoList"
        :autoresize="{ throttle: 500 }"
        :option="option"
        class="h-9/10 w-full"
      />
      <div
        v-else-if="!isLoading"
        key="small-empty"
        class="truncate text-center opacity-80"
      >
        暂无数据
      </div>
    </section>
  </div>
</template>
