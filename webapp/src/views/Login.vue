<template>
  <div class="registe">
    <!-- 标题 -->
    <div class="title">
      即时通讯
    </div>
    <div class="each_info" size="medium">
      <el-form ref="form" :model="formData" :rules="rule" class="apply-form first-form">
        <el-form-item prop="account_number"><i class="el-icon-user"></i>用户账号

          <el-input v-model="formData.userId" clearable placeholder="请输入账号"></el-input>

        </el-form-item>
        <el-form-item prop="password"><i class="el-icon-lock"></i>密码

          <el-input v-model="formData.password" :maxlength="18" :type="typePwd" clearable placeholder="请输入密码"
                    show-password>
          </el-input>

        </el-form-item>
      </el-form>
      <el-row>

        <!--                <router-link to="/companyRegister"><el-button round  type="primary" style="width: 250px;font-size: 15px;left: 10px;position: relative;" >企业认证请点我</el-button></router-link>-->
        <!--        <router-link to="/forgetAccount"></router-link>-->
        <el-button size="mini" style="font-size: 8px;left: 200px;position: relative;" type="text">忘记账号</el-button>
        <router-link to="/forgetPwd">
          <el-button size="mini" style="font-size: 8px;left: 240px;position: relative;" type="text">忘记密码</el-button>
        </router-link>

      </el-row>
      <el-row>
        <br>
        <el-button round style="width: 250px;left: 140px;position: relative;font-size: 18px;margin-bottom: 10px"
                   type="success"
                   @click="login">登录
        </el-button>
      </el-row>

      <el-row>
        <router-link to="/register">
          <el-button round style="width: 250px;left: 140px;position: relative;font-size: 18px;padding-bottom: 10px"
                     type="warning">
            注册
          </el-button>
        </router-link>
      </el-row>


    </div>
  </div>
</template>

<script>
import {Login} from "../service/login";


export default {
  name: "Login",
  data() {
    return {

      choice: '1',
      typePwd: 'password',
      formData: {
        userId: '',
        password: '',
        // status:''
      },
      rule: {
        mobile: [
          {required: true, message: '请输入账户', trigger: 'blur'},
          {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'},
          {
            required: true,
            pattern: /^[\u4e00-\u9fa5_a-zA-Z0-9.·-]+$/,
            message: '账户不支持特殊字符',
            trigger: 'blur'
          }
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 18, message: '请填写6-18位密码', trigger: 'blur'},
          {
            required: true,
            pattern: /^[a-zA-Z0-9]+$/,
            message: '请输入正确的字母或数字组合',
            trigger: 'blur'
          }
        ],
      }
    }
  },
  methods: {


    login() {

      if (this.formData.userId === '' || this.formData.password === '') {
        alert('账号或密码不能为空');
      } else {
        Login(this.formData).then(res => {
          console.log(res.data)
          if (res.data.msg === 'success') {

            // let token = Encrypt(res.data.data.token)
            // //要存储的值 //加密的秘钥（解密的时候必须要根据秘钥才能解密）
            // let level = Encrypt(res.data.data.level)
            let token = res.data.data.token
            console.log(token)
            //本地存储
            localStorage.setItem('Authorization', token)
            localStorage.setItem('userId', this.formData.userId)
            // localStorage.setItem('level',level)
            // store.dispatch('setLevel',res.data.data.level)

            this.$router.push('/Interface').catch(err => (console.log(err)))
            // ElNotification({
            //   title: 'Success',
            //   message: 'This is a success message',
            //   type: 'success',
            // })

          } else if (res.data.success == 'noAccount_number') {
            alert('账号不存在')
          } else if (res.data.success == 'noPass') {
            alert('账号未审核或未通过')
          }
          if (res.data.success == false) {
            alert('账号或密码错误')
          }
        }).catch(error => {
          alert('出错啦，请联系管理员');
          console.log(error);
        });
      }
    },
    // ...mapMutations(['set_token',"set_name","set_role","set_id"]),
    superLogin() {
      this.formData.status = 'super_admin'
      if (this.formData.account_number === '' || this.formData.password === '') {
        alert('账号或密码不能为空');
      } else {
        this.axios({
          method: 'post',
          url: '/log/login',
          data: this.formData
        }).then(res => {
          console.log(res)
          if (res.data.success == 'success') {

            this.set_token(JSON.stringify(res.data.token))
            this.set_name(JSON.stringify(res.data.object.name))
            this.set_id(JSON.stringify(res.data.object.id))
            this.set_role(JSON.stringify(res.data.jurisdiction))
            this.$router.push('/homepage').catch(err => (console.log(err)))
            alert('登陆成功');
          } else if (res.data.success == 'noAccount_number') {
            alert('账号不存在')
          } else {
            alert('账号或密码错误')
          }
        }).catch(error => {
          alert('出错啦，请联系管理员');
          console.log(error);
        });
      }
    }
  },

  mounted() {
    // window.localStorage.removeItem('Authorization')
  }

}
</script>

<style>
.changethecolor {
  color: red;
}

.title {
  display: flex;
  justify-content: center;
  align-self: center;
  margin-top: 150px;
  font-size: 70px;
  letter-spacing: 8px;
  color: #FFF;
  text-shadow: 5px 5px 10px black;
  z-index: 1;
}

.registe {
  width: 900px;
  height: 500px;
  margin: 60px auto 0;
  position: relative;
}

.each_info {
  justify-content: center;
  margin: 60px auto 0;
  position: relative;
  margin-left: 15px;
  width: 550px;
  height: 330px;
  left: 130px;
  background: #FFFFFF;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 10px 40px 0 rgb(0 13 97);

}

.el-input {
  outline: none;
  border: none;
  background: none;
  color: #fff;
  font-size: 18px;
}

.el-form-item__content {
  font-size: 20px;
  color: black;
}
</style>
