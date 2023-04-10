<template>
	<view>
		<view class="list">
			<view class="cot" v-for="(list,index) in data" :key="index">
				<view class="noread"  v-if="list.mszj == undefined ">
					<text>未面试</text>
				</view>
				<view class="read" v-if="list.exam != undefined">
					<text>已面试</text>
				</view>
				<view class="okread" v-if="list.msdp != undefined">
					<text>已点评</text>
				</view>
			<view class="alls">
			<view class="title">
				<text>公司:{{list.cmpname}}</text>
			</view>
			<view class="time">
				<text>面试时间:{{list.mstm}}</text>
			</view>
			<view class="message">
				<text>登记人:{{list.dbColumn_stuid}}</text>
				<text>面试准备:{{list.mszbqk}}星</text>
			</view>
		   </view>
		<view class="edit" v-if="list.exam == null ">
			<text @click="details($event,index)">详情</text>
			<text  @click="add($event,index)">面试总结</text>
		</view>
		<view class="edits" v-if="list.exam != null ">
			<text @click="details($event,index)" >详情</text>
		</view>
		</view>
		
		
		<view class="nav" @click="ad()">
			<text>面试登记</text>
		</view>
		</view>
	</view>
</template>

<script>
import {register} from "@/api/register.js"
	export default {
		data() {
			return {
				data:{},
				time:"",
				id:"",
				newtime:"",
				msg:[]
			}
		},
		methods: {
		
			
			details(e,index){
				this.id = this.data[index];
				// console.log(this.id);
				uni.navigateTo({
					url: `/pages/register/detail?msg=${encodeURIComponent(JSON.stringify(this.id))}`,
				});
			},
			add(e,index){
				this.id = this.data[index];
				uni.navigateTo({
					url:`/pages/register/supplement?msg=${encodeURIComponent(JSON.stringify(this.id))}`,
					
				})
				// if(this.data.)
			},
			ad(){
				uni.navigateTo({
					url:"/pages/register/registers",
					
				})
			},
			async list(){
				const sum = await register();
				this.data = sum.data.data.records;
				// this.newtime = this.time.substring(0,10)
				// this.time = this.data.mstm
				console.log(this.data);
			}
		},
		onShow() {
			this.list()
		}
	}
</script>

<style>
	page{
		padding-bottom:80rpx;
	}
	.cot{
		width: 700rpx;
		background: #6257cd;
		margin: 20rpx auto;
		border-radius: 10rpx;
		color: white;
		height:279rpx;
		position: relative;
	}
	.read,.noread,.okread{
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 10rpx;
		border-top-left-radius: 10rpx;
		background: green;
		position: absolute;
		right: 0;
		top: 20rpx;
		text-align: center;
		font-size: 12px;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.noread{
		background: red;
	}
	.okread{
		background: skyblue;
	}
	.alls{
		width: 650rpx;
		margin: 10rpx auto;
	}
	.title{
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding-top: 20rpx;
	}
	.message{
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding-top: 20rpx;
	}
	.time{
		padding-top: 20rpx;
	}
	.content{
		display: flex;
		flex-direction: column;
		padding-top: 20rpx;
	}
	.content text:nth-child(1){
		text-align: center;
	}
	.nav{
		width: 100%;
		height: 80rpx;
		position: fixed;
		bottom: 0;
		background: #ECE0F5;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.edit,.edits{
		width: 696rpx;
		height: 80rpx;
		background: white;
		display: flex;
		align-items: center;
		justify-content: space-between;
		color: black;
		border: 1px solid #6257cd;
		border-top: none;
		border-bottom-right-radius: 10rpx;
		border-bottom-left-radius: 10rpx;
	}
	.edits text{
		width: 100%;
		height: 80rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.edit text{
		width: 50%;
		height: 80rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.edit text:nth-child(1){
		border-right: 1px solid #6257cd;
	}
</style>
