package com.leisure.myproject.dao;

import com.leisure.myproject.entity.SiteInfoEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
* @author gonglei
* @date 2020/2/5 15:24
*/
@Repository
public interface SiteManageDAO extends Mapper<SiteInfoEntity> {
}
