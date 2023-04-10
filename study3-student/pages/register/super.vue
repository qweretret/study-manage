<template>
	<view>
		<view class="content">
			<view class="frame">
				
				<view class="name">
					<text>公司名称:</text>
					<view class="ipt">
						<uni-easyinput v-model="name" placeholder="请输入"  ></uni-easyinput>
					</view>
				</view>
			
				<view class="xitwo">
					<view class="scales">
						<text>招聘岗位:</text>
						<view class="ipt">
							<uni-easyinput v-model="gw" placeholder="请输入"  trim="all"></uni-easyinput>
						</view>
					</view>
					<view class="scales">
						<text>招聘人数:</text>
						<view class="ipt">
							<uni-easyinput v-model="renshu" placeholder="请输入"></uni-easyinput>
						</view>
					</view>
				</view>
				
				<view class="xueli">
					<text>要求学历:</text>
					<uni-combox v-model="xueli" :candidates="xueli" placeholder="请选择"  :clear="false"></uni-combox>
				</view>
				<view class="xithree">
					<view class="jyan">
						<text>经验:</text>
						<view class="ipt">
							<uni-combox v-model="jyan" :candidates="yan" placeholder="请选择"  :clear="false"></uni-combox>
						</view>
					</view>
					<view class="dyu">
						<text>待遇:</text>
						<view class="ipt">
							<uni-easyinput v-model="daiyu" placeholder="请输入"></uni-easyinput>
						</view>
					</view>
				</view>
				</view>
				<view class="btn" @click="btu">
					<button>提交</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import {qiyepost,gwpost} from "@/api/register.js"
	export default {
		data() {
			return {
				candidates:['1','2','3','4'],
				xiaoxi:['学生','内部'],
				xueli:['大专','本科','研究生'],
				yan:['无经验','1年左右','1-2年','2-3年'],
					name:"",
					guimo:"",
					xiaoxi:"",
					phone:"",
					dizi:"",
					miaosu:"",
					beizu:"",
					gw:"",
					renshu:"",
					xueli:"",
					jyan:"",
					daiyu:"",
					yaoqiu:""
			}
		},
		methods: {
			async btu(){
				// console.log(this.form);
				// this.post = datas.data.data.records;
				// console.log(datas);

				const data = {
					address:this.dizi,
					descb:this.miaosu,
					maxemps:this.guimo,
					mobile:this.phone,
					name:this.name,
					remark:this.beizu,
					source:this.xiaoxi,
				}
				
				const result =  await qiyepost(data)
				const datas = {
					company:this.name,
					exps:this.jyan,
					maxneed:this.renshu,
					sarry:this.daiyu,
					tech:this.yaoqiu,
					xueli:this.xueli
				}
				const res =  await gwpost(datas)
				console.log(result,res);
			}
		}
	}
</script>

<style>
	page{
	}
	.content{
		width: 700rpx;
		margin: 0 auto;
		background: lightskyblue;
		border-radius: 10rpx;
	}
	.frame{
		width: 650rpx;
		margin: 0 auto;
		padding-top: 10rpx;
	}
	.xi{
		margin-top: 30rpx;
	}
	.scale{
		display: flex;
		margin-top: 20rpx;
	}
	.scales{
		display: flex;
		flex-direction:column;
		justify-content: space-between;
	}
	.scales text{
		margin-bottom: 10rpx;
	}
	.scales uni-combox{
		width: 80%;
	}
	.phone text{
		margin-bottom: 10rpx;
	}
	.xueli , .yqiu ,.bzu ,.miaos ,.phone{
		margin-top: 20rpx;
	}

	.xitwo{
		display: flex;
		margin-top: 20rpx;
	}
	.xitwo .ipt{
		width: 90%;
	}
	.xithree{
		display: flex;
		margin-top: 20rpx;
	}
	.xithree .ipt{
		width: 90%;
	}
	.btn button{
		margin-top: 20rpx;
		width: 50%;
		background: lightgray;
	}
</style>
