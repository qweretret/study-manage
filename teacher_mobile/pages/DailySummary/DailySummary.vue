<template>
	<view>
		<!--  -->
		<view class="dete-contianer">
			<text :class="isActive == 0 ? 'day-before active' : 'day-before ' " @click="pre" v-show="isPre">前一天</text>
			<text :class="isActive == 1 ? 'time active' : 'time ' " @click="isClaendar">{{tcur}}</text>
			<text :class="isActive == 2 ? 'day-after active' : 'day-after ' " @click="next" v-show="isNext">后一天</text>

			<view class="pushInfo" v-show="isPushInfoPerson">
				<view class="selectData" @click="isSelected = !isSelected">
					{{className == '' ? '请选择班级' : className}}
					<transition name="one">
						<view class="selectDown" v-if="isSelected">
							<text v-for="item in tClass" :key="item.cid" @click="getClassId(item.teacherClz,item.cid)">
								{{item.teacherClz}}
							</text>
						</view>
					</transition>
				</view>
				<view class="Info">
					<view>
						<strong>{{newStu.allPerson}}</strong>
						<text>人数</text>
					</view>
					<view>
						<strong>{{newStu.pass}}</strong>
						<text>已合格</text>
					</view>
					<view>
						<strong>{{newStu.yesSubmit}}</strong>
						<text>已交</text>
					</view>
					<view>
						<strong>{{newStu.noPass}}</strong>
						<text>不合格</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 学生区域 -->
		<view class="studensView">
			<view class="studnesItem" @click="info(item,item.id)" v-for="item in stuList" :key="item.id">
				<view class="stuIcon" :style="item.num == '0' ? 'background-color:red' : 'background-color:skyblue' ">
				</view>
				<view class="stuName">
					{{item.value}}
				</view>
			</view>

			<!-- 			<view class="studnesItem">
				<view class="stuIcon"></view>
				<view class="stuName">
					陈小强
				</view>
			</view> -->
		</view>

		<view class="PushInfoPageContainer" v-show="isInfoPageShowView">
			<view class="direction" v-if="isDirection">
				<view class="pre" @click="selectDirection('pre')" v-if="isLeftOrRight.pre">
					&larr;
				</view>
				<view class="stu deName">
					{{stuDetail[0].name}}
				</view>
				<view class="next" @click="selectDirection('next')" v-if="isLeftOrRight.next">
					&rarr;
				</view>
			</view>
			<view class="PushInfoMoudle" v-if="isInfoPageShow">
				<view class="UserInfo">
					<view class="UserMoudle">
						<view class="HeadPortrait"></view>
						<view class="UserInformation">
							<p>{{stuDetail[0].name}}</p>
							<text>{{stuDetail[0].crtm}}</text>
						</view>
						<view class="ScoreMoudle">
							<text v-if="isScore" class="Score">{{score}}星</text>
							<button class="ScoreBtn" v-else @click="isRate = true, isInfoPageShow = false, score = 0">评分</button>
							<button class="ScoreBtn" @click="isInfoPageShowView = false, score = 0">取消</button>
						</view>
					</view>
				</view>
				<view class="TextArea">
					<view>
						{{stuDetail[0].content}}
					</view>
				</view>
			</view>
		</view>

		<view class="ScoreView" v-if="isRate">
			<text>标题</text>
			<view class="ScoreRate" style="padding: 0 10rpx;">
				<text space="Num">{{score}}星评分</text>
				<uni-section title="基本用法" type="line" padding>
					<uni-rate v-model="rateValue" @change="onChange" size="44" />
				</uni-section>
				<textarea v-model="content" class="contnet" placeholder="请输入内容" maxlength="-1" />
			</view>
			<view class="ScoreButton">
				<text @click="isRate = false, isInfoPageShow = true, rateValue = 0, content = ''">取消</text>
				<text @click="pullEvaluation">确定</text>
			</view>
		</view>

		<view class="calendar-content" v-if="showCalendar">
			<uni-section title="插入模式" type="line"></uni-section>
			<view>
				<uni-calendar class="uni-calendar--hook" :selected="info.selected" :showMonth="false" @change="change"
					@monthSwitch="monthSwitch" />
			</view>
		</view>

		<calendar @getTime="getSelectTime" v-if="isShowCalendar"></calendar>
	</view>
