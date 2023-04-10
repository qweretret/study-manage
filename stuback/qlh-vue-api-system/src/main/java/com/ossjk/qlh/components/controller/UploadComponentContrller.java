package com.ossjk.qlh.components.controller;

import com.ossjk.core.constant.Constant;
import com.ossjk.core.system.dto.MultiFile;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.IUploadCommonApi;
import com.ossjk.core.system.dto.UploadDto;
import com.ossjk.core.vo.ResponseBean;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * Copyright © 2021 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.components.controller
 * @ClassName: UploadComponentContrller
 * @Description: 上传组件-控制器
 * @author: chair
 * @date: 2021年7月25日 下午10:46:32
 */
@Api(tags = "上传组件-控制器")
@RestController
@Import(cn.hutool.extra.spring.SpringUtil.class)
@RequestMapping("/components/uploadComponent")
public class UploadComponentContrller extends BaseController {

	@Autowired
	private SpringUtil springUtil;

	@Autowired
	private ResourceMappersProperties ymlUri;

	@ApiOperation(value = "文件上传")
	@PostMapping("/fileUpload")
	public ResponseBean fileUpload(MultipartFile file, UploadDto uploadDto) throws Exception {
		String model = uploadDto.getModel();
		IUploadCommonApi iUploadCommonApi = springUtil.getBean(model, IUploadCommonApi.class);
		if (ObjectUtil.isNull(iUploadCommonApi)) {
			return ResponseBean.Fail("model:" + model + ",填写错误。请联系后端人员。");
		}
		uploadDto.setName(file.getName());
		uploadDto.setOriginalFilename(file.getOriginalFilename());
		uploadDto.setInputStream(file.getInputStream());
		uploadDto.setEmpty(file.isEmpty());
		uploadDto.setContentType(file.getContentType());
		uploadDto.setSize(file.getSize());
		return iUploadCommonApi.upload(uploadDto);

	}

	@ApiOperation(value = "微信文件上传")
	@PostMapping("/wxFileUpload")
	public ResponseBean<String> wxFileUpload(@RequestBody MultiFile multiFile, HttpServletRequest request) throws Exception {

		if(multiFile.getModel().length!=0){
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("[");
			int length = 1;
			String[] myList = new String[multiFile.getModel().length];
			for(String model : multiFile.getModel()){
				stringBuffer.append("\"");
				stringBuffer.append(uploadImg(model,multiFile.getUri(),request,ymlUri));
				stringBuffer.append("\"");
				if(length!=multiFile.getModel().length){
					++length;
					stringBuffer.append(",");
				}
			}
			stringBuffer.append("]");
			return ResponseBean.Success(null,stringBuffer.toString());
		}

		return ResponseBean.Fail();
	}

	/**
	 * 把 Base64字符串 转为文件
	 * @param img Base64字符串
	 * @param uri uri路径
	 * @param ymlUri 根据 “uri路径” 动态获取 “file” 路径
	 * @return 网络图片地址
	 */
	private static String uploadImg(String img,String uri,HttpServletRequest request,ResourceMappersProperties ymlUri) throws IOException {
		if (img != null && !img.equals("")) {
			String encode = img.split(",")[1];
			byte[] decode = Base64.getDecoder().decode(encode.getBytes(StandardCharsets.UTF_8));

			List<ResourceMappersProperties.ResourceMapper> resourceMapperList = ymlUri.getMappers();
			Map<String, String> uriMap = resourceMapperList.stream().collect(Collectors.toMap(ResourceMappersProperties.ResourceMapper::getUri, ResourceMappersProperties.ResourceMapper::getFile));

			String rootpath = uriMap.get(uri);

			String UUID = java.util.UUID.randomUUID().toString();
			String type = img.substring(img.indexOf("/") + 1, img.indexOf(";"));

			String imgPath =   UUID + "." + type;
			// 本站的URI  root
			String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+  request.getContextPath() + uri + "/" ;
			String url = basePath + imgPath;
			File file = new File(rootpath);

			if (!file.exists()) {
				file.mkdirs();
			}

			OutputStream outputStream = new FileOutputStream(new File(rootpath, imgPath));
			outputStream.write(decode);
			outputStream.close();
			return url;
		}
		return "";
	}


}
