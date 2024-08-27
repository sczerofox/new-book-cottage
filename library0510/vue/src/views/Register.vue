<template>
  <div class="fullscreen-container">
    <div class="register">
      <div class="container">
        <div class="form-box translate-x-80">

          <!-- 注册 -->
          <div class="register-box">
            <h1>register</h1>
            <!-- 登录表单 -->
            <el-form ref="registerFormRef" :model="registerParams"  :rules="registerRules">
              <el-form-item prop="username">
                <el-input v-model="registerParams.username" placeholder="用户名" prefix-icon="el-icon-user"/>
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="registerParams.password" show-password placeholder="密码" prefix-icon="el-icon-lock"/>
              </el-form-item>
              <el-form-item>
                <el-input v-model="rePassword" show-password placeholder="确认密码" prefix-icon="el-icon-lock"/>
              </el-form-item>
              <el-form-item prop="phone">
                <el-input v-model="registerParams.phone" @input="isPhoneInputValid()" placeholder="手机号" prefix-icon="el-icon-phone"/>
              </el-form-item>
              <el-form-item v-if="active" style="margin-top: 0">
                <el-input v-model="captcha" placeholder="验证码" prefix-icon="el-icon-thumb" style="width: 120px; margin-right: 10px"/>
                <el-button @click="getCaptcha()" style="width: 85px;">获取验证码</el-button>
              </el-form-item>
              <el-form-item style="text-align: center">
                <el-button type="primary" @click="register()">注 册</el-button>
              </el-form-item>
            </el-form>
          </div>

        </div>

        <div class="con-box left">
          <h2>欢迎来到<span>新书小屋</span></h2>
          <p>小雨姐姐说<span>墨哥哥</span>好有趣,<br/>我也这么觉得</p>
          <img src="@/assets/images/book/people.png" alt="">
          <p><a href="/login">已有账号？去登录</a></p>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Register",
  data(){
    return{

      //  注册提交参数
      registerParams:{
        username:'lazyGo',
        password:'123456',
        phone:'1322323476'
      },

      //  注册的基本校验
      registerRules:{
        username: [
          {required: true, message: '用户名不能为空',trigger: 'blur'}
        ],
        password:[
          {required: true, message: '密码不能为空',trigger: 'blur'},
          {min: 6, max: 18, message: '密码长度要求6-14个字符',trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '手机号不能为空',trigger: 'blur'},
          {min: 11, message: '请检查手机号是否正确',trigger: 'blur'}
        ],
      },

      // 表单的统一校验
      registerFormRef:{},

      rePassword:'123456',
      captcha:'',

      active:false,
    }
  },
  methods:{

    //  验证码是否为空
    isCaptchaNotEmpty(){
      return this.captcha === "" || this.captcha === null;
    },

    //  密码是否一致
    isPasswordAgree(){
      return this.registerParams.password !== this.rePassword;
    },

    //   手机号的基本验证
    isPhoneRel(){
      return  this.registerParams.phone.length > 10 && this.registerParams.phone.length < 12 ;
    },

    //  判断是否符合获取验证码要求
    isPhoneInputValid(){
      this.active = this.isPhoneRel();
    },

    // 检查表单全字段是否为空
    isFormEmpty() {
      return Object.values(this.registerParams).some(value => value.trim() === '');
    },

    //  注册
    register(){

      if(this.isPasswordAgree()){
        this.$message({
          type:'error',
          message:'两次密码不一致'
        });
        return false;
      }

      if(this.isCaptchaNotEmpty()){
        this.$message({
          type: 'error',
          message: '验证码不能为空'
        });
        return false;
      }

      if(this.isFormEmpty()){
        return false;
      }

      if(this.checkCaptcha()){
        this.$message({
          type:'error',
          message:'无法验证'
        });
        return false;
      }

      axios.post("http://localhost:9090/register",this.registerParams)
        .then(res=>{
          if(res.data.code === 0){
            this.$message({
              type: 'success',
              message: res.data.message
            });
            sessionStorage.setItem("username", this.registerParams.username);
            this.$router.replace({path: "/login"});
          } else {
            this.$message({
              type: 'error',
              message: res.data.message
            });
          }
      });
    },

    //  验证码验证
    checkCaptcha() {
      axios.get("http://localhost:9090/checkCaptcha", {
        params: {
          phone: this.registerParams.phone,
          captcha: this.captcha
        }
      }).then(response => {
        if (response.data.code === 1) {
          return true;
        } else {
          this.$message({
            type: 'error',
            message: response.data.message
          });
          return false;
        }
      }).catch(error => {
        this.$message({
          type: 'error',
          message: '验证码验证失败，请稍后再试。'
        });
        console.error("Check Captcha error:", error);
      });
    },

    //  验证码的生成
    getCaptcha() {
      if (this.isPhoneRel()) {
        axios.get("http://localhost:9090/captcha", {
          params: { phone: this.registerParams.phone }
        }).then(response => {
          if (response.data) {
            sessionStorage.setItem("captcha", response.data);
            this.captcha = response.data;
          } else {
            this.$message({
              type: 'error',
              message: '验证码: 生成错误'
            });
          }
        }).catch(error => {
          this.$message({
            type: 'error',
            message: '获取验证码失败，请稍后再试。'
          });
          console.error("Captcha error:", error);
        });
      } else {
        this.$message({
          type: 'info',
          message: '请确认手机号是否有误！'
        });
      }
    }

  },
}
</script>

