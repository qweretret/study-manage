<template>
	<view>
		<ClassName :tearchClass="teacherClass" @getClassId="getClassId" v-if="isSelect"></ClassName>
		<Class :Person="studensInfo" :isInterLearning="isInterLearning" v-if="isStu"></Class>
		<!-- 点击添加推送 -->
		<view class="add" @click="showAdd(true)" v-show="addBtn">
			+
		</view>
		<!-- 添加区域 -->
		<view class="addContainer" v-show="isAdd">
			<title>添加</title>
			<view class="box-view">
				<text>我的班级：</text>
				<view class="box" @click="add.addClassSelect = !add.addClassSelect">
					<text v-if="add.isClass" class="title">请选择班级</text>
					<text v-else>{{add.addClass}}</text>
					<view class="selected">
						<text v-for="item in teacherClass" :key="item.id" @click="addClss(item, 'class')"
							v-show="add.addClassSelect">{{item.value}}</text>
					</view>
				</view>
			</view>

			<view class="box-view">
				<text>
					学生姓名：
				</text>
				<view class="box" @click.capture="add.addClassSelect2 = !add.addClassSelect2">
					<text v-if="add.isStu" class="title">{{add.stuTitle}}</text>
					<view class="stubox" v-for="(item,index) in add.moreStu" :key="item.id" v-else
						@click.prevent="studelete(item,$event,index)" id="canle">
						<text>{{item.sname}}</text>
						<view class="canle">
							×
						</view>
					</view>
					<view class="selected">
						<text v-for="item in add.addStu" :key="item.id" @click="addClss(item, 'stu')"
							v-show="add.addClassSelect2">{{item.value}}</text>
					</view>
				</view>
				<view class="allView" @click="AllSelect">
					全选
				</view>
			</view>

			<view class="box-view teacher">
				<text>老&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 师：</text>
				<view class="box">
					<text>{{add.teacherName}}</text>
				</view>
			</view>

			<view class="box-view">
				<text>作业类型：</text>
				<view class="box" @click="add.addClassSelect3 = !add.addClassSelect3">
					<text v-if="add.isType" class="title">请选择作业类型</text>
					<text v-else>{{add.typeHomeWork}}</text>
					<view class="selected">
						<text v-for="item in add.selectedType" :key="item.id" v-show="add.addClassSelect3"
							@click="type(item)">{{item.text}}</text>
					</view>
				</view>
			</view>

			<view class="box-view addContnent">
				<text>内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 容</text>
				<textarea v-model="add.content" placeholder="请填写内容" />
			</view>

			<view class="addBtn">
				<button @click="addInfo">添加</button>
				<button @click="addOrPull">添加并推送</button>
				<button @click="showAdd(false)">取消</button>
			</view>
		</view>
	</view>
	</view>
</template>

