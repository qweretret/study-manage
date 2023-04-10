<template>
	<view>
		<view class="detail">
			<view class="frame">
				<view class="company">
					<text>公司</text>
					<view class="inputs" style="z-index: 10;">
						<superwei-combox :candidates="newname" placeholder="请选择或输入" v-model="city"></superwei-combox>
					</view>
				</view>
				<view class="gw">
					<text>岗位</text>
					<view class="inputs">
						<superwei-combox :candidates="newjob" placeholder="请选择或输入岗位" v-model="citys"></superwei-combox>
					</view>
				</view>
				<view class="time">
					<text>面试时间</text>
					<view class="inputsw">
						<uni-datetime-picker  type="date" :clear-icon="false"  v-model="value" @change="maskClick()" />
					</view>
				</view>
				<view class="prepare">
					<text>面试准备</text>
					<uni-rate @change="onChange()" touchable="false" :value="values"></uni-rate>
				</view>
				<view class="btn" @click="tj()">
					<button>提交</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		registers,
		register,
		registera,
		repost
	} from "@/api/register.js"
import getDate from "@/components/Date/date.js"
	export default {
		data() {
			return {
				info: {
					lunar: true,
					range: true,
					insert: false,
					selected: []
				},
				value: '',
				values: 0,
				city: "",
				citys: "",
				datas: [],
				name: [],
				job: [],
				newname: [],
				newjob: [],
				post: [],
				tai: false,
				requestStatus: false
			}
		},
		methods: {
			async list() {
				const sum = await register();
				this.data = sum.data.data.records;
				// this.time = this.data.crtm.substring(0, 10);
				console.log(this.data);

			},
			maskClick(e) {
				console.log('maskClick事件:', e);
				this.value = e
				console.log(this.value);
			},
			async qye() {
				const data = await registers();
				this.datas = data.data.data.records;
				this.datas.map((item) => {
					this.name.push(item.name)
					this.newname = Array.from(new Set(this.name))
					// console.log(this.newname)
				})
			},
			async jobs() {
				const datas = await registera();
				this.post = datas.data.data.records;
				// console.log(datas);
				this.post.map((item) => {
					this.job.push(item.name)
					this.newjob = Array.from(new Set(this.job))
					// console.log(this.newjob)
				})
			},
			blur() {
				console.log(this.value);
				const zz = /^\d{4}-\d{1,2}-\d{1,2}/;
				let num = zz.test(this.value);
				console.log(num);
			},
			onChange(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.values = JSON.stringify(e.value);
				console.log(this.values);
			},
			async tj() {
				var timeStr = getDate.dateTimeStr('y-m-d');
				console.log(timeStr)
				if(this.value >= timeStr){
					this.tai = this.newname.includes(this.city)
					if (this.tai == true) {
						const record = {
							cmpname: this.city,
							postid: this.citys,
							mstm: this.value,
							mszbqk: this.values,
						}
						let res = await repost(record);
						console.log(res);
						if (res.data.code == 1001) {
							uni.showToast({
								title: '请完善信息',
								icon: "error"
							})
						} else if (res.data.code == 1000) {
							uni.showToast({
								title: "添加成功",
								icon: 'success',
								success: (res) => {
									const time = setTimeout((res) => {
										uni.navigateBack({
											delta: 1
										})
										// console.log(this.city);
									}, 1000)
								}
							})
						}
					} else {
						uni.showToast({
							title: '无企业信息,需要添加企业信息',
							icon: 'none',
							success: (res) => {
								const time = setTimeout((res) => {
									uni.navigateTo({
										url: '/pages/register/super',
										success: (res) => {
											clearTimeout(time);
										}
									})
								}, 1000)
							}
						})
					}
				}else{
					uni.showToast({
						title:"请选择今天或明天",
						icon:"none"
					})
				}
				if (this.requestStatus) {
					// 利用 return 终止函数继续运行
					return false;
				}
				console.log('按钮点击函数执行');
				// 执行函数
				this.requestStatus = true;
				setTimeout(() => {
					// 模拟执行完毕
					// 改变 requestStatus
					this.requestStatus = false;
				}, 1000);
				if (this.city == "") {
					uni.showToast({
						icon: "none",
						title: "请输入公司"
					})
				}
				
			}
		},
		onShow() {
			this.list();
			this.qye();
			this.jobs()
			console.log(this.newname);
			console.log(this.newjob);
		}
	}
</script>

<style>
	.detail {
		width: 700rpx;
		margin: 10rpx auto;
	}

	.frame {
		/* width: 650rpx; */
		margin: 0 auto;
		background: #D0E8EE;
		display: flex;
		flex-direction: column;
		border-radius: 10rpx;
	}

	.company {
		display: flex;
		align-items: center;
		margin-top: 40rpx;
		justify-content: center;
	}

	.company text {
		margin-right: 30rpx;
	}

	.inputs {
		width: 400rpx;
	}

	.inputsw {
		width: 400rpx;
		margin-right: 40rpx;
	}

	.gw {
		display: flex;
		align-items: center;
		margin-top: 40rpx;
		justify-content: center;
	}

	.gw text {
		margin-right: 30rpx;
	}

	.time {
		display: flex;
		align-items: center;
		margin-top: 40rpx;
		justify-content: center;
	}

	.time text {
		margin-right: 20rpx;
	}

	.prepare {
		display: flex;
		align-items: center;
		margin-top: 20rpx;
		justify-content: center;
		margin-bottom: 20rpx;
	}

	.prepare text {
		margin-right: 20rpx;
	}
</style>
