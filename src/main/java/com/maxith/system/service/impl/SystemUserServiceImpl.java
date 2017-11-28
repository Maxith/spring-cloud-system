package com.maxith.system.service.impl;


import com.maxith.common.entity.BaseComponent;
import com.maxith.system.entity.SystemUser;
import com.maxith.system.mapper.SystemUserMapper;
import com.maxith.system.service.ISystemUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户service接口实现类
 * Created by zhouyou on 2017/7/4.
 */
@Service
public class SystemUserServiceImpl extends BaseComponent implements ISystemUserService {

    @Resource
    private SystemUserMapper systemUserMapper;

    @Override
    public SystemUser selectSystemUserByUsername(String username) {
        List<SystemUser> list = systemUserMapper.selectSystemUserByUsername(username);
        if (!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public SystemUser selectUserByUsernameAndPassword(String username, String password) {
        List<SystemUser> list = systemUserMapper.selectUserByUsernameAndPassword(username,password);
        if (!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateLoginTime(String gid) {
        systemUserMapper.updateLoginTime(gid);
    }

}
