package com.man.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.man.entity.Security.SysRule;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
* @author 艾莉希雅
* @description 针对表【sys_rule】的数据库操作Mapper
* @createDate 2023-09-11 16:14:09
* @Entity com.POJO.Security.SysRule
*/
@Mapper
public interface SysRuleMapper extends BaseMapper<SysRule> {

}




