<template>
	<div class="tab-body">
		<div class="search">
			<el-form ref="form" :model="searchParams" label-width="80px">
				<el-row>
					<el-col :span="5">
						<el-form-item label="作业类型" >
							<template>
								<el-select v-model="thejobtypevalue" @change="thejobtypetype($event)">
									<el-option v-for="item in options"
											   :key="item.value"
											   :label="item.label"
											   :value="item.value">
									</el-option>
								</el-select>
							</template>
						</el-form-item>
					</el-col>
					<el-collapse-transition>
						<div v-if="isShowMoreSearch">
							<!--隐藏区域-->
						</div>
					</el-collapse-transition>
					<el-col :span="4">
						<el-form-item label="状态">
							<el-select v-model="state" @change="searchxz($event)" placeholder="请选择">
								<el-option v-for="(i) in ['未签收', '已签收', '全部']" :key="i" :label="i" :value="i">
								</el-option>
							</el-select>
						</el-form-item>

					</el-col>
					<div class="search-btn">
						<el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
						<el-button @click="clearSearchVal">清空</el-button>
					</div>
				</el-row>
			</el-form>
		</div>
        <div class="dataTable autoFlex" v-if="listData">
			<el-table class="table" :data="listData.records" stripe border height="auto" @selection-change="selectionChange" v-loading="listLoading">
				<el-table-column type="selection" width="55" fixed="left"></el-table-column>
				<el-table-column  width="120" prop="classname" label="班级"></el-table-column>
				<el-table-column prop="sname" label="学生名字"></el-table-column>
				<el-table-column prop="tname" label="老师名字"></el-table-column>
				<el-table-column prop="thejobtype" label="作业类型"></el-table-column>
				<el-table-column prop="content" label="内容">
					<template slot-scope="scope">
						<el-tooltip :content="scope.row.content" placement="top">
							<el-button type="text" @click="content(scope.row.content)">详情</el-button>
						</el-tooltip>
					</template>
				</el-table-column>
				<el-table-column width="130" prop="subtime" label="学生签收时间">
					<template slot-scope="scope">
						<el-tag v-if="scope.row.subtime == null " type="danger">暂未签收</el-tag>
						<el-tag size="mini" v-if="scope.row.subtime != null" type="success">{{ scope.row.subtime }}</el-tag>
					</template>
				</el-table-column>
				<el-table-column width="130" prop="chetime" label="老师推送时间">
					<template slot-scope="scope">
						<el-tag size="mini" v-if="scope.row.chetime != null" type="success">{{ scope.row.chetime }}</el-tag>
						<el-tag v-if="scope.row.chetime == null" type="danger">暂未推送</el-tag>
					</template>
				</el-table-column>
				<el-table-column width="130" prop="whethertopush" label="签收状态状态">
					<template slot-scope="scope">
						<el-switch v-if="scope.row.subtime != null" :disabled="true" :value="true"></el-switch>
						<el-switch v-if="scope.row.subtime == null" :disabled="false" @change="tuisong(scope.$index, scope.row)">
						</el-switch>
					</template>
				</el-table-column>
			</el-table>
			<div class="pageBar">
				<el-pagination 
				@size-change="sizeChange" 
				@current-change="currentChange" 
				:total="listData.total" 
				:page-size="page.size"
				:current-page="page.current" 
				:layout="this.$constant.page.layout" 
				:page-sizes="this.$constant.page.pageSizes"></el-pagination>
			</div>
		</div>
		<edit ref="edit"></edit>
	</div>
