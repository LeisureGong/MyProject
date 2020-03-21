package com.leisure.myproject.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@ApiModel("站点")
@Table(name = "site")
public class SiteInfoEntity {

	@ApiModelProperty(value = "逻辑主键")
	@Id
	@KeySql(useGeneratedKeys = true)
	private Integer id;

	@ApiModelProperty(value = "站点名称")
	private String siteName;

	@ApiModelProperty(value = "站点编码")
	private String siteCode;

	@ApiModelProperty(value = "站点类型")
	private String siteType;

	@ApiModelProperty(value = "所属区县")
	private String areaName;

	@ApiModelProperty(value = "供气能力：万立方/日")
	private Double gasCapacity;

	@ApiModelProperty(value = "站点经度")
	private Double siteLng;

	@ApiModelProperty(value = "站点纬度")
	private Double siteLat;

	@ApiModelProperty(value = "位置描述")
	private String siteDetail;

	@ApiModelProperty(value = "服务范围描述")
	private String siteScope;

	@ApiModelProperty(value = "联系人姓名")
	private String contactPerson;

	@ApiModelProperty(value = "联系方式")
	private String contactPhone;

	@ApiModelProperty(value = "调度模式类型;0:手动,1:自动")
	private String dispatchMode;

	@ApiModelProperty(value = "操作人",hidden = true)
	private String operatorId;

	@ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;

	@ApiModelProperty(value = "更新时间",hidden = true)
	private Date updateTime;

	@ApiModelProperty(value = "是否被删除",hidden = true)
	private String isDelete;

	@ApiModelProperty(value = "备注",hidden = true)
	private String remarks;

	public Date getCreateTime() {
		return createTime == null ? null : new Date(createTime.getTime());
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime == null ? null : new Date(createTime.getTime());
	}
	public Date getUpdateTime() {
		return updateTime == null ? null : new Date(updateTime.getTime());
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime == null ? null : new Date(updateTime.getTime());
	}

	public SiteInfoEntity() {
	}

	public SiteInfoEntity(Integer id, String siteName, String siteCode, String siteType, String areaName, Double gasCapacity, Double siteLng, Double siteLat, String siteDetail, String siteScope, String contactPerson, String contactPhone, String dispatchMode, String operatorId, Date createTime, Date updateTime, String isDelete, String remarks) {
		this.id = id;
		this.siteName = siteName;
		this.siteCode = siteCode;
		this.siteType = siteType;
		this.areaName = areaName;
		this.gasCapacity = gasCapacity;
		this.siteLng = siteLng;
		this.siteLat = siteLat;
		this.siteDetail = siteDetail;
		this.siteScope = siteScope;
		this.contactPerson = contactPerson;
		this.contactPhone = contactPhone;
		this.dispatchMode = dispatchMode;
		this.operatorId = operatorId;
		this.createTime = createTime == null ? null : new Date(createTime.getTime());
		this.updateTime = updateTime == null ? null : new Date(updateTime.getTime());
		this.isDelete = isDelete;
		this.remarks = remarks;
	}
}
