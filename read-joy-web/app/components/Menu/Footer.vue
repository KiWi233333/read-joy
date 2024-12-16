<script lang="ts" setup>
</script>

<template>
  <footer class="footer py-6 border-default-t bg-color-second">
    <div class="layout-default">
      <div class="flex items-center gap-4 sm:gap-10">
        <NuxtLink to="/" class="logo">
          <img alt="logo-text" src="/maskable-icon.png" class="h-9 w-9 select-none object-cover border-default card-default">
        </NuxtLink>
        <a href="https://kiwi233.top" class="logo">
          <img alt="logo-text" src="https://kiwi233.top/logo.png" class="h-9 w-9 select-none object-cover border-default card-default !bg-light">
        </a>
        <ul ml-a flex flex-col sm:flex-row>
          <li mr-4 pr-4 sm:border-default-r sm:pr-4 text-small>
            邮箱：
            <a btn-primary target="_blank" href="mailto:kiwi2333@qq.com">kiwi2333@qq.com</a>
          </li>
          <li mr-4 pr-4 sm:border-default-r sm:pr-4 text-small>
            QQ：
            <a btn-primary href="javascript:;">1329634286</a>
          </li>
          <li text-small>
            <a btn-primary target="_blank" href="https://kiwi233.top">关于</a>
          </li>
        </ul>
      </div>
      <div mt-5 pt-5 text-center border-default-t text-small>
        © 1998 - 2024 ReadJoy Inc. All Rights Reserved
      </div>
    </div>
  </footer>
</template>

<style lang="scss" scoped>
</style>
