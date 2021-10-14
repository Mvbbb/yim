<template>
  <div class="inputBox">

    <div class="chatIcon" v-show="ibox">
      <el-popover placement="top-start" width="300" trigger="click" class="emoBox">
        <template #reference>
          <el-button
              type="text"
          ><img src="../../../assets/img/emoji.png"></el-button>
        </template>
        <div class="emotionList">
          <a href="javascript:void(0);" @click="getEmo(index)" v-for="(item,index) in faceList" :key="index"
             class="emotionItem">{{ item }}</a>
        </div>
      </el-popover>
    </div>

    <div class="inputDeep" v-show="ibox">

      <el-input
          style="width: 100%;left: -20px;top:-10px; position: relative"
          :rows="5"
          class="chatText"
          id="textarea"
          type="textarea"
          placeholder="请输入内容"
          v-model="textarea"

      >
        <!--     @keyup.enter.native=  按下回车键的作用-->


      </el-input>


    </div>
    <el-button
        v-show="ibox"
        class="submit-btn"
        type="primary"
        size="small"
        @click="submitMessage"
        :disabled="textarea == ''"
    >发送
    </el-button>


  </div>
</template>

<script>
import bus from "../../bus";

export default {
  name: "inputBox",
  data() {
    return {
      count: 0,
      ibox: false,
      socket: [],
      websocket: '',
      websocket2: '',
      appData: require("../../../assets/img/emoji.json"),
      faceList: [],
      textarea: "",
      chatPeopleUid: '',
      //握手
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
      handshake2: {
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
      communication: {
        cmdType: "MSG_DATA",
        data: {
          clientMsgId: "1",
          data: "",
          msgType: "TEXT",
          sessionType: "",
          toUserId: ""
        },
        headFlag: 55,
        logId: 1,
        sequenceId: 1,
        version: 1
      }
    }
  },

  mounted() {
    this.createBigSocket()

    //表情列表
    for (let i in this.appData) {
      this.faceList.push(this.appData[i].char);
    }

    bus.on('chatMsg', (e) => {
      var _this = this
      console.log(e)
      this.ibox = true,

          this.communication.data.sessionType = e.sessionType


      this.handshake.data.token = localStorage.getItem('Authorization')
      if (e.sessionType == "GROUP") {

        delete this.communication.data.toUserId
        this.handshake.data.userId = localStorage.getItem('userId')
        this.communication.data.groupId = e.groupId

      } else {
        delete this.communication.data.groupId
        this.handshake.data.userId = localStorage.getItem('userId')
        this.communication.data.toUserId = e.userId
      }

      // if(this.websocket.readyState===0){
      //   this.websocket.onclose=function (closeEvent){
      //     console.log(closeEvent)
      //     this.websocket.close()
      //   }
      // }

      this.websocket = new WebSocket("ws://115.159.148.114:7110");
      console.log(this.websocket)
//连接
      this.websocket.onopen = function (event) {
        console.log(JSON.stringify(_this.handshake))
        _this.websocket.send(JSON.stringify(_this.handshake))
        console.log("onopen");
      };
//错误报告
      this.websocket.onerror = function (error) {
        console.log("onerror: " + event.data);
      };
//接收信息
      this.websocket.onmessage = function (message) {
        console.log(message)
        console.log(message.data)
        bus.emit('listNewMsg', JSON.parse(message.data))
        bus.emit('newChatMsg', JSON.parse(message.data))
      };

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


  methods: {
    getEmo(index) {
      var textArea = document.getElementById('textarea');
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

      changeSelectedText(textArea, this.faceList[index]);
      this.textarea = textArea.value;// 要同步data中的数据

      return;
    },
    submitMessage() {

      this.communication.data.data = this.textarea
      let time_tamp = new Date()
      this.communication.data.timestamp = time_tamp
       console.log(this.communication)
      this.websocket.send(JSON.stringify(this.communication))
      this.textarea = ""
      bus.emit('newMyMsg', this.communication.data)
    },

    createBigSocket(){
      this.handshake2.data.token = localStorage.getItem('Authorization')
      this.handshake2.data.userId = localStorage.getItem('userId')
      this.websocket2 = new WebSocket(localStorage.getItem('ws'));
      let _this = this
      this.websocket2.onopen = function (event) {
        console.log(JSON.stringify(_this.handshake2))
        _this.websocket2.send(JSON.stringify(_this.handshake2))
        console.log("onopen");
      };

      this.websocket2.onerror = function (error) {
        console.log("onerror: " + event.data);
      };

      this.websocket2.onmessage = function (message) {
        console.log(message)
        bus.emit('listNewMsg', JSON.parse(message.data))

      };
      this.websocket2.onclose = function (e) {
        console.log('websocket 断开: ' + e.code + ' ' + e.reason + ' ' + e.wasClean)
        console.log(e)
      }
    }

  }


}
</script>

<style scoped>
.inputBox {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  height: 100%;
}

.submit-btn {
  float: right;

  position: relative;
}

.inputDeep {
  width: 100%;
  left: -20px;
  top: 55px;
  position: relative
}

/* 利用穿透，设置input边框隐藏 */
.inputDeep >>> .el-input__inner {
  border: 0;
}

/* 如果你的 el-input type 设置成textarea ，就要用这个了 */
.inputDeep >>> .el-textarea__inner {
  border-left: 0;
  border-bottom: 0;
  border-right: 0;
  resize: none; /* 这个是去掉 textarea 下面拉伸的那个标志，如下图 */
}

/* el-popover是和app同级的，所以scoped的局部属性设置了无效 */
/* 需要设置全局style */
.el-popover {
  height: 200px;
  width: 400px;
  overflow: scroll;
  overflow-x: auto;
}

.chatIcon {
  float: left;

  font-size: 25px;
}

.emotionList {
  display: flex;
  flex-wrap: wrap;
  padding: 5px;
  width: 300px;

}

.emotionItem {
  width: 10%;
  font-size: 20px;
  text-align: center;
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
