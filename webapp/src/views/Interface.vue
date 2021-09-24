<template>

  <el-container class="container">

    <el-aside width="8%" style="background-color: black;overflow: hidden;display: inline-block">
      <personal-inf></personal-inf>
    </el-aside>
    <el-aside width="23.5%" >
      <chat-list></chat-list>
    </el-aside>
    <el-container class="container2">
      <el-header>
        <chat-nmae></chat-nmae>
      </el-header>
      <el-main>
        <chat-box></chat-box>
      </el-main>
      <el-footer>
        <input-box></input-box>
      </el-footer>
    </el-container>
  </el-container>

</template>

<script>
import personalInf from "./detailPage/left/personalInf";
import chatList from "./detailPage/medium/chatList";
import chatNmae from "./detailPage/main/chatNmae";
import chatBox from "./detailPage/main/chatBox";
import inputBox from "./detailPage/main/inputBox";
export default {
name: "Interface",
  components:{
    personalInf,
    chatList,
    chatNmae,
    chatBox,
    inputBox
  }
}
</script>

<style scoped>
.container{
  margin: 120px 200px 120px 200px;
  height: 500px;
  position: relative;
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
.el-footer{
  height: 150px;
  background-color: white;
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
  /*line-height: 160px;*/
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
