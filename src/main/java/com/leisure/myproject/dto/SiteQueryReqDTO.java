package com.leisure.myproject.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author gonglei
 * @date 2020/2/5 15:03
 */
@Data
@Builder
public class SiteQueryReqDTO {

	@ApiModelProperty(value = "模糊查询条件：站点名称")
	private String keyword;

	@ApiModelProperty(value = "站点类型;1:门站、2:储配站、3:加气站、4:液化气站、5:运营网点、6:调压站")
	private String siteType;

	@ApiModelProperty(value = "所属区县")
	private String areaName;

	@ApiModelProperty(value = "分页页码")
	private Integer pageNum;

	@ApiModelProperty(value = "分页大小")
	private Integer pageSize;

}
