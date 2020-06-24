package com.java.scm.service.impl;

import com.github.pagehelper.PageHelper;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.UserDao;
import com.java.scm.service.UserService;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hujunhui
 * @date 2020/6/24
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private static final String USER = "user";

    private static final Byte STOP_USING = '1';

    private static final String NAME = "name";

    private static final String MOBILE = "mobile";

    private static final String ADMIN = "admin";

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
                RequestUtil.getSession().setAttribute(USER,user);
                BaseResult result = new BaseResult(user);
                result.setMessage("登录成功");
                return result;
            }else{
                throw  new BusinessException("账号或密码错误");
            }
        }else{
            throw  new BusinessException("请输入登录账号和密码");
        }
    }

    @Override
    public BaseResult logout() {
        RequestUtil.getSession().removeAttribute(USER);
        return new BaseResult(true,"退出登录");
    }

    @Override
    public BaseResult addUser(User user) {
        userDao.insert(user);
        return new BaseResult(true,"新增成功");
    }

    @Override
    public BaseResult modifyUser(User user) {
        userDao.updateByPrimaryKeySelective(user);
        return new BaseResult(true,"修改成功");
    }

    @Override
    public BaseResult stopUsing(Integer id) {
        User query = new User();
        query.setId(id);
        query.setState(STOP_USING);
        userDao.updateByPrimaryKeySelective(query);
        return new BaseResult(true,"账号已停用");
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
    public BaseResult list(String name, String mobile, int pageNum, int pageSize) {
        Example example = new Example(User.class);
        example.setOrderByClause(" create_time DESC ");
        Example.Criteria criteria =  example.createCriteria();
        criteria.andLike(NAME,"%"+name+"%");
        criteria.andLike(MOBILE,"%"+mobile+"%");
        criteria.andNotEqualTo(MOBILE,ADMIN);
        int totalCount = userDao.selectCountByExample(example);
        PageHelper.startPage(pageNum,pageSize);
        List<User> users  = userDao.selectByExample(example);
        return new BaseResult(users,totalCount);
    }
}
