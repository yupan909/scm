package com.java.scm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.*;
import com.java.scm.bean.excel.StockImportTemplate;
import com.java.scm.bean.so.StockRecordSO;
import com.java.scm.bean.so.StockSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.StockMapper;
import com.java.scm.dao.StockRecordMapper;
import com.java.scm.enums.StockRecordTypeEnum;
import com.java.scm.service.StockService;
import com.java.scm.service.WarehouseService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hujunhui
 * @date 2020/6/28
 */
@Slf4j
@Service
public class StockServiceImpl implements StockService {

    @Resource
    private StockMapper stockMapper;

    @Resource
    private StockRecordMapper stockRecordMapper;

    @Resource
    private WarehouseService warehouseService;

    /**
     * 初始化库存
     * @param stock
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initStock(Stock stock) {
        AssertUtils.notNull(stock, "初始化库存不能为空");
        AssertUtils.notNull(stock.getWarehouseId(), "仓库id不能为空");
        AssertUtils.notNull(stock.getProduct(), "物资名称不能为空");
        AssertUtils.notNull(stock.getModel(), "物资型号不能为空");
        AssertUtils.notNull(stock.getUnit(), "单位不能为空");
        AssertUtils.notNull(stock.getCount(), "数量不能为空");

        User user = RequestUtil.getCurrentUser();
        if(!stockCheck(null, stock.getWarehouseId(), stock.getProduct(), stock.getModel(), stock.getUnit())){
            throw new BusinessException("物资库存已存在，无法重复添加！");
        }
        stock.setCreateUserId(user.getId());
        stockMapper.insertSelective(stock);

        // 新增库存变更记录
        insertStockRecord(stock, user.getId());
    }

    /**
     * 修改库存物资
     * @param stock
     * @return
     */
    @Override
    public void modifyStockInfo(Stock stock) {
        AssertUtils.notNull(stock, "修改库存物资不能为空");
        AssertUtils.notNull(stock.getId(), "库存id不能为空");
        AssertUtils.notNull(stock.getWarehouseId(), "仓库id不能为空");
        AssertUtils.notNull(stock.getProduct(), "物资名称不能为空");
        AssertUtils.notNull(stock.getModel(), "物资型号不能为空");
        AssertUtils.notNull(stock.getUnit(), "单位不能为空");

        if(!stockCheck(stock.getId(),stock.getWarehouseId(),stock.getProduct(),stock.getModel(), stock.getUnit())){
            throw new BusinessException("无法修改成已存在的物资！");
        }
        User user = RequestUtil.getCurrentUser();
        stock.setUpdateUserId(user.getId());
        stockMapper.updateByPrimaryKeySelective(stock);
    }

    /**
     * 修改库存数量
     * @param stock
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyStockCount(Stock stock) {
        AssertUtils.notNull(stock, "修改库存参数不能为空");
        AssertUtils.notNull(stock.getId(), "库存id不能为空");
        AssertUtils.notNull(stock.getCount(), "库存数量不能为空");

        String id  = stock.getId();
        Stock oldStock = stockMapper.selectByPrimaryKey(id);

        User user = RequestUtil.getCurrentUser();
        stock.setUpdateUserId(user.getId());
        stockMapper.updateByPrimaryKeySelective(stock);

        // 新增库存变更记录
        stock.setCount(stock.getCount()-oldStock.getCount());
        insertStockRecord(stock, user.getId());
    }

    /**
     * 库存列表
     * @return
     */
    @Override
    public PageInfo<Stock> listStock(StockSO stockSO) {
        Page<Stock> stockList = stockMapper.listStock(stockSO);
        return stockList.toPageInfo();
    }

    @Override
    public void deleteStock(String id) {

    }