<script>
	import ClassName from '../../components/ClassName/ClassName.vue';
	import Class from '../../components/Class/Class.vue';
	import {
		getStudens,
		getTeacherClassList,
		getTeacherId,
		addInfo,
		getClassInStu,
		addOrPullMsg
	} from '@/api/studensPush.js'
	export default {
		data() {
			return {
				isActive: false,
				isStu: true,
				isSelect: true,
				isAdd: false,
				addBtn: true,
				//判断是否进入的改页面
				isInterLearning: true,
				PushInfo: [],
				code: {
					whethertopush: 0,
					thejobtype: 'null'
				},
				teacherClass: [],
				studensInfo: [],
				cid: {},
				sid: {},
				newPerson: [{
						id: 1,
						name: '123'
					},
					{
						id: 2,
						name: '456'
					}
				],
				add: {
					//班级选择
					addClass: '请选择班级',
					//添加学生
					addStu: [],
					//多选学生
					moreStu: [],
					//学生容器的显示
					isStu: true,
					isClass: true,
					isType: true,
					//班级下拉框
					stuTitle: '请选择学生',
					teacherName: '',
					classTitle: '请选择班级',
					content: '',
					typeHomeWork: '',
					addClassSelect: false,
					addClassSelect2: false,
					addClassSelect3: false,
					addParams: [],
					parasm: {
						tid: ''
					},
					cid: {
						cid: ''
					},
					selectedType: [{
						id: 'JAVA全栈',
						text: 'JAVA全栈'
					}, {
						id: 'JAVAEE',
						text: 'JAVAEE'
					}, {
						id: 'HTML&JS+前端',
						text: 'HTML&JS+前端'
					}, {
						id: 'MYSQL数据库',
						text: 'MYSQL数据库'
					}, {
						id: 'UI/UE设计',
						text: 'UI/UE设计'
					}, {
						id: '软件测试',
						text: '软件测试'
					}],
				}
			}
		},
		methods: {
			update() {
				this.isActive = true
			},
			async getList() {
				const result = await getTeacherClassList();
				if (result.data.code == 1000) {
					this.teacherClass = result.data.data
				}
			},
			async getStudentsInfo(cid) {
				const result = await getStudens(cid);
				this.studensInfo = result.data.data;
				// console.log(result);
			},

			async getStudensAllInfo() {
				const result = await getStudensAll(this.sid);
				// console.log(result);
			},
			getClassId(data) {
				this.getStudentsInfo(data)
			},
			//add
			async addClss(item, type) {
				if (type == 'class') {
					this.add.isClass = false
					this.add.addClass = item.value
					this.add.moreStu = []
					let cid = {
						cid: item.id
					}
					this.add.cid.cid = item.id
					const result = await getClassInStu(cid);
					console.log(result.data.data)
					const result2 = await getTeacherId(cid)
					this.add.teacherName = result2.data.data[0].value
					this.add.parasm.tid = result2.data.data[0].id
					this.add.addStu = result.data.data
				} else if (type == 'stu') {
					this.add.isStu = false
					this.add.moreStu.push({
						sid: item.id,
						sname: item.value
					})
					console.log(this.add.moreStu)
					for (let i = 0; i < this.add.moreStu.length; i++) {
						for (let j = i + 1; j < this.add.moreStu.length; j++) {
							if (this.add.moreStu[i].sid === this.add.moreStu[j].sid) {
								uni.showToast({
									//删除满足条件的元素
									icon: 'error',
									title: '该学生已存在',
									duration: 2000
								})
								this.add.moreStu.splice(j, 1);
								//因为当前索引值的元素被删除，且后面元素往前移  我们需要将下一次遍历索引从但当前索引开始，即j--
								j--;
							}
						}
					}
				}
			},
			AllSelect() {
				this.add.moreStu = []
				console.log(this.add.addClass)
				if (this.add.addClass == undefined || this.add.addClass == null || this.add.addClass == '' || this.add
					.addClass == '请选择班级') {
					uni.showToast({
						icon: 'error',
						title: '请选择班级',
						duration: 2000
					})
				} else {
					this.add.isStu = false
					this.add.addStu.forEach((item) => {
						this.add.moreStu.push({
							sid: item.id,
							sname: item.value
						})
					})
					for (let i = 0; i < this.add.moreStu.length; i++) {
						for (let j = i + 1; j < this.add.moreStu.length; j++) {
							if (this.add.moreStu[i].sid === this.add.moreStu[j].sid) {
								//删除满足条件的元素
								uni.showToast({
									icon: 'error',
									title: '该学生已存在',
									duration: 2000
								})
								this.add.moreStu.splice(j, 1);
								//因为当前索引值的元素被删除，且后面元素往前移  我们需要将下一次遍历索引从但当前索引开始，即j--
								j--;
							}
						}
					}
				}
			},
			studelete(item, e, index) {
				if (e.target.id = "canle") {
					this.add.addClassSelect2 = false
					this.add.moreStu.splice(index, 1)
				}
			},
			type(item) {
				this.add.isType = false
				this.add.typeHomeWork = item.text
			},
			async addInfo() {
				if (this.add.moreStu.length != 0) {
					let parmasSid = []
					this.add.moreStu.forEach(item => {
						parmasSid.push(item.sid)
					})
					parmasSid.forEach(item => {
						this.add.addParams.push({
							sid: item,
							tid: this.add.parasm.tid,
							content: this.add.content,
							thejobtype: this.add.typeHomeWork
						})
					})
					let {
						sid,
						tid,
						content,
						thejobtype
					} = this.add.addParams[0];
					if (sid == '' || tid == '' || this.add.content == '' || thejobtype == '') {
						uni.showToast({
							icon: 'error',
							title: '信息错误',
							duration: 1000
						})
						this.add.addParams = []
					} else {
						uni.showLoading({
							title: '加载中'
						})
						const result = await addInfo(this.add.addParams)
						uni.hideLoading()
						this.add.addParams = []
						this.isStu = true
						this.isAdd = false
						this.addBtn = true

						this.add.moreStu = []
						this.add.addClass = ''
						this.add.teacherName = ''
						this.add.typeHomeWork = ''
						this.add.content = ''
						this.getStudentsInfo(this.add.cid)
					}
				} else {
					uni.showToast({
						icon: 'error',
						title: '信息错误',
						duration: 1000
					})
				}
			},
			showAdd(type) {
				if (type) {
					this.isStu = false,
						// this.isSelect = false
						this.isAdd = true
					this.addBtn = false
				} else {
					this.isStu = true,
						// this.isSelect = true
					this.isAdd = false
					this.addBtn = true
					this.add.addClass = ''
					this.add.moreStu = []
					this.add.teacherName = ''
					this.add.typeHomeWork = ''
					this.add.content = ''
				}
			},
			async addOrPull() {
				if (this.add.moreStu.length != 0) {
					let parmasSid = []
					this.add.moreStu.forEach(item => {
						parmasSid.push(item.sid)
					})
					parmasSid.forEach(item => {
						this.add.addParams.push({
							sid: item,
							tid: this.add.parasm.tid,
							content: this.add.content,
							thejobtype: this.add.typeHomeWork
						})
					})
					let {
						sid,
						tid,
						content,
						thejobtype
					} = this.add.addParams[0];
					if (sid == '' || tid == '' || this.add.content == '' || thejobtype == '') {
						uni.showToast({
							icon: 'error',
							title: '信息错误',
							duration: 1000
						})
						this.add.addParams = []
					} else {
						uni.showLoading({
							title: '加载中'
						})
						const result = await addOrPullMsg(this.add.addParams)
						if (result.data.code == 1000) {
							uni.hideLoading()
							this.add.addParams = []
							this.isStu = true
							this.isAdd = false
							this.addBtn = true

							this.add.moreStu = []
							this.add.addClass = ''
							this.add.teacherName = ''
							this.add.typeHomeWork = ''
							this.add.content = ''
							this.getStudentsInfo(this.add.cid)
						} else if (result.data.code === 1001) {
							uni.hideLoading()
							uni.showModal({
								title: '提示',
								content: result.data.msg,
								showCancel: false,
								confirmText: '我知道了',
								success: function(res) {
									if (res.confirm) {
										console.log('用户点击确定');
									}
								}
							});
						}
					}
				} else {
					uni.showToast({
						icon: 'error',
						title: '信息错误',
						duration: 1000
					})
				}
			}
		},
		onLoad() {
			this.getList();
			// time2 = setTimeout(() => {
			// 	this.getStudensAllInfo()
			// }, 2000)
			uni.$on('getClassName', (data) => {
				this.getStudentsInfo(data.cid)
			})

			uni.$on('closeDrawer', (data) => {
				let last = uni.getStorageSync('pushClassSelectedLast')
				let cid = {
					cid: last.cid
				}
				if (last) {
					this.getStudentsInfo(cid)
				}
			})
		},
		components: {
			ClassName,
			Class,
		},
		onReachBottom() {
			console.log('123')
		}
	}
