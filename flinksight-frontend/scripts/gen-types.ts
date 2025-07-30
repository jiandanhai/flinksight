import fs from 'fs';
import path from 'path';

const OPENAPI_PATH = path.resolve(__dirname, '../public/openapi.json');
const TYPES_PATH = path.resolve(__dirname, '../src/types/');
const ENUMS_PATH = path.resolve(__dirname, '../src/constants/enums.ts');

const openapi = JSON.parse(fs.readFileSync(OPENAPI_PATH, 'utf-8'));
const schemas = openapi.components.schemas;

const tsTypes: string[] = [];
const enums: string[] = [];

for (const key in schemas) {
  const s = schemas[key];
  if (s.enum) {
    enums.push(
      `export enum ${key} {\n` +
      s.enum.map((v: string) => `  ${v} = '${v}'`).join(',\n') +
      '\n}\n'
    );
  } else if (s.type === 'object' && s.properties) {
    const props = Object.entries(s.properties)
      .map(([p, v]: [string, any]) =>
        `  ${p}${v.nullable ? '?' : ''}: ${v.type === 'integer' ? 'number' : v.type === 'array' ? (v.items?.$ref ? v.items.$ref.replace('#/components/schemas/', '') + '[]' : 'any[]') : v.type};`
      ).join('\n');
    tsTypes.push(`export interface ${key} {\n${props}\n}`);
  }
}
fs.writeFileSync(TYPES_PATH + '/auto-types.ts', tsTypes.join('\n\n'));
fs.writeFileSync(ENUMS_PATH, enums.join('\n\n'));
console.log('✔ types/enums 自动生成完成');
