package com.java.scm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.Stock;
import com.java.scm.bean.StockRecord;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.StockDao;
import com.java.scm.dao.StockRecordDao;
import com.java.scm.enums.AdminEnum;
import com.java.scm.enums.StockRecordTypeEnum;
import com.java.scm.service.StockService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hujunhui
 * @date 2020/6/28
 */
@Slf4j
@Service
public class StockServiceImpl implements StockService {

    @Resource
    private StockDao stockDao;

    @Resource
    private StockRecordDao stockRecordDao;

    /**
     * 初始化库存
     * @param stock
     * @return
     */
    @Transactional
    @Override
    public BaseResult initStock(Stock stock) {
        AssertUtils.notNull(stock, "初始化库存不能为空");
        AssertUtils.notNull(stock.getWarehouseId(), "仓库id不能为空");
        AssertUtils.notNull(stock.getProduct(), "物资名称不能为空");
        AssertUtils.notNull(stock.getModel(), "物资型号不能为空");
        AssertUtils.notNull(stock.getCount(), "数量不能为空");

        if(!stockCheck(null, stock.getWarehouseId(), stock.getProduct(), stock.getModel())){
            return new BaseResult(false,"物资已存在，无法重复添加！");
        }
        stockDao.insertSelective(stock);

        User user = RequestUtil.getCurrentUser();
        StockRecord stockRecord = new StockRecord();
        stockRecord.setCreateUser(user.getName());
        stockRecord.setStockId(stock.getId());
        stockRecord.setCount(stock.getCount());
        stockRecord.setType(StockRecordTypeEnum.手动修改.getType());
        stockRecordDao.insertSelective(stockRecord);
        return new BaseResult(true,"库存初始化成功！");
    }

    /**
     * 修改库存物资
     * @param stock
     * @return
     */
    @Override
    public BaseResult modifyStockInfo(Stock stock) {
        AssertUtils.notNull(stock, "修改库存物资不能为空");
        AssertUtils.notNull(stock.getId(), "库存id不能为空");
        AssertUtils.notNull(stock.getWarehouseId(), "仓库id不能为空");
        AssertUtils.notNull(stock.getProduct(), "物资名称不能为空");
        AssertUtils.notNull(stock.getModel(), "物资型号不能为空");

        if(!stockCheck(stock.getId(),stock.getWarehouseId(),stock.getProduct(),stock.getModel())){
            return new BaseResult(false,"无法修改成已存在的物资！");
        }
        stockDao.updateByPrimaryKeySelective(stock);
        return new BaseResult(true,"物资信息就该成功！");
    }

    /**
     * 修改库存数量
     * @param stock
     * @return
     */
    @Transactional
    @Override
    public BaseResult modifyStockCount(Stock stock) {
        AssertUtils.notNull(stock, "修改库存参数不能为空");
        AssertUtils.notNull(stock.getId(), "库存id不能为空");
        AssertUtils.notNull(stock.getCount(), "库存数量不能为空");

        String id  = stock.getId();
        Stock oldStock = stockDao.selectByPrimaryKey(id);

        User user = RequestUtil.getCurrentUser();
        stockDao.updateByPrimaryKeySelective(stock);

        Integer oldStockCount = oldStock.getCount();
        Integer newCount = stock.getCount();
        Integer diff = newCount - oldStockCount;
        StockRecord stockRecord = new StockRecord();
        stockRecord.setCreateUser(user.getName());
        stockRecord.setStockId(stock.getId());
        stockRecord.setCount(diff);
        stockRecord.setType(StockRecordTypeEnum.手动修改.getType());
        stockRecordDao.insertSelective(stockRecord);
        return new BaseResult(true,"库存数量更新成功！");
    }

    /**
     * 库存列表
     * @param key
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResult listStock(String key, int pageNum, int pageSize) {
        User user = RequestUtil.getCurrentUser();
        List<Map> data ;
        PageHelper.startPage(pageNum,pageSize);
        if(Objects.equals(user.getAdmin(), AdminEnum.管理员.getType())){
            data = stockDao.getStockInfosForAdmin(key);
        }else{
            data = stockDao.getStockInfos(user.getWarehouseId(),key);
        }
        PageInfo page = new PageInfo<>(data);
        return new BaseResult(data,page.getTotal());
    }

    @Override
    public BaseResult deleteStock(String id) {
        return new BaseResult(true,"暂不支持删除库存");
    }

    @Override
    public BaseResult getStockById(String id) {
        Stock stock =  stockDao.selectByPrimaryKey(id);
        return new BaseResult(stock);
    }

    /**
     * 获取库存变更明细
     * @return
     */
    @Override
    public BaseResult getChangeDetail(String id, String startDate, String endDate, int pageNum, int pageSize) {
        AssertUtils.notNull(id, "库存id不能为空");
        Example example = new Example(StockRecord.class);
        example.setOrderByClause(" create_time DESC ");
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
            one.setTypeInfo(StockRecordTypeEnum.getEnumByValue(one.getType()));
        }
        return new BaseResult(data,pageInfo.getTotal());
    }

    /**
     * 批量变更库存
     */
    @Override
    public void insertStock(List<InoutStock> inoutStockList) {
        if (CollectionUtils.isEmpty(inoutStockList)) {
            throw new BusinessException("变更库存参数不能为空");
        }
        inoutStockList.forEach(inoutStock -> {
            Stock oldStock = getStockBySelective(inoutStock.getWarehouseId(), inoutStock.getProduct(), inoutStock.getModel());
            if (oldStock == null) {
                throw new BusinessException(String.format("%s（%s）没有库存，请检查后再重新导入！", inoutStock.getProduct(), inoutStock.getModel()));
            }
            // 新库存数量
            Stock newStock = new Stock();
            newStock.setId(oldStock.getId());
            newStock.setCount(oldStock.getCount() + inoutStock.getCount());
            stockDao.updateByPrimaryKeySelective(newStock);

            // 库存记录
            StockRecord stockRecord = new StockRecord();
            stockRecord.setStockId(oldStock.getId());
            stockRecord.setProject(inoutStock.getProject());
            stockRecord.setCount(inoutStock.getCount());
            stockRecord.setType(inoutStock.getType());
            stockRecord.setInoutStockId(inoutStock.getId());
            stockRecord.setCreateUser(inoutStock.getCreateUser());
            stockRecordDao.insertSelective(stockRecord);
        });
    }

    /**
     * 按条件查询库存
     *
     * @return
     */
    private Stock getStockBySelective(String warehouseId, String product, String model) {
        Example example = new Example(Stock.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseId", warehouseId);
        criteria.andEqualTo("product", product);
        criteria.andEqualTo("model", model);
        List<Stock> stockList = stockDao.selectByExample(example);
        if (CollectionUtils.isEmpty(stockList)) {
            return null;
        }
        return stockList.get(0);
    }

    /**
     * 校验库存唯一
     * @param id
     * @param warehouseId
     * @param product
     * @param model
     * @return
     */
    private boolean stockCheck(String id, String warehouseId, String product, String model){
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
