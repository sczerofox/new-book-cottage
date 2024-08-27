<template>
  <div class="private-c">
    <!-- 操作区 -->
    <div class="private-c-header">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="基本信息" name="1">
          <div style="padding: 20px;">
            <el-descriptions :title="isAuthor ? '作家信息':'个人信息'" :column=1 size="18px">
              <el-descriptions-item label="等级">{{ privateInfo.grade }}</el-descriptions-item>
              <el-descriptions-item label="经验">{{ privateInfo.experience}}</el-descriptions-item>
              <el-descriptions-item label="性别">{{ privateInfo.sex }}</el-descriptions-item>
              <el-descriptions-item label="手机">{{ privateInfo.phone }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ privateInfo.email }}</el-descriptions-item>
              <el-descriptions-item label="备注">
                <el-tag v-if="isAuthor">没想到啊~ 发现一个野生小作者...</el-tag>
                <el-tag v-else>空空如也</el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-tab-pane>
        <el-tab-pane label="个人作品" name="2" v-if="isAuthor">
          <el-empty description="空空如也" v-if="false"></el-empty>
          <el-table v-else :data="works">
            <el-table-column prop="bookname" label="书名"></el-table-column>
            <el-table-column prop="author" label="作者"></el-table-column>
            <el-table-column prop="isStar" label="收藏数"></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="收藏栏" name="3">
          <el-empty description="暂无数据" v-if="this.allEmpty"></el-empty>
          <el-table v-else :data="stars">
            <el-table-column prop="bookname" label="书名"></el-table-column>
            <el-table-column prop="author" label="作者"></el-table-column>
            <el-table-column prop="type" label="类型"></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="历史记录" name="4">
          <el-empty description="暂无数据" v-if="this.allEmpty"></el-empty>
          <el-table v-else :data="history">
            <el-table-column prop="bookname" label="书名"></el-table-column>
            <el-table-column prop="type" label="类型"></el-table-column>
            <el-table-column prop="hot" label="热点"></el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'PrivateHeader',
    props: {
      isAuthor: Boolean,
      privateInfo: Object,
      works: Array,
      stars: Array,
      history: Array,
      allEmpty: Boolean
    },
    data(){
      return{
        headerButton: 1,
        activeName: '1',
      }
    },
    methods:{
      handleClick(tab) {
        // alert(this.booksList[0]);
      }
    }
  }
</script>

<style>

.private-c{
  padding: 5px;
  /*border: 1px solid black;*/
}
</style>