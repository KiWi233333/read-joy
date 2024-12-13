
<script setup lang="ts">
import type { DateRange } from "radix-vue";
import type { DatePreloadItem } from "~/composables/utils/useUtils";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { RangeCalendar } from "@/components/ui/range-calendar";
import {
  CalendarDate,
  getLocalTimeZone,
  today,
} from "@internationalized/date";
import { dayjs } from "element-plus";
import { Calendar as CalendarIcon } from "lucide-vue-next";

const {
  modelValue,
  calendarProps,
  popoverProps,
  placeholder = "选择日期范围",
  btnProps = {
    class: "w-[280px]  flex justify-start text-left font-normal",
  },
  rangeSeparator = " - ",
  size = "sm",
  local = "zh-CN",
  maxCalendarMonths = 1,
  preloadDates,
  format = "YYYY-MM-DD",
} = defineProps<{
  modelValue?: string[]
  preloadDates?: DatePreloadItem[]
  placeholder?: string
  maxCalendarMonths?: number
  format?: string
  rangeSeparator?: string
  size?: "default" | "sm" | "lg" | "icon" | null | undefined
  local?: string

  btnProps?: any
  popoverProps?: any
  calendarProps?: any
}>();
const emits = defineEmits(["update:modelValue"]);

const value = computed <DateRange | undefined> ({
  get() {
    if (modelValue?.length === 2) {
      const start = dayjs(modelValue[0]);
      const end = dayjs(modelValue[1]);
      return {
        // 传入年月日
        start: new CalendarDate(start.year(), start.month(), start.date()),
        end: new CalendarDate(end.year(), end.month(), end.date()),
      };
    }
    else {
      return undefined;
    }
  },
  set(newValue) {
    if (newValue?.start && newValue?.end) {
      const start = newValue?.start?.toDate?.(getLocalTimeZone());
      const end = newValue?.end?.toDate?.(getLocalTimeZone());
      emits("update:modelValue", [
        start ? dayjs(start).format(format) : undefined,
        end ? dayjs(end).format(format) : undefined,
      ]);
    }
  },
});

const btnSizeMap: Record<string, "" | "default" | "small" | "large"> = {
  sm: "small",
  lg: "large",
  default: "default",
};
</script>

<template>
  <Popover>
    <PopoverTrigger as-child>
      <el-button
        :size="size ? btnSizeMap[size] : ''"
        :class="{
          'text-muted-foreground flex': !value,
          [`${btnProps?.class}`]: btnProps?.class,
        }"
        type="default"
        v-bind="btnProps"
      >
        <slot name="icon">
          <CalendarIcon class="mr-2 h-4 w-4" />
        </slot>
        <template v-if="value?.start">
          <template v-if="value?.end">
            {{ dayjs(value?.start.toDate(getLocalTimeZone())).format(format) }}{{ rangeSeparator || ' - ' }}{{ dayjs(value?.end.toDate(getLocalTimeZone())).format(format) }}
          </template>
          <template v-else>
            {{ dayjs(value?.start.toDate(getLocalTimeZone())).format(format) }}
          </template>
        </template>
        <template v-else>
          {{ placeholder }}
        </template>
      </el-button>
    </PopoverTrigger>
    <PopoverContent class="w-auto p-0 card-default-br" v-bind="popoverProps">
      <slot name="popper-header" />
      <RangeCalendar
        v-model="value"
        :min-value="new CalendarDate(1900, 1, 1)"
        class="bg-transparent"
        :locale="local"
        :number-of-months="maxCalendarMonths"
        v-bind="calendarProps"
      />
      <slot name="popper-footer" :preload-dates="preloadDates">
        <template v-if="preloadDates?.length">
          <div class="pt- grid cols-3 gap-2 p-4">
            <el-button
              v-for="(item, index) in preloadDates" :key="index"
              style="margin-left: 0;"
              plain
              size="small"
              @click="$emit('update:modelValue', [dayjs(item.value()[0]).format(format), dayjs(item.value()[1]).format(format)])"
            >
              {{ item.text }}
            </el-button>
          </div>
        </template>
      </slot>
    </PopoverContent>
  </Popover>
</template>

<style scoped lange="scss">
</style>
