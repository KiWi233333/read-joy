@echo off
:: 设置字符编码为 UTF-8
chcp 65001

setlocal enabledelayedexpansion

:: 获取当前目录路径
set CURRENT_DIR=%cd%

:: 提示用户输入 MySQL 用户名和密码
set /p MYSQL_USER=请输入MySQL用户名: 
set /p MYSQL_PASS=请输入MySQL密码: 

:: 设置 MySQL 初始化脚本路径为当前目录下的 \database\insight_classroom.sql
set SQL_FILE=%CURRENT_DIR%\database\insight_classroom.sql

:: 设置 MySQL 数据库连接参数
set MYSQL_HOST=localhost
set MYSQL_PORT=3306

:: 设置目标数据库名
set DATABASE_NAME=insight_classroom  :: 请根据实际情况修改

:: 检查初始化 SQL 文件是否存在
if not exist "%SQL_FILE%" (
    echo 错误：SQL 文件不存在，请检查路径。
    exit /b 1
)

:: 检查数据库是否存在
echo 正在检查数据库是否存在...
mysql -u %MYSQL_USER% -p%MYSQL_PASS% -h %MYSQL_HOST% -P %MYSQL_PORT% -e "SHOW DATABASES LIKE '%DATABASE_NAME%';" > nul 2>&1

:: 判断数据库是否存在
if %errorlevel% neq 0 (
    echo 错误：无法连接到 MySQL，请检查用户名、密码和主机信息。
    exit /b 1
)

:: 判断数据库是否存在，如果不存在，则提示用户是否创建
for /f "tokens=1" %%i in ('mysql -u %MYSQL_USER% -p%MYSQL_PASS% -h %MYSQL_HOST% -P %MYSQL_PORT% -e "SHOW DATABASES LIKE '%DATABASE_NAME%'"') do set "DB_EXISTS=%%i"

if "!DB_EXISTS!" == "%DATABASE_NAME%" (
    echo 数据库 "%DATABASE_NAME%" 已存在。
) else (
    echo 数据库 "%DATABASE_NAME%" 不存在。
    set /p CREATE_DB=是否创建数据库 "%DATABASE_NAME%" (Y/N)? 
    if /i "!CREATE_DB!" == "Y" (
        echo 正在创建数据库...
        mysql -u %MYSQL_USER% -p%MYSQL_PASS% -h %MYSQL_HOST% -P %MYSQL_PORT% -e "CREATE DATABASE %DATABASE_NAME%;" > nul 2>&1
        if %errorlevel% neq 0 (
            echo 创建数据库失败，请检查权限和错误信息。
            exit /b 1
        ) else (
            echo 数据库创建成功。
        )
    ) else (
        echo 数据库创建已取消。
        exit /b 1
    )
)

:: 执行初始化 SQL 脚本
echo 正在执行数据库初始化...
mysql -u %MYSQL_USER% -p%MYSQL_PASS% -h %MYSQL_HOST% -P %MYSQL_PORT% -D %DATABASE_NAME% < "%SQL_FILE%"

:: 检查 SQL 执行结果
if %errorlevel% neq 0 (
    echo 数据库初始化失败，请检查错误信息。
    exit /b 1
)

:: 进入前端项目目录
cd /d %CURRENT_DIR%\insight-classroom-web\

:: 安装前端依赖
echo 正在安装前端依赖...
pnpm install

:: 检查安装结果
if %errorlevel% neq 0 (
    echo 前端依赖安装失败，请检查错误信息。
    exit /b 1
)

:: 启动前端开发服务器
echo 正在启动前端开发服务器...
pnpm dev

:: 完成
echo 项目初始化完成，前端服务已启动。
pause
