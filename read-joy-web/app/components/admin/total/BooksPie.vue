<script lang="ts" setup>
import type { AdminHomeStatisticsVO } from "~/composables/api/admin/res";
import { type EChartsOption, format } from "echarts";
import { colors } from "~/plugins/echart.client";


const { data } = defineProps<{
  data?: Partial<AdminHomeStatisticsVO>
  chartClass?: string
}>();
const colormode = useColorMode();
provide(THEME_KEY, colormode);
provide(INIT_OPTIONS_KEY, {
  renderer: "svg",
  width: "300px",
  height: "400px",
});
const option = ref<EChartsOption>({
  color: colors.color,
  backgroundColor: "transparent",
  tooltip: {
    trigger: "item",
  },
  legend: {
    bottom: 10,
    left: "center",
    textStyle: {
      color: "var(--el-text-color-primary)",
      fontFamily: "AliMama",
    },
  },
  series: [
    {
      name: "图书统计",
      type: "pie",
      radius: ["40%", "80%"],
      avoidLabelOverlap: true,
      itemStyle: {
        borderRadius: 10,
      },
      label: {
        show: true,
        position: "center",
        fontSize: 18,
        fontWeight: 800,
        fontFamily: "AliMama",
      },
      labelLine: {
        show: true,
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: "rgba(0, 0, 0, 0.4)",
        },
        label: {
          show: true,
          fontSize: 18,
          fontWeight: 800,
          fontFamily: "AliMama",
        },
      },
      data: computed(() => (data?.bookCategoryTotalList?.map(item => ({ name: item.categoryName, value: item.bookCount })) || []).filter(item => item.value > 0)),
    },
  ] as any,
});
</script>

<template>
  <div>
    <VChart
      :autoresize="{ throttle: 500 }"
      :option="option"
      :class="chartClass"
    />
  </div>
</template>

<style scoped lang="scss"></style>
