<template>
	<view>
		<view class="edit">
			<uni-forms>
				<uni-forms-item label="姓名:">
					<uni-easyinput trim="all" v-model="form.name" />
				</uni-forms-item>
				<uni-forms-item label="email:">
					<uni-easyinput trim="all" v-model="form.email" />
				</uni-forms-item>
				<uni-forms-item label="性别:">
					<uni-easyinput trim="all" v-model="form.sex" />
				</uni-forms-item>
				<uni-forms-item label="学校:">
					<uni-easyinput trim="all" v-model="form.school" />
				</uni-forms-item>
				<uni-forms-item label="专业:">
					<uni-easyinput trim="all" v-model="form.specialized" />
				</uni-forms-item>
				<uni-forms-item label="手机:">
					<uni-easyinput trim="all" v-model="form.phone" />
				</uni-forms-item>
				<uni-forms-item label="生日:">
					<uni-datetime-picker type="date" :clear-icon="false" v-model="form.brith" />
				</uni-forms-item>
				<uni-forms-item label="籍贯:">
					<uni-easyinput trim="all" v-model="form.hometown" />
				</uni-forms-item>
				<uni-forms-item label="紧急联系人关系：">
					<uni-data-select :placeholder="form.relation" v-model="form.relation" :localdata="range"
						@change="change()">
					</uni-data-select>
				</uni-forms-item>
				<uni-forms-item label="紧急联系人:">
					<uni-easyinput trim="all" v-model="form.emergency" />
				</uni-forms-item>
				<uni-forms-item label="紧急联系人电话:">
					<uni-easyinput trim="all" v-model="form.emergencyPhone" />
				</uni-forms-item>
				<uni-forms-item label="职务:">
					<uni-data-select :placeholder="form.position" v-model="form.position" :localdata="ranges"
						disabled="true" clear="false" @change="changes()">
					</uni-data-select>
				</uni-forms-item>
			</uni-forms>
			<view class="btn">
				<button type="primary" @click="submit()">更改</button>
				<button type="primary" @click="Cancel()">取消</button>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		studentgetinfo,
		studentputinfo
	} from "@/api/class.js"
	export default {
		data() {
			return {
				form: {
					name: "",
					email: "",
					sex: "",
					school: "",
					specialized: "",
					phone: "",
					birth: "",
					hometown: "",
					relation: "",
					emergency: "",
					emergencyPhone: "",
					position: "",
					id: "",
					isdisable: 0,
					lname: "",
					pwd: "",
					wxappopenid: "",
					wxappuid: ""
				},
				range: [{
						value: 1,
						text: "父亲"
					},
					{
						value: 2,
						text: "母亲"
					},
					{
						value: 3,
						text: "兄弟"
					},
					{
						value: 4,
						text: "姐妹"
					},
					{
						value: 5,
						text: "亲戚"
					},
					{
						value: 6,
						text: "朋友"
					},
				],
				ranges: [{
						value: 0,
						text: "成员"
					},
					{
						value: 1,
						text: "班长"
					},
					{
						value: 2,
						text: "组长"
					},
				],

			}
		},
		methods: {
			async studentget() {
				const res = await studentgetinfo()
				console.log(res);
				this.form.name = res.data.data.user.name
				this.form.email = res.data.data.user.email
				this.form.sex = res.data.data.user.dictionary_sex
				this.form.school = res.data.data.user.school
				this.form.specialized = res.data.data.user.professional
				this.form.phone = res.data.data.user.mobile
				this.form.brith = res.data.data.user.birth
				this.form.hometown = res.data.data.user.nativeplace
				this.form.relation = res.data.data.user.relationship
				this.form.emergency = res.data.data.user.rname
				this.form.emergencyPhone = res.data.data.user.rphone
				this.form.position = res.data.data.user.groupleader
				this.form.id = res.data.data.user.id
				this.form.isdisable = res.data.data.user.isdisable
				this.form.lname = res.data.data.user.lname
				this.form.pwd = res.data.data.user.pwd
				this.form.wxappopenid = res.data.data.user.wxappopenid
				this.form.wxappuid = res.data.data.user.wxappuid
			},
			async submit() {
				const slg = {
					name: this.form.name,
					email: this.form.email,
					dictionary_sex: this.form.sex,
					school: this.form.school,
					professional: this.form.specialized,
					mobile: this.form.phone,
					birth: this.form.birth,
					nativeplace: this.form.hometown,
					relationship: this.form.relation,
					rname: this.form.emergency,
					rphone: this.form.emergencyPhone,
					groupleader: this.form.position,
					id: this.form.id,
					isdisable: this.form.isdisable,
					lname: this.form.lname,
					pwd: this.form.pwd,
					wxappopenid: this.form.wxappopenid,
					wxappuid: this.form.wxappuid
				}
				const res = await studentputinfo(slg)
				console.log(res);
				if (res.data.code == 1000) {
					uni.showToast({
						title: '提交成功',
						icon: 'success',
						success: () => {
							const time = setTimeout((res) => {
								uni.navigateBack({
									delta: 1,
									success: (res) => {
										clearTimeout(time);
									}
								})
							}, 1000)
						}
					})
				}
			},
			Cancel() {
				uni.navigateBack({
					delta: 1
				})
			},
			change(e) {
				console.log("e:", e);
				this.form.relation = e;
			},
			changes(e) {
				console.log("e:", e);
				this.form.position = e;
			},
		},
		onShow() {
			this.studentget()
		}
	}
</script>

<style>
	.edit {
		width: 650rpx;
		margin: 30rpx auto;
	}

	.data-select {
		z-index: 10 !important;
	}

	.btn {
		display: flex;
	}

	.btn button {
		width: 40%;
	}
</style>
