<template>
  <el-scrollbar>
    <div>
      <div class="titleFather">
        <el-affix :offset="120" style=" overflow: hidden;width: 100%; text-overflow:ellipsis;white-space:nowrap;">
          <div class="searchTitle">
            <div class="searchAdd">
              <el-input v-model=searchInf clearable
                        maxlength="20"
                        placeholder="搜索"
                        prefix-icon="el-icon-search"
                        size="small"
                        style="width:70%;
      float: left;
      left: 5px;position: relative;
      display: inline-block;padding: 0px 10px 0px 5px">
              </el-input>
              <div class="icon" style="display: inline-block;">
                <i class="el-icon-circle-plus-outline"
                   style="font-size: 30px;"></i>
              </div>

            </div>
          </div>
        </el-affix>
      </div>
      <div class="list">

        <div v-for="(item,index) in recentChatItems" :key="index" class="inner" @click="sendInf(item)">
          <div class="inf">
            <div style="padding-right: 5px;width:50px;padding-left: 5px">
              <el-avatar :size="40" :src=item.a shape="square"></el-avatar>
            </div>
            <div style="width: 80px;padding-right: 20px;text-align: left">
              {{ item.name.length > 5 ? item.name.slice(0, 5).concat('…') : item.name }}
            </div>

            <div style="font-size: 5px;width: 120px;left: -30px;position: relative;">
              {{ getdate(item.latestMsgDate) }}
            </div>
            <br>
            <div style="font-size: 3px;width: 150px;left: -220px;position: relative;text-align: left;margin-top: 6px">
              {{
                item.latestMsgContent.length > 10 ? item.latestMsgContent.slice(0, 10).concat('…') : item.latestMsgContent
              }}
            </div>
            <!--          <div style="font-size: 15px;width: 150px;left: -220px;position: relative;text-align: left;color: red;-->
            <!--          margin-top: 4px">-->
            <!--            {{item.unread==0?"":item.unread}}-->
            <!--          </div>-->
            <!--          <div style="font-size: 3px;width: 150px;left: -360px;position: relative;text-align: left; margin-top: 6px">-->
            <!--            {{item.unread==0?"":'条未读'}}-->
            <!--          </div>-->
          </div>
        </div>


      </div>

    </div>
  </el-scrollbar>
</template>

<script>
import bus from "../../bus";
import {RecentChat} from "../../../service/chatList";

require('../../../mock/mock')
export default {
  name: "chatList",
  data() {
    return {
      searchInf: "",
      recentChatItems: [],
      // listInf:
      //    [{
      //      header:'',
      //      name:'张三',
      //      lastMsg:'zha',
      //      time:'2021/9/21'
      //    },{
      //      header:'',
      //      name:'任天然',
      //      lastMsg:'hahahahhaaaa',
      //      time:'2021/9/21'
      //    },{
      //      header:'',
      //      name:'西南石油大学聊天群',
      //      lastMsg:'hahahahhaaaaaaaaaaa',
      //      time:'今天'
      //    },{
      //      header:'',
      //      name:'西南石油大学聊天群',
      //      lastMsg:'hahahahhaaaaaaaaaaa',
      //      time:'今天'
      //    },{
      //      header:'',
      //      name:'西南石油大学聊天群',
      //      lastMsg:'hahahahhaaaaaaaaaaa',
      //      time:'今天'
      //    },{
      //      header:'',
      //      name:'西南石油大学聊天群',
      //      lastMsg:'hahahahhaaaaaaaaaaa',
      //      time:'今天'
      //    },{
      //      header:'',
      //      name:'西南石油大学聊天群',
      //      lastMsg:'hahahahhaaaaaaaaaaa',
      //      time:'今天'
      //    },{
      //      header:'',
      //      name:'西南石油大学聊天群',
      //      lastMsg:'hahahahhaaaaaaaaaaa',
      //      time:'今天'
      //    },{
      //      header:'',
      //      name:'西南石油大学聊天群',
      //      lastMsg:'hahahahhaaaaaaaaaaa',
      //      time:'今天'
      //    },{
      //      header:'',
      //      name:'西南石油大学聊天群',
      //      lastMsg:'hahahahhaaaaaaaaaaa',
      //      time:'今天'
      //    }]
    }
  },
  methods: {
    getdate(timestamp) {

      var now = new Date(timestamp),

          y = now.getFullYear(),

          m = now.getMonth() + 1,

          d = now.getDate();

      return y + "/" + (m < 10 ? "0" + m : m) + "/" + (d < 10 ? "0" + d : d) + " " + now.toTimeString().substr(0, 8);

    },
    sendInf(item) {

      // var websocket= new WebSocket("ws://121.40.165.18:8800");
      // websocket.onopen = function (event) {
      //   console.log("onopen");
      // };
      // websocket.onerror = function (error) {
      //   console.log("onerror: " + event.data);
      // };
      // websocket.onmessage = function (message) {
      //   console.log("onmessage: " + message.data);
      // };
      bus.emit('chatPeople', item)
      bus.emit('chatMsg', item)
    },
    getChatList() {
      let inf = {
        userId: localStorage.getItem('userId'),
        token: localStorage.getItem('Authorization')
      }
      RecentChat(inf).then((res) => {
        console.log(res.data)
        this.recentChatItems = res.data.data.recentChatItems
        console.log(this.recentChatItems)
      }).catch((err) => {
        console.log(err)
      })

    }
  },
  mounted() {
    this.getChatList()
    // var websocket= new WebSocket("ws://121.40.165.18:8800");
    // websocket.onopen = function (event) {
    //   console.log("onopen");
    // };
    // websocket.onerror = function (error) {
    //   console.log("onerror: " + event.data);
    // };
    // websocket.onmessage = function (message) {
    //   console.log("onmessage: " + message.data);
    // };

  }


}
</script>

<style scoped>
.titleFather {
  height: 60px;
  width: 100%;

}

.searchTitle {
  width: 100%;
  background-color: #F7F7F7;
  height: 60px;
  display: inline-block;
  float: left;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.searchAdd {
  padding: 14px 0px 0px 0px;
  height: 60px;
  overflow: hidden;
  float: left;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}

.icon i {
  transition: all 0.6s;
}

.icon i:hover {
  transform: rotate(-360deg);
}

.list {

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
