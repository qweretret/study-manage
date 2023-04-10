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
						<strong>{{newStu.totalnumberofpeople}}</strong>
						<text>人数</text>
					</view>
					<view>
						<strong>{{newStu.notSubmitted}}</strong>
						<text>未交</text>
					</view>
					<view>
						<strong>{{newStu.submitted}}</strong>
						<text>已交</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 学生区域 -->
		<view class="studensView">
			<view class="studnesItem" @click="info(item)" v-for="item in stuList" :key="item.sid">
				<view class="stuIcon" v-if="item.subtime == undefined" style="background-color:red">
				</view>
				<view class="stuIcon" v-else style="background-color:skyblue">
				</view>
				<view class="stuName">
					{{item.sname}}
				</view>
			</view>
		</view>

		<view class="PushInfoPageContainer" v-show="isInfoPageShowView">
			<view class="direction" v-if="isDirection">
				<view class="pre" @click="selectDirection('pre')" v-if="isLeftOrRight.pre">
					&larr;
				</view>
				<view class="stu deName">
					{{stuDetail.sname}}
				</view>
				<view class="next" @click="selectDirection('next')" v-if="isLeftOrRight.next">
					&rarr;
				</view>
			</view>
			<view class="PushInfoMoudle" v-if="isInfoPageShow">
				<view class="stuRateContainer">
					<view class="stuRateView">
						<text>周自我评价</text>
						<view class="stuRate">
							{{newPingJiaOne}}
						</view>
					</view>

					<view class="stuRateView">
						<text>周游戏时长</text>
						<view class="stuRate">
							{{newPingJiaTwo}}
						</view>
					</view>
					<view class="stuRateView">
						<text>周进度评价</text>
						<view class="stuRate">
							{{newPingJiaThree}}
						</view>
					</view>
				</view>

				<view class="feedbackContnet">
					<text>评价内容</text>
					<view class="box">
						{{stuDetail.content}}
					</view>
				</view>

				<view class="stuBtn">
					<button @click="isInfoPageShowView = false">取消</button>
				</view>
			</view>
		</view>

		<view class="ScoreView" v-if="isRate">
			<text>标题</text>
			<view class="ScoreRate">
				<text space="Num">{{score}}星评分</text>
				<uni-section title="基本用法" type="line" padding>
					<uni-rate v-model="rateValue" @change="onChange" size="44" />
				</uni-section>
				<input type="text" class="contnet" v-model="content">
			</view>
			<view class="ScoreButton">
				<text @click="isRate = false, isInfoPageShow = true">取消</text>
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
		getClass,
	} from '@/api/dayconclusion.js'
	import {
		getCommitInfo,
		getStuInfo,
		getLastdayOrnextDay,
		getDetailedList,
		pullRead
	} from '@/api/evaluation.js'
	import calendar from '@/components/calendar/calendar.vue'
	export default {
		data() {
			return {
				//推送内容视图的显示
				isInfoPageShowView: false,
				//反馈内容的模块
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
				//查看评分 
				toView: {
					//讲授语速
					speed: 0,
					//课堂案例
					classroomCase: 0,
					//答疑解忧
					melancholy: 0,
					//听课情况
					giftOf: 0,
					//作业完成
					jobCompletion: 0,
					//讲师技能
					lecturerSkills: 0,
				},
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
				//评分
				score: 0,
				//评价的内容,
				content: '',
				//当前的学生详细信息的数组，用于左右按钮的切换
				nowStuInfo: {},
				//上一天的按钮
				isPre: true,
				//下一天的按钮
				isNext: true,
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
					content: '',
					id: '',
					score: 0
				},
				//过滤出提交过学生
				filterStu: [],
				//当前的时间 格式 yyyy-mm-dd
				tcur: this.showTime(0),
				//班级id
				cid: '',
				//不选选项卡的id
				defalultCid: '',
				//自我评价
				my: {
					weekDvaluation: '',
					weekGame: '',
					WeeklySchedule: ''
				}
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
				this.isShowCalendar = false
				this.isInfoPageShow = true
				this.isPushInfoPerson = true
				if (this.lastOrNext.theDayAfterThat == "null") {
					this.isNext = false
					// return this.tcur
				} else {
					this.tcur = this.lastOrNext.theDayAfterThat;
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
				if (this.lastOrNext.theDayBefore == "null") {
					this.isPre = false
					// return this.tcur
				} else {
					this.tcur = this.lastOrNext.theDayBefore;
					this.isNext = true
				}
				this.getClassId(this.lastOrNext.item, this.lastOrNext.id)
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
			getSelectTime(data) {
				let {
					year,
					month,
					date
				} = data
				this.selectTime = `${year}-${month}-${date}`;
				this.showTime()
				this.tcur = this.showTime(0)
				this.getClassId(this.className, this.cid)
				if (this.lastOrNext.theDayAfterThat != "null") {
					this.isNext = true
				} else if (this.lastOrNext.theDayBefore != "null") {
					this.isPre = true
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

			// 获取班级id
			async getClassId(item, id) {
				console.log(item, id)
				this.className = item;
				this.cid = id
				let lastCid = {
					item,
					id
				}
				if (lastCid.id === undefined) {
					lastCid.id = this.defalultCid
					id = this.defalultCid
				}
				uni.setStorageSync('lastEvaluation', lastCid)
				let stu = {
					cid: id,
					subtime: this.tcur
				}
				const result = await getStuInfo(stu);
				const result2 = await getCommitInfo(stu);
				const result3 = await getLastdayOrnextDay(stu)
				this.stuInfo = result2.data.data
				this.stuList = result.data.data
				this.lastOrNext = {
					theCurrentTime: result3.data.data.theCurrentTime, //当前时间
					theDayAfterThat: result3.data.data.theDayAfterThat, //后一天
					theDayBefore: result3.data.data.theDayBefore, //前一天
					id,
					item
				}
				if (this.lastOrNext.theDayAfterThat == "null" && this.lastOrNext.theDayBefore == "null") {
					this.isNext = false
					this.isPre = false
				} else if (this.lastOrNext.theDayAfterThat == "null") {
					this.isNext = false
					this.isPre = true
				} else if (this.lastOrNext.theDayBefore == "null") {
					this.isPre = false
					this.isNext = true
				}
				this.filterStu = []
				this.stuList.filter((item) => {
					if (item.subtime) {
						this.filterStu.push(item)
					}
				})
			},
			// 点击学生详情
			async info(item) {
				let stuD = {
					cid: this.cid,
					sid: item.sid,
					subtime: this.tcur
				}
				// 过滤过当前选择的id
				// console.log(stuD.cid, '---------------------')
				if (stuD.cid == '') {
					stuD.cid = this.defalultCid
				}
				// console.log(stuD.cid, '---------------------')
				this.filterStu.filter((item, index) => {
					if (stuD.sid == item.sid) {
						this.nowStuInfo = {
							index,
							id: this.stuList[index].sid
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
				const result = await getDetailedList(stuD);
				this.stuDetail = result.data.data
				console.log(this.stuDetail)
				if (this.stuDetail == undefined) {
					uni.showToast({
						icon: 'error',
						title: '今日未提交',
						duration: 1000
					})
					return
				} else {
					this.isInfoPageShowView = true
				}
			},
			// 选择左右按钮 显示学生详细内容
			selectDirection(sd) {
				let index = this.nowStuInfo.index;
				if (sd == 'pre') {
					if (index > 0) {
						this.isLeftOrRight.next = true;
						--index
						this.nowStuInfo = {
							index,
							sid: this.filterStu[index].sid
						}
						this.info(this.nowStuInfo)
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
							sid: this.filterStu[index].sid
						}
						this.info(this.nowStuInfo)
						if (index == this.filterStu.length - 1) this.isLeftOrRight.next = false
					}
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
			// uni.hideLoading()
			setTimeout(async () => {
				if (this.tClass[0].teacherClz) {
					uni.hideLoading()

					let load = {
						cid: this.tClass[0].cid,
						subtime: this.tcur
					}
					let lastEvaluation = uni.getStorageSync('lastEvaluation')
					if (lastEvaluation) {
						load = {
							cid: lastEvaluation.id,
							subtime: this.tcur
						}
						this.defalultCid = lastEvaluation.id
						const result = await getStuInfo(load)
						const result2 = await getCommitInfo(load)
						const result3 = await getLastdayOrnextDay(load)
						this.stuList = result.data.data
						this.className = lastEvaluation.item;
						this.stuInfo = result2.data.data
						this.lastOrNext = {
							theCurrentTime: result3.data.data.theCurrentTime, //当前时间
							theDayAfterThat: result3.data.data.theDayAfterThat, //后一天
							theDayBefore: result3.data.data.theDayBefore, //前一天
							id: lastEvaluation.id,
							item: lastEvaluation.item
						}
						if (this.lastOrNext.theDayAfterThat == "null" && this.lastOrNext.theDayBefore ==
							"null") {
							this.isNext = false
							this.isPre = false
						} else if (this.lastOrNext.theDayAfterThat == "null") {
							this.isNext = false
						} else if (this.lastOrNext.theDayBefore == "null") {
							this.isPre = false
						}
						this.stuList.filter((item) => {
							if (item.subtime) {
								this.filterStu.push(item)
							}
						})
					} else {
						const result = await getStuInfo(load)
						const result2 = await getCommitInfo(load)
						const result3 = await getLastdayOrnextDay(load)
						this.stuList = result.data.data
						this.className = this.tClass[0].teacherClz
						this.defalultCid = this.tClass[0].cid
						this.stuInfo = result2.data.data
						this.lastOrNext = {
							theCurrentTime: result3.data.data.theCurrentTime, //当前时间
							theDayAfterThat: result3.data.data.theDayAfterThat, //后一天
							theDayBefore: result3.data.data.theDayBefore, //前一天
							id: this.id,
							item: this.className
						}
						if (this.lastOrNext.theDayAfterThat == "null" && this.lastOrNext.theDayBefore ==
							"null") {
							this.isNext = false
							this.isPre = false
						} else if (this.lastOrNext.theDayAfterThat == "null") {
							this.isNext = false
							this.isPre = true
						} else if (this.lastOrNext.theDayBefore == "null") {
							this.isPre = false
							this.isNext = true
						}
						this.stuList.filter((item) => {
							if (item.subtime) {
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
		},
		computed: {
			// 每日提交情况 美化数据
			newStu() {
				let {
					submitted,
					totalnumberofpeople
				} = this.stuInfo;
				let newData = {
					submitted: 0,
					totalnumberofpeople: 0,
					notSubmitted: 0
				}
				if (submitted) {
					newData = {
						submitted,
						totalnumberofpeople,
						notSubmitted: (totalnumberofpeople - submitted)
					}
				} else {
					newData = {
						submitted: 0,
						totalnumberofpeople: 0,
						notSubmitted: 0
					}
				}
				return newData
			},
			newPingJiaOne() {
				if (this.stuDetail?.quest1) {
					switch (this.stuDetail.quest1) {
						case 1:
							this.my.weekDvaluation = '自暴自弃'
							break
						case 2:
							this.my.weekDvaluation = '不够努力'
							break
						case 3:
							this.my.weekDvaluation = '已尽力而效果'
							break
						case 4:
							this.my.weekDvaluation = '刚刚好'
							break
						case 5:
							this.my.weekDvaluation = '学有余力'
							break
					}
				}

				return this.my.weekDvaluation
			},
			newPingJiaTwo() {
				switch (this.stuDetail?.quest2) {
					case 1:
						this.my.weekGame = '超每天3小时'
						break
					case 2:
						this.my.weekGame = '每天约2小时'
						break
					case 3:
						this.my.weekGame = '每天约1小时'
						break
					case 4:
						this.my.weekGame = '偶尔玩'
						break
					case 5:
						this.my.weekGame = '游戏绝缘'
						break
				}
				return this.my.weekGame
			},
			newPingJiaThree() {
				switch (this.stuDetail?.quest3) {
					case 1:
						this.my.WeeklySchedule = '偏慢'
						break
					case 2:
						this.my.WeeklySchedule = '太慢'
						break
					case 3:
						this.my.WeeklySchedule = '偏快'
						break
					case 4:
						this.my.WeeklySchedule = '太快'
						break
					case 5:
						this.my.WeeklySchedule = '适中'
						break
				}
				return this.my.WeeklySchedule
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
		height: 930rpx;
		background-color: #fff;
		top: 180rpx;
		left: 50%;
		transform: translateX(-50%);
		padding-top: 50rpx;
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

	.stuRateContainer {
		box-sizing: border-box;
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		width: 100%;
		height: 480rpx;
		/* background-color: red; */
	}

	.stuRateView {
		display: flex;
		flex-direction: column;
		align-items: center;
		text-align: center;
		box-sizing: border-box;
		/* background-color: red; */
	}

	.feedbackContnet {
		text-align: center;
	}

	.feedbackContnet .box {
		width: 600rpx;
		height: 250rpx;
		box-sizing: border-box;
		margin: 0 auto;
		overflow: scroll;
		text-align: left;
		padding-left: 10rpx;
		font-size: 30rpx;
		border: 1px solid #ccc;
		border-radius: 10rpx;
	}

	.stuRate {
		width: 320rpx;
		/* margin: 0 auto; */
		margin-top: 10rpx;
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

	.ScoreRate input {
		width: 100%;
		height: 173rpx;
		background-color: red;
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

	.stuBtn {
		width: 50%;
		margin: 0 auto;
		margin-top: 25rpx;
	}
</style>