</template>
<script>
import edit from './edit.vue';
import api from '@/api/study/tuisongzuoye/tuisongzuoye.js';
export default {
	name: 'tuisongzuoye',
	components: {
		edit
	},
		data() {
		return {
            searchParams: { whethertopush: 0},
			isShowMoreSearch: false,
			listLoading: false,
            distinguishCancelAndClose: false,
			listData: [],
			selected: [],
			form: {},
			state: "未签收",
			page: new this.$constant.pageObj(),
			options: [
			{
                value: '全部',
                label: '全部'
            },{
                value: 'JAVA全栈',
                label: 'JAVA全栈'
            }, {
                value: 'JAVAEE',
                label: 'JAVAEE'
            }, {
                value: 'HTML&JS+前端',
                label: 'HTML&JS+前端'
            }, {
                value: 'MYSQL数据库',
                label: 'MYSQL数据库'
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
            thejobtypevalue: '全部',
		};
	},
	methods: {
		//获取数据
		list() {
			this.listLoading = true;
			let params = this.$utils.merger(this.searchParams, this.page);
			api.list( params, response => {
				this.listData = response.data;
				this.listLoading = false;
			})
		},
		thejobtypetype(ron) {
			console.log(ron)
            switch (ron) {
                case "全部":
                    this.searchParams.thejobtype = "null";
                    break;
                case "JAVA全栈":
                    this.searchParams.thejobtype = "JAVA全栈";
					break;
                case "JAVAEE":
                    this.searchParams.thejobtype = "JAVAEE";
					break;
                case "HTML&JS+前端":
                    this.searchParams.thejobtype = "HTML&JS+前端";
					break;
                case "MYSQL数据库":
                    this.searchParams.thejobtype = "MYSQL数据库";
					break;
                case "UI/UE设计":
                    this.searchParams.thejobtype = "UI/UE设计";
					break;
                case "软件测试":
                    this.searchParams.thejobtype = "软件测试";
					break;
                case "产品经理":
                    this.searchParams.thejobtype = "产品经理";
                    break;
			}
			this.list();
		},
        searchxz(ron) {
            switch (ron) {
                case "全部":
                    this.searchParams.whethertopush = 2;
                    break;
                case "未签收":
                    this.searchParams.whethertopush = 0;
                    break;
                case "已签收":
                    this.searchParams.whethertopush = 1;
                    break;
            }
            this.list();
        },
		//推送该消息
		tuisong(res, ron) {
			this.$utils.confirm.warning('提示', '确定签收吗？', () => {
                api.qianshou({
                    aid: ron.id
                }, response => {
                        this.$utils.msg.success(response.msg);
                        this.list();
                    },
                    response => {
                        this.$utils.msg.warning(response.msg);
                    }
                );
            });
		},
		//内容展示
        content: function (content) {
            this.$alert(content, "详情", {
                dangerouslyUseHTMLString: true,
            });
        },
		//去添加
		toSave() {
			this.$refs.edit.open({}, '添加');
		},
		//去编辑
		toUpdate(index, row) {
			let editParameter = {
				id: row.id
			};
			this.$refs.edit.open(editParameter, '编辑');
		},
		//去详情
		toDetail(index, row) {
			let detailParameter = {
				id: row.id
			};
			this.$refs.edit.open(detailParameter, '详情', true);
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
		//批量删除
		batchRemove() {
			if (!this.selected || this.selected.length == 0) {
				this.$utils.msg.info('请选择至少一行');
				return;
			}
			let parameter = {
				ids: this.selected
			};
			this.$utils.confirm.warning('提示', '确定删除吗？', () => {
				api.remove({
						ids: this.selected
					},
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
		//改变选择项
		selectionChange(val) {
			//清空
			this.selected = [];
			for (let item of val) {
				this.selected.push(item.id);
			}
		},
		//改变每页显示数量
		sizeChange(val) {
			this.page.size = val;
			this.list();
		},
		//改变现在的页码
		currentChange(val) {
			this.page.current = val;
			this.list();
		},

		//搜索
		search() {
			this.list();
		},
		//清空搜索框
		clearSearchVal() {
			this.searchParams = {};
		}
	},
	mounted() {
		this.list();
	}
};
</script>

<style lang="scss" scoped="scoped">
	@import '~common/custom/css/common.scss';
</style>

