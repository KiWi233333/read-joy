-- 创建用户表（users）
CREATE TABLE `users` (
  `user_id` INT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(255) NOT NULL UNIQUE COMMENT '用户名',
  `email` VARCHAR(255) NOT NULL UNIQUE COMMENT '邮箱',
  `password` VARCHAR(255) NOT NULL COMMENT '加密存储的密码',
  `nickname` VARCHAR(255) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `bio` TEXT COMMENT '个人简介',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  INDEX `idx_username` (`username`),
  INDEX `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建课程分类表（categories）
CREATE TABLE `categories` (
  `category_id` INT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` VARCHAR(255) NOT NULL COMMENT '分类名称',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`category_id`),
  INDEX `idx_category_name` (`category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程分类表';

-- 创建课程表（courses）
CREATE TABLE `courses` (
  `course_id` INT NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `course_name` VARCHAR(255) NOT NULL COMMENT '课程名称',
  `description` TEXT COMMENT '课程描述',
  `cover_image` VARCHAR(255) COMMENT '封面图片URL',
  `teacher_name` VARCHAR(255) COMMENT '教师姓名',
  `category_id` INT COMMENT '课程分类ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`course_id`),
  INDEX `idx_course_name` (`course_name`),
  INDEX `idx_category_id` (`category_id`),
  FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 创建用户课程关联表（user_courses）
CREATE TABLE `user_courses` (
  `user_id` INT NOT NULL COMMENT '用户ID',
  `course_id` INT NOT NULL COMMENT '课程ID',
  `joined_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `progress` TEXT COMMENT '学习进度',
  PRIMARY KEY (`user_id`, `course_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_course_id` (`course_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户课程关联表';

-- 创建课程资源表（course_resources）
CREATE TABLE `course_resources` (
  `resource_id` INT NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `course_id` INT NOT NULL COMMENT '课程ID',
  `resource_name` VARCHAR(255) NOT NULL COMMENT '资源名称',
  `resource_type` VARCHAR(50) COMMENT '资源类型',
  `resource_url` VARCHAR(255) COMMENT '资源URL或文件路径',
  `upload_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `description` TEXT COMMENT '资源描述',
  `file_size` BIGINT COMMENT '文件大小',
  PRIMARY KEY (`resource_id`),
  INDEX `idx_course_id` (`course_id`),
  FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程资源表';

-- 创建课程评论表（course_comments）
CREATE TABLE `course_comments` (
  `comment_id` INT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` INT NOT NULL COMMENT '用户ID',
  `course_id` INT NOT NULL COMMENT '课程ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`comment_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_course_id` (`course_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程评论表';