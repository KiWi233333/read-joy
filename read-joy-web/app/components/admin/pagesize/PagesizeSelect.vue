<script lang="ts" setup>
const {
  appendOptions,
} = defineProps<{
  modelValue?: number
  appendOptions?: [{ label: string; value: number }]
}>();
const emit = defineEmits(["update:modelValue"]);


const options = ref([
  { label: "每页5条", value: 5 },
  { label: "每页10条", value: 10 },
  { label: "每页20条", value: 20 },
  { label: "每页50条", value: 50 },
  { label: "每页100条", value: 100 },
  ...(appendOptions || []),
]);
defineExpose({
  options,
});
</script>

<template>
  <el-select
    :model-value="modelValue"
    filterable
    allow-create
    default-first-option
    :reserve-keyword="false"
    placeholder="选择每页个数"
    @update:model-value="emit('update:modelValue', $event)"
  >
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
</template>

<style scoped lang="scss"></style>
