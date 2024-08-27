<template>
  <div class="private-h">
    <!-- 用户头像 -->
    <div class="private-h-header">
      <!-- 头像  :src="private.headerUrl" -->
      <el-avatar shape="square" :size="90" :src="private.headerUrl !== '' ? private.headerUrl:'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png'"></el-avatar>
      <span class="private-header-span">{{ private.name }}</span>
    </div>
    <!-- 标签 -->
    <div class="center-h">
      <el-icon class="el-icon-edit" v-if="publicIcon.edit"/>
      <el-icon class="el-icon-s-custom" v-if="publicIcon.admin"/>
      <span class="center-span-vip" v-if="publicIcon.vip">VIP</span>
    </div>
    <!-- 等级和V标 -->
    <div class="center-box">
      <span class="center-box-span-version">V</span><span class="center-box-span-grade">{{ private.grade }}</span>
    </div>
    <!-- 经验条 -->
    <div class="footer-h">
      <div class="footer-h-experience">
        <span class="footer-h-experience-percent">{{ (private.experience / private.max * 100).toFixed(2) }}%</span>
        <span class="footer-h-experience-current">{{ private.experience }} / {{ private.max }}</span>
        <el-progress :percentage="percentage" :color="color" :show-text="false"></el-progress>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'PrivateHeader',
    props: {
      publicIcon: Object,
      private: Object,
      percentage: {
        type: Number,
        validator: function (value) {
          return value >= 0 && value <= 100;
        }
      }
    },
    data(){
      return{
        color: 'violet'
      }
    }
  }
</script>

<style>
.footer-h-experience-percent{
  font-size: 12px;
  color: violet;
  position: relative;
  left: 5px;
}
.footer-h-experience-current{
  font-size: 12px;
  color: #a89972;
  position: relative;
  left: 64px;
}
.footer-h-experience{
  position: relative;
  top: 67px;
  height: 35px;
  /*border: 1px solid black;*/
 }
.footer-h{
  position: relative;
  top: -185px;
  left: 145px;
  width: 190px;
  height: 120px;
  padding: 10px;
  /*border: 1px solid black;*/
}
.center-box-span-grade{
  position: relative;
  left: 7px;
  color: violet;
  font-size: 12px;
}
.center-box-span-version{
  position: relative;
  left: 7px;
  color: violet;
}
.center-box{
  width: 35px;
  height: 25px;
  /*border: 1px solid black;*/
  position: relative;
  top: -75px;
  left: 100px;
}
.center-span-vip{
  /*font-size: 13px;*/
}
.center-h{
  position: relative;
  top: -76px;
  left: 100px;
  width: 35px;
  padding: 5px;
  /*border: 1px solid black;*/
}
.private-header-span{
  position: relative;
  top: 100px;
  left: -70px;
}
.private-h-header{
  display: flex;

}
.private-h{
  padding: 10px;
  height: 145px;
  /*border: 1px solid black;*/
}
</style>