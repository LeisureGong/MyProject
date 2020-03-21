package com.leisure.myproject.service;


import com.leisure.myproject.dto.SiteInfoDTO;
import com.leisure.myproject.dto.SiteQueryReqDTO;
import com.leisure.myproject.entity.SiteInfoEntity;

import java.util.List;

public interface SiteManageService {

	List<SiteInfoEntity> querySite(SiteQueryReqDTO siteQueryReqDTO);

}
