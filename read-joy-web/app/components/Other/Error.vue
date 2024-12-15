<script lang="ts" setup>
const props = withDefaults(defineProps<{
  msg: string
  icon?: string
}>(), {
  // @unocss-include
  icon: "i-solar:bone-broken-line-duotone text-color-danger",
});
function onRefresh() {
  if (window && window?.location)
    window?.location?.reload();
}
const router = useRouter();
function onBack(rollback?: string) {
  if (rollback) {
    navigateTo({
      path: rollback,
      replace: true,
    });
  }
  else {
    router.back();
  }
}
</script>

<template>
  <div data-fades relative>
    <i class="mx-a mb-4 block h-8rem w-8rem opacity-60" :class="icon" />
    <div
      flex-row-c-c
      flex-col
    >
      <h3 mb-5 opacity-80>
        {{ msg }}
      </h3>
      <div class="mx-a opacity-90">
        <slot name="footer">
          <BtnElButton
            type="primary"
            transition-icon plain
            icon-class="i-solar:alt-arrow-left-line-duotone mr-2"
            @click="onBack($route.query?.rollback ? String($route.query?.rollback) : '')"
          >
            返回
          </BtnElButton>
          <BtnElButton
            type="primary"
            transition-icon
            icon-class="i-solar:refresh-line-duotone mr-2"
            @click="onRefresh"
          >
            刷新
          </BtnElButton>
        </slot>
      </div>
    </div>
  </div>
</template>

