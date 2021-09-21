<template>
  <div>
    <div>
      <router-view></router-view>
    </div>
  </div>

</template>

<script>

export default {
  name: 'App',

}
</script>

<style>
* {
  margin: 0;
  padding: 0;
}
body {
  overflow: auto;
  height: 100%;

  background: linear-gradient(
      60deg, #42b983,sandybrown, mediumpurple) no-repeat fixed center top;
  background-size: cover;
}
</style>
