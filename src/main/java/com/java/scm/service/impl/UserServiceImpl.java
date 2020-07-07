package com.java.scm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.UserDao;
import com.java.scm.enums.AdminEnum;
import com.java.scm.enums.CommonConsts;
import com.java.scm.enums.StateEnum;
import com.java.scm.service.UserService;
import com.java.scm.service.WarehouseService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户相关服务
 * @author hujunhui
 * @date 2020/6/24
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private WarehouseService warehouseService;

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    @Override
    public BaseResult login(String userName, String password) {
        AssertUtils.notEmpty(userName, "用户名不能为空");
        AssertUtils.notEmpty(password, "密码不能为空");
        User query = new User();
        query.setName(userName);
        query.setPassword(password);
        List<User> users = userDao.select(query);
        if(users !=null && !users.isEmpty()){
            User user = users.get(0);
            if(Objects.equals(user.getState(), StateEnum.禁用.getType())){
                throw new BusinessException("账号已被停用");
            }else{
                Map<String, String> warehouseMap = warehouseService.getWarehouseMap(Arrays.asList(user.getWarehouseId()));
                user.setWarehouseName(StringUtil.isNotEmpty(warehouseMap.get(user.getWarehouseId())) ? warehouseMap.get(user.getWarehouseId()) : "");
                RequestUtil.setLoginUser(user);
                BaseResult result = new BaseResult(user);
                result.setMessage("登录成功");
                return result;
            }
        }else{
            throw new BusinessException("账号或密码错误");
        }

    }

    /**
     * 注销
     * @return
     */
    @Override
    public BaseResult logout() {
        RequestUtil.removeLoginUser();
        return new BaseResult(true,"退出登录");
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @Override
    public BaseResult addUser(User user) {
        AssertUtils.notNull(user, "用户信息不能为空");
        AssertUtils.notEmpty(user.getName(), "用户名不能为空");

        User queryName = new User();
        queryName.setName(user.getName());
        int nameCount = userDao.selectCount(queryName);
        if(nameCount > 0){
            throw new BusinessException("用户名已存在！（若用户存在重名情况，请使用姓名+编号进行区分）");
        }
        user.setPassword(CommonConsts.RESET_PASSWORD);
        userDao.insertSelective(user);
        return new BaseResult(true,"新增成功");
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @Override
    public BaseResult modifyUser(User user) {
        AssertUtils.notNull(user, "用户信息不能为空");
        AssertUtils.notNull(user.getId(), "用户ID不能为空");

        Example example = new Example(User.class);
        Example.Criteria criteria =  example.createCriteria();
        criteria.andNotEqualTo("id",user.getId());
        criteria.andEqualTo("name",user.getName());
        int nameCount = userDao.selectCountByExample(example);
        if(nameCount >0){
            throw new BusinessException("用户姓名："+user.getName()+"已存在，请修改成其他姓名");
        }

        Example mobileExample = new Example(User.class);
        Example.Criteria mobileCriteria =  mobileExample.createCriteria();
        mobileCriteria.andNotEqualTo("id",user.getId());
        mobileCriteria.andEqualTo("mobile",user.getMobile());
        int mobileCount = userDao.selectCountByExample(mobileExample);
        if(mobileCount >0){
            throw new BusinessException("电话号码："+user.getMobile()+"已存在，无法使用该电话号码");
        }
        userDao.updateByPrimaryKeySelective(user);
        return new BaseResult(true,"修改成功");
    }

    /**
     * 用户状态启用/停用
     * @param id
     * @return
     */
    @Override
    public BaseResult stopUsing(String id) {
        AssertUtils.notNull(id, "用户ID不能为空");
        User user = userDao.selectByPrimaryKey(id);
        if (user == null) {
            throw new BusinessException("用户信息不存在");
        }
        String msg;
        if(Objects.equals(user.getState(), StateEnum.禁用.getType())){
            user.setState(StateEnum.启用.getType());
            msg = "账号已启用";
        }else{
            user.setState(StateEnum.禁用.getType());
            msg = "账号已停用";
        }
        user.setUpdateTime(new Date());
        userDao.updateByPrimaryKeySelective(user);
        return new BaseResult(true,msg);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public BaseResult deleteUser(String id) {
        AssertUtils.notNull(id, "用户ID不能为空");
        userDao.deleteByPrimaryKey(id);
        return new BaseResult(true,"账号已删除");
    }

    /**
     * 修改密码
     * @return
     */
    @Override
    public BaseResult updatePassword(String id, String password) {
        AssertUtils.notNull(id, "用户ID不能为空");
        AssertUtils.notEmpty(password, "密码不能为空");
        User query = new User();
        query.setId(id);
        query.setPassword(password);
        userDao.updateByPrimaryKeySelective(query);
        return BaseResult.successResult();
    }

    /**
     * 用户列表
     * @param key
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResult list(String key, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Map> users  = userDao.getUserInfos(key);
        for (Map one : users){
            one.put("adminInfo", AdminEnum.getEnumByValue( Byte.valueOf(one.get("admin").toString())));
            one.put("stateInfo", StateEnum.getEnumByValue( Byte.valueOf(one.get("state").toString())));
        }
        PageInfo<Map> pageInfo = new PageInfo(users);
        return new BaseResult(users,pageInfo.getTotal());
    }

    /**
     * 获取用户详情
     * @param id
     * @return
     */
    @Override
    public BaseResult getUserById(String id) {
        AssertUtils.notNull(id, "用户ID不能为空");
        User user = userDao.selectByPrimaryKey(id);
        return  new BaseResult(user);
    }

}
