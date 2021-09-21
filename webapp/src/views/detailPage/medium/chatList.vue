<template>
<div>
  <div class="titleFather">
  <el-affix style=" overflow: hidden;width: 100%; text-overflow:ellipsis;white-space:nowrap;" :offset="120">
        <div class="title">
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
    <div v-for="(item,index) in listInf" :key="index" @click="sendInf(item)">
      <div class="inf">
        <div style="padding-right: 5px;width:50px;padding-left: 5px">
          <el-avatar shape="square" :size="40" :src=item.header></el-avatar>
        </div>
      <div style="width: 80px;padding-right: 20px;text-align: left">
        {{item.name.length>5?item.name.slice(0,5).concat('…'):item.name}}
      </div>

      <div style="font-size: 5px;width: 80px">
        {{item.time}}
      </div>
        <br>
        <div style="font-size: 3px;width: 150px;text-align: left">
          {{item.lastMsg.length>15?item.lastMsg.slice(0,15).concat('…'):item.lastMsg}}
        </div>
      </div>
    </div>
  </div>

</div>
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
      bus.emit('chatPeople',item)
  }
  }


}
</script>

<style scoped>
.titleFather{
  height: 60px;
  width: 100%;

}

.title{
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
  width: 100%;
}
.inf{
  align-items: center;
  background-color: #EAE8E7;
  border-bottom: 1px solid black;
  height: 50px;
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
