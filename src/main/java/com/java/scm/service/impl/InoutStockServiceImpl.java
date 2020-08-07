package com.java.scm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.Project;
import com.java.scm.bean.Stock;
import com.java.scm.bean.User;
import com.java.scm.bean.excel.InoutStockImportTemplate;
import com.java.scm.bean.so.InoutStockSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.InoutStockDao;
import com.java.scm.enums.InoutStockTypeEnum;
import com.java.scm.service.InoutStockService;
import com.java.scm.service.ProjectService;
import com.java.scm.service.StockService;
import com.java.scm.service.WarehouseService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 出入库
 *
 * @author yupan
 * @date 2020-06-24 12:05
 */
@Slf4j
@Service
public class InoutStockServiceImpl implements InoutStockService {

    @Autowired
    private InoutStockDao inoutStockDao;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ProjectService projectService;

    /**
     * 出入库列表
     * @return
     */
    @Override
    public PageInfo<InoutStock> listInoutStock(InoutStockSO inoutStockSO) {
        Page<InoutStock> inoutStockPage =  inoutStockDao.listInoutStock(inoutStockSO);
        PageInfo<InoutStock> pageInfo = inoutStockPage.toPageInfo();
        // 出入库类型
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            pageInfo.getList().forEach(p -> {
                p.setTypeText(InoutStockTypeEnum.getEnumByValue(p.getType()));
            });
        }
        return pageInfo;
    }

    /**
     * 导入出入库
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importInoutStock(List<InoutStockImportTemplate> importList, Byte inoutStockType) {
        if (CollectionUtils.isEmpty(importList)) {
            throw new BusinessException("导入excel数据为空！");
        }

        // 获取当前登陆用户
        User user = RequestUtil.getCurrentUser();
        if (user == null || user.getWarehouseId() == null) {
            throw new BusinessException("当前用户所属仓库为空，无法导入！");
        }

        // 1、校验数据
        importList.forEach(p -> {
            // 去空格
            p.setProject(StringUtil.trim(p.getProject()));
            p.setProduct(StringUtil.trim(p.getProduct()));
            p.setModel(StringUtil.trim(p.getModel()));
            p.setUnit(StringUtil.trim(p.getUnit()));
            p.setCount(StringUtil.trim(p.getCount()));
            p.setPrice(StringUtil.trim(p.getPrice()));
            p.setSource(StringUtil.trim(p.getSource()));
            p.setHandle(StringUtil.trim(p.getHandle()));
            p.setRemark(StringUtil.trim(p.getRemark()));

            // 校验
            AssertUtils.notEmpty(p.getProject(), "工程名称不能为空");
            AssertUtils.maxlength(p.getProject(), 50, "工程名称长度不能超过50");
            AssertUtils.notEmpty(p.getProduct(), "物资名称不能为空");
            AssertUtils.maxlength(p.getProduct(), 50, "物资名称长度不能超过50");
            AssertUtils.notEmpty(p.getModel(), "物资型号不能为空");
            AssertUtils.maxlength(p.getModel(), 50, "物资型号长度不能超过50");
            AssertUtils.notEmpty(p.getUnit(), "单位不能为空");
            AssertUtils.maxlength(p.getUnit(), 50, "单位长度不能超过50");
            AssertUtils.notEmpty(p.getCount(), "数量不能为空");
            AssertUtils.isInteger(p.getCount(), "数量格式有问题，必须是数字");
            AssertUtils.maxlength(p.getCount(), 11, "数量长度不能超过11");
            AssertUtils.notEmpty(p.getPrice(), "物资单价不能为空");
            AssertUtils.isBigDecimal(p.getPrice(), "物资单价格式有问题，必须是数字");
            AssertUtils.maxlength(p.getPrice(), 11, "物资单价长度不能超过11");
            AssertUtils.notEmpty(p.getSource(), "物资来源不能为空");
            AssertUtils.maxlength(p.getSource(), 50, "物资来源长度不能超过50");
            AssertUtils.notEmpty(p.getHandle(), "经手人不能为空");
            AssertUtils.maxlength(p.getHandle(), 50, "经手人长度不能超过50");
            AssertUtils.maxlength(p.getRemark(), 500, "备注长度不能超过50");
        });

        // 根据工程名称查询工程
        List<String> projectNameList = importList.stream().map(p -> StringUtil.trim(p.getProject())).distinct().collect(Collectors.toList());
        List<Project> projectList = projectService.getProjectByName(projectNameList);
        if (CollectionUtils.isEmpty(projectList)) {
            throw new BusinessException("导入失败，以下工程不存在：" + Arrays.toString(projectNameList.toArray()));
        }

        // 2、保存出入表
        List<InoutStock> inoutStockList = new ArrayList<>();
        for (InoutStockImportTemplate template : importList) {
            // 判断工程是否存在
            Optional<Project> projectOptional = projectList.stream().filter(p -> Objects.equals(p.getName(), template.getProject())).findFirst();
            if (!projectOptional.isPresent()) {
                throw new BusinessException(String.format("导入失败，当前工程不存在：[%s]", template.getProject()));
            }

            // 判断库存是否存在
            Stock stock = new Stock();
            stock.setWarehouseId(user.getWarehouseId());
            stock.setProduct(template.getProduct());
            stock.setModel(template.getModel());
            stock.setUnit(template.getUnit());
            String stockId = stockService.getStockBySelective(stock);
            if (stockId == null) {
                throw new BusinessException(String.format("导入失败，当前库存不存在：[物资名称：%s，物资型号：%s，单位：%s]", template.getProduct(), template.getModel(), template.getUnit()));
            }

            InoutStock inoutStock = new InoutStock();
            inoutStock.setType(inoutStockType);
            inoutStock.setWarehouseId(user.getWarehouseId());
            inoutStock.setProjectId(projectOptional.get().getId());
            inoutStock.setStockId(stockId);
            inoutStock.setCount(Integer.valueOf(template.getCount()));
            inoutStock.setPrice(new BigDecimal(template.getPrice()));
            inoutStock.setSource(template.getSource());
            inoutStock.setHandle(template.getHandle());
            inoutStock.setRemark(template.getRemark());
            inoutStock.setCreateUserId(user.getId());
            inoutStockDao.insertSelective(inoutStock);

            // 出库数量为负(计算库存)
            if (Objects.equals(inoutStockType, InoutStockTypeEnum.出库.getType())) {
                inoutStock.setCount(-1 * inoutStock.getCount());
            }
            inoutStockList.add(inoutStock);
        }

        // 3、变更库存
        stockService.changeStock(inoutStockList);
    }

    /**
     * 新增出入库
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveInoutStock(InoutStock inoutStock) {
        // 获取当前登陆用户
        User user = RequestUtil.getCurrentUser();
        if (user == null || user.getWarehouseId() == null) {
            throw new BusinessException("当前用户所属仓库为空，无法新增！");
        }

        // 校验
        AssertUtils.notNull(inoutStock, "新增出入库参数不能为空！");
        AssertUtils.notNull(inoutStock.getType(), "类别不能为空！");
        AssertUtils.notEmpty(inoutStock.getProject(), "工程名称不能为空！");
        AssertUtils.notEmpty(inoutStock.getProduct(), "物资名称不能为空！");
        AssertUtils.notEmpty(inoutStock.getModel(), "物资型号不能为空！");
        AssertUtils.notEmpty(inoutStock.getUnit(), "单位不能为空！");
        AssertUtils.notNull(inoutStock.getCount(), "数量不能为空！");
        AssertUtils.notNull(inoutStock.getPrice(), "物资单价不能为空！");
        AssertUtils.notEmpty(inoutStock.getSource(), "物资来源不能为空！");
        AssertUtils.notEmpty(inoutStock.getHandle(), "经手人不能为空！");

        // 根据工程名称查询工程
        List<Project> projectList = projectService.getProjectByName(Arrays.asList(inoutStock.getProject()));
        if (CollectionUtils.isEmpty(projectList)) {
            throw new BusinessException("新增失败，工程名称不存在！");
        }

        // 判断库存是否存在
        Stock stock = new Stock();
        stock.setWarehouseId(user.getWarehouseId());
        stock.setProduct(inoutStock.getProduct());
        stock.setModel(inoutStock.getModel());
        stock.setUnit(inoutStock.getUnit());
        String stockId = stockService.getStockBySelective(stock);
        if (stockId == null) {
            throw new BusinessException("新增失败，库存不存在！");
        }

        // (1)新增出入库
        inoutStock.setProjectId(projectList.get(0).getId());
        inoutStock.setStockId(stockId);
        inoutStock.setWarehouseId(user.getWarehouseId());
        inoutStock.setCreateUserId(user.getId());
        inoutStockDao.insertSelective(inoutStock);

        // (2)变更库存
        // 出库数量为负(计算库存)
        if (Objects.equals(inoutStock.getType(), InoutStockTypeEnum.出库.getType())) {
            inoutStock.setCount(-1 * inoutStock.getCount());
        }
        List<InoutStock> inoutStockList = new ArrayList<>();
        inoutStockList.add(inoutStock);
        stockService.changeStock(inoutStockList);
    }


}
