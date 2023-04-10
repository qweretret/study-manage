<template>
	<view>
		<view class="navbar">
			<navigator url="@/pages/index/index" open-type="redirect">
			<text class="new">消息</text>
			</navigator>
			<text @click="outshow=!outshow">个人中心</text>
		</view>
		<view class="out" v-show="outshow">
			<navigator url="/pages/Summarize/Summarize">
			   <text @click="outbg=0,outshow=false" :class="outbg == 0 ? 'outbg' : '' ">每日总结</text>
			</navigator>
			
			<navigator url="/pages/interview/interview">
			   <text @click="outbg=1,outshow=false" :class="outbg == 1 ? 'outbg' : '' ">每日反馈</text>
			</navigator>
			
			<navigator url="/pages/Evaluation/Evaluation">
			   <text @click="outbg=2,outshow=false" :class="outbg == 2 ? 'outbg' : '' ">自我评价</text>
			</navigator>
			
			<navigator url="/pages/register/register">
			   <text @click="outbg=3,outshow=false" :class="outbg == 3 ? 'outbg' : '' ">面试登记</text>
			</navigator>
			<navigator url="/pages/Personal/Personal">
			   <text @click="outbg=4,outshow=false" :class="outbg == 4 ? 'outbg' : '' ">个人信息</text>
			</navigator>
		</view>
	</view>
</template>

<script>
	export default {
		name:"navbar",
		data() {
			return {
				outshow:false,
				outbg:9
			};
		},
		methods:{
		},
	}
</script>

<style>
    .navbar{
		width: 100%;
	    height: 100rpx;
		position: fixed;
		bottom: 0;
		display: flex;
		align-items: center;
		/* justify-content: center; */
		background: #f7f7fa;
	}
	.navbar navigator{
		width: 50%;
		background: #f7f7fa;
		color: #7f8389;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.navbar  text{
		width: 50%;
		background: #f7f7fa;
		color: #7f8389;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.navbar text:nth-child(2){
		position: relative;
	}
	.out{
		width: 43%;
		/* height: 210rpx; */
		/* background: red; */
		display: flex;
		flex-direction:column;
		align-items: center;
		justify-content: space-between;
		position: fixed;
		right: 23rpx;
		bottom: 100rpx;
		background: #ffffff;
		border-radius: 10rpx;
	}
	.out text{
		width: 100%;
		height: 70rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.out navigator{
		width: 100%;
		height: 70rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.outbg{
		background: #e6f7ff;
	}
	.new{
		background:gray;
	}
</style>