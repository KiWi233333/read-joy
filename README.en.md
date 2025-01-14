<div align=center>
 <div align=center margin="10em" style="margin:4em 0 0 0;font-size: 30px;letter-spacing:0.3em;">
<img src="./.doc/img/logo.png" width="140px" height="140px" alt="logo" align=center />
 </div>
 <h2 align=center style="margin: 1em 0; padding-bottom:1em;">Read-Joy</h2>

<div>
      <a href="https://github.com/Kiwi233333/read-joy" target="_blank">
        <img class="disabled-img-view" src="https://img.shields.io/badge/Github-Project-blueviolet.svg?style=plasticr">
      </a>
      <a href="https://github.com/Kiwi233333/read-joy/stargazers" target="_blank">
        <img class="disabled-img-view" alt="License"
          src="https://img.shields.io/github/stars/Kiwi233333/read-joy.svg?style=social">
      </a>
    </div>
    <div >
      <a href="https://github.com/Kiwi233333/read-joy/commits" target="_blank">
        <img class="disabled-img-view" alt="Commit"
          src="https://img.shields.io/github/commit-activity/m/Kiwi233333/read-joy">
      </a>
      <a href="https://github.com/Kiwi233333/read-joy/issues" target="_blank">
        <img class="disabled-img-view" alt="Issues" src="https://img.shields.io/github/issues/Kiwi233333/read-joy">
      </a>
      <a href="https://github.com/Kiwi233333/read-joy/blob/master/LICENSE" target="_blank">
        <img class="disabled-img-view" alt="License"
          src="https://img.shields.io/github/license/KiWi233333/read-joy">
      </a>
    </div>
    <div>
      <a href="https://app.netlify.com/sites/readjoy/deploys">
          <img src="https://api.netlify.com/api/v1/badges/a54a9b5e-f15e-42b5-a381-fc4469738a68/deploy-status" alt="Netlify Status">
      </a>
    </div>  

Language : English | [简体中文](./README.md)
</div>

## Project Introduction

### Overview

Welcome to Read-Joy 🎉: a web application that makes reading more enjoyable and convenient! Based on the modern Nuxt3 and Spring Boot technology stack, we have created a simple yet powerful reading platform for you. Whether you are a book lover or a creator seeking inspiration, this platform can meet your needs.

Note: This project is a `course assignment`, and the database is `fixed` due to the project requirements, so the functionality is limited and is for learning purposes only.

### Demo Address:

