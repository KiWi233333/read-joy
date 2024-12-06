<template>
  <header class="border-default-b dark:shadow-sm">
    <div class="mx-a flex-row-bt-c py-2 container sm:py-4">
      <NuxtImg src="logo-text.png" class="block h-8 dark:hidden" />
      <NuxtImg src="logo-text-dark.png" class="hidden h-8 dark:block" />
      <div flex items-center gap-2 sm:gap-4>
        <NuxtLink to="login">
          注册
        </NuxtLink>
        <NuxtLink to="login">
          登录
        </NuxtLink>
        <ButtonToggleTheme />
      </div>
    </div>
  </header>
</template>
