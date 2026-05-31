-- ============================================
-- Tlias 增量更新 v2: 公告 + 请假模块
-- 安全执行, 不会影响已有数据
-- ============================================

USE tlias;

-- ============================================
-- 1. 公司公告表 (notice)
-- ============================================
CREATE TABLE IF NOT EXISTS notice (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    title VARCHAR(100) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    publisher_id INT UNSIGNED NOT NULL COMMENT '发布人ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '修改时间',
    CONSTRAINT fk_notice_emp FOREIGN KEY (publisher_id) REFERENCES emp(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公司公告表';

-- ============================================
-- 2. 员工请假表 (emp_leave)
-- ============================================
CREATE TABLE IF NOT EXISTS emp_leave (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    emp_id INT UNSIGNED NOT NULL COMMENT '员工ID',
    leave_type TINYINT UNSIGNED NOT NULL COMMENT '请假类型: 1-事假, 2-病假, 3-年假',
    begin_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    reason VARCHAR(500) COMMENT '请假原因',
    status TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态: 1-待审批, 2-已批准, 3-已拒绝',
    approver_id INT UNSIGNED COMMENT '审批人ID',
    approve_time DATETIME COMMENT '审批时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '修改时间',
    CONSTRAINT fk_leave_emp FOREIGN KEY (emp_id) REFERENCES emp(id),
    CONSTRAINT fk_leave_approver FOREIGN KEY (approver_id) REFERENCES emp(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工请假表';

-- 测试数据通过接口自行添加即可