- [Read-Joy - Frontend](https://readjoy.kiwi2333.top)

- [Read-Joy - Admin Backend](https://readjoy.kiwi2333.top/admin)

### Features That May Attract You

- **Elegant Interface**: Our carefully designed user interface ensures an unparalleled visual experience while reading.
- **Smart Recommendations**: Through advanced AI technology, we recommend the most suitable books for you, making every reading experience full of surprises.
- **Diverse Features**: From dark mode to convenient search functions, Read-Joy provides a comprehensive reading experience.
- **Community Interaction**: Share your insights with other readers, participate in discussions, and enhance your reading pleasure.

Join us and start your reading journey to discover more exciting content! Whether it's classic literature or the latest bestsellers, Read-Joy will be your best companion. Let's explore the sea of books together and enjoy every joyful reading moment!

### Default Account

- Username: ikun233
- Password: 123456

## Project Screenshots

- Frontend - Homepage

![Frontend - Homepage](./.doc/img/front/home.png)

- Global Dark Mode

![Global Dark Mode](./.doc/img/front/dark.png)

- Frontend - Search Page

![Frontend - Search Page](./.doc/img/front/search.png)

- Frontend - Detail Page

![Frontend - Detail Page](./.doc/img/front/detail.png)

![Frontend - Resources](./.doc/img/front/res.png)

![Frontend - Comments](./.doc/img/front/comment.png)

- Frontend - AI Recommendations

![Frontend - AI Recommendations](./.doc/img/front/ai.png)

- Frontend - Login

![Frontend - Login](./.doc/img/front/login.png)


- Backend - Management Page

![Backend - Management Page](./.doc/img/back/home.png)

- Backend - Management Dark Mode

![Backend - Management Dark Mode](./.doc/img/back/home-dark.png)

![Backend - Management Dark Mode](./.doc/img/back/home-dark-2.png)

- Backend - User Page

![Backend - User Page](./.doc/img/back/user.png)

- Backend - Book Page

![Backend - Book Page](./.doc/img/back/book.png)

- Backend - Category Page

![Backend - Category Page](./.doc/img/back/category.png)

- Backend - Comment Page

![Backend - Comment Page](./.doc/img/back/comment.png)

- Backend - Resource Page

![Backend - Resource Page](./.doc/img/back/res.png)

 
- Mobile Adaptation

<div style="display:flex;flex-wrap:wrap;">
 <img src="./.doc/img/front/phone1.png" width = "150" style="display:inline-block;margin: 2em;" alt="Mobile Adaptation" />

 <img src="./.doc/img/front/phone2.png" width = "150" style="display:inline-block;margin: 2em;" alt="Mobile Adaptation" />

 <img src="./.doc/img/front/phone-dark.png" width = "150" style="display:inline-block;margin: 2em;" alt="Mobile Adaptation" />

</div>

## ⏳ Getting Started

### ✨ Development

#### 🎨 Frontend

1. Prerequisites
- Node.js 20.0.0+
- pnpm 8.0.0+

2. Install Dependencies and Start
```sh
pnpm install
```
3. Start the Project
```sh
pnpm run dev
```

#### 🖥 Backend

1. Prerequisites
- JDK 17.0+
- MySQL 8.0+
- Redis 6.0+

2. Configuration
- Clone the project to your local machine
- Create a database and import the SQL script
- Start the Redis service
- Modify the database and Redis configuration in `application-dev.application`
- Start the project

3. Start the Project
  Open the project in Idea and start the `com.readjoy.readjoyapi.ReadJoyApiApplication` class.

### 📦 Deployment

#### 1. Frontend Deployment
  - Add and configure `.prod.development` in the frontend root directory (refer to `.env.development`)
  - Run `pnpm run build` to build
  - Deploy to a server, such as Nginx, Apache, Tomcat, etc.

```sh
# Frontend Build
pnpm run build

# Frontend Run
pnpm run start

```

#### 2. Backend Deployment
  - Add and configure `.prod.properties` in the backend root directory (refer to `.dev.properties`)
  - Run `mvn clean package` to build
  - Deploy to a server, such as Tomcat, Jetty, etc.

```sh
# Backend Build
mvn clean package -Dmaven.test.skip=true

# Backend Run
java -jar target/read-joy-api-1.0.0.jar --spring.profiles.active=prod
```

## 🔧 Frontend - Tech Stack

<details>
<summary>Expand</summary>

| Category         | Technology/Component          | Version       |
| ------------ | ----------------- | ------------ |
| Package Manager     | pnpm             | 9.14.4       |
| Framework         | Nuxt3            | ^3.14.1592   |
| UI Component Library    | Element Plus      | ^2.9.1       |
|              | Radix Vue         | ^1.9.11      |
|              | Element Plus Icons | >=0.2.6      |
| State Management     | Pinia            | ^2.3.0       |
|              | Pinia Persisted State | ^4.2.0    |
| Build Tool      | Vite             | ^6.0.2       |
|              | Webpack          | ^5           |
| Static Site Generation  | Nuxi             | -            |
| Server-Side Rendering    | Nitro            | -            |
| CSS Framework     | Tailwind CSS      | ^3.4.17      |
|              | Tailwind Merge    | ^2.5.5       |
|              | Tailwind CSS Animate | ^1.0.7     |
| TypeScript   | TypeScript       | 5.7.2        |
| ESLint Configuration   | @antfu/eslint-config | ^3.12.0     |
|              | @nuxt/eslint      | ^0.7.4       |
|              | @unocss/eslint-config | ^0.65.2    |
| Code Quality      | ESLint           | ^9.17.0      |
|              | Consola          | ^3.2.3       |
| Animation         | @formkit/auto-animate | 0.8.2      |
| Charts         | ECharts          | ^5.5.1       |
| Date Handling     | Dayjs            | ^1.11.13     |
| Markdown Editor | MD-Editor V3    | ^5.1.1       |
| Internationalization       | -                | -            |
| PWA           | @vite-pwa/nuxt   | ^0.10.6      |
| Vue Plugin      | @vueuse/nuxt     | ^12.2.0      |
| Version Control Hooks  | Husky            | -            |
| Code Commit Standard  | lint-staged      | -            |

</details>

## 🖥 Backend - Tech Stack

<details>
<summary>Expand</summary>

| Type          | Library                                            | Version         | Description                              |
|-------------|----------------------------------------------|-------------|-----------------------------------|
| **Main Framework**     | **Spring Boot**                              | **3.3.5**   | **Spring Boot Core Dependency**               |
|             | spring-boot-starter-web                      |             | Provides Web application support, including Tomcat and Spring MVC     |
|             | spring-boot-starter-validation               |             | Provides data validation capabilities                          |
| **Database & ORM** | **MyBatis Plus**                             | **3.5.9**   | **Integrated MyBatis Plus, enhanced MyBatis functionality**   |
|             | mybatis-plus-spring-boot3-starter            |             |                                   |
|             | mybatis-plus-jsqlparser                      |             | MyBatis Plus integrated JSqlParser for extensions, pagination, etc. |
|             | mybatis-plus-join-boot-starter               | 1.5.2       | Provides MyBatis Plus Join support for multi-table queries       |
|             | **MySQL**                                    |             | **MySQL Database Driver**                    |
|             | mysql-connector-j                            | 8.0.29      |                                   |
| **Caching**      | **Redis**                                    | **3.3.5**   | **Provides Redis caching support**                   |
|             | spring-boot-starter-data-redis               |             | Used for API rate limiting, data caching, etc.                      |
| **Security**      | **JWT**                                      | **4.2.1**   | **Provides JWT authentication support**                   |
|             | java-jwt                                     |             | User authentication and authorization                        |
|             | **mica-xss**                                 | **2.7.6**   | **Prevents XSS attacks**                       |
| **Utility Libraries**    | **Hutool**                                   | **5.8.25**  | **A Java utility library providing various tools**            |
|             | hutool-all                                   |             |                                   |
|             | **Lombok**                                   | **1.18.24** | **Simplifies Java object encapsulation**                 |
|             | lombok                                       |             |                                   |
| **API Documentation**   | **Swagger**                                  | **4.4.0**   | **Provides Swagger3 API documentation support**            |
|             | knife4j-openapi3-jakarta-spring-boot-starter |             |                                   |
| **Logging**      | **Log4j2**                                   | **2.17.0**  | **Provides logging support**                     |

</details>


## Trends

![Read-Joy Star History Chart](https://api.star-history.com/svg?repos=KiWi233333/read-joy&type=Date)


## Contribution

Welcome to contribute code to the project! Please ensure to run the `lint` command for code checking before submitting.

## License

This project is licensed under the ``GNU Affero General Public License v3.0``. For details, please refer to the LICENSE file.

We hope you enjoy using this project! If you have any questions, please feel free to contact us.
