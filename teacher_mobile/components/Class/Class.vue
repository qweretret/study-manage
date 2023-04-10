<template>
	<view class="ClassContainer">
		<Students v-for="item in Person" :key="item.id" :name="item.sname" :infoAll="item.aq" :sid="item.sid"
			:isInterLearning="isInterLearning">
		</Students>
		<StuPushDetails></StuPushDetails>
	</view>
</template>

<script>
	import Students from './Students.vue'
	import StuPushDetails from '@/components/StuPushDetails/StuPushDetails.vue'
	export default {
		name: "Class",
		props: ['Person', 'isInterLearning'],
		data() {
			return {

			};
		},
		components: {
			Students,
			StuPushDetails,
		}
	}
</script>

<style scoped>
	.ClassContainer {
		display: flex;
		flex-wrap: wrap;
		margin-top: 40rpx;
		width: 100%;
		border-top: 1px solid #ccc;

		/* background-color: red; */
	}
</style>
