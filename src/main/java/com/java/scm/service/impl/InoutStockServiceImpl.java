package com.java.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.so.InoutStockSO;
import com.java.scm.dao.InoutStockDao;
import com.java.scm.enums.InoutStockTypeEnum;
import com.java.scm.service.InoutStockService;
import com.java.scm.service.WarehouseService;
import com.java.scm.util.PageUtils;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
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
            List<Integer> warehouseIds = list.stream().map(p -> p.getWarehouseId()).distinct().collect(Collectors.toList());
            Map<Integer, String> warehouseMap = warehouseService.getWarehouseMap(warehouseIds);
            list.forEach(p -> {
                p.setTypeText(InoutStockTypeEnum.getEnumByValue(p.getType()));
                p.setWarehouseName(warehouseMap.get(p.getWarehouseId()));
            });
        }
        return new PageInfo<>(list);
    }

    /**
     * 新增出入库
     */
    @Override
    public Long insertInoutStock(InoutStock inoutStock) {
        inoutStockDao.insertSelective(inoutStock);
        return inoutStock.getId();
    }
}
