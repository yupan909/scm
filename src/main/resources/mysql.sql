-- 创建数据库
CREATE DATABASE `scm` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 仓库表
CREATE TABLE `scm`.`warehouse`(
  `id` int(11) PRIMARY KEY AUTO_INCREMENT COMMENT '仓库id',
  `name` varchar(50) DEFAULT NULL COMMENT '仓库名称'
) COMMENT='仓库表';

-- 用户表
CREATE TABLE `scm`.`user`(
  `id` int(11) PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
  `warehouse_id` int(11) DEFAULT NULL COMMENT '仓库id',
  `name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `password` varchar(20) DEFAULT NULL COMMENT '登陆密码',
  `state` tinyint(1) DEFAULT 0 COMMENT '状态 0：启用 1：禁用',
  `admin` tinyint(1) DEFAULT 0 COMMENT '状态 0：非管理员 1：管理员',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) COMMENT='用户表';

-- 工程表
CREATE TABLE `scm`.`project`(
  `id` int(11) PRIMARY KEY AUTO_INCREMENT COMMENT '工程id',
  `name` varchar(50) DEFAULT NULL COMMENT '工程名称',
  `content` varchar(500) DEFAULT NULL COMMENT '设计内容',
  `state` tinyint(1) DEFAULT 0 COMMENT '状态 0：启用 1：禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) COMMENT='工程表';

-- 出入库表
CREATE TABLE `scm`.`inout_stock`(
  `id` bigint(20) PRIMARY KEY AUTO_INCREMENT COMMENT '出入库Id',
  `warehouse_id` int(11) DEFAULT NULL COMMENT '仓库id',
  `project` varchar(50) DEFAULT NULL COMMENT '工程名称',
  `product` varchar(50) DEFAULT NULL COMMENT '物资名称',
  `model` varchar(50) DEFAULT NULL COMMENT '物资型号',
  `unit` varchar(50) DEFAULT NULL COMMENT '单位',
  `count` int(11) DEFAULT NULL COMMENT '数量',
  `handle` varchar(50) DEFAULT NULL COMMENT '经手人',
  `source` varchar(50) DEFAULT NULL COMMENT '物资来源',
  `price` decimal(10,2) DEFAULT 0 COMMENT '物资单价（元）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `type` tinyint(1) DEFAULT 0 COMMENT '类别 0：入库 1：出库',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  KEY `idx_project` (`project`),
  KEY `idx_product` (`product`),
  KEY `idx_create_time` (`create_time`)
) COMMENT='出入库表';

-- 库存表
CREATE TABLE `scm`.`stock`(
   `id` bigint(20) PRIMARY KEY AUTO_INCREMENT COMMENT '库存id',
   `warehouse_id` int(11) DEFAULT NULL COMMENT '仓库id',
   `product` varchar(50) DEFAULT NULL COMMENT '物资名称',
   `model` varchar(50) DEFAULT NULL COMMENT '物资型号',
   `unit` varchar(50) DEFAULT NULL COMMENT '单位',
   `count` int(11) DEFAULT NULL COMMENT '库存数量',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   KEY `idx_product` (`product`)
) COMMENT='库存表';

-- 库存变更记录表
CREATE TABLE `scm`.`stock_record`(
  `id` bigint(20) PRIMARY KEY AUTO_INCREMENT COMMENT '库存变更记录id',
  `stock_id` bigint(20) DEFAULT NULL COMMENT '库存id',
  `project` varchar(50) DEFAULT NULL COMMENT '工程名称',
  `count` int(11) DEFAULT NULL COMMENT '变更数量',
  `type` tinyint(1) DEFAULT 0 COMMENT '变更类型 0：入库 1：出库 2：手动修改',
  `inout_stock_id` bigint(20) DEFAULT NULL COMMENT '出入库id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  KEY `idx_stock_id` (`stock_id`)
) COMMENT='库存变更记录表';

-- 初始化数据
insert into `scm`.`user`(name, mobile, password, admin) values ('admin', 'admin', 'admin', 1);
insert into `scm`.`warehouse`(name) values ('武汉市东西湖区电力设备安装有限公司'), ('湖北得力电气有限公司');
