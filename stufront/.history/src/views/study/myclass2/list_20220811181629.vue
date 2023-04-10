<template>
  <div>
    <el-row>
      <el-col :span="12" class=".el-col-frame-left">
        <el-card class="box-card userInfo head">
          <div slot="header" class="userInfo-header clearfix">
            <span>班级信息</span>
          </div>
          <el-row class="userInfo-body">
            <el-col :span="12" class="userHead">
              <image-upload v-model="this.user.head" model="base64" :size="128"></image-upload>
              <div style="font-weight: bold"></div>
            </el-col>
            <el-col :span="12" class="info">
              <div>
                <label>班级名称:</label>
                <span> {{ myClass.dbColumn_cid }}</span>
              </div>
              <div>
                <label>学生名字:</label>
                <span> {{ user.name }}</span>
              </div>
              <div>
                <label>专业:</label>
                <span> {{ user.professional }}</span>
              </div>

              <div>
                <label>手机号码:</label>
                <span> {{ user.mobile }}</span>
              </div>
              <div>
                <label>所属课程:</label>
                <span> {{ myClass.dbColumn_courseid }}</span>
              </div>
              <div>
                <label>带班老师:</label>
                <span> {{ myClass.dbColumn_tid }}</span>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="12" class="el-col-frame-right">
        <el-card class="head_card head">
          <el-row>
            <el-col :span="8" v-show="tableData" v-for="(i, index) in tableData" :key="i.id">
              <el-card class="box-card userInfo title">
                <el-avatar :size="120" fit="fill">
                  <img :src="i.head" style="width:120px;">
                </el-avatar>
                <div>{{ index + 1 }}.{{ i.name }}</div>
              </el-card>
            </el-col>
            <el-col v-show="!tableData.length" :span="24">
              <el-card class="box-card userInfo title">
                无学生
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import "echarts/lib/chart/bar";
import api from "@/api/study/myclass2/myclass2.js";
import imageUpload from "./imageUpload.vue";
export default {
  name: "myclass",
  components: {
    imageUpload,
  },
  data() {
    return {
      myClass: {},
      user: {},
      dateFlag0: 0,
      classList: [],          //班级列表
      SummarizeAll: {},
      tableData: [],        //当前班级的全部学生
    };
  },
  methods: {

    //获取学生
     getStudent() {
      api.getStudent({}, (response) => {

        this.user = response.data[0];
        this.myClass = response.data[1];
        this.currentClassCid = this.myClass.cid;
        this.getStudengtList();
        console.log(user.head);
      });
    },
    //获取学生列表
    getStudengtList() {
      let params = { cid: this.user.cid };
      api.getStudentList(params, (response) => {
        this.tableData = response.data;
      });
    }
  },

  mounted() {
    //this.getClassOne();
    this.getStudent();
  },
};
</script>

<style lang="scss" scoped="scoped">
.el-col-frame-left {
  background-color: rgb(212, 248, 248);
}

.head{
  height: 500px !important;  
}
.head_card {
  overflow: auto;

  .title {
    text-align: center;
  }
}

@import "~common/custom/css/common.scss";
@import "~common/custom/css/main.scss";
</style>

