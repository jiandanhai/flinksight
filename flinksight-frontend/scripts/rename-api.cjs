const fs = require('fs');
const path = require('path');

const src = path.resolve(__dirname, '../src/api/gen/Api.ts');
const dest = path.resolve(__dirname, '../src/api/gen/api.ts');

if (fs.existsSync(src)) {
  fs.renameSync(src, dest);
  console.log('✅ 已自动重命名 Api.ts -> api.ts');
} else {
  console.log('⚠️ 未找到 Api.ts，无需重命名');
}
