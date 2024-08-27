<template>
  <div class="about" v-if="isAbout">
    <div class="header">
      <div class="logo">
        <!-- 这里可以放置Logo图片 -->
        <img :src="about.logo" alt="新书小屋 Logo">
      </div>
      <h1 class="title">{{ about.name }}</h1>
    </div>
    <div class="ab-content">
      <p class="version">{{ about.version }}</p>
      <p class="serial">{{ about.lang }}</p>
      <p class="serial">{{ about.uuid }}</p>
      <p class="serial">{{ about.companyName }}</p>
      <p class="identifiers">{{ about.text1 }}</p>
    </div>
    <div class="footer">
      <div class="icons">
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <!-- 这里可以放置四个不同颜色的图标 -->
        <div v-for="img in images">
          <img class="icon-img" :src="img" alt="">
        </div>

        <div></div>
        <div></div>
        <div></div>
        <div></div>
      </div>
      <p class="copyright">{{ about.copyright }} &nbsp; {{about.date}} &nbsp; {{ about.company }}</p>
      <p class="copyright">{{ about.text2 }}</p>
    </div>
  </div>
  <div class="about" v-else>
    <div class="header">
      <div class="logo">
        <!-- 这里可以放置Logo图片 -->
        <img src="@/assets/book.png" alt="新书小屋 Logo">
      </div>
      <h1 class="title">Source Conceal</h1>
    </div>
    <div class="ab-content">
      <p class="version">6.0.13</p>
      <p class="serial">（简体中文）</p>
      <p class="serial">NAVG-DSSQ-9S5H-****</p>
      <p class="serial">Yuanin</p>
      <p class="identifiers">BookLine</p>
    </div>
    <div class="footer">
      <div class="icons">
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <!-- 这里可以放置四个不同颜色的图标 -->
        <div>
          <img class="icon-img" src="@/assets/about/logo/Home.png" alt="">
        </div>
        <div>
          <img class="icon-img" src="@/assets/about/logo/WeChat.png" alt="">
        </div>
        <div>
          <img class="icon-img" src="@/assets/about/logo/WeiBo.png" alt="">
        </div>
        <div>
          <img class="icon-img" src="@/assets/about/logo/YouKu.png" alt="">
        </div>
        <div>
          <img class="icon-img" src="@/assets/about/logo/QQ.jpg" alt="">
        </div>

        <div></div>
        <div></div>
        <div></div>
        <div></div>
      </div>
      <p class="copyright">Copyright © 2024 - 2025 LazyGoSoft™ CyberTech Ltd.</p>
      <p class="copyright">保留所有权利</p>
    </div>
  </div>
</template>

<script>
  import axios from "axios";

  export default{
    name:'about',
    data(){
      return{
        id: 1,
        about:{},
        images:[],
        isAbout: false
      }
    },
    methods:{
      getAbout(){
        axios.get("http://localhost:9090/about",{
          params: {
            id: this.id
          }
        }).then(res=>{
          if(res.data.code === 1){
            this.isAbout = true;
            this.about = res.data.data.about;
            this.images = res.data.data.images;
          }
        });
      }
    },
    mounted() {
      this.getAbout();
    }
  }
</script>

<style>
.about {
  font-family: Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #212e3b;
  /*background-color: #f2f2f2;*/
  background-color: white;
}


.header {
  /*background-color: #f2f2f2;*/
  background-color: white;
  padding: 20px;
}

.logo img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
}

.title {
  font-size: 24px;
  margin: 10px 0;
}

.ab-content {
  /*background-color: #f2f2f2;*/
  background-color: white;
  padding: 20px;
}

.version, .serial, .identifiers {
  color: grey;
  font-size: 16px;
  margin: 5px 0;
}

.footer {
  /*background-color: #f2f2f2;*/
  background-color: white;
  padding: 20px;
}

.icons {
  display: flex;
  justify-content: space-around;
  padding: 0 190px;
}

.icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.icon-img{
  width: 50px;
  height: 50px;
}

.copyright {
  color: grey;
  margin-top: 20px;
  font-size: 14px;
}
</style>