</script>

<style scoped>
	.Theme {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 724rpx;
		height: 88rpx;
		background-color: #F0F8FA;
		margin: 0 auto;
		font-size: 14px;
		box-sizing: border-box;
	}

	.active {
		border-bottom: 1px solid #FFCC00;
		color: #FFCC00;
		box-sizing: border-box;
	}

	.select /deep/uni-section {
		text-align: center;
	}

	.add {
		position: fixed;
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		bottom: 20rpx;
		left: 50%;
		transform: translateX(-50%);
		z-index: 11;
		background-color: #2775b6;
		line-height: 120rpx;
		text-align: center;
		font-size: 60rpx;
		color: #96c24e;
	}

	.addContainer {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		width: 90%;
		/* height: 900rpx; */
		background-image: linear-gradient(to right top, #d16ba5, #c777b9, #ba83ca, #aa8fd8, #9a9ae1,
				#8aa7ec, #79b3f4, #69bff8, #52cffe, #41dfff, #46eefa, #5ffbf1);
		border-radius: 10rpx;
		padding-left: 20rpx;
	}

	.addContainer .title {
		font-size: 40rpx;
	}

	.box-view {
		position: relative;
		display: flex;
		width: 100%;
		/* height: 70rpx; */
		line-height: 70rpx;
		text-align: center;
		margin-bottom: 30rpx;
		/* background-color: skyblue; */
	}

	.box-view .box {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		position: relative;
		width: 400rpx;
		/* height: 70rpx; */
		border-radius: 10rpx;
		padding-left: 20rpx;
		background-color: #fff;
	}

	.selected {
		display: flex;
		flex-direction: column;
		position: absolute;
		top: 66rpx;
		left: 0;
		width: 100%;
		background-color: #c6e6e8;
		z-index: 3;
	}

	.stubox {
		display: flex;
		justify-content: space-between;
		width: 160rpx;
		height: 50rpx;
		background-color: skyblue;
		line-height: 50rpx;
		margin-right: 20rpx;
		margin-bottom: 20rpx;
		margin-top: 10rpx;
	}

	.canle {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 50rpx;
		height: 50rpx;
		border-radius: 50%;
		background-color: pink;
	}

	.title {
		color: #648e93;
	}

	.allView {
		position: absolute;
		display: flex;
		justify-content: center;
		align-items: center;
		width: 80rpx;
		height: 80rpx;
		background-color: skyblue;
		border-radius: 10rpx;
		top: 80rpx;
	}

	.teacher {
		margin-top: 100rpx;
	}

	.addContnent {
		display: flex;
		flex-direction: column;
	}

	.addContnent textarea {
		width: 80%;
		height: 400rpx;
		background-color: #fff;
		text-align: left;
		margin: 0 auto;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
	}

	.addBtn {
		display: flex;
	}

	.addBtn button {
		margin-bottom: 30rpx;
	}
</style>
