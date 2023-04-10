<template>
	<view>
		<view class="selectContainer">
			<view class="selectDown" @click="isSelect = !isSelect">
				{{className}}
			</view>

			<view class="selectedCard" v-if="isSelect">
				<text v-for="item in classT" :key="item.id" @click="getStu(item)">{{item.value}}</text>
			</view>
		</view>

		<!-- 学生区域 -->
		<view class="stu-view">
			<view class="stu-item" v-for="(item,index) in stuList" :key="index">
				<view class="stu-avatar">
					<img :src="newImg[index]" alt="">
				</view>
				<view class="stu-name">
					{{item.name}}
				</view>
				<view class="stu-position" v-if="item.groupleader == 0 || item.groupleader == undefined">
					组员
				</view>
				<view class="stu-position" v-if="item.groupleader == 1">
					组长
				</view>
				<view class="stu-position" v-if="item.groupleader == 2">
					班长
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		getClass,
		getStuinfo
	} from '@/api/myClass.js'
	export default {
		data() {
			return {
				isSelect: false,
				//班级
				classT: [],
				//班级名
				className: '',
				//学生列表
				stuList: [],
				newImg: []
			}
		},
		methods: {
			async getClassList() {
				const result = await getClass()
				if (result.data.code === 1000) {
					this.classT = result.data.data
				}
			},
			async getStu(item) {
				this.isSelect = false
				let cid = {
					cid: item.id
				}
				uni.setStorageSync('lastMyClass', item)
				const result = await getStuinfo(cid)
				if (result.data.code === 1000) {
					this.stuList = result.data.data
				}
				//将后台返回的base64格式的图片 去掉换行空格
				this.stuList.forEach((item, index) => {
					let pushImg = ''
					pushImg = item.head.replace(/[\r\n]/g, '')
					this.newImg.push(pushImg)
				})
			}
		},
		onLoad() {
			this.getClassList()
			setTimeout(async () => {
				let cid = {
					cid: ''
				}
				let lastMyClass = uni.getStorageSync('lastMyClass')
				if (this.classT[0].value) {
					if (lastMyClass) {
						this.className = lastMyClass.value
						cid.cid = lastMyClass.id
						const result = await getStuinfo(cid)
						if (result.data.code === 1000) {
							this.stuList = result.data.data
						}
						//将后台返回的base64格式的图片 去掉换行空格
						this.stuList.forEach((item, index) => {
							let pushImg = ''
							pushImg = item.head.replace(/[\r\n]/g, '')
							this.newImg.push(pushImg)
						})
					} else {
						this.className = this.classT[0].value
						cid.cid = this.classT[0].id
						const result = await getStuinfo(cid)
						if (result.data.code === 1000) {
							this.stuList = result.data.data
						}
						//将后台返回的base64格式的图片 去掉换行空格
						this.stuList.forEach((item, index) => {
							let pushImg = ''
							pushImg = item.head.replace(/[\r\n]/g, '')
							this.newImg.push(pushImg)
						})
					}
				}
			}, 1000)

		}
	}
</script>

<style scoped>
	.selectContainer {
		position: relative;
		margin-top: 20rpx;
	}

	.selectDown {
		width: 50%;
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

	.stu-view {
		display: flex;
		flex-wrap: wrap;
		margin-top: 30rpx;
		border-top: 1px solid #ccc;
	}

	.stu-item {
		width: 33.33%;
		height: 280rpx;
		/* background-color: red; */
		text-align: center;
		box-sizing: border-box;
		border-right: 1px solid #ccc;
		border-bottom: 1px solid #ccc;
	}

	.stu-item .stu-avatar {
		width: 120rpx;
		height: 120rpx;
		margin: 0 auto;
		margin-top: 30rpx;
		margin-bottom: 10rpx;
		background-color: skyblue;
		border-radius: 10rpx;
	}

	.stu-item .stu-avatar image {
		width: 100%;
		height: 100%;
	}
</style>
