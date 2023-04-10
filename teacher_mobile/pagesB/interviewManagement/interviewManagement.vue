<template>
	<view>
		<view class="selectContainer">
			<view class="selectDown" @click="isSelect = !isSelect">
				{{classId}}
			</view>

			<view class="selectedCard" v-if="isSelect">
				<text v-for="item in className" :key="item.cid" @click="getStuInfo(item)">{{item.teacherClz}}</text>
			</view>
		</view>

		<!-- 学生区域 -->
		<view class="stu-container">
			<!--  -->
			<view class="stu-view" v-for="(item,index) in stuInfo" :key="item.sid">
				<view class="stu-avatar" @click="stuDetailed(item.sid)">
					<img :src='imgUrl[index]' alt="">
				</view>
				<text>{{item.sname}}</text>
				<text>{{item.let}}</text>
			</view>
		</view>


		<!-- 面试详情区域 -->
		<view class="mianshi-view" v-show="enterView" @click="canleImg($event)" id="mianshi">

			<!-- 面试列表 -->
			<view class="mianshi-list" v-if="list" style="text-align: center;">
				<view class="mian-list-view">
					<view class="list-item" @click="detaile(item)" v-for="(item,index) in stuDetaile" :key="item.id">
						<view class="item-status" v-if="item.msdp == undefined">
							<text>{{index + 1}}</text>
						</view>
						<view class="item-status" v-if="item.msdp != undefined" style="background-color: #66c18c;">
							<text>{{index + 1}}</text>
						</view>
						<text>{{stuDetaile[index].crtm.substring(0,10)}}</text>
					</view>
				</view>
				<view class="list-btn" style="width: 50%; margin: 0 auto">
					<button @click="enterView = false">取消</button>
				</view>
			</view>

			<view class="interview-info" v-else>
				<view class="stu-info">
					<text>学生姓名：</text>
					<view class="Information-box">
						{{detaileContnet.dbColumn_stuid}}
					</view>
				</view>

				<view class="stu-info">
					<text>企业名称：</text>
					<view class="Information-box">
						{{detaileContnet.cmpname}}
					</view>
				</view>

				<view class="stu-info">
					<text>企业岗位：</text>
					<view class="Information-box">
						{{detaileContnet.dbColumn_postid}}
					</view>
				</view>

				<view class="stu-info">
					<text>面试时间：</text>
					<view class="Information-box">
						{{detaileContnet.mstm == undefined ? '暂无时间' : detaileContnet.mstm}}
					</view>
				</view>
				<view class="stu-info">
					<text>面试准备：</text>
					<view class="Information-box prepare">
						<uni-rate v-model="rateValue1" @change="onChange1" readonly="true" />
					</view>
				</view>

				<view class="stu-info">
					<text>面试打分：</text>
					<view class="Information-box prepare">
						<uni-rate v-model="rateValue2" @change="onChange2" readonly="true" />
					</view>
				</view>

				<view class="stu-info">
					<text>面试结果：</text>
					<view class="Information-box">
						{{detaileContnet.msresult == undefined ? '暂无结果' : detaileContnet.msresult}}
					</view>
				</view>

				<view class="stu-info">
					<text>面试时长：</text>
					<view class="Information-box">
						{{detaileContnet.mslast == undefined ? '暂无时间' : detaileContnet.mslast}}
					</view>
				</view>

				<view class="stu-info conclusion">
					<text>面试总结：</text>
					<view class="Information-box">
						{{detaileContnet.mszj == undefined ? '暂无总结' : detaileContnet.mszj}}
					</view>
				</view>

				<view class="stu-info problem" v-if="boxImg.length != 0">
					<text>面试难题：</text>
					<!-- 					<view class="Information-box" style="display: flex;" v-if="isImg">
						<text>暂无图片</text>
					</view> -->
					<view class="Information-box2">
						<view class="img-view" v-for="(item,index) in boxImg" :key="index"
							@click="amplification(item,index)">
							<img :src="item" alt="" id="box">
						</view>
						<view class="big-box" v-show="isBigBox">
							<view class="uni-padding-wrap uni-common-mt">
								<movable-area scale-area>
									<movable-view direction="all" @scale="onScale" scale="true" scale-min="1"
										scale-max="4" :scale-value="scale">
										<img :src="qhImg" alt="" id="imgBox">
										<view class="prev" @click="direction('pre')" v-show="isDirection.pre" id="box">
											&lsaquo;
										</view>
										<view class="next" @click="direction('next')" v-show="isDirection.next"
											id="box">&rsaquo;
										</view>
									</movable-view>
								</movable-area>
							</view>
						</view>
					</view>
				</view>

				<view class="stu-info review" v-if="isReview">
					<text>面试点评：</text>
					<textarea v-model="reviewContent" placeholder="暂无内容" maxlength="-1" />
				</view>

				<view class="stu-info isReview" v-else>
					<text>面试点评：</text>
					<view>sdsadasd</view>
				</view>

				<view class="stu-info btn">
					<button @click="review"
						v-if="detaileContnet.msdp == undefined && detaileContnet.mstm != undefined && detaileContnet.msscore != undefined && detaileContnet.mslast != undefined">点评</button>
					<view v-if="mianshiTime == '面试已过' && detaileContnet.msscore == undefined && detaileContnet.mslast == undefined"
						style="margin-left: 20rpx;margin-top: 10rpx;">面试已过期</view>
					<view
						v-if="mianshiTime == '未到面试时间' && detaileContnet.msscore == undefined && detaileContnet.mslast == undefined"
						style=" margin-left: 20rpx;margin-top: 10rpx;">未到面试时间</view>
					<button v-if="detaileContnet.msdp != undefined" disabled="true">已点评</button>
					<button @click="list = true, reviewContent = ''">取消</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		getClass,
		getStuList,
		getStuDetailed,
		pullMsg
	} from '@/api/interview.js'
	export default {
		data() {
			return {
				active: 0,
				//选项卡
				isShowOne: true,
				isShowTwo: false,
				isShowThree: false,
				//下拉框
				isSelect: false,
				//评分
				rateValue1: 0,
				rateValue2: 0,
				//用于图片切换的index
				index: 0,
				//是否已评价
				isReview: true,
				//进入点评界面
				enterView: false,
				// 进行面试次数与面试详情的切换
				list: true,
				//面试难题的切换
				isImg: false,
				//大图片图层的显示
				isBigBox: false,
				// 班级
				className: [],
				classId: '',
				//学生列表
				stuInfo: [],
				//学生详细信息
				stuDetaile: [],
				//详情内容
				detaileContnet: {},
				//放大图片
				boxImg: [],
				//切换图片
				qhImg: [],
				imgUrl: [],
				//用于方向按钮的显示
				isDirection: {
					pre: true,
					next: true
				},
				//点评内容
				reviewContent: '',
			}
		},
		methods: {
			isAcitve(type) {
				if (type == 1) {
					this.active = 0;
					this.isShowOne = true;
					this.isShowTwo = false;
					this.isShowThree = false
				} else if (type == 2) {
					this.active = 1;
					this.isShowOne = false;
					this.isShowTwo = true;
					this.isShowThree = false
				} else {
					this.active = 2;
					this.isShowOne = false;
					this.isShowTwo = false;
					this.isShowThree = true
				}

			},
			onChange1(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				// console.log(this.rateValue);
			},
			onChange2(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				// console.log(this.rateValue);
			},
			cancle() {
				this.enterView = false
			},
			//获取全部班级
			async getClassList() {
				const result = await getClass()
				if (result.data.code === 1000) {
					this.className = result.data.data
				}
			},

			//根据班级id去获取学生
			async getStuInfo(item) {
				let cid = {
					cid: ''
				}
				let lastInterview = uni.setStorageSync('lastInterview', item);
				this.classId = item.teacherClz
				this.isSelect = false
				cid.cid = item.cid
				const result = await getStuList(cid)
				if (result.data.code === 1000) {
					this.stuInfo = result.data.data
				}
			},
			//根据学生id去获取详细数据
			async stuDetailed(id) {
				let sid = {
					sid: id
				}
				uni.showLoading({
					title: '加载中'
				})
				const result = await getStuDetailed(sid)
				if (result.data.code === 1000) {
					this.stuDetaile = result.data.data
					console.log(this.stuDetaile, '--------------------------')
					uni.hideLoading()
					this.enterView = true
				}
			},

			// 点击面试 获取面试详情
			detaile(item) {
				this.list = false
				this.detaileContnet = item
				console.log(this.detaileContnet)
				this.rateValue1 = this.detaileContnet.mszbqk == undefined ? 0 : this.detaileContnet.mszbqk
				this.rateValue2 = this.detaileContnet.msscore == undefined ? 0 : this.detaileContnet.msscore
				this.boxImg = []
				this.isReview = true
				if (this.detaileContnet.msdp) {
					this.isReview = false
				}
				if (item?.exam) {
					this.boxImg = JSON.parse(item?.exam)
					this.isImg = false
					console.log(this.boxImg)
					if (this.boxImg.length == 1) {
						this.isDirection.pre = false
						this.isDirection.next = false
					}
				} else {
					this.isImg = true
				}
			},

			//切换图片
			direction(direction) {
				let Boxindex = this.boxImg.length - 1
				if (direction == 'pre') {
					if (this.index != 0) {
						this.qhImg = this.boxImg[--this.index]
						if (this.index == 0) {
							this.isDirection.pre = false
							this.isDirection.next = true
						}
					}
				} else {
					if (this.index != Boxindex) {
						this.qhImg = this.boxImg[++this.index]
						if (this.index == Boxindex) {
							this.isDirection.next = false
							this.isDirection.pre = true
						}
					}
				}
			},

			//点击放大图片
			amplification(item, id) {
				this.isBigBox = true
				this.qhImg = item
				this.index = id
				// console.log(item)
				if (this.index == 0) {
					this.isDirection.pre = false
				} else if (this.index == this.boxImg.length - 1) {
					this.isDirection.next = false
				}
			},

			canleImg(e) {
				if (e.target.id == 'mianshi') {
					this.enterView = false
				} else if (e.target.id != "imgBox" && e.target.id != "box") {
					this.isBigBox = false
				}
			},
			//点评
			async review() {
				let sid = {
					sid: this.detaileContnet.stuid
				}
				if (this.reviewContent.trim() != '') {
					const result = await pullMsg(this.detaileContnet)
					uni.showToast({
						icon: 'success',
						title: '点评成功',
						duration: 1000
					})
					this.list = true
					uni.showLoading({
						title: '加载中'
					})
					const result1 = await getStuDetailed(sid)
					if (result1.data.code === 1000) {
						this.stuDetaile = result1.data.data
						this.reviewContent = ''
						uni.hideLoading()
					}
				} else {
					uni.showToast({
						icon: 'error',
						title: '内容不为空',
						duration: 1000
					})
				}
			}
		},
		computed: {
			mianshiTime() {
				let nowTime = new Date()
				let nowTimeYear = nowTime.getFullYear()
				let nowTimeMonth = nowTime.getMonth() + 1
				let nowTimeDay = nowTime.getDay()
				nowTimeMonth = nowTimeMonth < 10 ? `0${nowTimeMonth}` : nowTimeMonth
				nowTimeDay = nowTimeDay < 10 ? `0${nowTimeDay}` : nowTimeDay
				let Time = `${nowTimeYear}-${nowTimeMonth}-${nowTimeDay}`
				if (this.detaileContnet.mstm) {
					this.detaileContnet.mstm = this.detaileContnet.mstm.substring(0,10)
					let futureTime = new Date(this.detaileContnet.mstm).getTime()
					let getTime = futureTime - Time
					if (getTime > 0) {
						return '未到面试时间'
					} else if (getTime < 0) {
						return '面试已过'
					}
				} else {
					return '未存在'
				}
			}
		},
		onLoad() {
			setTimeout(async () => {
				this.rateValue1 = 0
				this.rateValue2 = 0
				uni.showLoading({
					title: '加载中'
				})
				let cid = {
					cid: ''
				}
				let lastInterview = uni.getStorageSync('lastInterview')
				if (this.className[0].teacherClz) {
					if (lastInterview) {
						this.classId = lastInterview.teacherClz
						cid.cid = lastInterview.cid
						const result = await getStuList(cid)
						if (result.data.code === 1000) {
							this.stuInfo = result.data.data
						}
						uni.hideLoading()
					} else {
						this.classId = this.className[0].teacherClz
						cid.cid = this.className[0].cid
						const result = await getStuList(cid)
						if (result.data.code === 1000) {
							this.stuInfo = result.data.data
						}
						uni.hideLoading()
					}
					this.stuInfo.forEach((item, index) => {
						let newImg = ''
						newImg = item.head.replace(/[\r\n]/g, '');
						this.imgUrl.push(newImg)
					})
				} else {
					return null
				}
			}, 1000)

			this.getClassList()
		}
	}
