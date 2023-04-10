<template>
	<view>
		<view class="Students" @click="getStudentInfo($event)">
			<view class="StudentsIcon">
				{{infoAll}}
			</view>
			<text>{{name}}</text>
		</view>
	</view>

</template>

<script>
	export default {
		name: 'Students',
		props: ['name', 'infoAll', 'sid', 'isInterLearning'],
		data() {
			return {
				stuId: {},
			}
		},
		methods: {
			getStudentInfo(e) {
				let {
					sid,
					isInterLearning
				} = this
				// this.getStudensPushInfoAll()
				uni.$emit('getInfo', {
					e,
					sid,
					isInterLearning
				})
			},
		}
	}
</script>

<style scoped>
	.Students {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 250rpx;
		height: 250rpx;
		border: 1px solid #ccc;
		border-left: none;
		border-top: none;
		box-sizing: border-box;
		/* background-color: skyblue; */
	}

	.StudentsIcon {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 80rpx;
		height: 80rpx;
		background-color: #2b73af;
		border-radius: 50%;
		margin-bottom: 20rpx;
		color: #baccd9;
	}
</style>