<style scoped>

.translate-x-80 {
   transform: translateX(80%);
 }

.register{
  background: url("@/assets/bg.jpeg") no-repeat center;
  border-radius: 5px;
}

*{
  /* 初始化 */
  margin: 0;
  padding: 0;
}
.fullscreen-container{
  /* 100%窗口高度 */
  height: 100vh;
  /* 弹性布局 水平+垂直居中 */
  display: flex;
  justify-content: center;
  align-items: center;
  /* 渐变背景 */
  /*background: linear-gradient(200deg,#f3e7e9,#e3eeff);*/
  background: url("@/assets/background.png") no-repeat;
  background-size: cover;
}

.container{
  background-color: rgba(255,255,255,0.8);
  width: 650px;
  height: 415px;
  border-radius: 5px;
  /* 阴影 */
  box-shadow: 5px 5px 5px rgba(0,0,0,0.3);
  /* 相对定位 */
  position: relative;
}

.form-box{
  /* 绝对定位 */
  position: absolute;
  top: -10%;
  left: 5%;
  background-color: #d3b7d8;
  width: 320px;
  height: 500px;
  border-radius: 5px;
  box-shadow: 2px 0 10px rgba(0,0,0,0.1);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2;
  /* 动画过渡 加速后减速 */
  transition: 0.5s ease-in-out;
}
.register-box,.login-box{
  /* 弹性布局 垂直排列 */
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}
.hidden{
  display: none;
  transition: 0.5s;
}
h1{
  text-align: center;
  margin-bottom: 25px;
  /* 大写 */
  text-transform: uppercase;
  color: #fff;
  /* 字间距 */
  letter-spacing: 5px;
}
input{
  background-color: transparent;
  width: 70%;
  color: #fff;
  border: none;
  /* 下边框样式 */
  border-bottom: 1px solid rgba(255,255,255,0.4);
  padding: 10px 0;
  text-indent: 10px;
  margin: 8px 0;
  font-size: 14px;
  letter-spacing: 2px;
}
input::placeholder{
  color: #fff;
}
input:focus{
  color: #a262ad;
  outline: none;
  border-bottom: 1px solid #a262ad80;
  transition: 0.5s;
}
input:focus::placeholder{
  opacity: 0;
}
.form-box button{
  width: 70%;
  margin-top: 0px;
  background-color: #f6f6f6;
  outline: none;
  border-radius: 8px;
  padding: 10px;
  color: #a262ad;
  letter-spacing: 2px;
  border: none;
  cursor: pointer;
}
.form-box button:hover{
  background-color: #a262ad;
  color: #f6f6f6;
  transition: background-color 0.5s ease;
}
.con-box{
  width: 50%;
  /* 弹性布局 垂直排列 居中 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  /* 绝对定位 居中 */
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
}
.con-box.left{
  left: -2%;
}
.con-box.right{
  right: -2%;
}
.con-box h2{
  color: #8e9aaf;
  font-size: 25px;
  font-weight: bold;
  letter-spacing: 3px;
  text-align: center;
  margin-bottom: 4px;
}
.con-box p{
  font-size: 12px;
  letter-spacing: 2px;
  color: #8e9aaf;
  text-align: center;
}
.con-box span{
  color: #d3b7d8;
}
.con-box img{
  width: 150px;
  height: 150px;
  opacity: 0.9;
  margin: 40px 0;
  border-radius: 5px;
}
.con-box button{
  margin-top: 3%;
  background-color: #fff;
  color: #a262ad;
  border: 1px solid #d3b7d8;
  padding: 6px 10px;
  border-radius: 5px;
  letter-spacing: 1px;
  outline: none;
  cursor: pointer;
}
.con-box button:hover{
  background-color: #d3b7d8;
  color: #fff;
}

</style>