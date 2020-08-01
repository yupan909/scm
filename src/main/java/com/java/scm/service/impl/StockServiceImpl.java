package com.java.scm.service.impl;

import com.github.pagehelper.Page;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.Stock;
import com.java.scm.bean.StockRecord;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.bean.so.StockRecordSO;
import com.java.scm.bean.so.StockSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.StockDao;
import com.java.scm.dao.StockRecordDao;
import com.java.scm.enums.StockRecordTypeEnum;
import com.java.scm.service.StockService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult initStock(Stock stock) {
        AssertUtils.notNull(stock, "初始化库存不能为空");
        AssertUtils.notNull(stock.getWarehouseId(), "仓库id不能为空");
        AssertUtils.notNull(stock.getProduct(), "物资名称不能为空");
        AssertUtils.notNull(stock.getModel(), "物资型号不能为空");
        AssertUtils.notNull(stock.getUnit(), "单位不能为空");
        AssertUtils.notNull(stock.getCount(), "数量不能为空");

        User user = RequestUtil.getCurrentUser();
        if(!stockCheck(null, stock.getWarehouseId(), stock.getProduct(), stock.getModel(), stock.getUnit())){
            return new BaseResult(false,"物资已存在，无法重复添加！");
        }
        stock.setCreateUserId(user.getId());
        stockDao.insertSelective(stock);

        StockRecord stockRecord = new StockRecord();
        stockRecord.setStockId(stock.getId());
        stockRecord.setCount(stock.getCount());
        stockRecord.setType(StockRecordTypeEnum.手动修改.getType());
        stockRecord.setCreateUserId(user.getId());
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
        AssertUtils.notNull(stock.getUnit(), "单位不能为空");

        if(!stockCheck(stock.getId(),stock.getWarehouseId(),stock.getProduct(),stock.getModel(), stock.getUnit())){
            return new BaseResult(false,"无法修改成已存在的物资！");
        }
        User user = RequestUtil.getCurrentUser();
        stock.setUpdateUserId(user.getId());
        stockDao.updateByPrimaryKeySelective(stock);
        return new BaseResult(true,"物资信息修改成功！");
    }

    /**
     * 修改库存数量
     * @param stock
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult modifyStockCount(Stock stock) {
        AssertUtils.notNull(stock, "修改库存参数不能为空");
        AssertUtils.notNull(stock.getId(), "库存id不能为空");
        AssertUtils.notNull(stock.getCount(), "库存数量不能为空");

        String id  = stock.getId();
        Stock oldStock = stockDao.selectByPrimaryKey(id);

        User user = RequestUtil.getCurrentUser();
        stock.setUpdateUserId(user.getId());
        stockDao.updateByPrimaryKeySelective(stock);

        Integer oldStockCount = oldStock.getCount();
        Integer newCount = stock.getCount();
        Integer diff = newCount - oldStockCount;
        StockRecord stockRecord = new StockRecord();
        stockRecord.setStockId(stock.getId());
        stockRecord.setCount(diff);
        stockRecord.setType(StockRecordTypeEnum.手动修改.getType());
        stockRecord.setCreateUserId(user.getId());
        stockRecordDao.insertSelective(stockRecord);
        return new BaseResult(true,"库存数量更新成功！");
    }

    /**
     * 库存列表
     * @return
     */
    @Override
    public BaseResult listStock(StockSO stockSO) {
        // 获取当前登录人仓库
        stockSO.setWarehouseId(RequestUtil.getWarehouseId());
        Page<Stock> stockList = stockDao.listStock(stockSO);
        return new BaseResult(stockList.toPageInfo().getList(), stockList.getTotal());
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
    public BaseResult getChangeDetail(StockRecordSO stockRecordSO) {
        AssertUtils.notNull(stockRecordSO, "库存明细参数不能为空");
        AssertUtils.notNull(stockRecordSO.getStockId(), "库存id不能为空");

        Page<StockRecord> stockRecordPage = stockRecordDao.listStockRecord(stockRecordSO);
        // 变更类型
        stockRecordPage.toPageInfo().getList().forEach(p -> {
            p.setTypeInfo(StockRecordTypeEnum.getEnumByValue(p.getType()));
        });
        return new BaseResult(stockRecordPage.toPageInfo().getList(), stockRecordPage.getTotal());
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
    private boolean stockCheck(String id, String warehouseId, String product, String model, String unit){
        Example example = new Example(Stock.class);
        Example.Criteria criteria =  example.createCriteria();
        if(id !=null){
            criteria.andNotEqualTo("id",id);
        }
        criteria.andEqualTo("warehouseId",warehouseId);
        criteria.andEqualTo("product",product);
        criteria.andEqualTo("model",model);
        criteria.andEqualTo("unit",unit);
        int count = stockDao.selectCountByExample(example);
        if(count >0){
            return false;
        }else{
            return true;
        }

    }
}
