<template>
  <el-container class="container">

    <el-aside width="8%" style="background-color: black">
      <personal-inf></personal-inf>
    </el-aside>
    <el-aside width="20%"  >
      medium
    </el-aside>
    <el-container class="container2">
      <el-header>Header</el-header>
      <el-main>Main</el-main>
      <el-footer>Footer</el-footer>
    </el-container>
  </el-container>

</template>

<script>
import personalInf from "./detailPage/left/personalInf";
export default {
name: "Interface",
  components:{
    personalInf
  }
}
</script>

<style scoped>
.container{
  margin: 120px 200px 120px 200px;
  height: 500px;
  /*width: 1200px;*/
  position: relative;
  /*box-sizing: border-box;*/
}
.container2{
  width: 75%;
}
.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: var(--el-text-color-primary);
  text-align: center;

}

.el-main {
  background-color: #e9eef3;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>
