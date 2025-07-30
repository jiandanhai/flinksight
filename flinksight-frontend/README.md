# 项目结构
    flinksight-frontend/
    ├── public/                    # 静态资源
    ├── src/
    │   ├── api/                   # 所有后端接口管理
    │   ├── assets/                # 图片、icon、全局样式
    │   ├── components/            # 通用组件
    │   ├── layouts/               # 页面整体布局（含菜单/头部/面包屑）
    │   ├── pages/
    │   │   ├── Login/             # 登录页
    │   │   ├── Dashboard/         # 首页/大盘
    │   │   ├── Alerts/            # 报警流页面
    │   │   ├── Metrics/           # 指标分析
    │   │   ├── Jobs/              # 任务流（Flink/Spark）
    │   │   ├── Users/             # 用户与权限
    │   │   └── ...                # 其它业务
    │   ├── store/                 # 状态管理
    │   ├── utils/                 # 工具方法
    │   ├── mock/                  # 本地Mock数据
    │   ├── App.tsx                # 主入口
    │   ├── main.tsx               # 启动入口
    │   └── routes.tsx             # 路由配置
    ├── .env.development           # 环境变量
    ├── vite.config.ts             # Vite配置
    ├── package.json
    └── README.md


    flinksight-frontend/
    ├── public/
    ├── src/
    │   ├── api/          # 统一所有接口
    │   ├── assets/       # 图片/样式
    │   ├── components/   # 通用组件（可按功能域二次分级）
    │   ├── constants/    # 枚举常量
    │   ├── layouts/      # 布局相关
    │   ├── pages/        # 业务页面，每个一级菜单一个文件夹
    │   │   ├── Alerts/             # 业务一级页面，含多子页/弹窗
    │   │   ├── Alert/              # 单条报警详情
    │   │   ├── Jobs/               # 作业明细
    │   │   ├── Dashboard/          # 指标可视化
    │   │   ├── Cluster/            # 集群状态
    │   │   └── SaaSBoard/          # SaaS运营大屏
    │   ├── types/        # TypeScript类型定义
    │   ├── utils/        # 工具函数
    │   └── App.tsx
    ├── package.json
    └── tsconfig.json

# 一级代码功能列表与开发顺序
    我们将逐个给出以下一级模块的商业代码（每一块都含目录、包名、注释、说明，并有可直接用的业务代码）：
    工程与包结构说明
    全局入口（main.tsx、App.tsx）
    全局路由配置（routes.tsx）
    主布局框架（layouts/MainLayout.tsx）
    用户鉴权与路由守卫（components/AuthRoute.tsx）
    登录模块（pages/Login/）
    首页大盘（pages/Dashboard/）
    报警流（pages/Alerts/）
    指标分析（pages/Metrics/）
    任务流（pages/Jobs/）
    用户权限（pages/Users/）
    API请求层（api/xxx.ts）
    Mock方案/本地调试（mock/ & vite.config.ts）
    全局状态（store/）
    国际化、主题、暗色模式（可选，按需补充）
    package.json & 工程启动说明