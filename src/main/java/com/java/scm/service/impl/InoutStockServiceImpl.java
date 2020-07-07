package com.java.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.User;
import com.java.scm.bean.excel.InoutStockTemplate;
import com.java.scm.bean.so.InoutStockSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.InoutStockDao;
import com.java.scm.enums.InoutStockTypeEnum;
import com.java.scm.service.InoutStockService;
import com.java.scm.service.ProjectService;
import com.java.scm.service.StockService;
import com.java.scm.service.WarehouseService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.PageUtils;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

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
     * 新增出入库
     * @return
     */
    @Override
    public PageInfo<InoutStock> listInoutStock(InoutStockSO inoutStockSO) {
        log.info("查询出入库列表参数：{}", JSON.toJSONString(inoutStockSO));
        Example example = new Example(InoutStock.class);
        example.setOrderByClause("create_time desc, id");
        Example.Criteria criteria = example.createCriteria();
        // 仓库id
        criteria.andEqualTo("warehouseId", inoutStockSO.getWarehouseId());
        // 类别 0：入库 1：出库
        criteria.andEqualTo("type", inoutStockSO.getType());
        // 工程名称
        if (StringUtil.isNotEmpty(inoutStockSO.getProject())) {
            criteria.andLike("project", "%" + inoutStockSO.getProject() + "%");
        }
        // 物资名称
        if (StringUtil.isNotEmpty(inoutStockSO.getProduct())) {
            criteria.andLike("product", "%" + inoutStockSO.getProduct() + "%");
        }
        // 时间范围
        criteria.andGreaterThanOrEqualTo("createTime", inoutStockSO.getStartTime() );
        criteria.andLessThanOrEqualTo("createTime", inoutStockSO.getEndTime());
        // 分页
        PageUtils.addPage(inoutStockSO.getPageNum(),inoutStockSO.getPageSize());
        List<InoutStock> list = inoutStockDao.selectByExample(example);
        // 设置仓库名称和类型
        if (!CollectionUtils.isEmpty(list)) {
            // 查询仓库名称集合
            List<String> warehouseIds = list.stream().map(p -> p.getWarehouseId()).distinct().collect(Collectors.toList());
            Map<String, String> warehouseMap = warehouseService.getWarehouseMap(warehouseIds);
            list.forEach(p -> {
                p.setTypeText(InoutStockTypeEnum.getEnumByValue(p.getType()));
                p.setWarehouseName(warehouseMap.get(p.getWarehouseId()));
            });
        }
        return new PageInfo<>(list);
    }

    /**
     * 导入出入库
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void importInoutStock(List<InoutStockTemplate> importList, Byte inoutStockType) {
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
            AssertUtils.notEmpty(p.getProject(), "工程名称不能为空！");
            AssertUtils.maxlength(StringUtil.trim(p.getProject()), 50, "工程名称长度不能超过50！");
            AssertUtils.notEmpty(p.getProduct(), "物资名称不能为空！");
            AssertUtils.maxlength(StringUtil.trim(p.getProduct()), 50, "物资名称长度不能超过50！");
            AssertUtils.notEmpty(p.getModel(), "物资型号不能为空！");
            AssertUtils.maxlength(StringUtil.trim(p.getModel()), 50, "物资型号长度不能超过50！");
            AssertUtils.notEmpty(p.getUnit(), "单位不能为空！");
            AssertUtils.maxlength(StringUtil.trim(p.getUnit()), 50, "单位长度不能超过50！");
            AssertUtils.notEmpty(p.getCount(), "数量不能为空！");
            AssertUtils.isInteger(StringUtil.trim(p.getCount()), "数量格式有问题，必须是数字！");
            AssertUtils.isBigDecimal(StringUtil.trim(p.getPrice()), "物资单价格式有问题，必须是数字！");
            AssertUtils.maxlength(StringUtil.trim(p.getSource()), 50, "物资来源长度不能超过50！");
            AssertUtils.maxlength(StringUtil.trim(p.getHandle()), 50, "经手人长度不能超过50！");
            AssertUtils.maxlength(StringUtil.trim(p.getRemark()), 500, "备注长度不能超过50！");
            // 判断库存是否存在
        });

        // 判断工程名称是否存在
        List<String> projectNameList = importList.stream().map(p -> StringUtil.trim(p.getProject())).distinct().collect(Collectors.toList());
        List<String> existProjectNameList = projectService.getProjectByName(projectNameList);
        List<String> notExistProjectNameList = projectNameList.stream().filter(p -> !existProjectNameList.contains(p)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(notExistProjectNameList)) {
            throw new BusinessException("导入失败，以下工程不存在：" + Arrays.toString(notExistProjectNameList.toArray()));
        }

        // 2、保存出入表
        List<InoutStock> inoutStockList = new ArrayList<>();
        for (InoutStockTemplate template : importList) {
            InoutStock inoutStock = new InoutStock();
            inoutStock.setType(inoutStockType);
            inoutStock.setWarehouseId(user.getWarehouseId());
            inoutStock.setProject(StringUtil.trim(template.getProject()));
            inoutStock.setProduct(StringUtil.trim(template.getProduct()));
            inoutStock.setModel(StringUtil.trim(template.getModel()));
            inoutStock.setUnit(StringUtil.trim(template.getUnit()));
            inoutStock.setCount(Integer.valueOf(StringUtil.trim(template.getCount())));
            inoutStock.setPrice(new BigDecimal(StringUtil.trim(template.getPrice())));
            inoutStock.setSource(StringUtil.trim(template.getSource()));
            inoutStock.setHandle(StringUtil.trim(template.getHandle()));
            inoutStock.setRemark(StringUtil.trim(template.getRemark()));
            inoutStock.setCreateUser(user.getName());
            inoutStockDao.insertSelective(inoutStock);

            // 出库数量为负
            if (Objects.equals(inoutStockType, InoutStockTypeEnum.出库.getType())) {
                inoutStock.setCount(-1 * inoutStock.getCount());
            }
            inoutStockList.add(inoutStock);
        }

        // 3、变更库存
        stockService.insertStock(inoutStockList);
    }
}
