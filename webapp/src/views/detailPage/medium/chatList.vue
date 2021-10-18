<template>
  <el-scrollbar >
    <div>
    <div class="clist">

      <div v-for="(item,index) in ChatItem" :key="index" @click="sendInf(item)" class="inner">
        <div class="inf">
          <div style="padding-right: 5px;width:50px;padding-left: 5px">
            <el-avatar shape="square" :size="40" :src=item.avatar></el-avatar>
          </div>
          <div style="width: 80px;padding-right: 20px;text-align: left">
            {{ item.name.length > 5 ? item.name.slice(0, 5).concat('…') : item.name }}
          </div>

          <div style="font-size: 5px;width: 120px;left: -10px;position: relative;">
            {{ getdate(item.latestMsgDate) }}
          </div>

          <br>
          <div style="font-size: 3px;width: 150px;left: -220px;position: relative;text-align: left;margin-top: 6px">
            {{ item.latestMsgContent.length > 10 ? item.latestMsgContent.slice(0, 10).concat("…") : item.latestMsgContent }}
          </div>

          <div style="font-size: 15px;width: 150px;left: -220px;position: relative;text-align: left;color: red;
          margin-top: 4px">
            {{ item.unread === 0 ? "" : item.unread }}
          </div>
          <div style="font-size: 3px;width: 150px;left: -360px;position: relative;text-align: left; margin-top: 6px">
            {{ item.unread === 0 ? "" : '条未读' }}
          </div>
        </div>
      </div>


    </div>

    </div>
  </el-scrollbar>
</template>

<script>
import bus from "../../bus";
// import {RecentChat} from "../../../service/chatList";
// import {nextTick} from "@vue/runtime-core";

 require('../../../mock/mock')
export default {
  name: "chatList",
  data() {
    return {
      chatShow: true,
      searchInf: "",
      ChatItem: [],
      chatId: '',
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
  methods: {
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
    //创建聊天
    sendInf(item) {
      item.unread = 0
      item.groupId === null ? this.chatId = item.userId : this.chatId = item.groupId
      bus.emit('chatPeople', item)
      bus.emit('chatMsg', item)
    },
    // //获取最近聊天
    // getChatList() {
    //   let inf = {
    //     userId: localStorage.getItem('userId'),
    //     token: localStorage.getItem('Authorization')
    //   }
    //   RecentChat(inf).then((res) => {
    //     console.log(res.data.data)
    //     // console.log(res.data.data.ChatItems)
    //     this.ChatItem = res.data.data.ChatItems
    //     // this.ChatItem.pop()
    //     // console.log(this.ChatItems)
    //   }).catch((err) => {
    //     console.log(err)
    //   })
    //
    // },

  },
  mounted() {
    bus.on('chatList',(e)=>{

      console.log(this.ChatItem)
      this.ChatItem=e
      console.log(this.ChatItem)
    })
//     this.getChatList()
//列表最后的聊天记录更改
    //他人
    bus.on('listNewMsg', (e) => {
      // console.log(e)
      // console.log(e.groupId)
      var have = 0
      this.ChatItem.forEach(item => {
        //   console.log(item)
        if (item.groupId !== null && e.groupId !== undefined && item.groupId === e.groupId) {

          // console.log(item)
          item.latestMsgDate !== undefined ? item.latestMsgDate = e.timestamp : item.latestMsgDate

          item.latestMsgContent !== undefined ? item.latestMsgContent = e.msgData : item.latestMsgContent
          item.timestamp=e.timestamp
          have = 1
          if (this.chatId !== item.groupId) {
            item.unread++
          }

        } else if (item.groupId === null && e.groupId === undefined && item.userId === e.fromUid) {

          // console.log(item)
          item.latestMsgDate !== undefined ? item.latestMsgDate = e.timestamp : item.latestMsgDate

          item.latestMsgContent !== undefined ? item.latestMsgContent = e.msgData : item.latestMsgContent

          item.timestamp=e.timestamp

          have = 1
          if (this.chatId !== item.userId) {
            item.unread++
          }
        }

        // console.log(item.groupId,item.userId)
        // console.log(e.groupId,e.fromUid)
        //   if(have===0 && (e.groupId!==undefined || e.fromUid!==undefined)){
        //     let newChatPeo={
        //       avatar:e.avatar,
        //       msgs:null,
        //       name:e.name,
        //       sessionType:e.sessionType,
        //       groupId: e.groupId,
        //       latestMsgContent: e.latestMsgContent,
        //       latestMsgDate: e.latestMsgDate,
        //       unread: e.unread,
        //       userId: e.userId
        //     }
        //     this.ChatItem.push(newChatPeo)
        //   }
      })
      this.ChatItem.sort((a,b)=>{return b.timestamp - a.timestamp})
    })
    //自己
    bus.on('newMyMsg', (e) => {
      // console.log(e.data)//自己定义的结构
      this.ChatItem.forEach(item => {
        if (e.groupId !== undefined && item.groupId === e.groupId) {
          item.latestMsgDate !== undefined ? item.latestMsgDate = e.timestamp : item.latestMsgDate
          item.latestMsgContent !== undefined ? item.latestMsgContent = e.data : item.latestMsgContent
          item.timestamp=e.timestamp
        } else if (e.groupId === undefined && item.userId === e.toUserId) {
          item.latestMsgDate !== undefined ? item.latestMsgDate = e.timestamp : item.latestMsgDate
          item.latestMsgContent !== undefined ? item.latestMsgContent = e.data : item.latestMsgContent
          item.timestamp=e.timestamp
        }
      })
    })

    //从联系人/群聊列表来的数据创建新的item
    bus.on('NewListItem',(e)=>{
      console.log(e)
        let inf={
          avatar: e.avatar,
          groupId: e.groupId?e.groupId:null,
          latestMsgContent: "",
          latestMsgDate: "",
          msgs: null,
          name: e.username?e.username:e.groupName,
          sessionType:  e.groupId?"GROUP":"SINGLE",
          unread: 0,
          userId: e.userId?e.userId:null,
        }
         console.log(inf)
      let have=false
      console.log(have)
      for(let i of this.ChatItem)
     {
        if(i.userId===inf.userId&&i.groupId===inf.groupId) {
          have=true
          break
        }
      }
      console.log(have)
      if(have===false){
        this.ChatItem.push(inf)
      }else {
        this.sendInf(inf)
      }
      console.log(this.ChatItem)
    })

  },


}
</script>

<style scoped>

.clist {

  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.inner {
  width: 10000px;
}

.inf {
  background-color: #EAE8E7;
  height: 65px;
  /*width: 100%;*/
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.inf div {
  padding-top: 5px;
  overflow: hidden;
  float: left;

}

</style>
