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

# 封装所有 API 方法为 axios 格式（替代原来自动生成的 SDK 调用 import api from ‘@api/gen/client’ 使用 例如api.getUser）
    安装依赖：npm install axios prettier @apidevtools/swagger-parser
    执行脚本：
    npx tsx ./scripts/swagger-openApi-to-axios.ts --emitStatic=auto --swagger=../flinksight-backend/src/main/resources/static/openapi.json --outdir=../api --baseURL="import.meta.env.VITE_API_BASE_URL || '/api'"
    --emitStatic=auto|always|never（默认 auto）
        auto：静态文件仅在不存在时生成（你要的默认）
        always：每次都覆盖静态文件
        never：完全不动静态文件
    --returnWrapper=true|false（默认 false）
        false：函数返回直接是 data 的 DTO（更符合前端使用）
        true：返回整个 { code/msg/data } 包装

    npx tsx ./scripts/swagger-openapi-to-axios.ts --swagger=../flinksight-backend/src/main/resources/static/openapi.json --outdir=../api --baseURL="import.meta.env.VITE_API_BASE_URL || '/api'" --emitStatic=auto --verbose=true


npx tsx ./scripts/swagger-openapi-to-axios-cli.ts --swagger=../flinksight-backend/src/main/resources/static/openapi.json --outdir=../api --baseURL="import.meta.env.VITE_API_BASE_URL || '/api'" --emitStatic=auto --verbose=true