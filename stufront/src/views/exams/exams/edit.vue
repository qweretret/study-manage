<template>
  <popup ref="popup" :loading="popupLoading" @beforeClose="close">
    <div slot="body">
      <el-card v-for="i in this.orderanswers[0]" :key="i">{{ i }}</el-card>
      
    </div>
    <div slot="footer">
      <el-button
        v-if="!disabled"
        type="primary"
        :loading="confirmLoading"
        @click="confirm()"
        >确 定</el-button
      >
      <el-button @click="close">取 消</el-button>
    </div>
  </popup>
</template>

<script>
import popup from "@/components/popup/drawerPopup.vue";
import api from "@/api/exams/exams/exams.js";
import { log } from "console";
export default {
  name: "edit",
  components: {
    popup,
  },
  data() {
    return {
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      answers: {},
      orderanswers: [],
      row: {},
    };
  },
  methods: {
    //打开弹框
    async open(parameter, title, disabled, size) {
      console.log("------------------");
      this.row = parameter.id;
      this.disabled = disabled;
      this.$refs.popup.open(title, size);
      this.$nextTick(() => {
       this.init();
       this.$set
      });
    },
    //关闭弹框
    close() {
      //初始化
      this.answers = {};
      this.orderanswers = [];
      this.row = {};
    },

    init() {
      
        //获取答案数据
        api.getOneAnswers(this.row, (response) => {
          this.answers = JSON.parse(response.data.answer);
          console.log(response, "this.answers-");
          //答案对象重排序
          for (let index = 0; index < 7; index++) {
            // this.orderanswers[0]=
            switch (this.answers.types[index]) {
              case 1:
                this.orderanswers[0] = this.answers.contents[index].qid;
                break;
              case 2:
                this.orderanswers[1] = this.answers.contents[index].qid;
                break;
              case 3:
                this.orderanswers[2] = this.answers.contents[index].qid;
                break;
              case 4:
                this.orderanswers[3] = this.answers.contents[index].qid;
                break;
              case 5:
                this.orderanswers[4] = this.answers.contents[index].qid;
                break;
              case 6:
                this.orderanswers[5] = this.answers.contents[index].qid;
                break;
              case 7:
                this.orderanswers[6] = this.answers.contents[index].qid;
                break;

              default:
                break;
            }
          }
          console.log(this.orderanswers, "orderanswers");
     
          resolve();
        });
      
    },
  },
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
