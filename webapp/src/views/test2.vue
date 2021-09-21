<template>
  <div
      v-for="(item,index) in list" :key="index"
       @click="changeColor(index)"
       :class="{active:select=== index}">
    {{select}}
<!--    <i :class="item" ></i>-->
  </div>
</template>

<script>
import {reactive, ref} from "@vue/runtime-core";
    export default {
      name: "personalInf",
      setup(){
        let select=ref(0)
        let list=reactive([
          'el-icon-chat-dot-round',
          'el-icon-chat-dot-round'
        ])

        function changeColor(index){
          select=ref(index)
          console.log(select)
        }
        return {changeColor,list,select}
      },
}
</script>

<style scoped>
.active{
  color: green;
}
</style>
