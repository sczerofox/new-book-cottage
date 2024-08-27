<template>
  <div class="layout">

    <!-- 标题栏 -->
    <div style="height: 60px; line-height: 60px; background-color: white; margin-bottom: 2px">
      <img src="@/assets/book.png" alt="" style="height: 40px; position: relative; top: 10px; left: 10px">
      <span style="font-size: 24px; margin-left: 20px; color: rgb(18, 150, 219)">新书小屋</span>
    </div>

    <!-- 菜单栏和内容区域 -->
    <div style="display: flex">

      <!-- 菜单栏 -->
      <div style="width: 200px; height: 200vh;  background-color: white; overflow: hidden; margin-right: 2px">
        <el-menu :default-active="$route.path" router class="el-menu-demo">

          <!-- 首页 -->
          <el-menu-item index="/home">
            <i class="el-icon-s-home"></i>
            <span>大厅</span>
          </el-menu-item>

          <!-- 书屋区域 -->
          <el-submenu index="0" >
            <template slot="title">
              <i class="el-icon-data-line"></i>
              <span>小屋</span>
            </template>
            <el-menu-item index="/library">
              <i class="el-icon-s-management"></i>
              <span>书阁</span>
            </el-menu-item>
            <el-menu-item index="/elegant">
              <i class="el-icon-user"></i>
              <span>雅阁</span>
            </el-menu-item>
            <el-menu-item index="/bar">
              <i class="el-icon-bell"></i>
              <span>小屋吧</span>
            </el-menu-item>
          </el-submenu>

          <!-- 用户区域 -->
          <el-submenu index="2" >
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>{{tokenCheck.username}}</span>
            </template>
            <el-menu-item @click="openDrawer(1)">
              <i class="el-icon-user"></i>
              <span>个人信息</span>
            </el-menu-item>
            <el-menu-item @click="openDrawer(2)">
              <i class="el-icon-edit"></i>
              <span>作家信息</span>
            </el-menu-item>
          </el-submenu>

          <!-- 管理区域 -->
          <el-submenu index="3" v-if="isAdmin">
            <template slot="title">
              <i class="el-icon-data-line"></i>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/books">
              <i class="el-icon-s-management"></i>
              <span>书籍管理</span>
            </el-menu-item>
            <el-menu-item index="/users">
              <i class="el-icon-user"></i>
              <span>用户管理</span>
            </el-menu-item>
          </el-submenu>


          <!-- 设置区域 -->
          <el-menu-item index="/settings">
            <i class="el-icon-setting"></i>
            <span>设  置</span>
          </el-menu-item>

          <el-menu-item index="/about">
            <i class="el-icon-bell"></i>
            <span>关于系统</span>
          </el-menu-item>

          <!-- 操作区域 -->
          <el-menu-item @click="openConfirm()">
            <i class="el-icon-switch-button"></i>
            <span>退出登录</span>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 抽屉 -->
      <el-drawer :visible.sync="drawerModel" :title="drawerTitle" :with-header="true" :modal="false" style="margin-top: 62px; overflow: hidden;" size="400px">
        <el-container style="height: 100%;" v-if="drawerTitle === '用户信息'">
          <!-- 头部  -->
          <el-header style="height: 145px;">
            <div style="">
              <PrivateHeader :private="private" :public-icon="publicIcon" :percentage="privatePercentage"/>
            </div>
          </el-header>
          <!-- 中部  -->
          <el-main>
            <div style="">
              <PrivateCenter :private-info="privateInfo" :stars="pStars" :history="pHistory" :all-empty="allEmpty"/>
            </div>
          </el-main>
          <!-- 底部 -->
          <el-footer>
            <div style="padding: 10px; text-align: center;">
              <el-button @click="drawerModel = false" >取 消</el-button>
              <el-button @click="openMethods(1)" type="primary">编 辑</el-button>
            </div>
          </el-footer>
        </el-container>

        <el-container style="height: 100%;" v-if="drawerTitle === '作家信息'">
          <!-- 头部  -->
          <el-header style="height: 145px;">
            <div>
              <AuthorHeader :author="author" :public-icon="publicIcon" :percentage="authorPercentage"/>
            </div>
          </el-header>
          <!-- 中部  -->
          <el-main>
            <div v-if="isAuthor">
              <PrivateCenter :private-info="privateInfo" :is-author="isAuthor" :works="aWorks" :stars="aStars" :history="aHistory"/>
            </div>
            <div v-else style="text-align: center;">
              <el-empty description="还未成为作家">
                <el-button @click="openMethods(2) " type="primary">发布</el-button>
              </el-empty>
            </div>
          </el-main>
          <!-- 底部 -->
          <el-footer>
            <div style="padding: 10px; text-align: center;">
              <el-button @click="drawerModel = false" >取 消</el-button>
              <el-button v-if="isAuthor" @click="openMethods(3)" type="primary">编 辑</el-button>
            </div>
          </el-footer>
        </el-container>
      </el-drawer>
      <!-- 内容区域 -->
      <div style="flex: 1; background-color: white">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import PrivateHeader from "@/components/PrivateHeader.vue";
  import PrivateCenter from "@/components/PrivateCenter.vue";
  import AuthorHeader from "@/components/AuthorHeader";


  export default {
    name:'index',
    components: {
      AuthorHeader,
      PrivateHeader,
      PrivateCenter
    },
    data(){
      return{
        tokenCheck:{
          username:'',
          key:'',
          token:''
        },

        drawerModel: false,
        drawerTitle: '用户信息',
        drawerIndex: 0,

        private:{},
        privatePercentage: 0,
        authorPercentage: 0,

        author:{
          grade: 0,
          experience: 0,
          max: 2000,
        },

        publicIcon:{
          edit: false,
          admin: false,
          vip: false,
        },

        isAuthor: false,
        isAdmin: false,

        privateInfo:{
          grade: 0,
          experience: 0,
          sex: '',
          phone: '',
          email: ''
        },
        allEmpty: false,

        aWorks:[],
        aStars:[],
        aHistory:[],

        pStars:[],
        pHistory:[],

      }
    },
    methods:{

      //  打开抽屉
      openDrawer(val){
        if(val === 1){
          this.drawerModel = false;
          this.drawerTitle = '用户信息';
          this.drawerModel = true;
          this.getPrivate();
        }
        if(val === 2){
          this.drawerModel = false;
          this.drawerTitle = '作家信息';
          this.drawerModel = true;
          this.getAuthor();
        }
        if(val === 3){
          this.drawerModel = false;
          this.drawerTitle = 'Settings';
          this.drawerModel = true;
        }
      },

      //  方法调用
      openMethods(val){
        if(val === 1){
          if(this.$route.path !== '/settings'){
            this.drawerModel = false;
            this.$router.push({
              path: '/settings',
              query: {
                index: '1'
              }
            });
          }
          this.$message({type:'success',message: '你已经找到了~'});
          this.drawerModel = false;
          return;
        }
        if(val === 2){
          if(this.$route.path !== '/settings'){
            this.drawerModel = false;
            this.$router.push({
              path: '/settings',
              query: {
                index: '3'
              }
            });
          }
          this.$message({type:'success',message: '点击发布书籍~'});
          this.drawerModel = false;
          return;
        }
        if(val === 3){
          if(this.$route.path !== '/settings'){
            this.drawerModel = false;
            this.$router.push({
              path: '/settings',
              query: {
                index: '2'
              }
            });
          }
          this.$message({type:'success',message: '点击修改作家信息~'});
          this.drawerModel = false;
        }
      },

      returnUsername(){
        let name = "";
        if(sessionStorage.getItem("username") !== null){
          name = sessionStorage.getItem("username");
        }else {
          const userInfo = localStorage.getItem("userInfo");
          name = JSON.parse(userInfo).username;
        }
        return name;
      },

      // 查询用户信息
      getUserInfo(){
        let name = this.returnUsername();
        axios.get("http://localhost:9090/userInfo",{
          params: {
            name: name
          }
        }).then(res=>{
            this.userParams = JSON.parse(JSON.stringify(res.data));
            this.userParams.password = "";
            this.privateInfo.sex = res.data.gender;
            this.privateInfo.email = res.data.email;
            this.privateInfo.phone = res.data.phone;
        });
      },

      setUserInfo(){
        let name = this.returnUsername();
        localStorage.clear();
        axios.get("http://localhost:9090/private",{
          params: {
            name: name
          }
        }).then(res=>{
          localStorage.setItem("userPrivate",JSON.stringify(res.data));
        });
        axios.get("http://localhost:9090/userInfo",{
          params: {
            name: name
          }
        }).then(res=>{
          localStorage.setItem("userInfo",JSON.stringify(res.data));
        });
      },

      //  清理数据残余
      clearData(){
        this.privateInfo.grade = 0;
        this.privateInfo.experience = 0;
      },

      //  获取是否是管理员
      getIsAdmin(){
        let name = this.returnUsername();
        axios.get("http://localhost:9090/admin",{
          params: {
            name: name
          }
        }).then(res=>{
          if(res.data){
            this.isAdmin = res.data;
            this.publicIcon.admin = true;
            sessionStorage.setItem("isAdmin",res.data);
          }
        });
      },

      //  获取是否是作家
      getIsAuthor(){
        let name = this.returnUsername();
        axios.get("http://localhost:9090/isAuthor",{
          params: {
            name: name
          }
        }).then(res=>{
          if(res.data){
            this.isAuthor = res.data;
            this.publicIcon.edit = true;
            sessionStorage.setItem("isAuthor",res.data);
          }
        });
      },

      //  获取作家作品集
      getAWorks(){
        let name = this.returnUsername();
        axios.get("http://localhost:9090/aWork",{
          params: {
            name: name
          }
        }).then(res=>{
          this.aWorks = res.data;
        });
      },

      //  获取作家收藏集
      getAStars(){
        let name = this.returnUsername();
        axios.get("http://localhost:9090/aStar",{
          params: {
            name: name
          }
        }).then(res=>{
          this.aStars = res.data;
        });
      },

      //  获取作家历史记录
      getAHistory(){
        let name = this.returnUsername();
        axios.get("http://localhost:9090/aHistory",{
          params: {
            name: name
          }
        }).then(res=>{
          this.aHistory = res.data;
        });
      },

//  获取作家收藏集
      getPStars(){
        let name = this.returnUsername();
        axios.get("http://localhost:9090/pStar",{
          params: {
            name: name
          }
        }).then(res=>{
          this.pStars = res.data;
          this.allEmpty = this.pStars.length === 0;
        });
      },

      //  获取作家历史记录
      getPHistory(){
        let name = this.returnUsername();
        axios.get("http://localhost:9090/pHistory",{
          params: {
            name: name
          }
        }).then(res=>{
          this.pHistory = res.data;
        });
      },

      //  获取用户信息
      getPrivate(){
        this.clearData();
        this.getUserInfo();
        this.getPStars();
        this.getPHistory();
        let name = this.returnUsername();
        axios.get("http://localhost:9090/private",{
          params: {
            name: name
          }
        }).then(res=>{
          this.private = res.data;
          this.private.headerUrl = "http://localhost:9090/images/"+res.data.headerUrl;
          this.privateInfo.grade = this.private.grade;
          this.privateInfo.experience = this.private.experience;
          this.privatePercentage = this.private.experience / this.private.max * 100;
        });
      },

      //  获取作家信息
      getAuthor(){
        this.clearData();
        this.getUserInfo();
        if(this.isAuthor){
          this.getAWorks();
          this.getAStars();
          this.getAHistory();
        }
        let name = this.returnUsername();
        axios.get("http://localhost:9090/author",{
          params: {
            name: name
          }
        }).then(res=>{
          if(res.data === ""){}else {
            this.author = res.data;
            this.author.headerUrl = "http://localhost:9090/images/"+res.data.headerUrl;
            this.privateInfo.grade = res.data.grade;
            this.privateInfo.experience =  res.data.experience;
            if (Number.isNaN(this.author.experience / this.author.max)) {
              this.authorPercentage = 0;
            } else {
              this.authorPercentage = this.author.experience / this.author.max * 100;
            }
          }
        });
      },

      //  判断tokenCheck 为空
      isTokenCheckNotEmpty(){
        return this.tokenCheck.username === null || this.tokenCheck.key === null || this.tokenCheck.token === null;
      },

      //  获取全局token的验证
      getTokenCheck(){
        this.tokenCheck.username = sessionStorage.getItem("username");
        this.tokenCheck.key = sessionStorage.getItem("key");
        this.tokenCheck.token = sessionStorage.getItem("token");
        if(this.isTokenCheckNotEmpty()){
          sessionStorage.clear();
          this.$router.replace({ path: "/login" });
          this.$message({
            type: 'error',
            message: '无鉴权码'
          });
          return ;
        }
        axios.post("http://localhost:9090/checkToken",this.tokenCheck).then(res=>{
          if(res.data.code === 0){
            sessionStorage.setItem("checkToken","通过");
          }else {
            this.$message({
              type:'error',
              message:res.data.message
            });
            this.outLogin();
          }
        });
      },

      //  退出登录的方法
      outLogin() {
        let name = this.returnUsername();
        axios.get("http://localhost:9090/out", {
          params: {
            username: name,
            key: sessionStorage.getItem("key")
          }
        }).then(res => {
          if (res.data.code === 0) {
            sessionStorage.clear();
            localStorage.clear();
            this.$router.replace({ path: "/login" });
          } else {
            this.$message({
              type: 'error',
              message: res.data.message
            });
          }
        }).catch(error => {
          this.$message({
            type: 'error',
            message: '登出请求失败，请稍后再试。'
          });
          console.error("Logout error:", error);
        });
      },


      //  打开退出的提示窗口
      openConfirm(){
        this.$confirm('确定退出吗, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '退出成功'
          });
          this.outLogin();
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消退出'
          });
        });
      },

      gotoHome(){
        if(this.$route.path === '/'){
          this.$router.replace({path:'/home'});
          return true;
        }
      }
    },

    // watch:{
    //   $route:{
    //     //  监听路由变化
    //     handler(val,oldVal){
    //       this.getTokenCheck();
    //     },
    //     // 深度观察监听
    //     deep: true
    //   }
    // },

    mounted() {
      this.setUserInfo();
      //  构子函数 调用token全局验证
      this.getTokenCheck();
      this.getIsAuthor();
      this.getIsAdmin();
      this.gotoHome();
    }
  }
</script>

<style>
.layout{
  height: 100%;
  width: 100%;
}
</style>