</script>

<style scoped>
	movable-view {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 90%;
		height: 500rpx;
	}

	movable-area {
		height: 500rpx;
		width: 100%;
		position: fixed;
		overflow: hidden;
	}

	movable-view image {
		width: 100%;
	}

	.select-cart {
		display: flex;
		width: 100%;
		height: 80rpx;
		background-color: #fff;
		color: #868686;
	}

	.select-cart view {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 33.3%;
		height: 100%;
	}

	.select-cart view text.active {
		box-sizing: border-box;
		height: 100%;
		line-height: 80rpx;
		border-bottom: 2px solid #99debc;
		color: #99debc;
	}

	.selectContainer {
		position: relative;
		margin-top: 20rpx;
	}

	.selectDown {
		width: 60%;
		height: 50rpx;
		margin: 0 auto;
		border-radius: 10rpx;
		border: 1px solid #ccc;
		font-size: 16px;
		text-align: center;
		line-height: 50rpx;
		margin-bottom: 5rpx;
		background-color: #ccc;
	}

	.selectedCard {
		position: absolute;
		display: flex;
		flex-direction: column;
		width: 60%;
		border: 1px solid #ccc;
		border-radius: 10rpx;
		left: 50%;
		transform: translateX(-50%);
		background-color: #fff;
		text-align: center;
		padding-bottom: 10rpx;
	}

	.selectedCard text {
		margin-top: 20rpx;
	}

	.stu-container {
		display: flex;
		flex-wrap: wrap;
		width: 100%;
		margin-top: 20rpx;
		padding-left: 15rpx;
	}

	.stu-view {
		display: flex;
		flex-direction: column;
		width: 120rpx;
		text-align: center;
		margin-right: 30rpx;
		margin-bottom: 20rpx;
	}

	.stu-container .stu-avatar {
		width: 120rpx;
		height: 120rpx;
		/* background-color: red; */
		border-radius: 10rpx;
	}

	.stu-container .stu-avatar image {
		width: 100%;
		height: 100%;
	}

	.mianshi-view {
		position: absolute;
		width: 750rpx;
		height: 100%;
		top: 0;
		background-color: rgba(0, 0, 0, 0.5);
		animation: enter .5s linear;
	}

	.mianshi-list {
		width: 90%;
		/* height: 800rpx; */
		max-height: 800rpx;
		background-color: #2474b5;
		background-image: linear-gradient(
		to left bottom, #c8c2c6, #c2bec3, #bcbabf, #b7b6bb, 
		#b1b2b7, #acaeb3, #a8abaf, #a3a7ab, #9ea3a6, 
		#989fa1, #939a9b, #8f9696);
		border-radius: 10rpx;
		margin: 0 auto;
		margin-top: 170rpx;
		padding: 20rpx 0 20rpx 20rpx;
		overflow: scroll;
	}

	.mian-list-view {
		display: flex;
		flex-wrap: wrap;
	}

	.list-item {
		display: flex;
		flex-direction: column;
		width: 179rpx;
		height: 300rpx;
		text-align: center;
		margin-right: 46rpx;
		margin-bottom: 20rpx;
		/* background-color: pink; */
	}

	.list-item>.item-status {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 100%;
		height: 180rpx;
		background-color: #e2d849;
		border-radius: 50%;
		margin-bottom: 20rpx;
	}

	@keyframes enter {
		0% {
			opacity: 0;
		}

		100% {
			opacity: 1;
		}
	}

	.interview-info {
		position: absolute;
		width: 90%;
		height: 90%;
		background-color: #ccccd6;
		left: 50%;
		transform: translateX(-50%);
		margin-top: 50rpx;
		padding-top: 20rpx;
		padding-left: 20rpx;
		overflow: scroll;
		border-radius: 10rpx;
	}

	.stu-info {
		display: flex;
		width: 100%;
		height: 60rpx;
		/* background-color: red; */
		line-height: 60rpx;
		box-sizing: border-box;
		margin-bottom: 20rpx;
	}

	.stu-info>.Information-box {
		/* width: 304rpx; */
		box-sizing: border-box;
		height: 60rpx;
		padding-left: 20rpx;
		padding-right: 20rpx;
		border-radius: 10rpx;
		background-color: #93b5cf;
	}

	.prepare {
		padding-top: 5rpx;
		background-color: skyblue;
	}

	.conclusion {
		display: flex;
		flex-direction: column;
		/* background-color: red; */
		height: 200rpx;
		padding-right: 20rpx;
	}

	.conclusion>.Information-box {
		height: 140rpx;
		overflow: scroll;
	}

	.problem {
		display: flex;
		flex-direction: column;
		/* background-color: red; */
		height: 400rpx;
		padding-right: 20rpx;
	}

	.problem>.Information-box {
		justify-content: center;
		align-items: center;
		height: 339rpx;
	}

	.problem>.Information-box2 {
		display: flex;
		flex-wrap: wrap;
		height: 339rpx;
		background-color: #93b5cf;
		padding-left: 15rpx;
		padding-top: 10rpx;
		overflow: scroll;
	}

	.img-view {
		width: 120rpx;
		height: 120rpx;
		border-radius: 10rpx;
		margin-right: 10rpx;
		margin-bottom: 20rpx;
		background-color: red;
	}

	.img-view image {
		width: 100%;
		height: 100%;
	}

	.big-box {
		position: absolute;
		width: 90%;
		height: 500rpx;
		margin-right: 10rpx;
		border-radius: 10rpx;
		z-index: 1;
		/* background-color: palevioletred; */
	}

	.big-box img {
		width: 100%;
		height: 100%;
	}

	.prev,
	.next {
		position: absolute;
		font-size: 170rpx;
		text-align: center;
		color: red;
	}

	.prev {
		left: 0;
		top: 45%;
		transform: translateY(-50%);
	}

	.next {
		right: 0;
		top: 45%;
		transform: translateY(-50%);
	}

	.review,
	.isReview {
		display: flex;
		flex-direction: column;
		height: 400rpx;
		padding-right: 20rpx;
	}

	.review textarea,
	.isReview>view {
		box-sizing: border-box;
		width: 100%;
		border: 2px solid #8abcd1;
		border-radius: 10rpx;
	}

	.isReview>view {
		overflow: scroll;
		height: 330rpx;
	}

	.btn {
		display: flex;
		justify-content: center;
		margin-bottom: 60rpx;
	}

	.btn button {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 80rpx;
	}
</style>
