<template>
	<view style="padding-top: 100rpx;">
		<view class="HeadPortraitContainer">
			<view class="HeadPortrait">
				<img :src="userImg" alt="">
			</view>
		</view>

		<view class="OperatingOptionsContainer">
			<navigator url="/pages/LearningPush/LearningPush">学习推送签收</navigator>
			<navigator url="/pages/DailySummary/DailySummary">学员每日总结</navigator>
			<navigator url="/pagesB/feedback/feedback">学习每日反馈</navigator>
			<navigator url="/pagesB/selfAssessment/selfAssessment">学员自我评价</navigator>
			<navigator url="/pagesB/MyClass/MyClass">我的班级</navigator>
			<navigator url="/pagesB/interviewManagement/interviewManagement">面试点评</navigator>
		</view>
	</view>
</template>

<script>
	import {
		getUserHead
	} from '@/api/login.js'
	export default {
		data() {
			return {
				userImg: ""
			}
		},
		methods: {
			async getUser() {
				const result = await getUserHead()
				this.userImg = result.data.msg.replace(/[\r\n]/g, '');

			}
		},
		onLoad() {
			this.getUser()
		}
	}
</script>

<style scoped>
	page {
		padding-bottom: 40rpx;
	}

	.HeadPortraitContainer {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 384rpx;
		height: 380rpx;
		border-radius: 50%;
		margin: 0 auto;
		border: 2px solid #797979;
		marign-top: 40rpx;
	}

	.HeadPortrait {
		width: 320rpx;
		height: 314rpx;
		border-radius: 50%;
		background-color: skyblue;
	}

	.HeadPortrait image {
		width: 100%;
		height: 100%;
		border-radius: 50%;
	}

	.OperatingOptionsContainer navigator {
		margin: 40rpx auto;
		display: flex;
		justify-content: center;
		align-items: center;
		width: 320rpx;
		height: 88rpx;
		background-color: red;
		border-radius: 5px;
		color: white;
	}

	.OperatingOptionsContainer navigator:first-child {
		background-color: #666EE8;
	}

	.OperatingOptionsContainer navigator:nth-child(2) {
		background-color: #1E9FF2;
	}

	.OperatingOptionsContainer navigator:nth-child(3) {
		background-color: #B8BBF4;
	}

	.OperatingOptionsContainer navigator:nth-child(4) {
		background-color: #FFC71C;
	}

	.OperatingOptionsContainer navigator:nth-child(5) {
		background-color: #eeb8c3;
	}

	.OperatingOptionsContainer navigator:last-child {
		background-color: #28D094;
	}
</style>
