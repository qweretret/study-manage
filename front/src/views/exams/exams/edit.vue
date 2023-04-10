<template>
  <popup ref="popup" :loading="popupLoading">
    <div slot="body">
      <el-form
        class="dataForm"
        ref="form"
        :disabled="disabled"
        :model="formParameter"
        :inline="true"
        label-width="80px"
        :rules="rules"
      >
        <el-row>
          <el-col :span="24">
            <el-form-item label="考试名称" prop="name">
              <el-input
                v-model="formParameter.name"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="试卷名称" prop="expid">
              <el-select
                v-model="formParameter.expid"
                :clearable="true"
                :filterable="true"
                placeholder="请选择试卷/可搜索"
              >
                <el-option
                  v-for="item in listExam"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="生效时间" prop="startTime">
              <el-date-picker
                v-model="formParameter.startTime"
                type="datetime"
                placeholder="选择生效时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="截止时间" prop="endTime">
              <el-date-picker
                v-model="formParameter.endTime"
                type="datetime"
                placeholder="选择结束时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="考试班级" prop="cid">
              <el-select
                v-model="formParameter.cid"
                :clearable="true"
                :filterable="true"
                placeholder="请选择班级/可搜索"
                @change="chgClzz(formParameter.cid)"
              >
                <el-option
                  v-for="item in listClass"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="16">
            <el-form-item label="考试学生" prop="sid">
              <el-select
                v-model="formParameter.sid"
                :clearable="true"
                :filterable="true"
                multiple
                placeholder="请选择学生/可搜索"
                @change="changeSelect"
              >
                <el-option
                  v-for="item in listStu"
                  :key="item"
                  :label="item"
                  :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="1">
            <el-checkbox
              v-model="checked"
              @change="selectAll"
              >全选</el-checkbox
            >
          </el-col>
        </el-row>
      </el-form>
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
import examApi from "@/api/exams/exampaper/exampaper.js";
import classApi from "@/api/edu/clazz/clazz.js";
import stuapi from "@/api/stus/student/student5.js";
export default {
  name: "edit",
  components: {
    popup,
  },
  data() {
    return {
      checked: false,
      listClass: [],
      listExam: [],
      listStu: [],
      disabled: false,
      popupLoading: false,
      confirmLoading: false,
      parameter: {},
      formParameter: { sid: [] },
      rules: {
        //学生id
        sid: [
          {
            validator: this.$validate.required,
            trigger: "blur",
          },
          {
            validator: this.$validate.length,
            max: 32,
            trigger: "blur",
          },
        ],
        //考试班级id
        cid: [
          {
            validator: this.$validate.required,
            trigger: "blur",
          },
          {
            validator: this.$validate.length,
            max: 32,
            trigger: "blur",
          },
        ],
        //试卷id
        expid: [
          {
            validator: this.$validate.required,
            trigger: "blur",
          },
          {
            validator: this.$validate.length,
            max: 32,
            trigger: "blur",
          },
        ],
        //生效时间
        startTime: [
          {
            validator: this.$validate.required,
            trigger: "blur",
          },
          {
            validator: this.$validate.length,
            max: 50,
            trigger: "blur",
          },
        ],
        //截止时间
        endTime: [
          {
            validator: this.$validate.required,
            trigger: "blur",
          },
          {
            validator: this.$validate.length,
            max: 50,
            trigger: "blur",
          },
        ],
        //考试名称
        name: [
          {
            validator: this.$validate.required,
            trigger: "blur",
          },
          {
            validator: this.$validate.length,
            max: 32,
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    selectAll() {
      this.formParameter.sid = [];
      if (this.checked) {
          this.listStu.map((item) => {
              console.log(item)
            this.formParameter.sid.push(item);
        });
      } else {
        this.formParameter.sid = [];
      }
    },
    changeSelect(val) {
      if (val.length === this.listStu.length) {
        this.checked = true;
      } else {
        this.checked = false;
      }
    },
    //打开弹框
    open(parameter, title, disabled, size) {
      this.listStu = [];
      this.parameter = parameter;
      this.disabled = disabled;
      this.$refs.popup.open(title, size);
      this.$nextTick(() => {
        this.init();
      });
    },
    //关闭弹框
    close() {
      this.disabled = false;
      this.$refs.popup.close();
    },
    //提交表单
    confirm() {
      console.log('进入考试表单');
      this.$utils.checkForm(this.$refs.form, () => {
        this.confirmLoading = true;
        console.log(this.parameter.id);
        if (!this.parameter.id) {
          //添加
          let parameter = this.$utils.merger(this.formParameter);
          api.save(
            parameter,
            (response) => {
              this.$utils.msg.success(response.msg);
              this.confirmLoading = false;
              this.close();
              this.$parent.list();
            },
            (response) => {
              this.$utils.msg.warning(response.msg);
              this.confirmLoading = false;
            }
          );
        } else {
          //编辑
          let parameter = this.$utils.merger(this.formParameter);
          api.update(
            parameter,
            (response) => {
              this.$utils.msg.success(response.msg);
              this.confirmLoading = false;
              this.close();
              this.$parent.list();
            },
            (response) => {
              this.$utils.msg.warning(response.msg);
              this.confirmLoading = false;
            }
          );
        }
      });
    },
    chgClzz(cid) {
      console.log(this.formParameter.sid);
      let parameter = {
        cid: cid,
      };
      stuapi.stuInClass(parameter, (response) => {
        this.listStu = response.data;
        delete this.formParameter.sid;
      });
    },
    init() {
      this.popupLoading = true;
      //清除等待
      this.confirmLoading = false;
      //清空内容
      this.formParameter = {};
      //清楚校验
      this.$refs.form.clearValidate();

      examApi.list({}, (response) => {
        this.listExam = response.data.records;
      });
      classApi.list({}, (response) => {
        console.log(response.data);
        this.listClass = response.data.records;
      });
      if (this.parameter.id) {
        //编辑查询数据
        let parameter = {
          id: this.parameter.id,
        };
        api.toUpdate(parameter, (response) => {
          this.formParameter = response.data;
          let parameter = {
            cid: this.formParameter.cid,
          };
          stuapi.stuInClass(parameter, (response) => {
            this.listStu = response.data;
          });
          this.formParameter.sid = JSON.parse(response.data.sid);
          this.popupLoading = false;
        });
      } else {
        //添加
        this.popupLoading = false;
      }
    },
  },
};
</script>

<style lang="scss" scoped="scoped">
@import "~common/custom/css/common.scss";
@import "~common/custom/css/popup/popup.scss";
</style>
