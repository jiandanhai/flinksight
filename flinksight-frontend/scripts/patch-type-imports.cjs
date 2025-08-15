// scripts/patch-type-imports.cjs
const fs = require('fs');
const path = require('path');

const target = path.resolve(__dirname, '../src/api/gen/api.ts');
if (fs.existsSync(target)) {
    let content = fs.readFileSync(target, 'utf8');

    // 专门处理 http-client 的那一行
    content = content.replace(
        /import\s+\{\s*([^}]+)\s*\}\s+from\s+['"]\.\/http-client['"];?/,
        (full, allImports) => {
            const items = allImports.split(',').map(x => x.trim());
            const typeItems = items.filter(i => i !== 'HttpClient');
            const hasHttpClient = items.includes('HttpClient');
            let result = '';
            if (typeItems.length > 0) {
                result += '// 1. 类型：只用于类型推导\nimport type { ' + typeItems.join(', ') + ' } from "./http-client";\n';
            }
            if (hasHttpClient) {
                result += '// 2. 实现：会被 JS 编译，运行时可用\nimport { HttpClient } from "./http-client";\n';
            }
            return result.trim();
        }
    );

    // 其它所有 import { ... } from ... 替换为 import type { ... } from ...，但跳过 http-client 这行
    content = content.replace(
        /^(import\s+\{[^}]+\}\s+from\s+['"][^'"]+['"];)/gm,
        (full) => {
            if (full.includes('./http-client')) {
                return full; // 跳过已处理的
            }
            return full.replace(/^import\s+\{/, 'import type {');
        }
    );

    fs.writeFileSync(target, content, 'utf8');
    console.log('✅ import type & http-client 拆分已全部自动完成！');
} else {
    console.log('⚠️ 未找到 api.ts，跳过 patch');
}
