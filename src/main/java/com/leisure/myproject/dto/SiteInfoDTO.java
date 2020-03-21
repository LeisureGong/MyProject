package com.leisure.myproject.dto;


import com.leisure.myproject.validation.ValidGroup_Add_Site;
import com.leisure.myproject.validation.ValidGroup_Update_Site;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author gonglei
 * @date 2020/2/5 13:38
 */
@Data
@Builder
public class SiteInfoDTO {

	@ApiModelProperty(value = "逻辑主键;新增时不填写，修改时需填写..")
	@Id
	@NotNull(message = "逻辑主键id不可为空",groups = {ValidGroup_Update_Site.class})
	private Integer id;

	@ApiModelProperty(value = "站点名称",example = "廊坊第一加气站")
	@NotBlank(message = "站点名称不能为空",groups = {ValidGroup_Add_Site.class,ValidGroup_Update_Site.class})
	private String siteName;

	@ApiModelProperty(value = "站点编码",example = "lf00001")
	private String siteCode;

	@ApiModelProperty(value = "站点类型;1:门站、2:储配站、3:加气站、4:液化气站、5:运营网点、6:调压站",example = "3")
	@NotBlank(message = "站点类型不能为空",groups = {ValidGroup_Add_Site.class,ValidGroup_Update_Site.class})
	private String siteType;

	@ApiModelProperty(value = "所在区县",example = "广阳区")
	@NotBlank(message = "所在区县不能为空",groups = {ValidGroup_Add_Site.class,ValidGroup_Update_Site.class})
	private String areaName;

	@ApiModelProperty(value = "供气能力：万立方/日",example = "109.4")
	private Double gasCapacity;

	@ApiModelProperty(value = "站点经度",example = "34.4433710248")
	private Double siteLng;

	@ApiModelProperty(value = "站点纬度",example = "114.8921012878")
	private Double siteLat;

	@ApiModelProperty(value = "位置描述",example = "位于廊坊开发区纬一街道")
	private String siteDetail;

	@ApiModelProperty(value = "服务范围描述",example = "廊坊大学城及开发区")
	private String siteScope;

	@ApiModelProperty(value = "联系人",example = "苏大强")
	@NotBlank(message = "联系人不能为空",groups = {ValidGroup_Add_Site.class,ValidGroup_Update_Site.class})
	private String contactPerson;

	@ApiModelProperty(value = "联系方式",example = "16902139847")
	@NotBlank(message = "联系方式不能为空",groups = {ValidGroup_Add_Site.class,ValidGroup_Update_Site.class})
	@Pattern(regexp = "[0-9\\-]*", message = "联系方式格式不正确")
	private String contactPhone;

	@ApiModelProperty(value = "调度模式类型;0:手动,1:自动")
	private String dispatchMode;
}
