<template>
  <el-scrollbar ref="myScrollbar">
    <div class="chat-content" v-show="box">
      <!-- recordContent 聊天记录数组-->
      <div v-for="(item,index) in msg" :key="index">
        <!-- 对方 -->
        <div class="word" v-if="item.fromUid && item.fromUid!==uid&&item.sessionType===this.type">
          <img :src="otherpic">
          <div class="info">
            <p class="time"> {{ getdate(item.timestamp) }}</p>
            <div class="info-content">{{ item.msgData }}</div>
          </div>
        </div>
        <!-- 我的 -->
        <div class="word-my" v-else>
          <div class="info">
            <p class="time"> {{ getdate(item.timestamp) }}</p>
            <div class="info-content">{{ item.msgData }}</div>
          </div>
          <img :src="minepic">
        </div>
      </div>
    </div>
  </el-scrollbar>

</template>

<script>
import bus from "../../bus";
import {onBeforeUnmount} from "@vue/runtime-core";
import {PastMsg} from "../../../service/pastMsg";

export default {
  name: "chatBox",
  data() {
    return {
      box: false,
      minepic: '',//自己头像
      otherpic: '',//对方头像
      uid: '',//用户的id
      type: '',//当前聊天类型
      msg: [],

    }
  },
  methods: {
    //更换时间格式
    getdate(timestamp) {
      var now = new Date(timestamp),

          y = now.getFullYear(),

          m = now.getMonth() + 1,

          d = now.getDate();
      var today = new Date()
      var ty = today.getFullYear()
      if (ty - y >= 1) return y + "/" + (m < 10 ? "0" + m : m) + "/" + (d < 10 ? "0" + d : d)
      else return (m < 10 ? "0" + m : m) + "/" + (d < 10 ? "0" + d : d) + " " + now.toTimeString().substr(0, 8);

    },
    //获取当前聊天对象的历史信息
    getPastMsg(e) {
      console.log(e)
      let inf = {
        sessionType: e.sessionType,
        sessionId: e.groupId
      }
      if (inf.sessionId === null) inf.sessionId = e.userId
      PastMsg(inf).then((res) => {
        console.log(res.data)
        this.msg = res.data.data
        this.msg.reverse()

        this.$nextTick(() => {
          this.$refs['myScrollbar'].wrap.scrollTop = this.$refs['myScrollbar'].wrap.scrollHeight
        })//使得滚动条位于最底部

      }).catch((err) => {
        console.log(err)
      })
    }
  },
  mounted() {
    //自己头像
    this.minepic=localStorage.getItem('avatar')

    //接收列表分发来的聊天对象信息
    bus.on('chatPeople', (e) => {
      // console.log(e)
      this.box = true

      this.uid = localStorage.getItem('userId')
      this.otherpic = e.avatar
      this.type = e.sessionType
      if (e.groupId) {
        this.chatPeopleUid = e.groupId
      } else {
        this.chatPeopleUid = e.userId
      }
      // console.log(this.chatPeopleUid)
      this.getPastMsg(e)
    })
    //接收列表分发来的聊天对象上次离线后的未读消息
    bus.on('chatMsg', (e) => {
       // console.log(e)
      this.msg = []
      if (e.sessionType == "GROUP") {

      } else {
        this.msg = e.msgs
        this.otherpic = e.avatar
      }
    })
    //接收其他人的新消息，判断是否为当前聊天的信息，是就推入聊天框
    bus.on('newChatMsg', (e) => {
      // console.log(e)
//有问题
      if (e.sessionType === this.type || e.groupId===this.chatPeopleUid) {
        if (e.msgData) {
          this.msg.push(e)
        }
      }
      //保持聊天框滚动条始终位于最底部
      this.$nextTick(() => {
        this.$refs['myScrollbar'].wrap.scrollTop = this.$refs['myScrollbar'].wrap.scrollHeight
      })
    })
//将自己发送的消息推送至聊天框
    bus.on('newMyMsg', (e) => {
      // console.log(e)
      let newMine={
        clientMsgId: "1",
        fromUid: this.uid,
        groupId: null,
        msgData: e.data,
        msgType: e.msgType,
        serverMsgId: "",
        sessionType:e.sessionType,
        timestamp: e.timestamp,
      }
      this.msg.push(newMine)
      // console.log(this.msg)
      this.$nextTick(() => {
        this.$refs['myScrollbar'].wrap.scrollTop = this.$refs['myScrollbar'].wrap.scrollHeight
      })
    })

  },

}
</script>

<style scoped>
.chatBox {

  width: 1000px;
}

.chat-content {
  overflow: auto;
  width: 700px;
  padding: 20px 0px 20px 40px;
}

.word {
  display: flex;
  margin-bottom: 20px;
  left: -20px;
  position: relative;
}

.word img {
  width: 40px;
  height: 40px;
  /*border-radius: 50%;*/
}

.word .info {
  margin-left: 10px;
}

.word .time {
  font-size: 12px;
  color: rgba(51, 51, 51, 0.8);
  margin: 0;
  height: 20px;
  line-height: 20px;
  margin-top: -5px;
}

.word .info-content {
  padding: 10px;
  font-size: 14px;
  background: #fff;
  position: relative;
  margin-top: 8px;
  word-break: break-word;
  max-width: 70%;

  float: left;
  margin-right: 10px;

  text-align: left;
}

/*小三角形*/
.word .info-content::before {
  position: absolute;
  left: -8px;
  top: 8px;
  content: '';
  border-right: 10px solid #FFF;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
}


.word-my {
  display: flex;
  left: -20px;
  position: relative;
  margin-bottom: 20px;
}

.word-my img {
  width: 40px;
  height: 40px;
  /*border-radius: 50%;*/
}

.word-my .info {
  width: 90%;
  margin-left: 10px;
  text-align: right;
}

.word-my .time {
  font-size: 12px;
  color: rgba(51, 51, 51, 0.8);
  margin: 0;
  height: 20px;
  line-height: 20px;
  margin-top: -5px;
  margin-right: 10px;
}

.word-my .info-content {
  max-width: 70%;
  word-break: break-word;
  padding: 10px;
  font-size: 14px;
  float: right;
  margin-right: 10px;
  position: relative;
  margin-top: 8px;
  background: #A3C3F6;
  text-align: left;
}

/*//小三角形*/
.word-my .info-content::after {
  position: absolute;
  right: -8px;
  top: 8px;
  content: '';
  border-left: 10px solid #A3C3F6;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
}

</style>
