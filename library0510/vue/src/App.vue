<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<style>
/* 隐藏滚动条，但仍然可以滚动 */
html {
  scrollbar-width: none;  /* 对于Firefox */
}

html::-webkit-scrollbar {
  display: none;  /* 对于Chrome、Safari和Opera */
}
</style>