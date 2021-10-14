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
  }
  },
  methods:{
    changeColor(index){
      this.choose=index

    },
    changeList(index){

      if(index==1){

        bus.emit('friendList',false)
      }else {
        bus.emit('friendList',true)
      }
    }
  },
  mounted() {
  this.changeColor(0)
    this.header=localStorage.getItem('avatar')
    bus.on('NewListItem',()=>{
      this.changeColor(0)
      this.changeList(0)
    })
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
