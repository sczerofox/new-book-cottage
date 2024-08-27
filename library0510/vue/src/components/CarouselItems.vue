<template>

  <!-- 主体区域 -->
  <div class="container">

    <!-- 滚动式走马灯 -->
    <div class="carousel-y-card">
      <el-carousel type="card" height="250px" :interval="4000" indicator-position="none" arrow="never" style="width: 1000px;">
        <el-carousel-item v-for="(item,index) in carousel" :key="index" style=" border-radius: 10px;">
          <img :src="item.webUrl" class="carousel-img"/>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 卡片式走马灯 -->
    <div class="carousel-card">
      <el-carousel height="250px" :interval="4000" indicator-position="none" arrow="never" style="width: 200px;">
        <el-carousel-item v-for="(item,index) in card" :key="index" style=" border-radius: 10px;">
          <img :src="`http://localhost:9090/images/`+item.webUrl" class="carousel-img"/>
        </el-carousel-item>
      </el-carousel>
    </div>

  </div>

</template>

<script>
  export default {
    name: 'CarouselItem',
    props: {
      card: Array,
      carousel: Array,
    }
  }
</script>

<style>

*{
  padding: 0;
  margin: 0;
}

.container{
  height: 300px;
  width: 100%;
  display: flex;
  padding: 20px;
}

.carousel-card{
  margin-left: 40px;
}

.carousel-img{
  height: 100%;
  width: 100%;
  border-radius: 10px;
}

.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>