<template>
	<div class="tab-body">
		<div class="search">
			<el-form ref="form" :model="searchParams" label-width="80px">
				<el-row>
					<el-col :span="4">
						<div style="display: flex;margin-top:13px; margin-left:5px">
							<el-radio v-model="stype" label="1">按班级</el-radio>
							<el-radio v-model="stype" label="2">按学生</el-radio>
						</div>
					</el-col>
					<el-col :span="5">
						<el-form-item label="选择月份">
							<el-select size="mini"  v-model="mth" @change="chgMth()" placeholder="请选择">
								<el-option v-for="(item, idx) in mthList" :key="item" :label="item" :value="idx">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="5">
						<el-form-item label="班级">
							<el-select size="mini" v-model="curclz" placeholder="请选择" @change="chgClazz()">
								<el-option v-for="(item, idx) in clzList" :key="item.id" :label="item.name" :value="idx">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="5">
						<div v-if="stype == '2'" style="display: flex;margin-top:4px; margin-left:10px">
							<el-button :type="(sid > 0) ? 'success' : 'info'" :disabled="!sid > 0" size="mini"
								@click="naviStu(-1)">《《</el-button>
							<el-select size="mini" v-model="sid" placeholder="请选择学生" @change="naviStu()">
								<el-option v-for="(item, idx) in stuList" :key="item.id" :label="item.name" :value="idx">
								</el-option>
							</el-select>
							<el-button :type="endNxtByStu() ? 'success' : 'info'" :disabled="!endNxtByStu()" size="mini"
								@click="naviStu(1)">》》
							</el-button>
						</div>
						<div v-else style="display: flex;margin-top:4px; margin-left:10px">

							<el-button :type="endNxtByClz() ? 'warning' : 'info'" :disabled="!endNxtByClz()" size="mini"
								@click="naviStu2(1)">《《
							</el-button>
							<el-select size="mini" v-model="curDateIdx" placeholder="请选择日期" @change="naviStu2()">
								<el-option v-for="(item, idx) in clzNavi.mthDays" :key="item" :label="item" :value="idx">
								</el-option>
							</el-select>
							<el-button :type="(curDateIdx > 0) ? 'warning' : 'info'" :disabled="!curDateIdx > 0" size="mini"
								@click="naviStu2(-1)">》》</el-button>

						</div>
					</el-col>
					<el-col :span="2">
					<div class="search-btn" style="display: flex;margin-top:4px">
						<el-button size="mini" @click="clearSearchVal">重置</el-button>
					</div>
				   </el-col>
				</el-row>
			</el-form>
		</div>
		<div class="operator">
			<el-button type="primary" size="mini" icon="el-icon-document-add" @click="toImport()">导入考勤表</el-button>
			{{ msg }}
		</div>

		<!-- 考勤数据查询 -->
		<div class="dataTable autoFlex" v-if="listData">
			<el-table class="table" :data="listData" stripe border height="auto" v-loading="listLoading">
				<el-table-column v-if="stype == '2'" prop="kqdate" label="日期" width="120"></el-table-column>
				<el-table-column v-else prop="name" label="姓名" width="120"></el-table-column>
				<el-table-column prop="kqdate" label="星期" width="120">
					<template slot-scope="scope">
						{{ showXinqi2(scope.row.kqdate) }}
					</template>
				</el-table-column>

				<el-table-column prop="chkin" label="上班考勤时间" width="120">
					<template slot-scope="scope">
						<el-tag :type="gettime(scope.row.chkin) ? 'success' : 'danger'">{{ scope.row.chkin == undefined ?
								'未打卡' : scope.row.chkin
						}}</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="chkout" label="下班考勤时间" width="120">
					<template slot-scope="scope">
						<el-tag :type="gettime2(scope.row.kqdate, scope.row.chkout) ? 'success' : 'danger'">
							{{ scope.row.chkout == undefined ?
									'未打卡' : scope.row.chkout
							}}</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="cfmdate" label="确认人" width="220">
					<template slot-scope="scope">
						<el-tag v-if="scope.row.cfmdate" type="info">{{ scope.row.dbColumn_mder }}</el-tag>
						<el-tag v-if="scope.row.cfmdate" type="info">{{ scope.row.cfmdate }}</el-tag>
					</template>
				</el-table-column>

				<el-table-column prop="result" label="考勤结果" width="120">
					<template slot-scope="scope">
						<el-tag
							:type="(scope.row.result == '异常' || scope.row.result == '迟到' || scope.row.result == '早退') ? 'danger' : 'success'">
							{{ scope.row.result }}</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="remarks" label="考勤备注"> </el-table-column>
				<el-table-column prop="feedback" label="异常反馈"></el-table-column>

				<el-table-column label="操作" fixed="right" width="220px">
					<template slot-scope="scope">
						<div class="dataTable-operator">

							<el-button size="mini" icon="el-icon-edit" v-show="!scope.row.cfmdate"
								v-if="!gettime3(scope.row.chkin, scope.row.chkout, scope.row.kqdate)"
								@click="toUpdate(scope.$index, scope.row)">管理
							</el-button>
						</div>
					</template>
				</el-table-column>
			</el-table>

		</div>
		<!-- 考勤导入按钮 -->
		<el-dialog title="导入考勤表" :visible.sync="dialogVisible" width="30%">
			<excel-upload drag model="fileSystem" uri="/statics/attendance" :size="upsize" v-on:change="uploadSuccess">
			</excel-upload>

			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="dialogVisible = false">确 定</el-button>
			</span>
		</el-dialog>

		<!-- 导入考勤回显数据查看，数据正确，确认导入 -->
		<el-dialog :title="title" :visible.sync="dialogUpVisible" width="90%" class="abow_dialog">
			<el-table :data="qlistdata0" class="el-dialog_body" height="300">
				<el-table-column prop="stuname" label="名字"></el-table-column>
				<el-table-column prop="clzname" label="班级"></el-table-column>
				<el-table-column prop="kqday" label="考勤日期"></el-table-column>
				<el-table-column prop="chkin" label="早上打卡时间"></el-table-column>
				<el-table-column prop="chkout" label="下午打卡时间"></el-table-column>
			</el-table>
			<div slot="footer">
				<el-button type="primary" @click="confirm()">确 定</el-button>
				<el-button @click="close">取 消</el-button>
			</div>
		</el-dialog>

		<!-- 操作考勤  -->
		<el-dialog title="操作考勤" :visible.sync="dialogCfmVisible" width="30%">
			<div>
				<el-radio v-model="isActive" label="迟到">迟到</el-radio>
				<el-radio v-model="isActive" label="早退">早退</el-radio>
				<el-radio v-model="isActive" label="请假">请假</el-radio>
				<el-radio v-model="isActive" label="病假">病假</el-radio>
				<el-radio v-model="isActive" label="正常">正常</el-radio>
				<br><br>
				<el-input type="textarea" row="3" v-model="activeTxt" placeholder="请填写原由">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogCfmVisible = false">取 消</el-button>
				<el-button type="primary" @click="mangeKaoqin()">确 定</el-button>
			</div>
		</el-dialog>

	</div>
