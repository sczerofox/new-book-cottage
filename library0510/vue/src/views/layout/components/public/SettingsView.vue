<template>
  <div class="setting">
    <!-- 导航栏 -->
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">修改用户信息</el-menu-item>
      <el-menu-item index="2">修改作家信息</el-menu-item>
      <el-menu-item index="3">发布书籍</el-menu-item>
      <el-menu-item index="4">添加数据</el-menu-item>
      <el-menu-item index="5">修改密码</el-menu-item>
    </el-menu>

    <!-- 内容栏 -->
    <div class="content">
      <!-- 用户信息 -->
      <div v-if="activeIndex === '1'">
        <div class="block">
            <el-upload class="avatar-uploader" action="http://localhost:9090/privateUpload"
                       :show-file-list="false"
                       :data="uploadData"
                       :on-success="uploadPrivateSuccess">
              <img v-if="imagePrivateUrl" :src="imagePrivateUrl" class="avatar" alt="" @click="setUploadData()">
              <i v-else class="el-icon-plus avatar-uploader-icon" ></i>
            </el-upload>
            <span class="block-avatar-span">用户头像</span>
        </div>
        <!-- 用户信息表单 -->
        <el-form :model="userParams">
          <el-form-item label="用户名">
            <el-input v-model="userParams.username" :disabled="true"/>
          </el-form-item>
          <el-form-item label="真实姓名">
            <el-input v-model="userParams.realName"/>
          </el-form-item>
          <el-form-item label="性别">
            <el-input v-model="userParams.gender"/>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="userParams.email"/>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="userParams.phone"/>
          </el-form-item>
          <el-form-item label="借书证号">
            <div class="card-num-input">
              <el-input v-model="userParams.cardNum" :disabled="true"></el-input>
              <el-button type="primary" @click="getCardNum()" :disabled="this.userParams.cardNum !== ''">获取证号</el-button>
            </div>
          </el-form-item>
          <el-form-item style="text-align: center">
            <el-button type="primary" @click="editPrivate()">修 改</el-button>
          </el-form-item>
        </el-form>
      </div>


      <div v-if="activeIndex === '2'">
        <!-- 作家基本信息 -->
        <div class="au-div">
          <!-- 作家信息 -->
          <div class="au-left">
            <div v-if="isAuthor === 1">
              <!-- 上传区 -->
              <div class="block">
                <el-upload class="avatar-uploader" action="http://localhost:9090/authorUpload" :show-file-list="false" :data="uploadData" :on-success="uploadAuthorSuccess">
                  <img v-if="imageAuthorUrl" :src="imageAuthorUrl" class="avatar" @click="setUploadData()" alt="">
                  <i v-else class="el-icon-plus avatar-uploader-icon" ></i>
                </el-upload>
                <span class="block-avatar-span">作家头像</span>
              </div>

              <!-- 简单信息区 -->
              <div class="au-left-info">
                <el-descriptions title="作家信息" :column=1 size="18px">
                  <el-descriptions-item label="笔名">{{ authorParams.name }}</el-descriptions-item>
                  <el-descriptions-item label="等级">{{ authorParams.grade }}</el-descriptions-item>
                  <el-descriptions-item label="经验">{{ authorParams.experience }}</el-descriptions-item>
                  <el-descriptions-item label="最大经验">{{ authorParams.max }}</el-descriptions-item>
                  <el-descriptions-item label="小屋">
                    <el-tag v-if="authorParams.name !== ''">这个作家~ 好好吃...</el-tag>
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </div>
            <!-- 空状态 -->
            <div v-else>
              <el-empty description="你还不是作家">
                <el-button @click="activeIndex = '3'" type="primary">去发布书籍</el-button>
              </el-empty>
            </div>
          </div>

          <!-- 作家作品列表 -->
          <div class="au-center">
            <div v-if="isAuthor === 1">
              <div class="au-center-top" >
                <span class="au-center-top-span">作品列表</span>
              </div>
              <div class="au-center-bottom">
                <el-table :data="works">
                  <el-table-column prop="bookname" label="书名"></el-table-column>
                  <el-table-column prop="author" label="作者"></el-table-column>
                  <el-table-column label="操作" >
                    <template v-slot="scope">
                      <el-button type="text" @click="openEditWindows(scope.row)">编 辑</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
            <div v-else>
              <el-empty description="空空如也">

              </el-empty>
            </div>
          </div>

          <!-- 作家作品信息 -->
          <div class="au-right" v-if="isUpdateBook">
            <div class="au-right-top">
              <span class="au-right-top-span">修改作品信息</span>
            </div>
            <div class="au-right-bottom">
              <div class="au-right-bottom-left">
                <div class="block">
                  <el-upload class="book-uploader" action="http://localhost:9090/coverUpload"
                             :show-file-list="false"
                             :data="uploadCoverData"
                             :on-success="uploadCoverSuccess">
                    <img v-if="imageCoverUrl" :src="imageCoverUrl" class="book" alt="" @click="setUploadData()">
                    <i v-else class="el-icon-plus book-uploader-icon" ></i>
                  </el-upload>
                  <span class="block-avatar-span">书籍封面</span>
                </div>
              </div>

              <div class="au-right-bottom-right">
                <div class="box-left-bottom-form">
                  <el-form :model="bookInfo">
                    <el-form-item label="书名">
                      <el-input v-model="bookInfo.bookname"/>
                    </el-form-item>
                    <el-form-item label="作家">
                      <el-input v-model="bookInfo.author" disabled/>
                    </el-form-item>
                    <el-form-item label="价格">
                      <el-input v-model="bookInfo.price"/>
                    </el-form-item>
                    <el-form-item label="发布时间">
                      <el-input placeholder="系统默认时间" disabled/>
                    </el-form-item>
                    <el-form-item label="页数">
                      <el-input v-model="bookInfo.count"/>
                    </el-form-item>
                    <el-form-item label="类型">
                      <el-input v-model="bookInfo.type"></el-input>
                    </el-form-item>
                    <el-form-item style="text-align: center">
                      <el-button type="primary" @click="editBook()">编 辑</el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </div>

            </div>
          </div>
        </div>

      </div>

      <div v-if="activeIndex === '3'">
        <div class="box-all">
          <!-- 发布书籍信息 -->
          <div class="box-left">
            <div class="box-left-top">
              <span class="box-left-top-span">上 传 区</span>
            </div>

            <div class="box-left-bottom">
              <div class="box-left-bottom-upload">
                <el-upload class="book-uploader"
                           ref="upload"
                           action="#"
                           :show-file-list="false"
                           :auto-upload="false"
                           :http-request="uploadFile"
                           :on-change="handleChange"> <!-- :disabled="true" :auto-upload="false"-->
                  <img v-if="imageBookUrl" :src="imageBookUrl" class="book" @click="setUploadData()" alt="">
                  <i v-else class="el-icon-plus book-uploader-icon" ></i>
                </el-upload>
                <span class="block-avatar-span">书籍封面</span>
              </div>

              <div class="box-left-bottom-form">
                <el-form :model="bookInfo">
                  <el-form-item label="书名">
                    <el-input v-model="bookInfo.bookname"/>
                  </el-form-item>
                  <el-form-item label="作家">
                    <el-input v-model="bookInfo.author" disabled/>
                  </el-form-item>
                  <el-form-item label="价格">
                    <el-input v-model="bookInfo.price"/>
                  </el-form-item>
                  <el-form-item label="发布时间">
                    <el-input placeholder="系统默认时间" disabled/>
                  </el-form-item>
                  <el-form-item label="页数">
                    <el-input v-model="bookInfo.count"/>
                  </el-form-item>
                  <el-form-item label="类型">
                    <el-input v-model="bookInfo.type"></el-input>
                  </el-form-item>
                  <el-form-item style="text-align: center">
                    <el-button type="primary" @click="addBook()">发 布</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </div>


          <!-- 上传成功之后的效果 -->
          <div class="box-right">
            <div class="box-right-top">
              <span class="box-right-top-span">预 览 区</span>
            </div>
            <div class="box-right-bottom">
              <div v-if="!isBookUploadSuccess">
                <el-empty description="未发布状态"></el-empty>
              </div>
              <div v-else>
                <div class="box-right-bottom-img">
                  <el-upload class="book-uploader" action="#" :show-file-list="false" :auto-upload="false" :disabled="true"> <!-- :disabled="true" :auto-upload="false"-->
                    <img v-if="imageReBookUrl" :src="imageReBookUrl" class="book" alt="">
                    <i v-else class="el-icon-plus book-uploader-icon" ></i>
                  </el-upload>
                  <span class="block-avatar-span">书籍封面</span>
                </div>

                <div class="box-right-bottom-info">
                  <el-descriptions title="书籍信息" :column=1 size="18px">
                    <el-descriptions-item label="书名">{{ bookParams.bookname }}</el-descriptions-item>
                    <el-descriptions-item label="作者">{{ bookParams.author }}</el-descriptions-item>
                    <el-descriptions-item label="价格">{{ bookParams.price }}</el-descriptions-item>
                    <el-descriptions-item label="时间">{{ bookParams.date }}</el-descriptions-item>
                    <el-descriptions-item label="页数">{{ bookParams.count }}</el-descriptions-item>
                    <el-descriptions-item label="类型">{{ bookParams.type }}</el-descriptions-item>
                    <el-descriptions-item label="小屋">
                      <el-tag v-if="bookParams.id !== ''">新书啊~ 真好吃...</el-tag>
                    </el-descriptions-item>
                  </el-descriptions>
                </div>
              </div>

            </div>
          </div>
        </div>

      </div>

      <div v-if="activeIndex === '4'">
        <!-- 添加数据 -->
        <div class="box-all">
          <!-- 发布书籍信息 -->
          <div class="box-left">
            <div class="box-left-top">
              <span class="box-left-top-span">上 传 区</span>
            </div>

            <div class="box-left-bottom">
              <div class="box-left-bottom-upload">
                <el-upload class="book-uploader"
                           ref="upload"
                           action="#"
                           :show-file-list="false"
                           :auto-upload="false"
                           :http-request="uploadFile"
                           :on-change="handleChange"> <!-- :disabled="true" :auto-upload="false"-->
                  <img v-if="imageBookUrl" :src="imageBookUrl" class="book" @click="setUploadData()" alt="">
                  <i v-else class="el-icon-plus book-uploader-icon" ></i>
                </el-upload>
                <span class="block-avatar-span">书籍封面</span>
              </div>
            </div>
          </div>


          <!-- 上传成功之后的效果 -->
          <div class="box-right">

            <div class="box-right-top">
              <span class="box-right-top-span">预 览 区</span>
            </div>
            <div class="box-right-bottom">
              <div v-if="!isBookUploadSuccess">
                <el-empty description="未发布状态"></el-empty>
              </div>
              <div v-else>

              </div>

            </div>
          </div>
        </div>
      </div>

      <div v-if="activeIndex === '5'">
        <div class="pwd-box">

          <div class="pwd-top">
            <span class="pwd-top-span">修 改 密 码</span>
          </div>

          <div  class="pwd-bottom">
            <!-- 修改密码 -->
            <el-form :model="userParams">
              <el-form-item label="原密码">
                <el-input v-model="changePassword.oldPassword" show-password/>
              </el-form-item>
              <el-form-item label="新密码">
                <el-input v-model="changePassword.newPassword" show-password/>
              </el-form-item>
              <el-form-item label="确认密码">
                <el-input v-model="changePassword.rePassword" show-password/>
              </el-form-item>
              <el-form-item style="text-align: center">
                <el-button type="primary" @click="updatePassword()">修 改</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";

  export default {
    name: 'SettingsViews',
    data(){
      return{
        isAuthor: 0,

        isBookUploadSuccess: false,

        isUpdateBook: false,

        activeIndex: "4",
        // activeModel: '',

        imagePrivateUrl: '',
        imageAuthorUrl: '',
        imageBookUrl: '',
        imageReBookUrl: '',
        imageCoverUrl: '',

        uploadData: {
          name: '', // 这里应该是动态的用户名
        },

        uploadCoverData:{
          name: '', // 这里应该是动态的书
        },

        userParams:{
          username:'',
          realName:'',
          password:'',
          gender:'',
          email:'',
          phone:'',
          cardNum: ''
        },

        bookParams: {
          id: '',
          bookname: '',
          author: '',
          price: '',
          date: '',
          count: '',
          type:''
        },

        authorParams:{
          name:'',
          grade:'',
          experience:'',
          max:'',
          work:'',
          star:'',
          history: ''
        },

        bookInfo: {
          bookname: '',
          author: '',
          price: '',
          count: '',
          type:''
        },

        formData: {
          name: '',
          file: null
        },

        userPrivate:{
          headerUrl:''
        },

        publicIcon:{
          edit: false,
          admin: false,
          vip: false,
        },

        changePassword:{
          name: '',
          oldPassword: '',
          newPassword: '',
          rePassword: ''
        },

        works:[],

      }
    },

    methods: {

      editBook(){
        axios.post('http://localhost:9090/books/edit', this.bookInfo).then(res => {
          console.log(res.data);
          if (res.data === 1) {
            this.$message({
              type: 'success',
              message: '修改成功'
            });
          }
        }).catch(err=>{
          console.log(err);
        });
      },

      openEditWindows(row){
        this.isUpdateBook = true;
        this.bookInfo = row;
        this.uploadCoverData.name = row.bookname;
        this.imageCoverUrl = "http://localhost:9090/images/"+row.webUrl;
      },

      getAuthorWorks(){
        let name = sessionStorage.getItem('username');
        axios.get('http://localhost:9090/aWork', {
          params: {
            name: name
          }
        }).then(res => {
          if (res.data.size !== 0) {
            this.works = res.data;
          }
        }).catch(err=>{
          console.log(err);
        });
      },

      getAuthorInfo(){
        let name = sessionStorage.getItem('username');
        axios.get('http://localhost:9090/author', {
          params: {
            name: name
          }
        }).then(res => {
          if (res.data.auId !== "") {
            this.authorParams = res.data;
            this.imageAuthorUrl = "http://localhost:9090/images/"+res.data.headerUrl;
          }
        }).catch(err=>{
          console.log(err);
        });
      },

      getBookInfo(name){
        axios.get('http://localhost:9090/books/bookOne', {
          params: {
            name: name
          }
        }).then(res => {
              if (res.data.code === 1) {
                this.bookParams = res.data.data;
                this.imageReBookUrl = "http://localhost:9090/images/"+res.data.data.webUrl;
                this.setUserInfo();
                this.getUserInfo();
              } else {
                this.$message({
                  type: 'error',
                  message: res.data.message
                });
              }
            }).catch(err=>{
          console.log(err);
        });
      },

      addBook(){
        let form = new FormData();
        form.append("file", this.formData.file);
        let bookInfo = this.bookInfo;
        for (let key in bookInfo) {
          if (bookInfo.hasOwnProperty(key)) {
            form.append(key, bookInfo[key]);
          }
        }

        if(this.formData.file === null){
          this.$message({
            type:'error',
            message:'未选择封面'
          })
          return
        }

        axios.post('http://localhost:9090/bookUpload', form,{
          "Content-Type": "multipart/form-data;charset=utf-8"
        }).then(res => {
              if (res.data.code === 1) {
                this.$message({
                  type: 'success',
                  message: '发布成功!'
                });
                this.isBookUploadSuccess = res.data.data.addWork;
                this.imageBookUrl = '';
                this.bookInfo = {};
                this.bookInfo.author = sessionStorage.getItem('username');
                this.getBookInfo(res.data.data.name);
                this.getUserInfo(res.data.data.name);
                alert(this.isAuthor);
              } else {
                this.$message({
                  type: 'error',
                  message: res.data.message
                });
              }
            }).catch(err=>{
          console.log(err);
        });
      },

      handleChange() {
        this.$refs.upload.submit();
      },

      //自定义上传文件
      uploadFile (file) {
        // console.log(file.file, "文件");
        this.formData.file = file.file;
        let reader = new FileReader()
        reader.readAsDataURL(file.file)
        reader.onload = () => {
          this.imageBookUrl = reader.result;
        }
      },

      getCardNum(){
        const userInfo = localStorage.getItem("userInfo");
        let name = JSON.parse(userInfo).username;
      axios.get('http://localhost:9090/getCardNum', {
          params: {
            name: name
          }
        })
      .then(res => {
              if (res.data.code === 1) {
                this.$message({
                  type: 'success',
                  message: '密码修改成功!'
                });
                this.setUserInfo();
                this.userParams.cardNum = res.data.message;
              } else {
                this.$message({
                  type: 'error',
                  message: res.data.message
                });
              }
            }).catch(err=>{
          console.log(err);
        });
      },

      updatePassword(){
        const userInfo = localStorage.getItem("userInfo");
        this.changePassword.name = JSON.parse(userInfo).username;
        axios.post('http://localhost:9090/users/updatePassword', this.changePassword)
            .then(res => {
              // console.log(res.data.code);
              if (res.data.code === 1) {
                this.$message({
                  type: 'success',
                  message: '密码修改成功!'
                });
                this.setUserInfo();
                this.changePassword = {};
              } else {
                this.$message({
                  type: 'error',
                  message: res.data.message
                });
              }
            }).catch(err=>{
              console.log(err);
        });
      },

      editPrivate(){
        axios.post('http://localhost:9090/users/edit', this.userParams)
          .then(res => {
            if (res.data > 0) {
              this.$message({
                  type: 'success',
                  message: '编辑成功!'
              });
              this.setUserInfo();
            } else {
              this.$message({
                type: 'info',
                message: '编辑失败!'
              });
            }
        }).catch(err=>{
          console.log(err);
        });
      },

      setUploadData(){
        this.uploadData.name = sessionStorage.getItem('username');
      },

      uploadPrivateSuccess(res) {
        if(res.data.isUpdate){
          this.imagePrivateUrl = "http://localhost:9090/images/"+res.data.filePath;
          this.$message({type:'success',message:res.message});
          this.setUserInfo();
          return true;
        }
        this.$message({type:'error',message:res.message});
        return false;
      },

      uploadAuthorSuccess(res) {
        if(res.data.isUpdate){
          this.imageAuthorUrl = "http://localhost:9090/images/"+res.data.filePath;
          this.$message({type:'success',message:res.message});
          this.getAuthorInfo();
          return true;
        }
        this.$message({type:'error',message:res.message});
        return false;
      },

      uploadCoverSuccess(res) {
        if(res.data.isUpdate){
          this.imageCoverUrl = "http://localhost:9090/images/"+res.data.filePath;
          this.$message({type:'success',message:res.message});

          return true;
        }
        this.$message({type:'error',message:res.message});
        return false;
      },

      uploadBookSuccess(res){
        if(res.data.isUpdate){
          this.imageBookUrl = "http://localhost:9090/images/"+res.data.filePath;
          this.$message({type:'success',message:res.message});
          this.setUserInfo();
          return true;
        }
        this.$message({type:'error',message:res.message});
        return false;
      },

      handleSelect(key){
        this.activeIndex = key;
        if(key === '2' && this.isAuthor === 1){
          this.isUpdateBook = false;
          this.getAuthorInfo();
          this.getAuthorWorks();
          this.uploadData.name = sessionStorage.getItem('username');
        }
        if(key === '3'){
          this.setUploadData();
          this.bookInfo = {};
          this.bookInfo.author = sessionStorage.getItem('username');
          this.bookParams.author = sessionStorage.getItem('username');
        }
        // if(key === '1'){this.activeModel = 'private';}
        // if(key === '2'){this.activeModel = 'author';}
      },

      //  获取用户数据
      getUserInfo() {
        if(this.$route.query.index === undefined){}else {this.activeIndex = this.$route.query.index;}
        const userInfo = localStorage.getItem("userInfo");
        const userPrivate = localStorage.getItem("userPrivate");
        this.userParams = JSON.parse(userInfo);
        this.userPrivate = JSON.parse(userPrivate);
        sessionStorage.setItem("isAuthor",this.userPrivate.isAuthor);
        sessionStorage.setItem("admin",this.userPrivate.admin);
        this.bookInfo.author = sessionStorage.getItem('username');
        this.isAuthor = this.userPrivate.isAuthor;
        if(this.userPrivate.headerUrl === ""){
          this.imagePrivateUrl = "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png";
        }else {
          this.imagePrivateUrl = "http://localhost:9090/images/"+this.userPrivate.headerUrl;
        }
      },

      //  重新设置本地缓存
      setUserInfo(){
        let name = this.returnUsername();
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
    },

    mounted() {
      this.getAuthorInfo();
      this.getAuthorWorks();
      this.setUserInfo();
      this.getUserInfo();
    }
  }
</script>

<style>
.au-div{
  width: 1300px;
  height: 600px;
  padding: 20px;
  /*border: 1px solid black;*/
}
.au-left{
  width: 320px;
  float: left;
  height: 555px;
  margin-right: 20px;
  /*border: 1px solid black;*/
}
.au-left-info{
  padding: 20px;
  /*border: 1px solid black;*/
}
.au-center{
  float: left;
  width: 320px;
  height: 555px;
  margin-right: 20px;
  border-radius: 20px;
  /*border: 1px solid black;*/
  /*border: 1px dashed #409EFF;*/
}
.au-center-top{
  text-align: center;

}
.au-center-top-span{
  font-size: 20px;
  color: #409EFF;
}
.au-center-bottom{
  padding: 10px;
}
.au-right{
  border-radius: 20px;
  float: left;
  width: 580px;
  height: 555px;
  /*border: 1px solid black;*/
  /*border: 1px dashed #409EFF;*/
}
.au-right-top{
  text-align: center;
}
.au-right-top-span{
  font-size: 20px;
  color: #409EFF;
}
.au-right-bottom{
  padding: 10px;
}
.au-right-bottom-left{
  width: 150px;
  float: left;
  margin-right: 10px;
  margin-top: 120px;
  /*border: 1px solid black;*/
}
.au-right-bottom-right{
  float: left;
  /*border: 1px solid black;*/
}




/* 书籍上传 */

.box-all {
  width: 1300px;
  height: 600px;
  padding: 10px;
  /*border: 1px solid black;*/
}
.box-left{
  margin-left: 10px;
  width: 550px;
  height: 583px;
  float: left;
  border-radius: 20px;
  border: 1px dashed #409EFF;
}
.box-left-top{
  text-align: center;
  /*border: 1px solid black;*/
}
.box-left-top-span{
  color: violet;
  font-size: 20px;
}
.box-left-bottom{
  height: 555px;
  padding: 10px;
  /*border: 1px solid black;*/
}
.box-left-bottom-upload{
  margin-top: 170px;
  margin-left: 10px;
  margin-right: 20px;
  text-align: center;
  float: left;
}
.box-left-bottom-form{
  width: 360px;
  float: left;
  /*border: 1px solid black;*/
}
.box-right{
  margin-left: 20px;
  width: 550px;
  float: left;
  border-radius: 20px;
  border: 1px dashed #409EFF;
}
.box-right-top{
  text-align: center;
  /*border: 1px solid black;*/
}
.box-right-top-span{
  color: violet;
  font-size: 20px;
}
.box-right-bottom{
  height: 555px;
  /*border: 1px solid black;*/
}
.box-right-bottom-img{
  margin-top: 180px;
  margin-left: 40px;
  margin-right: 20px;
  text-align: center;
  float: left;
}
.box-right-bottom-info{
  width: 360px;
  float: left;
  margin-top: 100px;
  padding: 20px;
  /*border: 1px solid black;*/
}




.pwd-box{
  width: 400px;
  border-radius: 20px;
  padding: 20px;
  text-align: center;
  border: 1px dashed #409EFF;
}
.pwd-top-span{
  color: violet;
  font-size: 20px;
}


.block-book-span{
  color: #409EFF;
  font-size: 13px;
}
.book-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 3px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.book-uploader .el-upload:hover {
  border-color: #409EFF;
}
.book-uploader-icon {
  font-size: 15px;
  color: #8c939d;
  width: 120px;
  height: 160px;
  line-height: 160px;
  text-align: center;
}
.book {
  width: 120px;
  height: 160px;
  display: block;
}


.card-num-input {
  position: relative; /* 设置相对定位 */
  width: 255px; /* 您可以根据需要调整这个值 */
}

.card-num-input .el-input {
  width: 100%; /* 设置输入框宽度为100%，以覆盖按钮 */
}

.card-num-input .el-button {
  position: absolute; /* 设置按钮为绝对定位 */
  top: 33px; /* 设置按钮顶部位置与输入框顶部对齐 */
  right: -105px; /* 设置按钮右侧距离为10像素 */
}

.block{
  padding: 20px;
  text-align: center;
}

.block-avatar-span{
  color: #409EFF;
  font-size: 13px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 3px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 15px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
}


.el-menu-demo{
  background-color: #fcf9f9;
}

.content{
  width: 400px;
  padding: 20px;
  margin: 20px;
}
</style>