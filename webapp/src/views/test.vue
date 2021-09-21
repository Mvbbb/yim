<template>

</template>

<script>
import {onBeforeUnmount, onMounted, reactive, ref} from "@vue/runtime-core";
import  { io }  from "socket.io-client";
export default {
name: "test",
 setup(){
   let socket = null
   let word=ref('test')
   let list=reactive([])
   onMounted(() => {
     console.log(1111)
     // 创建客户端 websocket 的实例
     socket = io('ws://localhost:8862/webSocketBySpring/webSocketHandler/1')
     console.log(socket)
     // 建立连接的事件
     socket.on('connect', () => console.log('connect: websocket 连接成功！'))

     send()
   })
   // 组件被销毁之前，清空 sock 对象
   onBeforeUnmount(() => {
     // 关闭连接
     socket.close()

     // 销毁 websocket 实例对象
     socket = null
   })
  // // 关闭连接的事件
  //  socket.on('disconnect', () => console.log('disconnect: websocket 连接关闭！'))
  //  // 接收到消息的事件
  //  socket.on('message', msg => console.log(msg))
  //  // 接收到消息的事件
  //  socket.on('message', msg => {
  //    // 把服务器发送过来的消息，存储到 list 数组中
  //    list.push({ name: 'xs', msg })
  //  })
   const send = () => {
     // 如果输入的聊天内容为空，则 return 出去
     if (!word) return

     // 向服务器发送消息
     socket.emit('send',word)
     // 将用户填写的消息存储到 list 数组中
     list.push({ name: 'me', msg: word })
     // 清空文本框中的消息内容
     word = ''
   }
    return socket,word,list
 },
}
</script>

<style scoped>

</style>
