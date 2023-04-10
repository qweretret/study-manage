<template>
	<view>
		<view class="content">
			<view class="tab">
				<view class="logo" >
					<img :src="stuimg" alt="" >
				</view>
				<view class="title">
					<text>我的班级</text>
				</view>
				<view class="search">
					<uni-icons type="person" size="20"></uni-icons>
				</view>
			</view>
			<view class="hr">
			</view>


			<view class="class">
				<view class="frame" v-for="(list,index) in list" :key="index">
					<view class="top">
						<image :src="imglist[index]" mode=""></image>
					</view>
					<view class="bot">
						<text>{{list.name}}</text>
					</view>
				</view>


			</view>
		</view>
	</view>
</template>

<script>
	import {
		Class,
		student
	} from "@/api/class.js"
	import {
		pathToBase64,
		base64ToPath
	} from '@/js_sdk/mmmm-image-tools/index.js'
	export default {
		data() {
			return {
				list: {},
				imglist: [],
				student: [],
				stuimg: [],
				newimg:[]
			}
		},
		methods: {
			async getClass() {
				const res = await Class();
				this.list = res.data.data
				console.log(this.list);
				let sum = this.list;
				sum.map((res) => {
					this.imglist.push(res.head.replace(/[\r\n]/g, ''))
					// console.log(this.imglist);
				})
				const result = await student();
				this.student = result.data.data
				let sums = this.student;
				sums.map((res) => {
					this.stuimg.push(res.head.replace(/[\r\n]/g, ''))
					console.log(this.stuimg);
				})
			}
		},
		onShow() {
			this.getClass()
		}
	}
</script>

<style>
	.tab {
		width: 690rpx;
		height: 80rpx;
		padding-top: 30rpx;
		margin: 0 auto;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.logo {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50rpx;
	}

	.logo img {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50rpx;
	}

	.title {
		font-size: 18px;
	}

	.search {}

	.hr {
		height: 2rpx;
		width: 100%;
		background: #f7f7f7;
		margin-top: 20rpx;
	}

	.class {
		width: 720rpx;
		margin: 50rpx auto;
		/* align-items: center; */
		display: flex;
		/* justify-content: space-between; */
		flex-wrap: wrap;
	}

	.frame {
		width: 33%;
		height: 150rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-between;
	}

	.top {
		width: 100rpx;
		height: 100rpx;
		background: orangered;
		border-radius: 50rpx;
	}

	.top image {
		width: 100rpx;
		height: 100rpx;
		border-radius: 50rpx;
	}

	.cot {
		text-align: center;
		font-size: 14px;
	}
</style>
