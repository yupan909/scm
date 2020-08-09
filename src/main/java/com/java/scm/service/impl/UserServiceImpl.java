package com.java.scm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.User;
import com.java.scm.bean.so.UserSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.UserMapper;
import com.java.scm.enums.CommonConsts;
import com.java.scm.enums.RoleEnum;
import com.java.scm.enums.StateEnum;
import com.java.scm.service.UserService;
import com.java.scm.service.WarehouseService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户相关服务
 * @author hujunhui
 * @date 2020/6/24
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private WarehouseService warehouseService;

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    @Override
    public User login(String userName, String password) {
        AssertUtils.notEmpty(userName, "用户名不能为空");
        AssertUtils.notEmpty(password, "密码不能为空");
        User query = new User();
        query.setName(userName);
        query.setPassword(password);
        List<User> users = userMapper.select(query);
        if (CollectionUtils.isEmpty(users)) {
            throw new BusinessException("账号或密码错误");
        }
        User user = users.get(0);
        if(Objects.equals(user.getState(), StateEnum.禁用.getType())){
            throw new BusinessException("账号已被停用");
        }
        Map<String, String> warehouseMap = warehouseService.getWarehouseMap(Arrays.asList(user.getWarehouseId()));
        user.setWarehouseName(StringUtil.isNotEmpty(warehouseMap.get(user.getWarehouseId())) ? warehouseMap.get(user.getWarehouseId()) : "");
        RequestUtil.setLoginUser(user);
        return user;
    }

    /**
     * 注销
     * @return
     */
    @Override
    public void logout() {
        RequestUtil.removeLoginUser();
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @Override
    public void addUser(User user) {
        AssertUtils.notNull(user, "用户信息不能为空");
        AssertUtils.notEmpty(user.getName(), "用户名不能为空");

        User queryName = new User();
        queryName.setName(user.getName());
        int nameCount = userMapper.selectCount(queryName);
        if(nameCount > 0){
            throw new BusinessException("用户名已存在！（若用户存在重名情况，请使用姓名+编号进行区分）");
        }
        user.setPassword(CommonConsts.RESET_PASSWORD);
        user.setCreateUserId(RequestUtil.getCurrentUser().getId());
        userMapper.insertSelective(user);
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @Override
    public void modifyUser(User user) {
        AssertUtils.notNull(user, "用户信息不能为空");
        AssertUtils.notNull(user.getId(), "用户ID不能为空");
        AssertUtils.notEmpty(user.getName(), "用户名不能为空");

        Example example = new Example(User.class);
        Example.Criteria criteria =  example.createCriteria();
        criteria.andNotEqualTo("id",user.getId());
        criteria.andEqualTo("name",user.getName());
        int nameCount = userMapper.selectCountByExample(example);
        if(nameCount >0){
            throw new BusinessException("用户姓名："+user.getName()+"已存在，请修改成其他姓名");
        }
        user.setUpdateUserId(RequestUtil.getCurrentUser().getId());
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 用户状态启用/停用
     * @param id
     * @return
     */
    @Override
    public void stopUsing(String id) {
        AssertUtils.notNull(id, "用户ID不能为空");
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new BusinessException("用户信息不存在");
        }
        String msg;
        if(Objects.equals(user.getState(), StateEnum.禁用.getType())){
            user.setState(StateEnum.启用.getType());
        }else{
            user.setState(StateEnum.禁用.getType());
        }
        user.setUpdateUserId(RequestUtil.getCurrentUser().getId());
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public void deleteUser(String id) {
        AssertUtils.notNull(id, "用户ID不能为空");
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改密码
     * @return
     */
    @Override
    public void updatePassword(String id, String password) {
        AssertUtils.notNull(id, "用户ID不能为空");
        AssertUtils.notEmpty(password, "密码不能为空");
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setUpdateUserId(RequestUtil.getCurrentUser().getId());
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 用户列表
     * @return
     */
    @Override
    public PageInfo<User> list(UserSO userSO) {
        Page<User> users  = userMapper.listUser(userSO);
        for (User user : users){
            user.setStateInfo(StateEnum.getEnumByValue(user.getState()));
            user.setRoleInfo(RoleEnum.getEnumByValue(user.getRole()));
        }
        return users.toPageInfo();
    }

    /**
     * 获取用户详情
     * @param id
     * @return
     */
    @Override
    public User getUserById(String id) {
        AssertUtils.notNull(id, "用户ID不能为空");
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

}
