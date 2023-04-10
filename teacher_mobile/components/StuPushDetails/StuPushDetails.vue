<template>
	<view>
		<uni-section title="右侧滑出" type="line">
			<view class="example-body">
				<!-- <button type="primary" @click="showDrawer('showRight')"><text class="word-btn-white">显示Drawer</text> -->
				</button>
				<uni-drawer ref="showRight" mode="right" :mask-click="true" @change="change($event,'showRight')"
					v-show="isShow">
					<view class="scroll-view">
						<scroll-view class="scroll-view-box" scroll-y="true" @scrolltolower="lowerBottom">
							<!-- 							<view class="close">
										<button @click="closeDrawer('showRight')"><text
												class="word-btn-white">关闭Drawer</text></button>
									</view> -->
							<view class="info-content">
								<!-- 下拉框区域开始 -->
								<view class="select-container" v-if="isSelectView">
									<view class="input-view" @click="isSelectShowOne = !isSelectShowOne">
										<input class="selected" type="text" v-model="valueOne">
										<view class="select-down" v-if="isSelectShowOne">
											<text v-for="(item,index) in selectedOne" :key="item.id"
												@click="selectedIndexOne(item.name,item.id)">{{item.name}}</text>
										</view>
									</view>

									<view class="input-view" @click="isSelectShowTwo = !isSelectShowTwo">
										<input class="selected" type="text" v-model="valueTwo">
										<view class="select-down" v-if="isSelectShowTwo">
											<text v-for="(item,index) in selectedTwo" :key="item.id"
												@click="selectedIndexTwo(item.text,item.id)">{{item.text}}</text>
										</view>
									</view>
								</view>
								<!-- 下拉框区域结束 -->

								<!-- 学生推送详情区域 -->
								<view class="stdents-view" v-if="edit">
									<view class="studens-item" v-for="item in studensInfo" :key="item.id">
										<view class="Title">
											<view class="type">
												<view class="studnes-push-status"
													:style="item.chetime == undefined ? 'background-color:#ec2c64' : 'background-color:#2bae85' ">
												</view>
												<view class="homework-type">{{item.thejobtype}}</view>
											</view>
											<view class="time-view">
												<view class="time"> 创建:{{item.crtm}}</view>
												<view class="time">
													推送:{{item.chetime == undefined ? '未推送' : item.chetime}}
												</view>
												<view class="time">
													学生:{{item.subtime == undefined ? '学生未读取' : item.subtime}}
												</view>
											</view>
										</view>
										<view class="contnet">
											{{item.content}}
										</view>
										<view class="btn">
											<button :disabled=" item.chetime == undefined ? false : true "
												@click="editData(item)">修改</button>
											<button @click="pullMsg(item.id)"
												:disabled=" item.chetime == undefined ? false : true ">{{item.chetime == undefined ? '推送' : '已推送'}}</button>
											<button @click="Delete(item.id)">删除</button>
										</view>
									</view>
								</view>

								<view class="edit" v-else>
									<view class="edit-input-view">
										<label>我的班级：<input type="text" disabled="true"
												v-model="editEcho.classname"></label>
									</view>
									<view class="edit-input-view">
										<label>我的姓名：<input type="text" disabled="true" v-model="editEcho.sname"></label>
									</view>
									<view class="edit-input-view">
										<label>老&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;师：<input type="text"
												disabled="true" v-model="editEcho.tname"></label>
									</view>
									<view class="edit-input-view">
										<label>
											作业类型：<view class="zuoye" type="text"
												@click.stop="editSelectData = !editSelectData">
												{{editEcho.thejobtype}}
											</view>
										</label>
									</view>
									<view class="edit-selected-down" v-if="editSelectData">
										<text v-for="item in editSelectedDown" :key="item.id"
											@click="editType(item.id)">{{item.text}}</text>
									</view>
									<view class="edit-input-view textbox">
										<label class="teshu">
											内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;容
											<textarea v-model="editEcho.content" placeholder="请输入内容" />
										</label>
									</view>
									<view class="editBtn">
										<button @click="confirmEdit">确认</button>
										<button @click="edit = true">取消</button>
									</view>
								</view>

							</view>
							<view class="close">
								<button @click="closeDrawer('showRight')"><text
										class="word-btn-white">关闭Drawer</text></button>
							</view>
						</scroll-view>
					</view>
				</uni-drawer>
			</view>
		</uni-section>
	</view>
</template>


