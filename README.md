# 员工管理系统

基于 Spring Boot + Vue 3 的全栈 Web 应用，涵盖登录认证、工作台仪表盘、员工管理、部门管理、请假审批、公告管理及数据统计报表七大功能模块，支持 JWT 令牌认证与角色化操作。

---

## 项目一级目录及功用说明

### 1. src（源代码核心目录）
该目录是项目源代码的核心存放位置，包含项目全部业务逻辑与功能实现代码。后端采用 Java 语言，基于 Spring Boot 框架进行分层开发，子包按功能拆分：controller 层负责接收 HTTP 请求与参数校验，service 层封装业务逻辑，mapper 层对接 MyBatis 实现数据库持久化操作，pojo 层定义实体类与查询参数对象，filter 层提供 JWT 认证拦截，exception 层统一处理全局异常与业务异常。

### 2. frontend（前端目录）
该目录为 Vue 3 前端工程，基于 Vite 构建工具开发。src/views 目录存放各功能页面组件，包括登录页（Login.vue）、主布局（Layout.vue）、工作台仪表盘（Dashboard.vue）、员工信息统计页（EmployeeStats.vue）、员工管理页（EmpList.vue）、部门管理页（DeptList.vue）、请假管理页（LeaveList.vue）、公告管理页（NoticeList.vue）；src/router 目录配置 Vue Router 路由与登录守卫，登录后默认进入工作台，未登录自动跳转登录页；src/components 目录封装公共组件；src/assets 目录存放静态资源（图标、样式文件）；main.js 为入口文件，集成 Element Plus 组件库与 Axios 拦截器，自动在请求头携带 JWT 令牌。

### 3. config（配置文件目录）
存放项目的所有配置文件。后端配置文件集中位于 src/main/resources 目录下，application.yml 负责数据库连接信息（数据库地址、端口、账号密码）及服务器运行参数（如服务端口号），logback.xml 配置日志输出格式与级别；WebConfig.java 位于 config 包中，负责跨域请求等 Web 层全局配置。前端配置文件包括 vite.config.js（Vite 构建配置与开发环境 API 代理）和 package.json（依赖声明与脚本命令）。

### 4. utils（工具类目录）
存放项目通用的工具类，为整个项目提供可复用的基础能力。JwtUtils.java 负责 JWT 令牌的生成与解析校验，CurrentHolder.java 基于 ThreadLocal 机制维护当前请求上下文中的登录用户信息，供业务层在需要时获取当前操作用户 ID。

### 5. test（测试代码目录）
存放项目的测试用例代码，位于 src/test/java 目录下，包括 JWT 令牌生成与解析测试、ThreadLocal 上下文测试、日志输出测试、UUID 生成测试等。通过运行该目录下的测试代码可验证核心工具类的正确性，保障代码质量。

### 6. docs（文档目录）
存放项目相关的补充文档。README.md 为项目说明文档，涵盖目录结构、功能模块、技术栈与运行说明；init_database.sql 为基础数据库初始化脚本，包含部门表、员工表、工作经历表的建表语句及测试数据；update_v2.sql 为增量更新脚本，新增公告表与请假表。便于团队成员查阅项目背景和设计细节，也方便后续维护和迭代。

### 7. resources（资源文件目录）
存放项目运行所需的非代码类资源文件。src/main/resources 目录下包含 application.yml（系统运行参数配置）、logback.xml（日志模板配置），以及 com/yuangong/mapper 目录下的 MyBatis XML 映射文件，定义各模块的 SQL 查询语句。frontend/src/assets 目录存放前端静态资源如图标、样式文件等。

### 8. bin（可执行文件目录，可选）
存放项目的可执行脚本。init_database.sql 用于初始化数据库表结构与测试数据，update_v2.sql 用于增量更新数据库表（公告表、请假表），在首次部署或版本升级时依次执行即可完成数据库环境准备。

### 9. logs（日志目录，可选）
用于存放项目运行过程中生成的日志文件，包括 MyBatis SQL 执行日志、业务操作日志、异常错误日志等。日志框架采用 Logback，日志输出格式与级别通过 logback.xml 配置，便于问题排查和运行状态监控。

---

## 功能模块

| 模块 | 说明 |
|------|------|
| 登录认证 | JWT 令牌认证，TokenFilter 全局拦截，无令牌或令牌无效返回 401，前端自动跳转登录页。Header 显示当前用户姓名，支持退出登录 |
| 工作台仪表盘 | 登录后首页，展示员工总数、部门总数、待审批请假数、公告总数四大统计卡片，以及近期公告列表和近期请假动态 |
| 员工管理 | 按姓名/性别/入职日期搜索、分页展示、新增/编辑员工（含工作经历）、批量删除 |
| 部门管理 | 部门列表展示、新增/编辑/删除（删除前检测是否有关联员工） |
| 请假管理 | 管理者代员工提交请假申请、按姓名/类型/状态搜索、审批（批准/拒绝）、批量删除 |
| 公告管理 | 发布/编辑公司公告、按标题搜索、分页展示、批量删除 |
| 数据统计报表 | ECharts 可视化图表：职位人数柱状图、性别占比环形图、各部门人数分布横向柱状图 |

---

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Java 17 + Spring Boot 3.2 + Spring Web |
| 持久层 | MyBatis 3.0.3 + MySQL + PageHelper 1.4.7 |
| 认证 | JWT 0.9.1（JSON Web Token） |
| 前端框架 | Vue 3 + Vue Router + Vite |
| UI 组件 | Element Plus + ECharts |
| HTTP 客户端 | Axios（请求拦截器自动携带 Token） |
| 构建工具 | Maven（后端）+ npm（前端） |

---

## 项目运行说明

1. 数据库初始化：在 MySQL 中依次执行 init_database.sql（基础表 + 测试数据）和 update_v2.sql（公告表 + 请假表）；
2. 配置数据库连接：修改 src/main/resources/application.yml 中的数据库地址、端口、账号和密码；
3. 启动后端：在 IDEA 中运行 TliasWebManagementApplication 主类，或执行 mvn spring-boot:run，后端默认运行在 http://localhost:8080；
4. 启动前端：进入 frontend 目录，执行 npm install 安装依赖，再执行 npm run dev 启动开发服务器，默认运行在 http://localhost:5173（已配置 API 代理至后端 8080 端口）；
5. 登录系统：浏览器访问 http://localhost:5173，使用测试账号 zhangwuji / 123456 登录（或数据库 emp 表中任意用户密码均为 123456）。
