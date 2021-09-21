<template>
<div class="left">
  <div class="portrait">
    <el-avatar shape="square" :size="50" :src="squareUrl"></el-avatar>
  </div>
  <div class="icon">
    <div style="padding-bottom: 10px;font-size: 30px"
        v-for="(item,index) in list" :key="index"
         @click="changeColor(index)"
         :class="{active:choose === index}">
      <i :class="item" ></i>
    </div>


  </div>
</div>
</template>

<script>

export default {
name: "personalInf",
  data(){
  return {
    choose: '',
    list:[
      'el-icon-chat-dot-round',
      'el-icon-user-solid'
    ],
  }
  },
  methods:{
    changeColor(index){
      this.choose=index

    }
  }


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
