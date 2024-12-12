<script lang="ts" setup>
// 搜索
const props = defineProps<{
  modelValue: string
  inputAttrs?: any
}>();
const emit = defineEmits(["update:modelValue", "open", "close"]);
const inputRef = ref();
// 实例
const word = computed({
  get(): string {
    return props.modelValue;
  },
  set(v: string) {
    emit("update:modelValue", v);
  },
});

function onSearch() {
  if (!word.value.trim())
    return ElMessage.warning("搜索内容不能为空！");

  emit("open");
  navigateTo({
    path: "/search",
    query: {
      keyword: word.value.trim(),
    },
  });
}

function keySearch(e: KeyboardEvent) {
  if (e.ctrlKey && e.key === "k") {
    e.preventDefault();
    emit("open");
    inputRef.value?.focus && inputRef.value?.focus();
  }
}
// 注册快捷键
onMounted(() => {
  window.addEventListener("keydown", keySearch);
});
onUnmounted(() => {
  window.removeEventListener("keydown", keySearch);
});
defineExpose({
  inputRef,
});
</script>

<template>
  <div class="group relative z-9 flex items-center" @click="inputRef?.focus">
    <ElInput
      ref="inputRef"
      v-model.trim="word"
      type="text"
      :prefix-icon="ElIconSearch"
      v-bind="inputAttrs"
      placeholder="搜索"
      class="search mx-1 hidden sm:block"
      @keyup.enter="onSearch"
      @focus="$emit('open')"
      @keyup.esc="() => {
        inputRef?.blur()
        $emit('close')
      }"
    />
    <el-button
      v-show="word !== ''"
      type="primary"
      class="btn absolute right-0 group-hover:(scale-100 opacity-100) md:(scale-0 opacity-0) border-default"
      :class="{ 'sm:(!scale-100 !opacity-100)': word }"
      round
      style="font-size: 0.5em;height: 60%;margin: 0 0.8rem;box-shadow: none;"
      @click.prevent="onSearch()"
    >
      搜索
    </el-button>
  </div>
</template>

<style lang="scss" scoped>
$input-width: min(50vw, 22rem);
$scle-input-width: min(70vw, 32rem);
.search {
  :deep(.el-input__wrapper) {
    width: $input-width;
    transition: width $transition-delay;
    transition-timing-function: $animate-cubic-bount;
    border-radius: 20px;
    font-weight: 500;
    height: 2rem;
    font-size: 14px;
    border: 0;
    background-color: #f3f3f3c2;
    box-shadow: rgba(0, 0, 0, 0.02) 0px 1px 2px 0px, rgba(27, 31, 35, 0.15) 0px 0px 0px 1px;
    input::placeholder {
      letter-spacing: 0.3em;
      transition: width $transition-delay;
      color: var(--el-text-color-placeholder);
    }
    input:focus::placeholder {
      color: var(--el-color-primary-light-5);
    }
    input:focus,
    input:valid,
    &.is-focus {
      width: $scle-input-width;
    }
    &.is-focus {
      box-shadow: rgba(0, 0, 0, 0.2) 0px 18px 50px -10px;
    }
    .el-input__suffix {
      position: absolute;
      right: 3%;
    }
  }
}
.dark .search :deep(.el-input__wrapper) {
  background-color: rgb(15, 15, 15);
  box-shadow: inset 0 0 2px 1px rgb(15, 15, 15), inset 0 0 2px 1px rgb(15, 15, 15);
  input::placeholder {
    color: #ffffffb3;
  }
}
@media (max-width: 768px) {
  .search {
    :deep(.el-input__wrapper) {
      width: 2rem;
      height: 2rem;
      font-size: 0.8em;
      transition: all $transition-delay;
      transform: translateY(-4rem);
      &.is-focus {
        transform: none;
        .el-input__inner {
          padding: 0 4rem 0 0.5rem;
        }
      }
      .el-input__prefix-inner .el-input__icon {
        margin: auto;
      }
    }
    :deep(.el-input__wrapper input.el-input__inner) {
      width: 100%;
    }
  }
}
</style>
