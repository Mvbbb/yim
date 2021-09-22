<template>

</template>

<script>
import {reactive, ref} from "@vue/runtime-core";
import {Test} from "../service/test";
    export default {
      name: "personalInf",
      data(){
        return{
          form:{
            userId:'xiaohei',
            token:'8e8f54ca'
          }

        }
      },
      methods:{
        test(){
          console.log(11)
          Test(this.form).then((res)=>{
            console.log(res.data)
          })
        }
      },
      mounted() {
        this.test()
      }

    }
</script>

<style scoped>
.active{
  color: green;
}
</style>
