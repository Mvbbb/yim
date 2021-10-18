<template>
<div class="left">
  <div class="portrait">
    <img style="height: 45px;width: 45px" :src=header>
  </div>
  <div class="icon">
    <div style="padding-bottom: 10px;font-size: 30px"
        v-for="(item,index) in list" :key="index"
         @click="changeColor(index),changeList(index)"
         :class="{active:choose === index}">
      <i :class="item" ></i>
    </div>


  </div>
</div>
</template>

<script>
import bus from "../../bus";
import {RecentChat} from "../../../service/chatList";
import {FriendList} from "../../../service/relation/friendList";
import {GroupList} from "../../../service/relation/groupList";
export default {
name: "personalInf",
  data(){
  return {
    header:"",
    choose: '',
    list:[
      'el-icon-chat-dot-round',
      'el-icon-user-solid',

    ],
    recentChatItem:[],
    allFriendsItems: [],
    allGroupsItems: [],
  }
  },
  methods:{
    changeColor(index){
      this.choose=index

    },
    //更改界面
    changeList(index){
      // bus.emit('chatList', this.recentChatItem)
      if(index==1){

        bus.emit('friendList',true)
        bus.emit('friendsList', this. allFriendsItems)
        bus.emit('groupsList', this.allGroupsItems)
      }else {
        // bus.emit('chatList', this.recentChatItem)
        bus.emit('friendList',false)
        bus.emit('chatList', this.recentChatItem)
      }
    },
    //创建聊天
    sendInf(item) {
      item.unread = 0
      item.groupId === null ? this.chatId = item.userId : this.chatId = item.groupId
      bus.emit('chatPeople', item)
      bus.emit('chatMsg', item)
    },
    //获取最近聊天
    getChatList() {
      let inf = {
        userId: localStorage.getItem('userId'),
        token: localStorage.getItem('Authorization')
      }
      RecentChat(inf).then((res) => {
        console.log(res.data.data)
        // console.log(res.data.data.recentChatItems)
        this.recentChatItem = res.data.data.recentChatItems
        bus.emit('chatList', this.recentChatItem)
        // this.recentChatItem = res.data.data.recentChatItems
        // this.recentChatItem.pop()
        // console.log(this.recentChatItems)
      }).catch((err) => {
        console.log(err)
      })
    },
    //获取联系人列表
    getFriendList() {
      let inf = {
        userId: localStorage.getItem('userId'),
        token: localStorage.getItem('Authorization')
      }
      FriendList().then((res) => {

        this.allFriendsItems = res.data.data
        console.log(this.allFriendsItems)
      }).catch((err) => {
        console.log(err)
      })
    },
    //获取群聊列表
    getGroup() {
      let inf = {
        userId: localStorage.getItem('userId'),
        token: localStorage.getItem('Authorization')
      }
      GroupList().then((res) => {

        this.allGroupsItems = res.data.data
        console.log(this.allGroupsItems)
      }).catch((err) => {
        console.log(err)
      })
    },
  },
  mounted() {
  this.changeColor(0)
    this.header=localStorage.getItem('avatar')
    bus.on('NewListItem',()=>{
      this.changeColor(0)
      this.changeList(0)
    })

    this.getChatList()
    this.getFriendList()
    this.getGroup()

  },

}
</script>

<style scoped>
.left{
  align-items: center;
}
.portrait{
  top:10px;
  position: relative;
  padding-bottom: 20px;
}

.icon div:hover{
  color: green;
}
.active{
  color: green;
}
.icon{
  color: whitesmoke;
}
/*:link 选择器设置指向未被访问页面的链接的样式，:visited 选择器用于设置指向已被访问的页面的链接，:active 选择器用于活动链接。*/
/*:active，元素被点击时变色，但颜色在点击后消失；*/
/*  :focus， 元素被点击后变色，且颜色在点击后不消失；*/
</style>
