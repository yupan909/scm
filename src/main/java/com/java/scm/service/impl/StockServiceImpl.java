package com.java.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.Stock;
import com.java.scm.bean.StockRecord;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.dao.StockDao;
import com.java.scm.dao.StockRecordDao;
import com.java.scm.enums.StateEnum;
import com.java.scm.enums.StockRecordTypeEnum;
import com.java.scm.service.StockService;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hujunhui
 * @date 2020/6/28
 */
@Slf4j
@Service
public class StockServiceImpl implements StockService {

    private static Byte TYPE = (byte) 2;

    private static Byte ADMIN = (byte) 1 ;

    @Resource
    private StockDao stockDao;

    @Resource
    private StockRecordDao stockRecordDao;

    @Transactional
    @Override
    public BaseResult initStock(Stock stock) {
        if(!stockCheck(null,stock.getWarehouseId(),stock.getProduct(),stock.getModel())){
            return new BaseResult(false,"物资已存在，无法重复添加！");
        }
        User user = RequestUtil.getCurrentUser();
        stock.setCreateTime(new Date());
        stock.setUpdateTime(new Date());
        stockDao.insert(stock);
        Long stockId = stock.getId();
        StockRecord stockRecord = new StockRecord();
        stockRecord.setCreateUser(user.getName());
        stockRecord.setStockId(stockId);
        stockRecord.setCount(stock.getCount());
        stockRecord.setType(TYPE);
        stockRecord.setCreateTime(new Date());
        stockRecordDao.insert(stockRecord);
        return new BaseResult(true,"库存初始化成功！");
    }

    @Override
    public BaseResult modifyStockInfo(Stock stock) {
        if(!stockCheck(stock.getId(),stock.getWarehouseId(),stock.getProduct(),stock.getModel())){
            return new BaseResult(false,"无法修改成已存在的物资！");
        }
        stock.setCount(null);
        stock.setUpdateTime(new Date());
        stockDao.updateByPrimaryKeySelective(stock);
        return new BaseResult(true,"物资信息就该成功！");
    }

    @Transactional
    @Override
    public BaseResult modifyStockCount(Stock stock) {
        User user = RequestUtil.getCurrentUser();
        Long id  = stock.getId();
        Stock oldStock = stockDao.selectByPrimaryKey(id);
        Integer oldStockCount = oldStock.getCount();
        Integer newCount = stock.getCount();
        Integer diff = newCount - oldStockCount;
        StockRecord stockRecord = new StockRecord();
        stockRecord.setCreateUser(user.getName());
        stockRecord.setStockId(id);
        stockRecord.setCount(diff);
        stockRecord.setType(TYPE);
        stockRecord.setCreateTime(new Date());
        stockDao.updateByPrimaryKeySelective(stock);
        stockRecordDao.insert(stockRecord);
        return new BaseResult(true,"库存数量更新成功！");
    }

    @Override
    public BaseResult listStock(String key, int pageNum, int pageSize) {
        User user = RequestUtil.getCurrentUser();
        List<Map> data ;
        PageHelper.startPage(pageNum,pageSize);
        if(ADMIN.equals(user.getAdmin())){
            data = stockDao.getStockInfosForAdmin(key);
        }else{
            data = stockDao.getStockInfos(user.getWarehouseId(),key);
        }
        PageInfo page = new PageInfo<>(data);
        return new BaseResult(data,page.getTotal());
    }

    @Override
    public BaseResult deleteStock(Long id) {
        return new BaseResult(true,"暂不支持删除库存");
    }

    @Override
    public BaseResult getStockById(Long id) {
        Stock stock =  stockDao.selectByPrimaryKey(id);
        return new BaseResult(stock);
    }

    @Override
    public BaseResult getChangeDetail(Long id, String startDate, String endDate, int pageNum, int pageSize) {
        Example example = new Example(StockRecord.class);
        Example.Criteria criteria =  example.createCriteria();
        criteria.andEqualTo("stockId",id);
        if(StringUtil.isEmpty(startDate)){
            startDate = "2000-01-01 00:00:00";
        }
        if(StringUtil.isEmpty(endDate)){
            endDate = "2100-01-01 00:00:00";
        }
        criteria.andBetween("createTime",startDate,endDate);
        PageHelper.startPage(pageNum,pageSize);
        List<StockRecord> data = stockRecordDao.selectByExample(example);
        PageInfo<StockRecord> pageInfo = new PageInfo<>(data);
        for (StockRecord one : data){
            if(one.getProject() == null){
                one.setProject("");
                one.setTypeInfo(StockRecordTypeEnum.getEnumByValue(one.getType()));
            }
        }
        return new BaseResult(data,pageInfo.getTotal());
    }

    /**
     * 校验库存唯一
     * @param id
     * @param warehouseId
     * @param product
     * @param model
     * @return
     */
    private boolean stockCheck(Long id,Integer warehouseId,String product ,String model){
        Example example = new Example(Stock.class);
        Example.Criteria criteria =  example.createCriteria();
        if(id !=null){
            criteria.andNotEqualTo("id",id);
        }
        criteria.andEqualTo("warehouseId",warehouseId);
        criteria.andEqualTo("product",product);
        criteria.andEqualTo("model",model);
        int count = stockDao.selectCountByExample(example);
        if(count >0){
            return false;
        }else{
            return true;
        }

    }
}
