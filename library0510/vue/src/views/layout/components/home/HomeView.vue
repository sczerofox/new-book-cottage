<template>
  <div class="home">

    <!-- 头部 -->
    <div class="home-header">
      <CarouselItem :carousel="carousel" :card="card"/>
    </div>

    <!-- 中部 -->
    <div class="home-center">
      <!-- 面包屑 -->
      <div class="home-bread">
        <span class="home-span">{{ typeName }} > </span>
        <el-button type="text" class="home-el-button" @click="getInfo()">换一换 <i class="el-icon-refresh-right"></i></el-button>
      </div>
      <!-- 类型按钮 -->
      <div class="home-button">
        <el-button v-for="(item,index) in classify" @click="isActiveButton(item)" :key="index+1" :class="isActive === index+1 ? 'color-button':''">{{ item.classType }}</el-button>
      </div>
      <!-- 组件遍历 -->
      <div class="home-items">
        <BookItem v-for="(book,index) in books" :key="index+1" :book="book" :index="index+1"/>
      </div>
    </div>

    <!-- 足部 -->
    <div class="home-footer">
      <!-- 标题 -->
      <div class="home-div">
        <h2 class="home-h2">NEWS OR BOOKS</h2>
      </div>
      <!-- 组件 -->
      <div class="home-box">
        <CardItems v-for="(item,index) in news" :key="index" :card="item"/>
      </div>

    </div>

  </div>
</template>

<script>
  import BookItem from '@/components/BookItems.vue';
  import CarouselItem from '@/components/CarouselItems.vue';
  import CardItems from '@/components/CardItems.vue';
  import axios from "axios";

  export default {
    name: 'HomeView',
    components: {
      BookItem,
      CarouselItem,
      CardItems
    },
    data(){
      return{
        //  卡片封面的数量
        type: 0,

        class: 1,
        isActive: '',
        typeName: '推荐榜',
        classify:[
          {id:1, classType: '推荐榜'},
          {id:2, classType: '新书榜'},
          {id:3, classType: '收藏榜'}
        ],

        //   上面图片数量
        cardSize: 5,
        //  下面新闻数量
        newsSize: 5,
        //  中间书籍数量
        number: 8,

        //  新闻式 展示栏
        news:[],  // {id: 1, newPhoto: '', text: ''},

        card:[],  //  书籍封面 竖式 滚动栏

        carousel:[], //  优雅 横式 滚动栏

        books: [],  //  书籍 展示栏
      }
    },

    methods: {
      isActiveButton(val){
        this.isActive = val.id;
        this.class = val.id;
        this.typeName = val.classType;
        this.getBookItems();
      },

      getBookItems(){

        //  获取Books数据进行基本的页面渲染
        axios.get("http://localhost:9090/home/books", {
          params: {
            type: this.class,
            number: this.number
          }
        }).then(res => {
          this.books = res.data;
        }).catch(error => {
          this.$message({
            type: 'error',
            message: '数据请求失败，请稍后再试。'
          });
          console.error("Logout error:", error);
        });
      },

      //  获取基本数据进行页面渲染
      getInfo(){
        this.getBookItems();

        //  获取Card数据进行基本的页面渲染
        axios.get("http://localhost:9090/home/booksCard", {
          params: {
            cardSize: this.cardSize,
            type: this.type
          }
        }).then(res => {
          this.card = res.data;
        }).catch(error => {
          this.$message({
            type: 'error',
            message: '数据请求失败，请稍后再试。'
          });
          console.error("Logout error:", error);
        });

        //  获取News数据进行基本的页面渲染
        axios.get("http://localhost:9090/news/newsCard", {
          params: {
            newsSize: this.newsSize
          }
        }).then(res => {
          this.carousel = res.data;
        }).catch(error => {
          this.$message({
            type: 'error',
            message: '数据请求失败，请稍后再试。'
          });
          console.error("Logout error:", error);
        });


        //  获取News数据进行基本的页面渲染
        axios.get("http://localhost:9090/news/news", {
          params: {
            newsSize: this.newsSize
          }
        }).then(res => {
          this.news = res.data;
        }).catch(error => {
          this.$message({
            type: 'error',
            message: '数据请求失败，请稍后再试。'
          });
          console.error("Logout error:", error);
        });
      },

    },
    mounted() {
      this.getInfo();
    }
  }
</script>

<style>
.home-h2{
  color: #66c6fa;
  padding-top: 20px;
  padding-left: 20px;
}

.home-footer{
  height: 400px;
  float: left;
}

.color-button{
  color: black;
  background: #b9ccf6;
}

.home-button{
  padding-left: 20px;
}

.home-el-button{
  margin-left: 20px;
  color: black;
  font-size: 20px;
}

.home-span{
  font-size: 24px;
}

.home-bread{
  padding: 20px;
}

</style>