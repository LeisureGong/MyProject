package com.leisure.myproject.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.leisure.myproject.dao.SiteManageDAO;
import com.leisure.myproject.dto.SiteInfoDTO;
import com.leisure.myproject.dto.SiteQueryReqDTO;
import com.leisure.myproject.entity.SiteInfoEntity;
import com.leisure.myproject.service.SiteManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author gonglei
 * @date 2020/2/5 13:11
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SiteManageServiceImpl implements SiteManageService {

	@Autowired
	private SiteManageDAO siteManageDAO;

	@Override
	public List<SiteInfoEntity> querySite(SiteQueryReqDTO siteQueryReqDTO){

		log.info("----【禁止】----------------");

		List<SiteInfoDTO> siteInfoDTOS;
		List<SiteInfoEntity> siteInfoEntityList;
		Example siteExample = new Example(SiteInfoEntity.class);
		siteExample.orderBy("createTime").desc();
		Example.Criteria siteCriteria = siteExample.createCriteria().andEqualTo("isDelete","0");
		if(StringUtils.isNotBlank(siteQueryReqDTO.getKeyword())){
			siteCriteria.andLike("siteName",
					"%" + siteQueryReqDTO.getKeyword() + "%");
		}
		if(StringUtils.isNotBlank(siteQueryReqDTO.getSiteType())){
			siteCriteria.andEqualTo("siteType",siteQueryReqDTO.getSiteType());
		}
		if(StringUtils.isNotBlank(siteQueryReqDTO.getAreaName())){
			siteCriteria.andEqualTo("areaName",siteQueryReqDTO.getAreaName());
		}

		try{
			siteInfoEntityList = siteManageDAO.selectByExample(siteExample);
		} catch(Exception e){
			log.error("查询站点信息失败:{}",e.getMessage());
			throw e;
		}

//		siteInfoDTOS = BeanMapper.mapList(siteInfoEntityList,SiteInfoDTO.class);

		return siteInfoEntityList;
	}



}
