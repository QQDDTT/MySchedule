# 使用本地 Openjdk 镜像
FROM openjdk:17

# jar 文件复制
COPY target/*.jar app.jar

#  生成日志路径
RUN mkdir -p /home/logs

# 设置环境变量，让日志写入指定目录（可选，需确保应用支持）
ENV LOG_HOME=/home/logs

# 暴露端口 8090
EXPOSE 8090

# 运行 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar"]