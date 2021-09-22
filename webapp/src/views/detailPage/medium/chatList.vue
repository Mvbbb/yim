<template>
  <el-scrollbar>
<div>
  <div class="titleFather">
  <el-affix style=" overflow: hidden;width: 100%; text-overflow:ellipsis;white-space:nowrap;" :offset="120">
        <div class="searchTitle">
          <div class="searchAdd">
        <el-input v-model=searchInf clearable
                  maxlength="20"
                  size="small"
                  placeholder="搜索"
                  prefix-icon="el-icon-search"
                  style="width:70%;
      float: left;
      display: inline-block;padding: 0px 10px 0px 10px">
        </el-input>
        <div class="icon" style="display: inline-block;" >
          <i style="font-size: 30px;"
             class="el-icon-circle-plus-outline"></i>
        </div>

    </div>
  </div>
  </el-affix>
  </div>
  <div class="list">

      <div v-for="(item,index) in listInf" :key="index" @click="sendInf(item)"  class="inner">
        <div class="inf">
          <div style="padding-right: 5px;width:50px;padding-left: 5px">
            <el-avatar shape="square" :size="40" :src=item.header></el-avatar>
          </div>
          <div style="width: 80px;padding-right: 20px;text-align: left">
            {{item.name.length>5?item.name.slice(0,5).concat('…'):item.name}}
          </div>

          <div style="font-size: 5px;width: 80px;left: 20px;position: relative;">
            {{item.time}}
          </div>
          <br>
          <div style="font-size: 3px;width: 150px;left: -180px;position: relative;text-align: left">
            {{item.lastMsg.length>15?item.lastMsg.slice(0,15).concat('…'):item.lastMsg}}
          </div>
        </div>
      </div>


  </div>

</div>
  </el-scrollbar>
</template>

<script>
import bus from "../../bus";
export default {
name: "chatList",
  data(){
  return{
    searchInf:"",
     listInf:
        [{
          header:'',
          name:'张三',
          lastMsg:'zha',
          time:'2021/9/21'
        },{
          header:'',
          name:'任天然',
          lastMsg:'hahahahhaaaa',
          time:'2021/9/21'
        },{
          header:'',
          name:'西南石油大学聊天群',
          lastMsg:'hahahahhaaaaaaaaaaa',
          time:'今天'
        },{
          header:'',
          name:'西南石油大学聊天群',
          lastMsg:'hahahahhaaaaaaaaaaa',
          time:'今天'
        },{
          header:'',
          name:'西南石油大学聊天群',
          lastMsg:'hahahahhaaaaaaaaaaa',
          time:'今天'
        },{
          header:'',
          name:'西南石油大学聊天群',
          lastMsg:'hahahahhaaaaaaaaaaa',
          time:'今天'
        },{
          header:'',
          name:'西南石油大学聊天群',
          lastMsg:'hahahahhaaaaaaaaaaa',
          time:'今天'
        },{
          header:'',
          name:'西南石油大学聊天群',
          lastMsg:'hahahahhaaaaaaaaaaa',
          time:'今天'
        },{
          header:'',
          name:'西南石油大学聊天群',
          lastMsg:'hahahahhaaaaaaaaaaa',
          time:'今天'
        },{
          header:'',
          name:'西南石油大学聊天群',
          lastMsg:'hahahahhaaaaaaaaaaa',
          time:'今天'
        }]
  }
  },
  methods:{
  sendInf(item){
    var websocket= new WebSocket("ws://121.40.165.18:8800");
    websocket.onopen = function (event) {
      console.log("onopen");
    };
    websocket.onerror = function (error) {
      console.log("onerror: " + event.data);
    };
    websocket.onmessage = function (message) {
      console.log("onmessage: " + message.data);
    };
    bus.emit('chatPeople',item)
  }
  },
  mounted() {
    var websocket= new WebSocket("ws://121.40.165.18:8800");
    websocket.onopen = function (event) {
      console.log("onopen");
    };
    websocket.onerror = function (error) {
      console.log("onerror: " + event.data);
    };
    websocket.onmessage = function (message) {
      console.log("onmessage: " + message.data);
    };
  }


}
</script>

<style scoped>
.titleFather{
  height: 60px;
  width: 100%;

}

.searchTitle{
  width: 100%;
  background-color: #F7F7F7;
  height: 60px;
  display: inline-block;
  float: left;
  overflow:hidden;
  text-overflow:ellipsis;
  white-space:nowrap;
}
.searchAdd{
  padding: 14px 0px 0px 0px ;
  height: 60px;
  overflow:hidden;
  float: left;
  text-overflow:ellipsis;
  white-space:nowrap;
  overflow: hidden;
}
.icon i{
  transition: all 0.6s;
}
.icon i:hover{
  transform: rotate(-360deg);
}
.list{

  overflow: hidden;
  text-overflow:ellipsis;
  white-space:nowrap;
}
.inner{
  width: 10000px;
}
.inf{
  background-color: #EAE8E7;
  height: 65px;
  width: 100%;
  overflow:hidden;
  text-overflow:ellipsis;
  white-space:nowrap;
}
.inf div{
  width: 100%;
  padding-top: 5px;
  overflow: hidden;
  float: left;

}

</style>
