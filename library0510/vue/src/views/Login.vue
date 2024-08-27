<template>
  <div class="fullscreen-container">
    <div class="login">
      <div class="container">
        <div class="form-box">

          <!-- 登录 -->
          <div class="login-box" style="text-align: center">
            <h1>login</h1>
            <!-- 登录表单 -->
            <el-form :model="loginParams">
              <el-form-item>
                <el-input v-model="loginParams.username" placeholder="用户名" prefix-icon="el-icon-user"/>
              </el-form-item>
              <el-form-item>
                <el-input v-model="loginParams.password" type="password" placeholder="密码" prefix-icon="el-icon-lock"/>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="login()">登 录</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <div class="con-box right">
          <h2>欢迎来到<span>新书小屋</span></h2>
          <p>墨哥哥说<span>书爷爷</span>好老啊,<br/>但他讲的故事真的好好听啊</p>
          <img src="@/assets/images/book/crow.png" alt="">
          <p><a href="/register">没有账号？去注册</a></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "Login",
  data(){
    return{
      loginParams: {
        username: 'lazyfox',
        password:'password11'
      },

    }
  },
  methods:{


    //  获取注册的用户名信息
    getInfo() {
      const username = sessionStorage.getItem("username");
      if (username) {
        this.loginParams = {
          username: username,
          password: '' // 直接在这里初始化密码为空字符串
        };
        sessionStorage.clear(); // 只有当存在用户名时才清除会话存储
      }
    },

    //   登录的请求和逻辑
    login(){
      axios.post("http://localhost:9090/login",this.loginParams)
          .then(res=>{
          if(res.data.code === 0){
            this.$message({
              type:'success',
              message:res.data.message
            });
            sessionStorage.setItem("username",res.data.data.username);
            sessionStorage.setItem("key",res.data.data.key);
            sessionStorage.setItem("token",res.data.data.token);
            this.$router.replace({path:'/home'});
          }else {
            this.$message({
              type:'error',
              message:res.data.message
            });
          }
      });
    }
  },

  mounted() {
    this.getInfo();
  }
}
</script>

<style scoped>
.login{
  background: url("@/assets/bg.jpeg") no-repeat center center;
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
  margin-top: 35px;
  background-color: #f6f6f6;
  outline: none;
  border-radius: 8px;
  padding: 13px;
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