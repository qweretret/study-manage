<template>
	<popup ref="popup" :loading="popupLoading">
		<div slot="body">
			<el-form class="dataForm" ref="form" :disabled="disabled" :model="formParameter" :inline="true" label-width="80px" :rules="rules">
				<el-row>
					<el-col :span="24">
						<el-form-item label="我的班级" prop="classname">
							<el-select v-model="formParameter.classname"
									   :clearable="true"
									   :filterable="true"
									   placeholder="请选择班级/可搜索"
									   @change="chgClzz(formParameter.classname)">
								<el-option v-for="item in listClass"
										   :key="item.id"
										   :label="item.value"
										   :value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="学生名字" prop="sname">
							<el-select v-model="formParameter.sname"
									   :clearable="true"
									   :filterable="true"
									   multiple
									   placeholder="请选择学生/可搜索"
									   @change="changeSelect">
								<el-option v-for="item in listStu"
										   :key="item.id"
										   :label="item.value"
										   :value="item">
								</el-option>
							</el-select>
						</el-form-item>
						<el-col :span="1">
							<el-checkbox v-model="checked"
										 @change="selectAll">全选</el-checkbox>
						</el-col>
					</el-col>
				</el-row>
					<el-row>
						<el-col :span="24">
							<el-form-item label="老师" prop="tname">
								<el-input v-if="formParameter.tname != null" v-model="listUser.value " disabled autocomplete="off"></el-input>
								<el-input v-if="formParameter.tname == null" v-model="listUser.value" disabled autocomplete="off"></el-input>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="24">
							<el-form-item label="作业类型" prop="thejobtype">
							<template>
							<el-select v-model="formParameter.thejobtype" placeholder="请选择作业类型">
							<el-option v-for="item in options"
										:key="item.value"
										:label="item.label"
									    :value="item.value">
							</el-option>
							</el-select>
							 </template>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="24">
							<el-form-item label="内容" prop="content"><el-input v-model="formParameter.content" autocomplete="off"></el-input></el-form-item>
						</el-col>
					</el-row>
			</el-form>
		</div>
		<div slot="footer">
			<el-button v-if="!disabled" type="primary" :loading="confirmLoading" @click="confirm()">确 定</el-button>
			<el-button @click="close">取 消</el-button>
		</div>
	</popup>
</template>

