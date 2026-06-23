-- ============================================
-- Tlias 精简版 - 数据库初始化脚本
-- 仅包含：登录、部门、员工、报表相关表
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tlias;

-- ============================================
-- 1. 部门表 (dept)
-- ============================================
DROP TABLE IF EXISTS dept;
CREATE TABLE dept (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    name VARCHAR(10) NOT NULL COMMENT '部门名称',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ============================================
-- 2. 员工表 (emp)
-- ============================================
DROP TABLE IF EXISTS emp;
CREATE TABLE emp (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    username VARCHAR(20) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(32) NOT NULL COMMENT '密码',
    name VARCHAR(10) NOT NULL COMMENT '姓名',
    gender TINYINT UNSIGNED NOT NULL COMMENT '性别: 1-男, 2-女',
    phone VARCHAR(11) COMMENT '手机号',
    job TINYINT UNSIGNED COMMENT '职位: 1-班主任, 2-讲师, 3-学工主管, 4-教研主管, 5-咨询师',
    role TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色: 0-普通员工, 1-管理者',
    salary DECIMAL(10,2) UNSIGNED COMMENT '薪资',
    image VARCHAR(300) COMMENT '头像URL',
    entry_date DATE NOT NULL COMMENT '入职日期',
    dept_id INT UNSIGNED COMMENT '所属部门ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '修改时间',
    CONSTRAINT fk_emp_dept FOREIGN KEY (dept_id) REFERENCES dept(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- ============================================
-- 3. 员工工作经历表 (emp_expr)
-- ============================================
DROP TABLE IF EXISTS emp_expr;
CREATE TABLE emp_expr (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    emp_id INT UNSIGNED NOT NULL COMMENT '员工ID',
    begin DATE NOT NULL COMMENT '开始时间',
    end DATE NOT NULL COMMENT '结束时间',
    company VARCHAR(50) NOT NULL COMMENT '公司名称',
    job VARCHAR(20) NOT NULL COMMENT '职位名称',
    CONSTRAINT fk_emp_expr_emp FOREIGN KEY (emp_id) REFERENCES emp(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工工作经历表';

-- ============================================
-- 插入测试数据 - 部门
-- ============================================
INSERT INTO dept (id, name, create_time, update_time) VALUES
(1, '学工部', NOW(), NOW()),
(2, '教研部', NOW(), NOW()),
(3, '咨询部', NOW(), NOW());

-- ============================================
-- 插入测试数据 - 员工（所有密码都是 123456）
-- ============================================
INSERT INTO emp (id, username, password, name, gender, phone, job, role, salary, image, entry_date, dept_id, create_time, update_time) VALUES
(1, 'zhangwuji', '123456', '张无忌', 1, '18812345678', 1, 1, 8000, 'https://example.com/1.jpg', '2020-01-01', 1, NOW(), NOW()),
(2, 'luyi', '123456', '鹿鼎记', 1, '18812345679', 2, 0, 10000, 'https://example.com/2.jpg', '2020-03-15', 2, NOW(), NOW()),
(3, 'xiaolongnv', '123456', '小龙女', 2, '18812345680', 2, 0, 12000, 'https://example.com/3.jpg', '2020-06-01', 2, NOW(), NOW()),
(4, 'huangrong', '123456', '黄蓉', 2, '18812345681', 3, 0, 9000, 'https://example.com/4.jpg', '2021-01-10', 1, NOW(), NOW()),
(5, 'guojing', '123456', '郭靖', 1, '18812345682', 4, 0, 15000, 'https://example.com/5.jpg', '2021-03-20', 2, NOW(), NOW()),
(6, 'zhouzhiruo', '123456', '周芷若', 2, '18812345683', 5, 0, 7500, 'https://example.com/6.jpg', '2021-06-15', 3, NOW(), NOW());

-- ============================================
-- 插入测试数据 - 员工工作经历
-- ============================================
INSERT INTO emp_expr (emp_id, begin, end, company, job) VALUES
(1, '2018-01-01', '2019-12-31', '阿里巴巴', 'Java开发工程师'),
(1, '2020-01-01', '2020-12-31', '腾讯科技', '高级Java工程师'),
(2, '2019-01-01', '2020-03-14', '百度', '前端开发工程师'),
(3, '2018-06-01', '2020-05-31', '华为', '测试工程师'),
(4, '2019-06-01', '2021-01-09', '美团', '产品经理'),
(5, '2017-01-01', '2021-03-19', '字节跳动', '技术总监'),
(6, '2020-01-01', '2021-06-14', '京东', '销售顾问');

-- ============================================
-- 查询验证
-- ============================================
SELECT '部门数据:' AS info;
SELECT * FROM dept;

SELECT '员工数据:' AS info;
SELECT e.id, e.username, e.name, e.gender, e.phone, 
       CASE e.job 
           WHEN 1 THEN '班主任'
           WHEN 2 THEN '讲师'
           WHEN 3 THEN '学工主管'
           WHEN 4 THEN '教研主管'
           WHEN 5 THEN '咨询师'
           ELSE '其他'
       END AS job_name,
       e.salary, e.entry_date, d.name AS dept_name
FROM emp e LEFT JOIN dept d ON e.dept_id = d.id;

SELECT '员工工作经历:' AS info;
SELECT ee.*, e.name AS emp_name 
FROM emp_expr ee 
LEFT JOIN emp e ON ee.emp_id = e.id;

-- ============================================
-- 补充：公告表 (notice)
-- ============================================
DROP TABLE IF EXISTS notice;
CREATE TABLE notice (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    title VARCHAR(100) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    publisher_id INT UNSIGNED NOT NULL COMMENT '发布人ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '修改时间',
    CONSTRAINT fk_notice_emp FOREIGN KEY (publisher_id) REFERENCES emp(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公司公告表';

-- ============================================
-- 补充：请假表 (emp_leave)
-- ============================================
DROP TABLE IF EXISTS emp_leave;
CREATE TABLE emp_leave (
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
