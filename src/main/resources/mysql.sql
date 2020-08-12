-- 创建数据库
CREATE DATABASE `scm` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 仓库表
CREATE TABLE `scm`.`warehouse`(
  `id` varchar(32) PRIMARY KEY COMMENT '仓库id',
  `name` varchar(50) DEFAULT NULL COMMENT '仓库名称'
) COMMENT='仓库表';

-- 用户表
CREATE TABLE `scm`.`user`(
  `id` varchar(32) PRIMARY KEY COMMENT '用户id',
  `warehouse_id` varchar(32) DEFAULT NULL COMMENT '仓库id',
  `name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `password` varchar(20) DEFAULT NULL COMMENT '登陆密码',
  `state` tinyint(1) DEFAULT 0 COMMENT '状态 0：启用 1：禁用',
  `role` tinyint(1) DEFAULT 0 COMMENT '角色 0：仓库普通人员 1：仓库管理员',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '修改人id'
) COMMENT='用户表';

-- 工程表
CREATE TABLE `scm`.`project`(
    `id` varchar(32) PRIMARY KEY COMMENT '工程id',
    `name` varchar(50) DEFAULT NULL COMMENT '工程名称',
    `content` varchar(500) DEFAULT NULL COMMENT '工程内容',
    `contract_money` decimal(14,2) DEFAULT 0 COMMENT '合同金额',
    `final_money` decimal(14,2) DEFAULT 0 COMMENT '结算金额',
    `state` tinyint(1) DEFAULT 0 COMMENT '状态 0：启用 1：禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `update_user_id` varchar(32) DEFAULT NULL COMMENT '修改人id',
    KEY `idx_name` (`name`)
) COMMENT='工程表';

-- 工程流水帐表
CREATE TABLE `scm`.`project_record`(
  `id` varchar(32) PRIMARY KEY COMMENT '工程流水帐id',
  `project_id` varchar(32) DEFAULT NULL COMMENT '工程id',
  `type` tinyint(1) DEFAULT 0 COMMENT '流水帐类型 0：收入 1：支出',
  `record_date` varchar(50) DEFAULT NULL COMMENT '日期',
  `digest` varchar(500) DEFAULT NULL COMMENT '摘要',
  `money` decimal(14,2) DEFAULT 0 COMMENT '金额',
  `handle` varchar(50) DEFAULT NULL COMMENT '经手人',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '修改人id',
  KEY `idx_project_id` (`project_id`)
) COMMENT='工程流水帐表';

-- 库存表
CREATE TABLE `scm`.`stock`(
   `id` varchar(32) PRIMARY KEY COMMENT '库存id',
   `warehouse_id` varchar(32) DEFAULT NULL COMMENT '仓库id',
   `product` varchar(50) DEFAULT NULL COMMENT '物资名称',
   `model` varchar(50) DEFAULT NULL COMMENT '物资型号',
   `unit` varchar(50) DEFAULT NULL COMMENT '单位',
   `count` int(11) DEFAULT NULL COMMENT '库存数量',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人id',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `update_user_id` varchar(32) DEFAULT NULL COMMENT '修改人id',
   KEY `idx_product` (`product`),
   KEY `idx_model` (`model`)
) COMMENT='库存表';

-- 库存变更记录表
CREATE TABLE `scm`.`stock_record`(
  `id` varchar(32) PRIMARY KEY COMMENT '库存变更记录id',
  `stock_id` varchar(32) DEFAULT NULL COMMENT '库存id',
  `inout_stock_id` varchar(32) DEFAULT NULL COMMENT '出入库id',
  `count` int(11) DEFAULT NULL COMMENT '变更数量',
  `type` tinyint(1) DEFAULT 0 COMMENT '变更类型 0：入库 1：出库 2：手动修改',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人id',
  KEY `idx_stock_id` (`stock_id`),
  KEY `idx_inout_stock_id` (`inout_stock_id`),
  KEY `idx_create_time` (`create_time`)
) COMMENT='库存变更记录表';

-- 出入库表
CREATE TABLE `scm`.`inout_stock`(
  `id` varchar(32) PRIMARY KEY COMMENT '出入库Id',
  `warehouse_id` varchar(32) DEFAULT NULL COMMENT '仓库id',
  `project_id` varchar(32) DEFAULT NULL COMMENT '工程id',
  `stock_id` varchar(32) DEFAULT NULL COMMENT '库存id',
  `count` int(11) DEFAULT NULL COMMENT '数量',
  `price` decimal(10,2) DEFAULT 0 COMMENT '物资单价（元）',
  `source` varchar(50) DEFAULT NULL COMMENT '物资来源',
  `handle` varchar(50) DEFAULT NULL COMMENT '经手人',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `type` tinyint(1) DEFAULT 0 COMMENT '类别 0：入库 1：出库',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人id',
  KEY `idx_project_id` (`project_id`),
  KEY `idx_stock_id` (`stock_id`),
  KEY `idx_source` (`source`),
  KEY `idx_create_time` (`create_time`)
) COMMENT='出入库表';

-- 上传文件表
CREATE TABLE `scm`.`file`(
    `id` varchar(32) PRIMARY KEY COMMENT '文件id',
    `name` varchar(100) DEFAULT NULL COMMENT '文件名',
    `business_id` varchar(32) DEFAULT NULL COMMENT '业务id，用于区分业务类别',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人id',
    KEY `idx_business_id` (`business_id`)
) COMMENT='上传文件表';

-- 初始化数据
insert into `scm`.`user`(id, name, password, role) values (uuid_short(),'管理员', '123456', 2);
insert into `scm`.`warehouse`(id, name) values (uuid_short(), '武汉市东西湖区电力设备安装有限公司'), (uuid_short(), '湖北得力电气有限公司');
