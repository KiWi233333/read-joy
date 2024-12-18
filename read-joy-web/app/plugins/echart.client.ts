import { BarChart, LineChart, PieChart } from "echarts/charts";
import {
  DatasetComponent, // 数据集组件
  DataZoomComponent,
  GridComponent,
  LegendComponent, // 头部标签
  TitleComponent,
  ToolboxComponent,
  TooltipComponent,
  TransformComponent, // 内置数据转换器组件 (filter, sort)
} from "echarts/components";
// echart
import * as echarts from "echarts/core";
import { LabelLayout, UniversalTransition } from "echarts/features";
import { SVGRenderer } from "echarts/renderers";


export const colors = {
  // 全局调色盘。
  color: [
    "#3583fd",
    "#67c23a",
    "#ffbf11f1",
    "#ff4545",
    "#ff2323f6",
    "#85ff48",
    "#0bdb85",
    "#3559f8",
  ],

  series: [
    {
      type: "bar",
      // 此系列自己的调色盘。
      color: [
        "#3583fd",
        "#67c23a",
        "#ffbf11f1",
        "#ff4545",
        "#ff2323f6",
        "#85ff48",
        "#0bdb85",
        "#3559f8",
      ],
    },
    {
      type: "pie",
      // 此系列自己的调色盘。
      color: [
        "#3583fd",
        "#67c23a",
        "#ffbf11f1",
        "#ff4545",
        "#ff2323f6",
        "#85ff48",
        "#0bdb85",
        "#3559f8",
      ],
    },
  ],
};

echarts.use([
  BarChart,
  LineChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  ToolboxComponent,
  DataZoomComponent,
  GridComponent,
  DatasetComponent,
  TransformComponent,
  LabelLayout,
  LegendComponent,
  UniversalTransition,
  SVGRenderer,
]);
export default defineNuxtPlugin((nuxtApp) => {
});
