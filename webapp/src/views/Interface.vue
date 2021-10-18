<template>

  <el-container class="container">

    <el-aside width="8%" style="background-color: black;overflow: hidden;display: inline-block">
      <personal-inf></personal-inf>
    </el-aside>
    <el-aside width="23.5%" style="overflow: hidden">
      <search-title ></search-title>
      <chat-list v-show="!show" style="overflow: auto"></chat-list>
      <friend-list v-show="show" style="overflow: auto"></friend-list>
    </el-aside>
    <el-container class="container2">
      <el-header>
        <chat-nmae v-show="!show"></chat-nmae>
      </el-header>
      <el-main>
        <chat-box v-show="!show" ></chat-box>
      </el-main>
      <el-footer>
        <input-box v-show="!show"></input-box>
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
import friendList from "./detailPage/medium/friendList";
import searchTitle from "./detailPage/medium/searchTitle";
import bus from "./bus";
import {RecentChat} from "../service/chatList";

export default {
name: "Interface",
  components:{
    personalInf,
    chatList,
    friendList,
    chatNmae,
    chatBox,
    inputBox,
    searchTitle
  },
  data(){
    return{
      show:false,
      websocket:null,
      handshake: {
        cmdType: "GREET",
        data: {
          token: "",
          userId: ""
        },
        headFlag: 55,
        logId: 1,
        sequenceId: 1,
        version: 1
      },

    }
  },

  methods:{

    createBigSocket(){
      this.handshake.data.token = localStorage.getItem('Authorization')
      this.handshake.data.userId = localStorage.getItem('userId')
      this.websocket = new WebSocket(localStorage.getItem('ws'));
      let _this = this
      //连接
      this.websocket.onopen = function (event) {
        console.log(JSON.stringify(_this.handshake))
        _this.websocket.send(JSON.stringify(_this.handshake))
        console.log("onopen");
      };
      //错误
      this.websocket.onerror = function (error) {
        console.log("onerror: " + event.data);
      };
      //收取消息
      this.websocket.onmessage = function (message) {
        console.log(message)
        // bus.emit('listNewMsg', JSON.parse(message.data))
        bus.emit('listNewMsg', JSON.parse(message.data))
        bus.emit('newChatMsg', JSON.parse(message.data))

      };
      // 关闭
      // this.websocket.onclose = function (e) {
      //   console.log('websocket 断开: ' + e.code + ' ' + e.reason + ' ' + e.wasClean)
      //   console.log(e)
      // }
    },

  },
  mounted() {
    this.createBigSocket()
    //最近聊天与朋友列表的切换
    bus.on('friendList', (e) => {
      this.show = e
    })

    //接收输入框发送的信息
    bus.on('sendMsg', (e) => {
      console.log(111)
      this.websocket.send(JSON.stringify(e))
    })


  },
  beforeUnmount() {
    // 组件被销毁之前，清空sock 对象
    this.websocket.onclose=function (closeEvent){
      console.log(closeEvent)
      this.websocket.close()
    }
    // 销毁 websocket 实例对象
    this.websocket = null
  },

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
