<script lang="ts" setup>
const props = defineProps<{
  menu: MenuButtonType[]
}>();
const {
  menu = () => [
    { show: true, title: "筛选", icon: "i-solar:sort-from-top-to-bottom-outline", type: "default" },
    { show: true, title: "刷新", icon: "i-solar:refresh-outline", type: "info" },
  ],
} = props;

export interface MenuButtonType {
  title: string
  icon?: string | object | Array<string | object>
  type?: "" | "default" | "info" | "text" | "success" | "warning" | "primary" | "danger"
  plain?: boolean
  show?: boolean
  class?: string
  btnProps?: Record<string, any>
  methods?: () => any
}


const menuList = computed(() => {
  return props.menu?.filter(p => p?.show);
});
</script>

<template>
  <el-button
    v-for="p in menuList"
    :key="p.title"
    class="group ml-12px w-full tracking-0.2em transition-300 md:ml-0 md:w-auto"
    :plain="p.plain !== undefined ? p.plain : true "
    :type="p.type"
    :class="p?.class"
    v-bind="p.btnProps || {}"
    @click="() => p.methods && p.methods()"
  >
    <i v-if="p.icon" class="mr-1 p-0.6em" :class="p.icon" />
    <div mr-1>
      {{ p.title }}
    </div>
  </el-button>
</template>

