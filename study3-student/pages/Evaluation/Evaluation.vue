<template>
	<view>
		<view class="top">
			<uni-card :title="list.crtm" :extra="`第${list.week}周`" v-for="(list,index) in list" :key="index">
				<text class="uni-body uni-body1">{{list.content}}</text>
				<view class="content">
					<view class="">
						<text>学习评价:{{list.quest1}}星</text>
					</view>
					<view class="">
						<text>游戏时长:{{list.quest2}}小时</text>
					</view>
					<view class="">
						<text>进度评价:{{list.quest3}}星</text>
					</view>
				</view>
				<view v-if="list.readst == 1" class="read">
					<text>已读</text>
				</view>
				<view v-if="list.readst == 0"  class="noread">
					<text>未读</text>
				</view>
			</uni-card>
		</view>
		<view class="bot">
			<view class="yuan" @click="add()">
				<uni-icons type="plusempty" size="28" color="white"></uni-icons>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		Evaluation
	} from "@/api/Evaluation.js"
	export default {
		data() {
			return {
				list: {},
				readsts: 0
			}
		},
		methods: {
			async evation() {
				const als = {
					readst: null
				}
				const sum = await Evaluation(als)
				this.list = sum.data.data.records;
				console.log(this.list);
			},
			add() {
				uni.navigateTo({
					url: "/pages/Evaluation/evaluations"
				})
			}
		},
		onShow() {
			this.evation()
		}
	}
</script>

<style>
	.read{
		width: 120rpx;
		height: 40rpx;
		border-radius: 10rpx;
		background: green;
		color: white;
		text-align: center;
		font-size: 14px;
	}
	.noread{
		width: 120rpx;
		height: 40rpx;
		border-radius: 10rpx;
		background: red;
		color: white;
		text-align: center;
		font-size: 14px;
	}
	.content {
		display: flex;
		align-items: center;
		justify-content: space-between;
		color: #362228;
		margin: 20rpx auto;
		/* border-top: 1px solid #362228; */
		padding-top: 10rpx;
	}

	.uni-card {
		background: #F3D8D9 !important;
		/* color: black; */
	}

	.uni-body1 {
		width: 600rpx;
		overflow: hidden;
		word-wrap: break-word;
		/* background: #362228; */
	}

	.bot {
		width: 100%;
		height: 80rpx;
		background: #F1FAF3;
		position: fixed;
		bottom: 0;
		display: flex;
		justify-content: center;
	}

	.yuan {
		width: 100rpx;
		height: 100rpx;
		position: fixed;
		background: #FDF71C;
		bottom: 30rpx;
		border-radius: 50rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
</style>
