package com.java.scm.dao;

import com.java.scm.bean.Stock;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author hujunhui
 * @date 2020/6/28
 */
@Mapper
@Component
public interface StockDao extends TkMapper<Stock> {


    /**
     * 根据产品名称进行库存查询
     * @param product
     * @return
     */
    @Results({
            @Result(column="warehouse_id", property="warehouseId"),
            @Result(column="create_time", property="createTime",jdbcType = JdbcType.DATE),
            @Result(column="update_time", property="updateTime")
    })
    @Select("SELECT a.`id`,a.`warehouse_id`,a.`product`,a.`model`,a.`unit`,a.`count` , DATE_FORMAT(  a.`create_time`, '%Y-%m-%d %H:%m:%s' ) AS create_time , DATE_FORMAT(  a.update_time, '%Y-%m-%d %H:%m:%s' )  AS update_time ,b.name AS warehouseName FROM `stock` a LEFT JOIN warehouse b ON a.warehouse_id = b.`id` where a.product like CONCAT('%',#{product},'%')")
    List<Map> getStockInfosForAdmin(@Param("product") String product);

    /**
     * 根据产品名称和仓库id 进行库存查询
     * @param warehouseId
     * @param product
     * @return
     */
    @Results({
            @Result(column="warehouse_id", property="warehouseId"),
            @Result(column="create_time", property="createTime"),
            @Result(column="update_time", property="updateTime")
    })
    @Select("SELECT a.`id`,a.`warehouse_id`,a.`product`,a.`model`,a.`unit`,a.`count` , DATE_FORMAT(  a.`create_time`, '%Y-%m-%d %H:%m:%s' ) AS create_time , DATE_FORMAT(  a.update_time, '%Y-%m-%d %H:%m:%s' )  AS update_time ,b.name AS warehouseName FROM `stock` a LEFT JOIN warehouse b ON a.warehouse_id = b.`id` where a.product like CONCAT('%',#{product},'%') and a.warehouse_id = #{warehouseId}")
    List<Map> getStockInfos(@Param("warehouseId") Integer warehouseId ,@Param("product") String product);
}