</template>
<script>

import api from '@/api/study/attendance/attendance.js';
import excelUpload from '@/components/biz/fileUpload/excelUpload.vue';
import stuapi from "@/api/stus/student/student5.js";


export default {
	name: 'attendance',
	components: {
		excelUpload
	},
	data() {
		return {
			searchParams: {},
			isShowMoreSearch: false,
			listLoading: false,
			listData: [],
			dialogVisible: false,
			upsize: '1024',
			qlistdata0: [],//弹窗内的数组
			qlistdata: [], //确认后传入
			dialogUpVisible: false,
			disabled: false,

			title: "",
			curclz: 0,  //当前班级
			clzList: [], //班级列表
			sid: '无数据',//当前学生
			stuList: [], //学生列表
			mth: 0,
			mthList: [], //月份列表
			msg: "",

			dialogCfmVisible: false,
			isActive: false,
			activeRow: {},
			activeTxt: "",
			xinqi: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],

			stype: "2",
			curDateIdx: '无数据',
			clzNavi: {}

		};
	}, watch: {
		stype(val) {
			this.sid = "无数据"
			this.curDateIdx = "无数据"
			if (val == "1") {  //按班级
				this.naviStu2()
			} else { //按学生

				this.naviStu()
			}
		}
	},
	methods: {
		naviStu2(val) {
			//学生导航
			if (val)
				this.curDateIdx += val
			this.list2()
		}, endNxtByStu() {
			if (this.sid == "无数据") {
				return false
			} else if (this.stuList && this.stuList.length > 0 && (this.sid < this.stuList.length - 1)) {
				return true
			}
			return false
		},
		endNxtByClz() {
			if (this.curDateIdx == "无数据") {
				return false
			} else if (this.clzNavi && this.clzNavi.mthDays && this.clzNavi.mthDays.length > 0 && (this.curDateIdx < this.clzNavi.mthDays.length - 1)) {
				return true
			}
			return false
		},//获取数据
		list() {
			this.listLoading = true;
            // 如果学生列表有数据 并且 学生序号为"无数据" 时，默认查第一个学生的考勤
			if( this.stuList && this.stuList.length>0 && this.sid=="无数据"){
				this.sid = 0 
			}
			this.searchParams = { mth: this.mthList[this.mth], sid: this.stuList[this.sid] ? this.stuList[this.sid].id : null }
			
			api.list(this.searchParams, res => {
				if (this.sid == "无数据") {
					this.sid = 0
				}
				this.listData = res.data.data;
				//考勤统计
				this.calKaoqin()
				this.listLoading = false;
			})
		},
		list2() {
			if (this.curDateIdx > 0) {
				this.listLoading = true;
				this.searchParams = { cid: this.clzList[this.curclz].id, mth: this.mthList[this.mth], kqdate: this.clzNavi.mthDays[this.curDateIdx] }
			
				api.list2(this.searchParams, res => {
					this.msg = ""
					if (res.data.data) {
						this.listData = res.data.data;
						this.clzNavi = res.data.naviMth
						this.calKaoqin()
					} else {
						this.listData = [];
						this.clzNavi = { mthDays: [] }
					}

					this.listLoading = false
				})
			} else {
				this.listLoading = true;
				this.searchParams = { mth: this.mthList[this.mth], cid: this.clzList[this.curclz].id }		

				//没传日期默认本班本月最大的太累          
				api.list2( this.searchParams , res => {
					this.msg = ""

					if (res.data.data) {
						if (this.curDateIdx == "无数据") {
					     	this.curDateIdx = 0
					    }
						this.listData = res.data.data;
						this.clzNavi = res.data.naviMth
						this.calKaoqin()
					} else {
						this.listData = [];
						this.clzNavi = { mthDays: [] }
					}

					this.listLoading = false
				})
			}
		}, calKaoqin() {
			//考勤统计
			if (this.listData.length > 0) {
				let normal = 0, unnormal = 0, shijia = 0, ill = 0
				this.listData.forEach(itm => {
					if (itm.result != '异常' && itm.result != '迟到' && itm.result != '早退') {
						normal += 1
						if (itm.result == '病假') {
							ill += 1
						} else if (itm.result == '请假') {
							shijia += 1
						}
					}
				});

				unnormal = this.listData.length - normal

				if (this.stype == "2") {
					this.msg = "出勤天数:" + this.listData.length + "天，其中正常:" + normal + "天" + (ill ? "(病假=" + ill + ")、" : "") + (shijia ? "(事假=" + shijia + ")" : "") + "，异常:" + unnormal + "天"
				} else {
					this.msg = "本月出勤:" + this.clzNavi.mthDays.length + "天，[" + this.clzNavi.mthDays[this.curDateIdx] + "]出勤人数:" + this.listData.length + "人，其中正常:" + normal + "人" + (ill ? "(病假=" + ill + ")、" : "") + (shijia ? "(事假=" + shijia + ")" : "") + "，异常:" + unnormal + "人"
				}

			} else {
				this.msg = "无数据"
			}
		},//选择月份
		chgMth() {
			if (this.stype == "1") {
				this.searchParams = {};
				this.curDateIdx = "无数据";
				this.list2()
			} else {
				this.list()
			}

		},
		//选择班级
		chgClazz() {
			this.sid = '无数据'
			stuapi.toStus({ id: this.clzList[this.curclz].id }, res => {
				this.stuList = res.data
				if (this.stuList && this.stuList.length > 0) {
					//第一个学生
					this.sid = 0
                	if (this.stype == "1") {  //按班级
						this.list2()
			        } else { //按学生
						this.list()
			        }
				}
			})
		},
		showXinqi(str) {
			return new Date(str).getDay()
		}, showXinqi2(str) {
			return this.xinqi[new Date(str).getDay()]
		},
		naviStu(val) {
			//学生导航
			if (val)
				this.sid += val
			this.list()
		},
		// 判断上班时间
		gettime(ress) {
			if (ress) {
				let i = ress.replace(":", "").replace(":", "") * 1;
				return i < 93000 ? 1 : 0;
			}
			return 0
		},
		// 判断下班时间
		gettime2(day, time) {
			if (time) {
				let str = time.replace(":", "").replace(":", "") * 1;
				let week = this.showXinqi(day)
				if (week == 0) {
					week = 7
				}

				if (week < 5) {
					//console.log(day,"week<5", week,  str,180000, str > 180000)
					if (str > 180000) {  //周1-4 此处应该是21点
						return 1
					} else {
						return 0
					}
				} else {
					if (str > 173000) {  //周5-6-7 此处应该是17:30点
						return 1
					} else {
						return 0
					}
				}
			}
			return 0
		},

		gettime3(chkin, chkout, day) {
			return (this.gettime(chkin) > 0 && this.gettime2(day, chkout) > 0) ? 1 : 0
		},
		//确认导入
		confirm() {
			let params = this.qlistdata0;
			api.toComfirm2(params, response => {
				if (response.code === 1000) {
					this.qlistdata = this.qlistdata0
					this.$utils.msg.success(response.msg, 3000);
					this.list();
				} else {
					this.$utils.msg.warning("导入失败");
				}
			})
			this.dialogUpVisible = false

		},
		toImport() {
			this.dialogVisible = true;
		},
		uploadSuccess(res) {
			//成功上传后的回调
			if (res && res.length > 32) {

				let fname = res.substr(res.lastIndexOf("/") + 1)
				this.dialogVisible = false;
				//通过文件名字调用后端启动解析文件，返回数据
				api.toParse({ fname: fname }, res2 => {
					let arr = res2.data;
					if (arr && arr.length > 0) {
						this.title = "本次解析成功 " + arr.length + " 条数据，确认导入?"
						this.qlistdata0 = [];
						this.qlistdata0 = arr;
						this.dialogUpVisible = true;
						this.$utils.msg.success("上传成功", 2000);
					} else {
						this.$utils.msg.warning("导入文件为空", 2000);
					}
				}, err => {
					console.log(err)
				})
			} else {
				this.$utils.msg.warning("导入失败，请联系管理员");
			}
		},
		close() {
			this.dialogUpVisible = false;
		},
		//管理
		toUpdate(index, row) {
			this.dialogCfmVisible = true
			this.activeRow = row
		}, mangeKaoqin() {
			//提交修改考勤
			this.activeRow.remarks = this.activeTxt
			this.activeRow.state = 2
			this.activeRow.result = this.isActive

			//提交
			api.update(this.activeRow, res => {
				this.$utils.msg.success(res.msg);
				//刷新
				this.list()
				//reset
				this.activeRow = {}
				this.dialogCfmVisible = false
				this.activeTxt = ""
				this.isActive + ""

			})
		},
		//删除
		remove(index, row) {
			let parameter = {
				ids: row.id
			}
			this.$utils.confirm.warning('提示', '确定删除吗？', () => {
				api.remove(
					parameter,
					response => {
						this.$utils.msg.success(response.msg);
						this.list();
					},
					response => {
						this.$utils.msg.warning(response.msg);
					}
				);
			});
		},
		//搜索
		search() {
			//班级列表+月份
			api.list3({}, res => {
				this.clzList = res.data.clzs 
				this.mthList = res.data.mths 
				//默认老师所带的班
				stuapi.toStus({ id: this.clzList[0].id }, res => {
					this.sid = 0
					this.stuList = res.data
					this.list();
				})
			});
		},
		//清空搜索框
		clearSearchVal() {
			this.searchParams = {};
			this.stype = "2";
			this.curDateIdx = "无数据";
			this.msg = '无数据'
			this.clzNavi = {}
			this.sid = "无数据";
			this.search()
		}
	},
	mounted() {

		this.search()

	}
};
</script>

<style lang="scss" scoped="scoped">
@import '~common/custom/css/common.scss';
</style>

