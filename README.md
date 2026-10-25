# Student Management System（学生管理系统 Demo）

适合 Java 初学者的 Spring Boot 校园管理 REST API，采用 Controller → Service → Mapper 分层。

## 技术栈
- Java 24、Spring Boot 3.5.6、Maven
- MyBatis、H2（默认）、MySQL（可选）
- Springdoc OpenAPI / Swagger UI

## 功能
- 学生信息查询、新增
- 课程查询、新增
- 排课记录查询、新增
- 选课/抢课：重复选课检查、数据库原子扣减名额
- 考试成绩录入、发布、查询已发布成绩
- 请假申请、审批
- 学生在校状态记录与查询

> 学习用 Demo。尚未接入登录认证和角色授权，不适合直接存储真实学生隐私数据或用于生产环境。正式使用前需要补充 Spring Security、审计、数据权限、数据库迁移和并发测试。

## 启动
要求 JDK 24 和 Maven 3.9+。

```bash
mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui/index.html  
H2 Console: http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:mem:schooldb`，用户名 `sa`，密码留空。

## MySQL（可选）
创建数据库 `schooldb`，设置环境变量：
- `SPRING_PROFILES_ACTIVE=mysql`
- `DB_URL=jdbc:mysql://localhost:3306/schooldb?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai`
- `DB_USERNAME=你的数据库用户`
- `DB_PASSWORD=你的数据库密码`

## 主要接口
| 方法 | 路径 | 说明 |
|---|---|---|
| GET/POST | `/api/students` | 查询/新增学生 |
| GET/POST | `/api/courses` | 查询/新增课程 |
| GET/POST | `/api/schedules` | 查询/创建排课 |
| POST | `/api/enrollments` | 选课/抢课 |
| GET | `/api/enrollments/student/{studentId}` | 学生选课记录 |
| POST | `/api/grades` | 录入成绩（未发布） |
| POST | `/api/grades/{id}/publish` | 发布成绩 |
| GET | `/api/grades/student/{studentId}` | 查询已发布成绩 |
| POST/GET | `/api/leaves` | 提交/查看请假 |
| POST | `/api/leaves/{id}/review` | 审批请假 |
| POST | `/api/attendance/status` | 更新在校状态 |
| GET | `/api/attendance/student/{studentId}` | 查询最新在校状态 |

示例新增学生：
```json
{"studentNo":"20260001","name":"张三","className":"计算机 1 班","email":"zhangsan@example.com"}
```

## 项目结构
```text
src/main/java/com/example/school/
├── SchoolManagementApplication.java
├── common/ApiExceptionHandler.java
├── controller/SchoolController.java
├── service/SchoolService.java
└── mapper/SchoolMapper.java
src/main/resources/
├── application.yml
├── application-mysql.yml
├── schema.sql
├── schema-mysql.sql
└── data.sql
```
