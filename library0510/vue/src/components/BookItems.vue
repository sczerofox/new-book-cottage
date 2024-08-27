<template>
  <!-- 主体卡片 -->
  <el-card class="card-item">
    <!-- 整体 -->
    <div class="card-middle">
      <!-- 图片区 -->
      <div class="card-img">
        <!-- 图片 -->
        <img :src="`http://localhost:9090/images/`+book.webUrl" class="c-img"/>
      </div>

      <!-- 排行榜 -->
      <div class="card-index">
        <span :class="index < 4 ? 'text-green' : 'text'">{{ index }}</span>
      </div>

      <!-- 内容区 -->
      <div class="card-content">

        <!-- 信息区 -->
        <div class="card-info">
          <h4 class="card-h4">{{ book.bookname }}</h4>
          <p class="card-p"><span class="type">{{ book.type }}</span> <b> · </b> {{ book.hot }}万</p>

          <span :class="index < 4 ? 'text-green' : 'text'">&yen; <del>{{ book.price }}</del> | &yen; 0.00</span><br>
        </div>
      </div>

    </div>

  </el-card>

</template>

<script>
  export default {
    name: 'BookItem',
    props: {
      index: Number,
      book: Object,
      icon: Object
    }
  }
</script>

<style>
.card-p{
  margin-bottom: 10px;
}

.type{
  color: #7796f1;
}

.card-span{
  margin-top: 20px;
  color: #58afaa;
}

.card-h4{
  margin-bottom: 10px;
}

.card-info{
  padding: 10px;
}

.card-content{
  height: 130px;
  width: 150px;
  position: relative;
  top: -260px;
  left: 120px;
}

.text{
  color: #000000;
}

.text-green{
  color: #9ac261;
}

.card-index{
  height: 130px;
  width: 20px;
  position: relative;
  top: -130px;
  left: 100px;
  text-align: center;
}

.c-img{
  width: 100%;
  height: 100%;
  border-radius: 10px;
}

.card-img{
  height: 130px;
  width: 100px;
}

.card-middle{
  height: 130px;
  width: 270px;
}

.card-item{
  width: 310px;
  height: 170px;
  margin-top: 19px;
  margin-left: 19px;
  float: left;
  background-color: rgba(255,255,255,0.5);
  box-shadow: 0 0 10px rgba(0,0,0,0.3);
}
</style>