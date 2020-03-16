# 基础镜像
FROM java:8
# 复制打包完成后的jar文件到/home目录下
ADD pannet.jar /home/pannet.jar
# 挂载/home/file目录到主机
VOLUME /home/file
# 设置时区
RUN echo "Asia/Shanghai" > /etc/timezone
# 启动容器时执行
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home/pannet.jar"]