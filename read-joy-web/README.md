<div align=center>
 <div align=center margin="10em" style="margin:4em 0 0 0;font-size: 30px;letter-spacing:0.3em;">
<img src="../.doc/img/logo.png" width="120px" height="120px" alt="logo" align=center />
 </div>
 <h2 align=center style="margin: 2em 0; padding-bottom:1em;">悦读时光 | Read-Joy-Web</h2>
</div>


# read-joy-web 项目说明

欢迎使用 read-joy-web 项目！这是一个阅读时光的前端项目，基于 Nuxt.js 的前端应用程序。🎉

## 目录结构

- `package.json`：项目的依赖管理文件。
- `scripts`：常用的命令脚本。

## 安装依赖

在开始之前，请确保您已经安装了 [Node.js](https://nodejs.org/) 和 [pnpm](https://pnpm.js.org/)。然后在终端中运行以下命令安装项目依赖：

```bash
pnpm install
```

## 常用命令

以下是一些常用的脚本命令：

- `开发环境`：启动开发服务器。
  ```bash
  pnpm run dev
  ```
- `生产环境构建`：生成用于生产环境的代码。
  ```bash
  pnpm run build
  ```

- `启动生产服务器`：运行已生成的项目。
  ```bash
  pnpm run start
  ```

- `代码校验`：运行 ESLint 进行代码检查。
  ```bash
  pnpm run lint
  ```

- `代码格式修复`：自动修复格式问题。
  ```bash
  pnpm run lint:fix
  ```

- `生成静态站点`：生成静态站点文件。
  ```bash
  pnpm run generate
  ```

## 版本管理

在进行版本发布时，您可以使用以下命令：

- `发布次要版本`：
  ```bash
  pnpm run release
  ```

- `发布主要版本`：
  ```bash
  pnpm run release:major
  ```

- `发布补丁版本`：
  ```bash
  pnpm run release:patch
  ```


## 🔧 涉及技术栈 | Tech Stack

| 类别         | 技术/组件          | 版本号       |
| ------------ | ----------------- | ------------ |
| 包管理器     | pnpm             | 9.14.4       |
| 框架         | Nuxt3            | ^3.14.1592   |
| UI 组件库    | Element Plus      | ^2.9.1       |
|              | Radix Vue         | ^1.9.11      |
|              | Element Plus Icons | >=0.2.6      |
| 状态管理     | Pinia            | ^2.3.0       |
|              | Pinia Persisted State | ^4.2.0    |
| 构建工具      | Vite             | ^6.0.2       |
|              | Webpack          | ^5           |
| 静态站点生成  | Nuxi             | -            |
| 服务端渲染    | Nitro            | -            |
| CSS 框架     | Tailwind CSS      | ^3.4.17      |
|              | Tailwind Merge    | ^2.5.5       |
|              | Tailwind CSS Animate | ^1.0.7     |
| TypeScript   | TypeScript       | 5.7.2        |
| ESLint 配置   | @antfu/eslint-config | ^3.12.0     |
|              | @nuxt/eslint      | ^0.7.4       |
|              | @unocss/eslint-config | ^0.65.2    |
| 代码质量      | ESLint           | ^9.17.0      |
|              | Consola          | ^3.2.3       |
| 动画         | @formkit/auto-animate | 0.8.2      |
| 图表         | ECharts          | ^5.5.1       |
| 日期处理     | Dayjs            | ^1.11.13     |
| Markdown 编辑器 | MD-Editor V3    | ^5.1.1       |
| 国际化       | -                | -            |
| PWA           | @vite-pwa/nuxt   | ^0.10.6      |
| Vue 插件      | @vueuse/nuxt     | ^12.2.0      |
| 版本控制钩子  | Husky            | -            |
| 代码提交规范  | lint-staged      | -            |

## 贡献

欢迎您为项目贡献代码！请确保在提交之前运行 `lint` 命令进行代码检查。

## License

此项目遵循 GNU Affero General Public License v3.0 许可证，具体内容请查看 LICENSE 文件。

希望您在使用此项目时感到愉快！如有任何问题，请随时联系。
