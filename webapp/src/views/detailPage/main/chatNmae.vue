<template>
<div>
  {{name}}
</div>
</template>

<script>
import bus from "../../bus";
export default {
  name: "chatName",
  data(){
    return {
      name:''
    }
  },
  mounted() {
    console.log(111)
  },
  created() {
    bus.on('chatPeople',(e)=>{
      this.name=e.name
    })
  },
}
</script>

<style scoped>

</style>