</template>

<script>
	import {
		getList,
		getClass,
		getStuInfo,
		stuDetailed,
		getLastDay,
		pullScroe
	} from '@/api/dayconclusion.js'
	import calendar from '@/components/calendar/calendar.vue'
	export default {
		data() {
			return {
				//推送内容视图的显示
				isInfoPageShowView: false,
				//推送内容的模块
				isInfoPageShow: true,
				//每日推送的人数
				isPushInfoPerson: true,
				// 评分的显示
				isScore: false,
				// 评分视图的显示
				isRate: false,
				// 显示方向
				isDirection: true,
				isShowCalendar: false,
				// 显示下拉框
				isSelected: false,
				isActive: 0,
				rateValue: 0,
				selectTime: "",
				//学生提交的情况
				stuInfo: {},
				//老师管理的班级
				tClass: [],
				//班级名
				className: '',
				//学生列表
				stuList: [],
				//学生详情信息
				stuDetail: [],
				//上一天的按钮
				isPre: true,
				//下一天的按钮
				isNext: true,
				//评分
				score: 0,
				//评价的内容,
				content: '',
				//当前的学生详细信息的数组，用于左右按钮的切换
				nowStuInfo: {},
				//左右方向的显示
				isLeftOrRight: {
					pre: true,
					next: true,
				},
				//上下天的总结情况
				lastOrNext: {
					lastDay: '',
					nextDay: '',
					id: '0'
				},
				// 提价评价所需的参数
				pullContnet: {
					id: '',
					score: 0
				},
				//过滤出没提交过学生
				filterStu: [],
				//当前的时间 格式 yyyy-mm-dd
				tcur: this.showTime(0),
				//班级id
				cid: '',
				//不选选项卡的id
				defalultCid: ''
			}
		},
		methods: {
			//得到日期  主方法
			showTime(pdVal) {
				var trans_day = "";
				var eT = ""
				var cur_date = this.selectTime == undefined ? new Date() : new Date(this
					.selectTime); /* 如果日期框内为空的话就获取系统的时间为输入框初始化赋值，如果有值（用户自己选择的时间），那就获取用户自己选择的时间 */
				var cur_year = cur_date.getFullYear();

				var cur_month = cur_date.getMonth() + 1;
				var real_date = cur_date.getDate();
				cur_month = cur_month > 9 ? cur_month : ("0" + cur_month);
				real_date = real_date > 9 ? real_date : ("0" + real_date);
				eT = cur_year + "-" + cur_month + "-" + real_date;
				if (pdVal == 1) {
					trans_day = this.addByTransDate(this.tcur, 1);
				} else if (pdVal == -1) {
					trans_day = this.reduceByTransDate(this.tcur, 1);
				} else {
					trans_day = eT;
				}
				//处理
				return trans_day;
			},

			next() {
				this.isActive = 2;
				let {
					showTime
				} = this
				this.tcur = showTime(1)
				this.isShowCalendar = false,
					this.isInfoPageShow = true;
				this.isPushInfoPerson = true;
				if (this.lastOrNext.nextDay == 'null') {
					this.isNext = false
				} else {
					this.tcur = this.lastOrNext.nextDay;
					this.isPre = true
				}
				this.getClassId(this.lastOrNext.item, this.lastOrNext.id);
			},
			pre() {
				this.isActive = 0
				let {
					showTime
				} = this
				this.tcur = showTime(-1)
				this.isShowCalendar = false,
					this.isInfoPageShow = true;
				this.isPushInfoPerson = true;
				if (this.lastOrNext.lastDay == 'null') {
					this.isPre = false
				} else {
					this.tcur = this.lastOrNext.lastDay;
					this.isNext = true
				}
				this.getClassId(this.lastOrNext.item, this.lastOrNext.id);
			},
			addByTransDate(dateParameter, num) {
				var translateDate = "",
					dateString = "",
					monthString = "",
					dayString = "";
				translateDate = dateParameter.replace("-", "/").replace("-", "/");
				var newDate = new Date(translateDate);
				newDate = newDate.valueOf();
				newDate = newDate + num * 24 * 60 * 60 * 1000;
				newDate = new Date(newDate);
				//如果月份长度少于2，则前加 0 补位   
				if ((newDate.getMonth() + 1).toString().length == 1) {
					monthString = 0 + "" + (newDate.getMonth() + 1).toString();
				} else {
					monthString = (newDate.getMonth() + 1).toString();
				}
				//如果天数长度少于2，则前加 0 补位   
				if (newDate.getDate().toString().length == 1) {
					dayString = 0 + "" + newDate.getDate().toString();
				} else {
					dayString = newDate.getDate().toString();
				}
				dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString;
				return dateString;
			},
			reduceByTransDate(dateParameter, num) {
				var translateDate = "",
					dateString = "",
					monthString = "",
					dayString = "";
				translateDate = dateParameter;
				var newDate = new Date(translateDate);
				newDate = newDate.valueOf();
				// console.log(newDate);
				newDate = newDate - num * 24 * 60 * 60 * 1000;
				newDate = new Date(newDate);
				// console.log(newDate);
				//如果月份长度少于2，则前加 0 补位   
				if ((newDate.getMonth() + 1).toString().length == 1) {
					monthString = 0 + "" + (newDate.getMonth() + 1).toString();
				} else {
					monthString = (newDate.getMonth() + 1).toString();
				}
				//如果天数长度少于2，则前加 0 补位   
				if (newDate.getDate().toString().length == 1) {
					dayString = 0 + "" + newDate.getDate().toString();
				} else {
					dayString = newDate.getDate().toString();
				}
				dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString;
				return dateString;
			},
			onChange(e) {
				this.score = e.value;
			},
			getSelectTime(data) {
				let {
					year,
					month,
					date
				} = data
				this.selectTime = `${year}-${month}-${date}`;
				this.showTime()
				this.tcur = this.showTime(0)
				if (this.cid != '') {
					this.getClassId(this.className, this.cid)
				} else {
					this.getClassId(this.className, this.defalultCid)
				}
			},

			isClaendar() {
				this.isActive = 1;
				this.isShowCalendar = !this.isShowCalendar
				if (this.isShowCalendar == false) {
					this.isInfoPageShow = true;
					this.isPushInfoPerson = true;
				} else {
					this.isInfoPageShowView = false;
					this.isInfoPageShow = false;
					this.isPushInfoPerson = false;
				}
			},

			//获取老师班级
			async getClassInfo() {
				let result = await getClass()
				this.tClass = result.data.data;
			},

			//获取班级id
			async getClassId(item, id) {
				this.className = item;
				this.cid = id;
				let lastCid = {
					item,
					id
				}
				if (lastCid.id === undefined) {
					lastCid.id = this.defalultCid
					id = this.defalultCid
				}
				uni.setStorageSync('lastCid', lastCid)
				let stu = {
					cid: id,
					cur: this.tcur
				}
				const result = await getStuInfo(stu);
				const result2 = await getList(stu);
				const result3 = await getLastDay(stu)
				this.stuInfo = result2.data.data
				this.stuList = result.data.data
				this.lastOrNext = {
					lastDay: result3.data.data.per,
					nextDay: result3.data.data.nxt,
					id,
					item
				}
				if (this.lastOrNext.lastDay == "null" && this.lastOrNext.nextDay == "null") {
					this.isNext = false
					this.isPre = false
				} else if (this.lastOrNext.nextDay == "null") {
					this.isNext = false
					this.isPre = true
				} else if (this.lastOrNext.lastDay == "null") {
					this.isPre = false
					this.isNext = true
				}
				this.filterStu = []
				// console.log(this.stuList)
				// console.log(this.filterStu,'filterStu---------------')
				this.stuList.filter((item) => {
					if (item.num == "1") {
						this.filterStu.push(item)
					}
				})
			},
			// 点击学生详情
			async info(item, id) {
				let stuD = {
					sid: id,
					cur: this.tcur
				}
				// console.log(item, '145486464d6as4das56d4s5a6d4sa56d4sa5d4s5a6d4a')
				//过滤过当前选择的id
				this.filterStu.filter((item, index) => {
					if (stuD.sid === item.id) {
						this.nowStuInfo = {
							index,
							id: this.stuList[index].id
						}
						if (index == 0 && index == this.filterStu.length - 1) {
							this.isLeftOrRight.pre = false
							this.isLeftOrRight.next = false
						} else if (index == 0) {
							this.isLeftOrRight.pre = false
							this.isLeftOrRight.next = true
						} else {
							this.isLeftOrRight.next = false
							this.isLeftOrRight.pre = true
						}
					}
				})
				const result = await stuDetailed(stuD);
				this.stuDetail = result.data.data
				// console.log(this.stuDetail, '----------------------?????')
				// console.log(this.filterStu, 'filer')
				if (item.num == "0") {
					uni.showToast({
						icon: 'error',
						title: '今日未提交',
						duration: 2000
					})
				} else {
					this.isInfoPageShowView = true
					if (result.data.data[0]?.id) {
						this.pullContnet.id = result.data.data[0].id
					}
					if (this.stuDetail[0].score == 0) {
						this.isScore = false
					} else {
						this.isScore = true
						this.score = this.stuDetail[0].score
					}
				}
			},
			//选择左右按钮 显示学生详细内容
			selectDirection(sd) {
				let index = this.nowStuInfo.index;
				if (sd == 'pre') {
					if (index > 0) {
						this.isLeftOrRight.next = true;
						--index
						this.nowStuInfo = {
							index,
							id: this.filterStu[index].id
						}
						console.log(this.filterStu)
						this.info(this.nowStuInfo, this.nowStuInfo.id)
						if (index == 0) {
							this.isLeftOrRight.pre = false
						}
					}
				} else if (sd == 'next') {
					if (index < this.filterStu.length - 1) {
						this.isLeftOrRight.pre = true
							++index
						this.nowStuInfo = {
							index,
							id: this.filterStu[index].id
						}
						console.log(this.filterStu)
						this.info(this.nowStuInfo, this.nowStuInfo.id)
						if (index == this.filterStu.length - 1) this.isLeftOrRight.next = false
					}
				}

			},
			//提交评价
			async pullEvaluation() {
				this.pullContnet.score = this.score
				if (this.rateValue == 0) {
					uni.showToast({
						icon: 'error',
						title: '至少为1星',
						duration: 2000
					})
				} else {
					if (this.cid == '' || this.cid == null || this.cid == undefined) {
						this.cid = this.defalultCid
					}
					let stu = {
						cid: this.cid,
						cur: this.tcur
					}
					const result = await pullScroe(this.pullContnet)
					const result2 = await getList(stu)
					this.stuInfo = result2.data.data

					this.content = ''
					this.rateValue = 0
					this.isRate = false
					this.isInfoPageShow = true
					this.isScore = true
				}
			}
		},
		components: {
			calendar
		},
		onLoad() {
			//加载推送信息
			uni.showLoading({
				title: '加载中'
			})
			// 模拟动态赋值
			this.getClassInfo()
			setTimeout(async () => {
				this.rateValue = 0
				if (this.tClass[0].teacherClz) {
					uni.hideLoading()

					let load = {
						cid: this.tClass[0].cid,
						cur: this.tcur
					}
					let lastCid = uni.getStorageSync('lastCid')
					if (lastCid) {
						load = {
							cid: lastCid.id,
							cur: this.tcur
						}
						const result = await getStuInfo(load)
						const result2 = await getList(load)
						const result3 = await getLastDay(load)
						this.defalultCid = lastCid.id
						this.stuList = result.data.data
						this.className = lastCid.item;
						this.stuInfo = result2.data.data
						this.lastOrNext = {
							lastDay: result3.data.data.per,
							nextDay: result3.data.data.nxt,
							id: lastCid.id,
							item: this.className
						}
						if (this.lastOrNext.lastDay == "null" && this.lastOrNext.nextDay == "null") {
							this.isNext = false
							this.isPre = false
						} else if (this.lastOrNext.nextDay == "null") {
							this.isNext = false
						} else if (this.lastOrNext.lastDay == "null") {
							this.isPre = false
						}
						this.stuList.filter((item) => {
							if (item.num == "1") {
								this.filterStu.push(item)
							}
						})
					} else {
						const result = await getStuInfo(load)
						const result2 = await getList(load)
						const result3 = await getLastDay(load)

						this.stuList = result.data.data
						this.className = this.tClass[0].teacherClz;
						this.defalultCid = this.tClass[0].cid
						this.stuInfo = result2.data.data
						this.lastOrNext = {
							lastDay: result3.data.data.per,
							nextDay: result3.data.data.nxt,
							id: this.defalultCid,
							item: this.className
						}
						if (this.lastOrNext.lastDay == "null" && this.lastOrNext.nextDay == "null") {
							this.isNext = false
							this.isPre = false
						} else if (this.lastOrNext.nextDay == "null") {
							this.isNext = false
						} else if (this.lastOrNext.lastDay == "null") {
							this.isPre = false
						}
						this.stuList.filter((item) => {
							if (item.num == "1") {
								this.filterStu.push(item)
							}
						})
					}
				}
			}, 1000);
		},
		watch: {
			isInfoPageShowView: {
				handler(newData) {
					if (newData) {
						this.isPushInfoPerson = false;
					} else {
						this.isPushInfoPerson = true;
					}
				}
			},
			isRate: {
				handler(newData) {
					if (newData == false) {
						this.isDirection = true;
					} else {
						this.isDirection = false;
					}
				}
			},
		},
		computed: {
			newStu() {
				let {
					allPerson,
					noPass,
					yesSubmit,
					pass
				} = this.stuInfo;
				let newData = {
					allPerson: 0,
					noPass: 0,
					yesSubmit: 0,
					pass: 0
				}
				if (allPerson) {
					newData = {
						allPerson,
						noPass,
						yesSubmit,
						pass
					}
				} else {
					newData = {
						allPerson: 0,
						noPass: 0,
						yesSubmit: 0,
						pass: 0
					}
				}
				return newData
			}
		}
	}
