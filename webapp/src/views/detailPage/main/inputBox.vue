<template>
<div >
  <el-button
      class="submit-btn"
      type="primary"
      @click="submitMessage"
      :disabled="content == ''"
  >发送
  </el-button>
  <div class="inputDeep">
    <el-input
        style="width: 780px;left: -20px;top:22px; position: relative"
        :rows="5"
        type="textarea"
        placeholder="请输入内容"
        @keyup.enter.native="submitMessage"
        v-model="chatMsg"
    >
      <!--     @keyup.enter.native  按下回车键的作用-->


    </el-input>
  </div>



</div>
</template>

<script>
export default {
  name: "inputBox",
  data(){
    return{
      chatMsg:''
    }
  }
}
</script>

<style scoped>
.inputBox{
  height: 100%;

}
.submit-btn{
  float: right;
  top:10px;
  position: relative;
}
/* 利用穿透，设置input边框隐藏 */
.inputDeep>>>.el-input__inner {
  border: 0;
}
/* 如果你的 el-input type 设置成textarea ，就要用这个了 */
.inputDeep>>>.el-textarea__inner {
  border-left: 0;
  border-bottom: 0;
  border-right: 0;
  resize: none;/* 这个是去掉 textarea 下面拉伸的那个标志，如下图 */
}
</style>