<script>
import popup from '@/components/popup/drawerPopup.vue';
import api from '@/api/study/tuisongzuoye/tuisongzuoye.js';
export default {
	name: 'edit',
	components: {
			popup
		},
	data() {
		return {
			disabled: false,
			popupLoading: false,
			confirmLoading: false,
			listClass: [],
			listStu: [],
            listStu2: [],
            listUser: [],
			checked: false,
			parameter: {},
			formParameter: { sname: [] },
            options: [{
                value: 'JAVA全栈',
                label: 'JAVA全栈'
            }, {
                value: 'JAVAEE',
                label: 'JAVAEE'
            }, {
                value: 'HTML&JS+前端',
                label: 'HTML&JS+前端'
            }, {
                value: 'UI/UE设计',
                label: 'UI/UE设计'
            }, {
                value: '软件测试',
                label: '软件测试'
            }, {
                value: '产品经理',
                label: '产品经理'
            }],
            value: '',
			rules: {
						//学生名字
						sid: [
							{
								validator: this.$validate.required,
								trigger: 'blur'
							},
							{
								validator: this.$validate.length,
								max: 32,
								trigger: 'blur'
							}
						],
						//老师名字
						tid: [
							{
								validator: this.$validate.required,
								trigger: 'blur'
							},
							{
								validator: this.$validate.length,
								max: 32,
								trigger: 'blur'
							}
						],
						//作业类型
						thejobtype: [
							{
								validator: this.$validate.required,
								trigger: 'blur'
							},
							{
								validator: this.$validate.length,
								max: 32,
								trigger: 'blur'
							}
						],
						//内容
						content: [
							{
								validator: this.$validate.required,
								trigger: 'blur'
							},
							{
								validator: this.$validate.length,
								max: 32,
								trigger: 'blur'
							}
						],
			}
		};
	},
	methods: {
			//打开弹框
		open(parameter, title, disabled, size) {
				this.parameter = parameter;
				this.disabled = disabled;
				this.$refs.popup.open(title, size);
				this.$nextTick(() => {
					this.init();
				})
		},
		//班级选择
		chgClzz(ren) {
			api.listStuid({ cid: ren }, (res) => {
				if (res.data.length > 0) {
					this.listStu = res.data;
				} else {
					this.listStu = [];
                    this.checked = false;
                }
			});
			api.intnTcr({ tid: ren }, (res) => {
				this.listUser = res.data[0];
				delete this.formParameter.sname;
			});
		},
		//学生选择
		changeSelect(val) {
			this.listStu2 = [];
			if (val.length === this.listStu.length) {
                this.checked = true;
			} else {
                this.checked = false;
			}
			this.listStu2 = val;
			console.log(this.listStu2)
		},
		//学生全选
		selectAll() {
			this.formParameter.sname = [];
			this.listStu2 = [];
            if (this.checked) {
				this.listStu.map((item) => {
					this.formParameter.sname.push(item);
                    this.listStu2.push(item);
                });
            } else {
                this.formParameter.sname = [];
			}
			console.log(this.listStu2)
        },
			//关闭弹框
			close() {
				this.disabled = false;
				this.$refs.popup.close();
			},
			//提交表单
			confirm() {
				this.$utils.checkForm(this.$refs.form, () => {
					this.confirmLoading = true;
					if (!this.parameter.id) {
						//添加
						let parameters = [];
						console.log(this.listStu2);
						for (var i = 0; i < this.listStu2.length; i++) {
							this.formParameter.sid = this.listStu2[i].id; this.formParameter.whethertopush = 0; this.formParameter.crtm = new Date;
                            this.formParameter.sname = this.listStu2[i].value;
                            this.formParameter.tid = this.listUser.id; this.formParameter.tname = this.listUser.value;
							let parameter = this.$utils.merger(this.formParameter);
							parameters.push(parameter);
                        }
						api.save(parameters,
							response => {
								this.$utils.msg.success(response.msg);
								this.confirmLoading = false;
								this.close();
								this.$parent.list();
							},
							response => {
								this.$utils.msg.warning(response.msg);
								this.confirmLoading = false;
							}
						);
					} else {
						//编辑
                        let parameterss = [];
                        console.log(this.listStu2);
                        for (var i = 0; i < this.listStu2.length; i++) {
                            this.formParameter.sid = this.listStu2[i].id; this.formParameter.whethertopush = 0; this.formParameter.crtm = new Date;
                            this.formParameter.sname = this.listStu2[i].value;
                            this.formParameter.tid = this.listUser.id; this.formParameter.tname = this.listUser.value;
                            let parametera = this.$utils.merger(this.formParameter);
                            parameterss.push(parametera);
                        }
						api.update(parametess,
							response => {
								this.$utils.msg.success(response.msg);
								this.confirmLoading = false;
								this.close();
								this.$parent.list();
							},
							response => {
								this.$utils.msg.warning(response.msg);
								this.confirmLoading = false;
							}
						);
					}
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
				api.listClass({}, (response) => {
					this.listClass = [];
					this.listClass = response.data;
					this.listStu = [];
					this.listUser = [];
					this.checked = false;
                });
				if (this.parameter.id) {
					//编辑查询数据
					let parameter = {
						id: this.parameter.id
					};
					api.toUpdate(parameter, response => {
						console.log(response.data)
						this.formParameter = response.data;
						this.listUser.value = this.formParameter.tname;
						this.popupLoading = false;
					});
				} else {
					//添加
					this.popupLoading = false;
				}
			}
		}
	};
</script>

<style lang="scss" scoped="scoped">
	@import '~common/custom/css/common.scss';
	@import '~common/custom/css/popup/popup.scss';
</style>