    @Override
    public Stock getStockById(String id) {
        AssertUtils.notNull(id, "库存id不能为空");
        return stockMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取库存变更明细
     * @return
     */
    @Override
    public PageInfo<StockRecord> getChangeDetail(StockRecordSO stockRecordSO) {
        AssertUtils.notNull(stockRecordSO, "库存明细参数不能为空");
        AssertUtils.notNull(stockRecordSO.getStockId(), "库存id不能为空");

        Page<StockRecord> stockRecordPage = stockRecordMapper.listStockRecord(stockRecordSO);
        // 变更类型
        if (!CollectionUtils.isEmpty(stockRecordPage.toPageInfo().getList())) {
            stockRecordPage.toPageInfo().getList().forEach(p -> {
                p.setTypeInfo(StockRecordTypeEnum.getEnumByValue(p.getType()));
            });

        }
        return stockRecordPage.toPageInfo();
    }

    /**
     * 批量变更库存
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeStock(List<InoutStock> inoutStockList) {
        if (CollectionUtils.isEmpty(inoutStockList)) {
            throw new BusinessException("变更库存参数不能为空");
        }
        inoutStockList.forEach(inoutStock -> {
            Stock oldStock = stockMapper.selectByPrimaryKey(inoutStock.getStockId());
            if (oldStock == null) {
                throw new BusinessException("库存不存在，请检查后再重新导入！");
            }
            // 新库存数量
            Stock newStock = new Stock();
            newStock.setId(oldStock.getId());
            newStock.setCount(oldStock.getCount() + inoutStock.getCount());
            newStock.setUpdateUserId(inoutStock.getCreateUserId());
            stockMapper.updateByPrimaryKeySelective(newStock);

            // 库存记录
            StockRecord stockRecord = new StockRecord();
            stockRecord.setStockId(inoutStock.getStockId());
            stockRecord.setInoutStockId(inoutStock.getId());
            stockRecord.setCount(inoutStock.getCount());
            stockRecord.setType(inoutStock.getType());
            stockRecord.setCreateUserId(inoutStock.getCreateUserId());
            stockRecordMapper.insertSelective(stockRecord);
        });
    }

    /**
     * 按条件查询库存Id
     * @return
     */
    @Override
    public String getStockBySelective(Stock stock) {
        Example example = new Example(Stock.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseId", stock.getWarehouseId());
        criteria.andEqualTo("product", stock.getProduct());
        criteria.andEqualTo("model", stock.getModel());
        criteria.andEqualTo("unit", stock.getUnit());
        List<Stock> stockList = stockMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(stockList)) {
            return null;
        }
        return stockList.get(0).getId();
    }

    /**
     * 导入库存
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importStock(List<StockImportTemplate> importList) {
        if (CollectionUtils.isEmpty(importList)) {
            throw new BusinessException("导入excel数据为空！");
        }

        // 1、校验数据
        importList.forEach(p -> {
            // 去空格
            p.setProduct(StringUtil.trim(p.getProduct()));
            p.setModel(StringUtil.trim(p.getModel()));
            p.setUnit(StringUtil.trim(p.getUnit()));
            p.setCount(StringUtil.trim(p.getCount()));
            p.setWarehouseName(StringUtil.trim(p.getWarehouseName()));

            // 校验
            AssertUtils.notEmpty(p.getProduct(), "物资名称不能为空");
            AssertUtils.maxlength(p.getProduct(), 50, "物资名称长度不能超过50");
            AssertUtils.notEmpty(p.getModel(), "物资型号不能为空！");
            AssertUtils.maxlength(p.getModel(), 50, "物资型号长度不能超过50");
            AssertUtils.notEmpty(p.getUnit(), "单位不能为空！");
            AssertUtils.maxlength(p.getUnit(), 50, "单位长度不能超过50");
            AssertUtils.notEmpty(p.getCount(), "库存数量不能为空");
            AssertUtils.isInteger(p.getCount(), "库存数量格式有问题，必须是数字");
            AssertUtils.maxlength(p.getCount(), 11, "库存数量长度不能超过11");
            AssertUtils.notEmpty(p.getWarehouseName(), "所属仓库不能为空");
            AssertUtils.maxlength(p.getWarehouseName(), 50, "所属仓库长度不能超过50");
        });

        // 获取所有仓库
        List<Warehouse> warehouseList = warehouseService.getAllWarehouse();

        // 2、保存库存
        User user = RequestUtil.getCurrentUser();
        for (StockImportTemplate template : importList) {
            // 判断仓库是否存在
            Optional<Warehouse> warehouseOptional = warehouseList.stream().filter(p -> Objects.equals(p.getName(), template.getWarehouseName())).findFirst();
            if (!warehouseOptional.isPresent()) {
                throw new BusinessException(String.format("导入失败，当前仓库不存在：[%s]", template.getWarehouseName()));
            }

            // 判断库存是否重复
            if(!stockCheck(null, warehouseOptional.get().getId(), template.getProduct(), template.getModel(), template.getUnit())){
                throw new BusinessException(String.format("导入失败，当前库存已存在：[物资名称：%s，物资型号：%s，单位：%s，所属仓库：%s]", template.getProduct(), template.getModel(), template.getUnit(), template.getWarehouseName()));
            }

            // 新增库存
            Stock stock = new Stock();
            BeanUtils.copyProperties(template, stock);
            stock.setCount(Integer.valueOf(template.getCount()));
            stock.setWarehouseId(warehouseOptional.get().getId());
            stock.setCreateUserId(user.getId());
            stockMapper.insertSelective(stock);

            // 新增库存变更记录
            insertStockRecord(stock, user.getId());
        }
    }

    /**
     * 新增库存变更记录
     */
    private void insertStockRecord(Stock stock, String userId) {
        StockRecord stockRecord = new StockRecord();
        stockRecord.setStockId(stock.getId());
        stockRecord.setCount(stock.getCount());
        stockRecord.setType(StockRecordTypeEnum.手动修改.getType());
        stockRecord.setCreateUserId(userId);
        stockRecordMapper.insertSelective(stockRecord);
    }

    /**
     * 校验库存唯一
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
        int count = stockMapper.selectCountByExample(example);
        if(count >0){
            return false;
        }else{
            return true;
        }

    }
}
