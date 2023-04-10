<template>
	<view class="classview">
		<!-- 		<view :class="isActive ? 'ClassNameContainer active' : 'ClassNameContainer' " @click="updateActive">
			{{title}}
		</view> -->
		<!-- <uni-data-select v-model="value" :localdata="range" @change="change" :clear="false" collection='lastSelect'></uni-data-select> -->

		<view class="selectContainer">
			<view class="selectDown" @click="isSelect = !isSelect">
				{{className == '' ? '请选择班级' : className}}
			</view>

			<view class="selectedCard" v-if="isSelect">
				<text v-for="item in range" :key="item.id" @click="change(item.id, item.value)">{{item.value}}</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "ClassName",
		props: ['tearchClass'],
		data() {
			return {
				value: 0,
				range: [],
				isSelect: false,
				className: ''
			};
		},
		methods: {
			change(cid, value) {
				this.$emit('getClassId', {
					cid
				});
				this.isSelect = false;
				this.className = value
				uni.setStorageSync('pushClassSelectedLast', {
					cid,
					value
				})
			},
		},
		created() {

		},
		watch: {
			tearchClass: {
				handler(newData, oldData) {
					this.range = newData
					this.className = newData[0].value
					let lastClass = uni.getStorageSync('pushClassSelectedLast');
					let cid = {}
					if(lastClass) {
						this.className = lastClass.value
						cid.cid = lastClass.cid
					}else {
						cid.cid = newData[0].id
					}
					uni.$emit('getClassName', {
						cid
					})
				}
			}
		}
	}
</script>

<style scoped>
	/* .ClassNameContainer {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 724rpx;
		height: 88rpx;
		background-color: #F0F8FA;
		margin: 0 auto;
		font-size: 14px;
	}

	.active {
		color: #FFCC00;
	}

	.text {
		font-size: 12px;
		color: #666;
		margin-top: 5px;
	}

	.uni-px-5 {
		padding-left: 10px;
		padding-right: 10px;
	} */

	/* 	.uni-pb-5 {
		padding-bottom: 10px;
	} */

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
		display: block;
		margin-top: 20rpx;
		text-align: center;
	}
</style>
