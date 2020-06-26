package com.java.scm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.UserDao;
import com.java.scm.enums.AdminEnum;
import com.java.scm.enums.StateEnum;
import com.java.scm.service.UserService;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hujunhui
 * @date 2020/6/24
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final Byte STOP_USING = (byte) 1;

    @Resource
    private UserDao userDao;

    @Override
    public BaseResult login(String mobile, String password) {
        if(StringUtil.isNotEmpty(mobile) && StringUtil.isNotEmpty(password)){
            User query = new User();
            query.setMobile(mobile);
            query.setPassword(password);
            List<User> users = userDao.select(query);
            if(users !=null && !users.isEmpty()){
                User user = users.get(0);
                if(STOP_USING.equals(user.getState())){
                    throw  new BusinessException("账号已被停用");
                }else{
                    RequestUtil.setLoginUser(user);
                    BaseResult result = new BaseResult(user);
                    result.setMessage("登录成功");
                    return result;
                }
            }else{
                throw  new BusinessException("账号或密码错误");
            }
        }else{
            throw  new BusinessException("请输入登录账号和密码");
        }
    }

    @Override
    public BaseResult logout() {
        RequestUtil.removeLoginUser();
        return new BaseResult(true,"退出登录");
    }

    @Override
    public BaseResult addUser(User user) {
        User queryMobile = new User();
        queryMobile.setMobile(user.getMobile());
        int mobileCount = userDao.selectCount(queryMobile);
        if(mobileCount > 0){
            throw new BusinessException("电话号码已存在，电话号码作为用户登陆账号，不可重复");
        }
        User queryName = new User();
        queryName.setName(user.getName());
        int nameCount = userDao.selectCount(queryName);
        if(nameCount >0){
            throw new BusinessException("用户姓名已存在，若用户存在重名情况，请使用姓名+编号进行区分");
        }
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setState((byte)0);
        userDao.insert(user);
        return new BaseResult(true,"新增成功");
    }

    @Override
    public BaseResult modifyUser(User user) {
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

    @Override
    public BaseResult stopUsing(Integer id) {
        User user = userDao.selectByPrimaryKey(id);
        String msg ;
        if(STOP_USING.equals(user.getState())){
            user.setState((byte)0);
            msg = "账号已启用";
        }else{
            user.setState(STOP_USING);
            msg = "账号已停用";
        }
        userDao.updateByPrimaryKey(user);
        return new BaseResult(true,msg);
    }

    @Override
    public BaseResult deleteUser(Integer id) {
        userDao.deleteByPrimaryKey(id);
        return new BaseResult(true,"账号已删除");
    }

    @Override
    public BaseResult resetPassword(Integer id, String password) {
        User query = new User();
        query.setId(id);
        query.setPassword(password);
        userDao.updateByPrimaryKeySelective(query);
        return new BaseResult(true,"账号密码已被重置");
    }

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

    @Override
    public BaseResult getUserById(Long id) {
        User user = userDao.selectByPrimaryKey(id);
        return  new BaseResult(user);
    }

}
