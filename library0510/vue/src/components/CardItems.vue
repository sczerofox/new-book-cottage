<template>

  <el-card class="card-home" :body-style="{ padding: '0px' }">
    <img :src="card.cardUrl" class="card-img">
    <div class="card-div">
      <span class="card-span-h6">{{ card.text }}</span>
    </div>
  </el-card>

</template>

<script>
export default {
  name: "CardItems",
  props: {
    card: Object
  }
}
</script>

<style scoped>
.card-span-h6{
  font-size: 12px;
}

.card-span-h4{
  font-size: 16px;
}

.card-div{
  padding: 10px;
  margin-bottom: 10px;
  text-align: center;
}

.card-img{
  width: 240px;
  height: 240px;
}

.card-home{
  width: 240px;
  height: 320px;
  margin-top: 20px;
  margin-left: 20px;
  float: left;
}
</style>