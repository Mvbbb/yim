<template>
<div class="inputBox">

  <div class="chatIcon">
    <el-popover placement="top-start" width="300" trigger="click" class="emoBox">
      <template #reference>
        <el-button
            type="text"
        ><img src="../../../assets/img/emoji.png"></el-button>
      </template>
      <div class="emotionList">
        <a href="javascript:void(0);" @click="getEmo(index)" v-for="(item,index) in faceList" :key="index" class="emotionItem">{{item}}</a>
      </div>
    </el-popover>
  </div>

  <div class="inputDeep">

    <el-input
        style="width: 100%;left: -20px;top:-10px; position: relative"
        :rows="5"
        class="chatText"
        id="textarea"
        type="textarea"
        placeholder="请输入内容"
        @keyup.enter.native="submitMessage"
        v-model="textarea"
    >
      <!--     @keyup.enter.native  按下回车键的作用-->


    </el-input>


  </div>
  <el-button

      class="submit-btn"
      type="primary"
      size="small"
      @click="submitMessage"
      :disabled="content == ''"
  >发送
  </el-button>


</div>
</template>

<script>
export default {
  name: "inputBox",
  data(){
    return{
      appData : require("../../../assets/img/emoji.json"),
      faceList: [],
      textarea: ""
    }
  },
  mounted() {
    for(let i in this.appData){
      this.faceList.push(this.appData[i].char);
    }



  },
  methods:{
    getEmo(index){
      var textArea=document.getElementById('textarea');
      console.log(textArea)
      function changeSelectedText(obj, str) {
        if (window.getSelection) {
          // 非IE浏览器
          textArea.setRangeText(str);
          // 在未选中文本的情况下，重新设置光标位置
          textArea.selectionStart += str.length;
          textArea.focus()
        } else if (document.selection) {
          // IE浏览器
          obj.focus();
          var sel = document.selection.createRange();
          sel.text = str;
        }
      }
      changeSelectedText(textArea,this.faceList[index]);
      this.textarea=textArea.value;// 要同步data中的数据

      return;
    },
  }

}
</script>

<style scoped>
.inputBox{
  overflow:hidden;
  text-overflow:ellipsis;
  white-space:nowrap;
  height: 100%;
}
.submit-btn{
  float: right;

  position: relative;
}
.inputDeep{
  width: 100%;
  left: -20px;
  top:55px;
  position: relative
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

/* el-popover是和app同级的，所以scoped的局部属性设置了无效 */
/* 需要设置全局style */
.el-popover{
  height:200px;
  width:400px;
  overflow: scroll;
  overflow-x:auto;
}

  .chatIcon {
    float: left;

    font-size: 25px;
  }
.emotionList{
  display: flex;
  flex-wrap: wrap;
  padding:5px;
  width: 300px;

}
.emotionItem{
  width:10%;
  font-size:20px;
  text-align:center;
}
/*包含以下四种的链接*/
.emotionItem {
  text-decoration: none;
}
/*正常的未被访问过的链接*/
.emotionItem:link {
  text-decoration: none;
}
/*已经访问过的链接*/
.emotionItem:visited {
  text-decoration: none;
}
/*鼠标划过(停留)的链接*/
.emotionItem:hover {
  text-decoration: none;
}
/* 正在点击的链接*/
.emotionItem:active {
  text-decoration: none;
}

</style>