</script>

<style scoped>
	.dete-contianer {
		position: relative;
		width: 724rpx;
		height: 88rpx;
		background-color: #F0F8FA;
		box-sizing: border-box;
		padding: 0 10px;
		margin: 0 auto;
	}

	.day-after,
	.day-before {
		position: absolute;
		color: #2179DD;
		font-size: 14px;
	}

	.day-after {
		right: 0;
		top: 50%;
		transform: translateY(-50%);
	}

	.day-before {
		left: 0;
		top: 50%;
		transform: translateY(-50%);
	}

	.active {
		border-bottom: 4px solid #FFCC00;
		height: 88rpx;
		line-height: 88rpx;
		color: #FFCC00;
		box-sizing: border-box;
	}

	.time {
		position: absolute;
		left: 50%;
		top: 50%;
		transform: translate(-50%, -50%);
		font-size: 14px;
	}

	.pushInfo {
		position: absolute;
		bottom: -196rpx;
		left: 0;
		width: 724rpx;
		height: 174rpx;
		background-color: #fff;
		padding-top: 20rpx;
		border-top: 1px solid #ccc;
		z-index: 1;
	}

	.Info {
		display: flex;
		justify-content: space-around;
		margin-top: 20rpx;
	}

	.Info view {
		text-align: center;
	}

	.Info view strong {
		font-weight: bold;
		font-size: 18px;
	}

	.Info view text {
		color: #BBB6B1;
		font-size: 12px;
	}

	.PushInfoPageContainer {
		position: absolute;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0.5);
	}

	.PushInfoMoudle {
		position: absolute;
		width: 686rpx;
		height: 800rpx;
		background-color: #fff;
		top: 180rpx;
		left: 50%;
		transform: translateX(-50%);
		padding: 0 20rpx;
		padding-top: 50rpx;
		overflow: scroll;
	}

	.UserInfo {
		display: flex;
		justify-content: space-between;
	}

	.UserMoudle {
		display: flex;
	}

	.HeadPortrait {
		width: 80rpx;
		height: 84rpx;
		border-radius: 50%;
		background-color: #FFCC00;
		margin-right: 20rpx;
		margin-top: 10rpx;
	}

	.UserInformation text {
		font-size: 12px;
		color: #D2D2D7;
	}

	.More view {
		width: 12rpx;
		height: 24rpx;
		border-radius: 50%;
		background-color: #ccc;
	}

	.More view:nth-child(2) {
		margin: 12rpx 0;
	}

	.TextArea view {
		width: 100%;
		font-size: 14px;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		word-break: break-all;
		-webkit-line-clamp: 3;
		margin-top: 20rpx;
	}

	.Score {
		display: block;
		margin-top: 20rpx;
		margin-left: 60rpx;
		font-size: 18px;
	}

	.ScoreMoudle {
		display: flex;
	}

	.ScoreBtn {
		margin-left: 60rpx;
		height: 93rpx;
		width: 114rpx;
		font-size: 28rpx;
		line-height: 93rpx;
	}

	.ScoreView {
		position: absolute;
		width: 520rpx;
		height: 486rpx;
		background-color: #fff;
		border-radius: 10px;
		left: 50%;
		top: 50%;
		margin-left: -260rpx;
		transform: translateY(-50%);
		padding-top: 20rpx;
	}

	.ScoreView>text {
		display: block;
		width: 100%;
		text-align: center;
		font-size: 20px;
	}

	.ScoreRate textarea {
		width: 100%;
		height: 173rpx;
		border: 1px solid #ccc;
		border-radius: 10rpx;
	}

	.ScoreRate /deep/ .uni-rate {
		margin-top: 20rpx;
		margin-left: 15rpx;
	}

	.ScoreButton {
		position: relative;
		display: flex;
		height: 80rpx;
		top: 30rpx;
	}

	.ScoreButton text {
		width: 50%;
		height: 100%;
		text-align: center;
		line-height: 80rpx;
		margin-left: 0;
		border-top: 1px solid #ccc;
	}

	.ScoreButton text:active {
		color: #2179DD;
	}

	.ScoreButton text:first-child {
		border-right: 1px solid #ccc;
	}

	.calendar-content /deep/ .example-body {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
	}

	.calendar-content /deep/ .calendar-button {
		flex: 1;
		font-weight: bold;
		font-size: 32rpx;
	}

	.studensView {
		position: absolute;
		display: flex;
		flex-wrap: wrap;
		width: 100%;
		height: 40rpx;
		top: 300rpx;
		border-top: 1px solid #ccc;
		text-align: center;
	}

	.studensView>view {
		width: 33.33%;
		height: 200rpx;
		border-right: 1px solid #ccc;
		border-bottom: 1px solid #ccc;
		box-sizing: border-box;
	}

	.studensView>view .stuIcon {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background-color: red;
		margin: 0 auto;
		margin-top: 30rpx;
		margin-bottom: 20rpx;
	}

	.direction {
		position: absolute;
		width: 100%;
		height: 60rpx;
		/* background-color: red; */
		top: 80rpx;
	}

	.pre,
	.next {
		position: absolute;
		top: -26rpx;
		font-size: 80rpx;
		color: #f1939c;
	}

	.deName {
		position: absolute;
		left: 50%;
		transform: translateX(-50%);
		color: skyblue;
	}

	.pre {
		left: 60rpx;
	}

	.next {
		right: 60rpx;
	}

	.selectData {
		position: relative;
		display: block;
		width: 100%;
		text-align: center;
	}

	.selectDown {
		position: absolute;
		display: flex;
		flex-direction: column;
		/* width: 40%; */
		background-color: #e2e1e4;
		left: 50%;
		border-radius: 10rpx;
		transform: translateX(-50%);
	}

	.selectDown text {
		margin-top: 15rpx;
	}

	.one-enter {
		height: 0;
	}

	.one-enter-active {
		transition: all 3s;
	}

	.one-enter-to {
		height: 80rpx;
	}
</style>