<script>
	import {
		getStudensAll,
		pullHomeWork,
		deleteHomWork,
		confirmChange,
	} from '@/api/studensPush.js'
	export default {
		data() {
			return {
				showRight: true,
				// showLeft: false,
				isShow: true,
				edit: true,
				editSelectData: false,
				// 显示下拉框
				isSelectShowOne: false,
				isSelectShowTwo: false,
				//是否显示选择器
				isSelectView: true,
				//是否通过下拉框选择的数据
				isSelectedData: false,
				// 数据回显
				editEcho: {},
				valueOne: '未推',
				valueTwo: '全部',
				studensInfo: [],
				selectedOne: [{
						id: '0',
						name: '未推',
					},
					{
						id: '1',
						name: '已推'
					},
					{
						id: '2',
						name: '全部'
					}
				],
				selectedTwo: [{
					id: 'null',
					text: '全部'
				}, {
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
				editSelectedDown: [{
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
				sid: '',
				filter: {
					id: this.sid,
					thejobtype: '2',
					whethertopush: 'null',
				},
				type: 'null',
				index: 0,
				loadingMore: {
					pages: 1, //第一页
					total: 0, //总页数
					size: 0, //每页显示的数据
					current: 1 // 当前页码
				}
			}
		},
		methods: {
			confirm() {},
			// 打开窗口
			showDrawer(e) {
				this.$refs[e].open()
			},
			// 关闭窗口
			closeDrawer(e) {
				this.$refs[e].close()
			},
			// 抽屉状态发生变化触发
			change(e, type) {
				this[type] = e,
					this.isSelectShow = false
				if (e == false) {
					uni.$emit('closeDrawer', e)
				}
			},
			async selectedIndexOne(item, index) {
				this.filter = {
					id: this.sid,
					thejobtype: this.type,
					whethertopush: index,
					size: 5
				}
				this.valueOne = item
				this.index = index
				const result = await getStudensAll(this.filter);
				if (result.data.code == 1000) {
					this.studensInfo = result.data.data.records
					this.loadingMore = {
						pages: result.data.data.pages,
						size: result.data.data.size,
						total: result.data.data.total,
						current: result.data.data.current,
					}
				}
				this.isSelectedData = true
			},
			async selectedIndexTwo(item, index) {
				this.valueTwo = item;
				this.type = index
				this.filter = {
					id: this.sid,
					thejobtype: this.type,
					whethertopush: this.index,
					size: 5,
				}
				const result = await getStudensAll(this.filter);
				if (result.data.code == 1000) {
					this.studensInfo = result.data.data.records
					this.loadingMore = {
						pages: result.data.data.pages,
						size: result.data.data.size,
						total: result.data.data.total,
						current: result.data.data.current,
					}
				}
				this.isSelectedData = true
			},
			//获取学生签收状态和内容
			async getStudensPushInfoAll() {
				this.stuId = {
					id: this.sid,
					whethertopush: 2,
					thejobtype: 'null',
					size: 5
				}
				const result = await getStudensAll(this.stuId)
				if (result.data.code == 1000) {
					this.studensInfo = result.data.data.records
					this.loadingMore = {
						pages: result.data.data.pages,
						size: result.data.data.size,
						total: result.data.data.total,
						current: result.data.data.current,
					}
				}
			},

			async pullMsg(id) {
				uni.showLoading({
					title: '加载中'
				})
				let aid = {
					aid: id
				}
				const result = await pullHomeWork(aid);
				if (result.data.code === 1001) {
					uni.showToast({
						icon: 'error',
						title: '学生未绑定'
					})
				}
				uni.hideLoading()
				this.getStudensPushInfoAll()
			},

			Delete(id) {
				return new Promise((resolve) => {
					uni.showModal({
						title: '提示',
						content: '确认要删除吗',
						success: async (res) => {
							if (res.confirm) {
								let ids = {
									ids: id
								}
								console.log(ids)
								const result = await deleteHomWork(ids)
								this.getStudensPushInfoAll()
							}
						}
					})
				})
			},
			editType(id) {
				this.editEcho.thejobtype = id
				this.editSelectData = false
			},
			editData(item) {
				this.editEcho = {
					classname: item.classname,
					sname: item.sname,
					tname: item.tname,
					thejobtype: item.thejobtype,
					content: item.content,
					id: item.id
				}
				this.edit = false
			},
			// 确认修改
			async confirmEdit() {
				const result = await confirmChange(this.editEcho)
				this.edit = true
				this.getStudensPushInfoAll()
				// console.log('123')
			},
			async lowerBottom() {
				let {
					pages,
				} = this.loadingMore
				this.loadingMore.current++
				if (this.loadingMore.current <= pages) {
					if (this.isSelectedData) {
						this.filter = {
							id: this.sid,
							thejobtype: this.type,
							whethertopush: this.index,
							current: this.loadingMore.current,
							size: 5,
						}
						const result = await getStudensAll(this.filter);
						this.studensInfo = [...this.studensInfo, ...result.data.data.records]
					} else {
						this.stuId = {
							id: this.sid,
							whethertopush: 2,
							thejobtype: 'null',
							current: this.loadingMore.current,
							size: 5
						}
						const result = await getStudensAll(this.stuId);
						this.studensInfo = [...this.studensInfo, ...result.data.data.records]
					}
				} else {
					uni.showToast({
						icon: 'error',
						title: '没有更多数据啦',
						duration: 1000
					})
				}
			}
		},
		components: {},
		created() {
			uni.$on('getInfo', (data) => {
				if (data.isInterLearning == true) {
					this.sid = data.sid
					this.showDrawer('showRight')
					this.getStudensPushInfoAll()
				} else {
					return null;
				}
			})
		},
		onNavigationBarButtonTap(e) {
			if (this.showLeft) {
				this.$refs.showLeft.close()
			} else {
				this.$refs.showLeft.open()
			}
		},
		// app端拦截返回事件 ，仅app端生效
		onBackPress() {
			if (this.showRight || this.showLeft) {
				this.$refs.showLeft.close()
				this.$refs.showRight.close()
				return true
			}
		}
	}
</script>



<style lang="scss" scoped>
	.example-body {
		padding: 10px;
	}

	.scroll-view {
		// /* #ifndef APP-NVUE */
		width: 100%;
		height: 100%;
		// /* #endif */
		flex: 1
	}

	// 处理抽屉内容滚动
	.scroll-view-box {
		flex: 1;
		position: absolute;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
	}

	.info-text {
		font-size: 14px;
		color: #666;
	}

	.close {
		padding: 10px;
	}

	.select-container {
		display: flex;
		justify-content: space-around;
		width: 100%;
		height: 60rpx;
		margin-top: 40rpx;
	}

	.select-container .input-view {
		width: 40%;
		height: 100%;
		background-color: #ede3e7;
	}

	.select-container .input-view .selected {
		height: 100%;
		padding-left: 20rpx;
		line-height: 60rpx;
		caret-color: transparent;
	}

	.select-down {
		position: relative;
		display: flex;
		flex-direction: column;
		width: 100%;
		background-color: #e2e1e4;
		padding-top: 20rpx;
		box-shadow: 0 2px 2px 0;
		z-index: 1;
		margin-top: 2rpx;
	}

	.select-down text {
		width: 100%;
		margin-bottom: 20rpx;
		text-align: center;
	}

	.stdents-view {
		padding: 0 20rpx;
		margin-top: 80rpx;
	}

	.studens-item {
		width: 100%;
		height: 450rpx;
		background-color: #ccccd6;
		border-radius: 15rpx;
		box-sizing: border-box;
		margin-bottom: 90rpx;
		padding-top: 10rpx;
	}

	.Title {
		display: flex;
		justify-content: space-between;
		width: 100%;
		box-sizing: border-box;
		padding: 0 10rpx;
	}

	.studnes-push-status {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background-color: #fed71a;
	}

	.type {
		display: flex;
		flex-direction: column;
	}

	.homework-type {
		margin-top: 10rpx;
	}

	.time-view .time {
		margin-bottom: 5rpx;
	}

	.contnet {
		display: -webkit-box;
		width: 100%;
		height: 205rpx;
		box-sizing: border-box;
		background-color: #93b5cf;
		// white-space: normal; // 规定段落中的文本不进行换行
		word-break: break-all; // 允许单词中换行，在容器的最右边进行断开不会浪费控件
		word-wrap: break-word; // 防止长单词溢出，单词内部短句
		overflow: hidden;
		text-overflow: ellipsis;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 5;
		border-radius: 10px;
		padding: 0 10rpx;
		margin-top: 10rpx;
	}

	.btn {
		display: flex;
		width: 100%;
		height: 74rpx;
		padding-top: 8rpx;
	}

	.btn button {
		width: 33.33;
		line-height: 74rpx;
	}

	.edit {
		position: relative;
		width: 100%;
	}

	.edit-input-view {
		font-size: 40rpx;
		margin-bottom: 40rpx;
		box-sizing: border-box;
		transform: translateY(50%);
	}

	.edit-input-view label {
		position: relative;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.teshu {
		display: flex;
		flex-direction: column;
	}

	.edit-input-view input,
	.edit-input-view .zuoye {
		width: 50%;
		height: 80rpx;
		padding-left: 10rpx;
		border-radius: 15rpx;
		border: 2px solid #ccccd6;
		font-size: 32rpx;
		caret-color: transparent;
	}

	.edit-input-view .zuoye {
		line-height: 80rpx;
	}

	.textbox {
		margin-top: -115rpx;
		margin-bottom: 30rpx;
	}

	.teshu textarea {
		width: 70%;
		border: 2px solid #ccccd6;
		font-size: 32rpx;
		border-radius: 10rpx;
		// overflow: hidden;
		// text-overflow: ellipsis;
		// -webkit-box-orient: vertical;
		// -webkit-line-clamp: 5;
	}

	.editBtn {
		width: 100%;
		position: absolute;
		display: flex;
		left: 50%;
		transform: translateX(-50%);
		margin-top: 190rpx;
	}

	.editBtn button {
		// margin-right: 45rpx;
	}

	.edit-selected-down {
		position: absolute;
		display: flex;
		flex-direction: column;
		left: 275rpx;
		top: 518rpx;
		width: 50%;
		height: 380rpx;
		font-size: 30rpx;
		text-align: center;
		z-index: 1;
		border-radius: 10rpx;
		background-color: #fffef9;
	}

	.edit-selected-down text {
		margin: 10rpx;
	}

	.add {
		position: fixed;
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		right: 0;
		z-index: 11;
		background-color: #737c7b;
		line-height: 120rpx;
		text-align: center;
		font-size: 60rpx;
	}
</style>
