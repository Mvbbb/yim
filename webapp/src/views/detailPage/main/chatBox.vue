<template>

  <el-scrollbar>
    <div class="chat-content">
      <!-- recordContent 聊天记录数组-->
      <div v-for="(item,index) in msg" :key="index">
        <!-- 对方 -->
        <div v-if="item.fromUid" class="word">
          <img :src="this.otherpic">
          <div class="info">
            <p class="time"> {{ getdate(item.timestamp) }}</p>
            <div class="info-content">{{ item.msgData }}</div>
          </div>
        </div>
        <!-- 我的 -->
        <div v-else class="word-my">
          <div class="info">
            <p class="time"> {{ getdate(item.timestamp) }}</p>
            <div class="info-content">{{ item.msgData }}</div>
          </div>
          <img :src="item.avatar">
        </div>
      </div>
    </div>
  </el-scrollbar>

</template>

<script>
import bus from "../../bus";

export default {
  name: "chatBox",
  data() {
    return {
      chatMsg: '',
      minepic: '',
      otherpic: '',
      msg: [],

    }
  },
  methods: {
    getdate(timestamp) {

      var now = new Date(timestamp),

          y = now.getFullYear(),

          m = now.getMonth() + 1,

          d = now.getDate();

      return y + "/" + (m < 10 ? "0" + m : m) + "/" + (d < 10 ? "0" + d : d) + " " + now.toTimeString().substr(0, 8);

    }
  },
  mounted() {

    bus.on('chatPeople', (e) => {
      this.chatMsg = e.name
    })
    bus.on('chatMsg', (e) => {
      this.msg = null
      if (e.msgs == null) {

      } else {
        this.msg = e.msgs
        this.otherpic = e.avatar
      }
    })
  }
}
</script>

<style scoped>
.chatBox {

  width: 1000px;
}

.chat-content {
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
  border-radius: 50%;
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
  border-radius: 50%;
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
