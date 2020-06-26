package com.java.scm.controller;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.bean.excel.InoutStockTemplate;
import com.java.scm.bean.so.InoutStockSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.enums.InoutStockTypeEnum;
import com.java.scm.service.InoutStockService;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.excel.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 出入库
 *
 * @author yupan
 * @date 2020-06-25 23:49
 */
@RestController
@RequestMapping("/inoutStock")
public class InoutStockController {

    @Autowired
    private InoutStockService inoutStockService;

    /**
     * 查询出入库列表
     * @return
     */
    @PostMapping("/list")
    public BaseResult listInoutStock(@RequestBody InoutStockSO inoutStockSO) {
        PageInfo<InoutStock> pageInfo = inoutStockService.listInoutStock(inoutStockSO);
        return new BaseResult(pageInfo.getList(), Long.valueOf(pageInfo.getTotal()).intValue());
    }

    /**
     * 导出模版
     * @param response
     * @throws Exception
     */
    @GetMapping("/exportTemplate/{type}")
    public BaseResult exportTemplate(HttpServletResponse response, @PathVariable("type") Byte type) throws Exception {
        String title = "入库单";
        String fileName = "入库单模版";
        if (Objects.equals(type, InoutStockTypeEnum.出库.getType())) {
            title = "出库单";
            fileName = "出库单模版";
        }
        ExcelUtils.exportExcel(new ArrayList<>(), InoutStockTemplate.class, title, fileName, response);
        return BaseResult.successResult();
    }

    /**
     * 导入
     * @throws Exception
     */
    @PostMapping("/import/{type}")
    public BaseResult importInoutStock(@RequestParam("file")MultipartFile file, @PathVariable("type") Byte type, HttpServletRequest request) throws Exception {
        // 获取导入的excel数据
        List<InoutStockTemplate> importList = ExcelUtils.importExcel(file, 1,1, InoutStockTemplate.class);
        if (CollectionUtils.isEmpty(importList)) {
            throw new BusinessException("导入excel数据为空！");
        }

        Byte inoutStockType = InoutStockTypeEnum.入库.getType();
        if (Objects.equals(type, InoutStockTypeEnum.出库.getType())) {
            inoutStockType = InoutStockTypeEnum.出库.getType();
        }

        // 获取当前登陆用户
        User user = RequestUtil.getLoginUser();

        // 保存数据
        for (InoutStockTemplate template : importList) {
            InoutStock inoutStock = new InoutStock();
            inoutStock.setType(inoutStockType);
            inoutStock.setWarehouseId(user.getWarehouseId());
            inoutStock.setProject(template.getProject());
            inoutStock.setProduct(template.getProduct());
            inoutStock.setModel(template.getModel());
            inoutStock.setUnit(template.getUnit());
            inoutStock.setCount(Integer.valueOf(template.getCount()));
            inoutStock.setPrice(new BigDecimal(template.getPrice()));
            inoutStock.setSource(template.getSource());
            inoutStock.setHandle(template.getHandle());
            inoutStock.setRemark(template.getRemark());
            inoutStock.setCreateUser(user.getName());
            Long inoutStockId = inoutStockService.insertInoutStock(inoutStock);
        }
        return BaseResult.successResult();
    }
}
