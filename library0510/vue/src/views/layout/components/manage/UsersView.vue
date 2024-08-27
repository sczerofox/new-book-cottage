<template>
  <div class="users">
    <!--搜索栏-->
    <div>
      <el-input style="width: 240px; margin-top: 20px; margin-left: 20px" placeholder="请输入姓名" v-model="searchParams.username" @input="searchData"></el-input>
      <el-button style="margin-left: 20px" type="primary" @click="searchData">搜 索</el-button>
      <el-button style="margin-left: 20px" type="primary" @click="resetDate">重 置</el-button>
<!--      <el-button style="margin-left: 20px" type="success" @click="openAddDialog">新 增</el-button>-->
      <el-button style="margin-left: 20px" type="primary" @click="openRecoverDialog">恢 复</el-button>
    </div>

    <!--列表-->
    <div style="margin-top: 30px; margin-left: 20px">
      <el-table :data="tableData"
                :header-cell-style="{'text-align':'center'}"
                :cell-style="{'text-align':'center'}">

        <el-table-column label="序号" width="50px">
          <template v-slot="scope">
            {{ (pageParams.page - 1) * pageParams.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="realName" label="真实姓名"></el-table-column>
        <el-table-column prop="gender" label="性别"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="phone" label="手机"></el-table-column>
        <el-table-column prop="cardNum" label="借书证号"></el-table-column>
        <el-table-column prop="updateTime" label="更新时间"></el-table-column>

        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button type="primary" @click="openEditDialog(scope.row)">编 辑</el-button>
            <el-button type="danger" @click="deleteUser(scope.row)">删 除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--分页-->
    <div style="margin-top: 30px; margin-left: 20px">
      <el-pagination
          background
          layout="total, prev, pager, next"
          :page-size="pageParams.pageSize"
          @current-change="handleCurrentChange"
          :total="total">
      </el-pagination>
    </div>

    <!-- 弹窗 -->
    <el-dialog :title="titleName" :visible.sync="dialogFormVisible">
      <!-- 恢复窗口信息 -->
      <el-table :data="recoverTableData" v-if="isSelector == 3"
                :header-cell-style="{'text-align':'center'}"
                :cell-style="{'text-align':'center'}">
        <el-table-column label="序号" width="60px">
          <template v-slot="scope">
            {{ (pageParams.page - 1) * pageParams.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="realName" label="姓名"></el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column prop="cardNum" label="借书证号"></el-table-column>

        <el-table-column label="选择">
          <template v-slot="scope">
            <el-button
                :type="scope.$index+1 == isActive ? 'primary':''"
                :icon="scope.$index+1 == isActive ? 'el-icon-check':'el-icon-close'"
                @click="recoverParams.userId = scope.row.userId; isRecoverView = (pageParams.page - 1) * pageParams.pageSize + scope.$index + 1; isActive = (pageParams.page - 1) * pageParams.pageSize + scope.$index + 1"
                circle></el-button>
          </template>
        </el-table-column>

      </el-table>
      <!-- 分页 -->
      <el-pagination
          background
          v-if="isSelector == 3"
          layout="total, prev, pager, next"
          :page-size="pageParams.pageSize"
          @current-change="handleCurrentChange"
          :total="total">
      </el-pagination>

      <!-- 表单操作窗口信息 -->
      <el-form :model="userParams" :inline="true" label-width="100px" v-else>
        <el-form-item label="用户名">
          <el-input v-model="userParams.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="userParams.realName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="userParams.gender" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="密码">-->
<!--          <el-input v-model="userParams.password" show-password autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="邮箱">
          <el-input v-model="userParams.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="userParams.phone" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="借书证号">-->
<!--          <el-input v-model="userParams.cardNum" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
      </el-form>

      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button type="text" v-if="isSelector == 3">{{isRecoverView}}</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogSelector">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'UsersView',
  data(){
    return{
      tableData: [],
      recoverTableData: [],
      total: 0,

      pageParams: {
        page: 1,
        pageSize: 5,
      },
      searchParams: {
        page: 1,
        pageSize: 5,
        username: '',
      },
      userParams: {
        userId: '',
        username: '',
        realName: '',
        gender: '',
        password: '',
        email: '',
        phone: '',
        cardNum: '',
        isDel: '',
        updateTime: ''
      },
      recoverParams:{
        userId: ''
      },

      isSearch: null,
      isRecoverPage: null,
      dialogFormVisible: false,
      titleName: null,
      isSelector: 0,
      isActive: '',
      isRecoverView: '',
    }
  },

  methods: {

    // axios 网络请求接口
    // 从后端拿数据
    getData() {
      axios.get('http://localhost:9090/users/home').then(res => {
        this.tableData = res.data;
      })
    },
    // 重置所有数据
    resetDate(){
      this.searchParams.username ='';
      this.pageParams.page = 1;
      this.getDataByPage();
    },
    // 根据分页参数从后端拿数据
    getDataByPage() {
      this.isSearch = false;
      this.isRecoverPage = false;
      axios.get('http://localhost:9090/users/pagination', {params: this.pageParams})
          .then(res => {
            this.total = res.data.total;
            this.tableData = res.data.list;
          })
    },
    // 模糊搜索
    searchData() {
      if(this.searchParams.username !== ''){
        this.isSearch = true;
        axios.get('http://localhost:9090/users/search', {params: this.searchParams})
            .then(res => {
              this.total = res.data.total;
              this.tableData = res.data.list;
            });
      }
    },
    // 根据id恢复数据
    recoverData() {
      this.isRecoverPage = true;
      axios.get('http://localhost:9090/users/recoverData', {params: this.pageParams})
          .then(res => {
            this.total = res.data.total;
            this.recoverTableData = res.data.list;
          });
    },
    // 删除
    deleteUser(row) {
      this.$confirm('此操作将删除该图书信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.userParams = row;
        axios.post('http://localhost:9090/users/remove', this.userParams)
            .then(res => {
              if (res.data > 0) {
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });
              }
              this.getDataByPage();
            })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 新增窗口点确定
    insertUser() {
      axios.post('http://localhost:9090/users/add', this.userParams)
          .then(res => {
            if (res.data > 0) {
              this.$message({
                type: 'success',
                message: '新增成功!'
              });
            } else {
              this.$message({
                type: 'info',
                message: '新增失败!'
              });
            }
            this.dialogFormVisible = false;
            this.getDataByPage();
          })
    },
    // 编辑窗口点确定
    updateUser() {
      axios.post('http://localhost:9090/users/edit', this.userParams)
          .then(res => {
            if (res.data > 0) {
              this.$message({
                type: 'success',
                message: '编辑成功!'
              });
            } else {
              this.$message({
                type: 'info',
                message: '编辑失败!'
              });
            }
            this.dialogFormVisible = false;
            this.getDataByPage();
          })
    },
    // 选择性恢复删除信息
    recoverUser() {
      axios.post('http://localhost:9090/users/recover', this.recoverParams)
          .then(res => {
            if(res.data > 0){
              this.$message({
                type: 'success',
                message: '恢复成功!'
              });
              this.getDataByPage();
              this.dialogFormVisible = false;
            } else{
              this.$message({
                type: 'error',
                message: '恢复失败!'
              });
              this.getDataByPage();
              this.dialogFormVisible = false;
            }
          });
    },

    // 方法
    // 点击页码的方法
    handleCurrentChange(val) {
      // this.pageParams.page = val;
      // this.getDataByPage();
      if (this.isSearch === true) {
        this.searchParams.page = val;
        this.searchData();
      } else if(this.isRecoverPage === true){
        this.pageParams.page = val;
        this.recoverData();
      } else {
        this.pageParams.page = val;
        this.getDataByPage();
      }
    },


    // 属性值操作
    // 窗口选择器
    dialogSelector() {
      if (this.isSelector == 1) {
        this.insertUser();
      } else if(this.isSelector == 2){
        this.updateUser();
      } else if(this.isSelector == 3){
        if (!this.isActive == '') {
          this.recoverUser();
        } else {
          this.$message({
            type: 'info',
            message: '请在确认前选择一个需要恢复的信息或取消恢复窗口~'
          });
        }
      }
    },

    // 窗口操作
    // 打开编辑窗口
    openEditDialog(row) {
      this.isSelector = 2;
      this.titleName = '编辑用户信息';
      this.dialogFormVisible = true;
      // 浅拷贝,非常danger！
      // this.bookParams = row;
      // 深拷贝
      this.userParams = JSON.parse(JSON.stringify(row));
    },
    // 打开新增窗口
    openAddDialog() {
      this.isSelector = 1;
      this.titleName = '新增用户信息';
      this.userParams = {};
      this.dialogFormVisible = true;
    },
    // 打开恢复窗口
    openRecoverDialog(){
      this.isActive = '';
      this.isRecoverView = '';
      this.isSelector = 3;
      this.titleName = '恢复用户信息';
      this.pageParams.page = 1;
      this.recoverData();
      this.dialogFormVisible = true;
    },

  },

  // 钩子函数, 开局运行
  mounted() {
    this.getDataByPage();
  }
}
</script>

<style scoped>

</style>