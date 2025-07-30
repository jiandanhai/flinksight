//自动化 E2E 测试与持续集成脚本 (Playwright 示例)
import { test, expect } from '@playwright/test';

test('用户登录与退出', async ({ page }) => {
  await page.goto('http://localhost:3000/login');
  await page.fill('input[name=username]', 'admin');
  await page.fill('input[name=password]', 'admin123');
  await page.click('button[type=submit]');
  await expect(page).toHaveURL(/dashboard/);
  await page.click('text=退出登录');
  await expect(page).toHaveURL(/login/);
});
