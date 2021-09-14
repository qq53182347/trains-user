# 第一阶段：环境构建
FROM gradle:7.1.1-jdk8 AS builder
WORKDIR /app
ADD ./ /app
RUN chmod 777 /app
RUN ./gradlew build --stacktrace

# 第二阶段，最小运行时环境，只需要jre
FROM openjdk:8-jre-alpine
LABEL maintainer="xx@xx.com"

# 从上一个阶段复制内容
COPY --from=builder /app/build/libs/*.jar /trains-user.jar

# 修改时区,保证唯一
RUN ln -sf /usr/share/zoneinfo/Asia/Beijing /etc/localtime && echo 'Asia/Beijing' >/etc/timezone && touch /trains-user.jar

ENV JAVA_OPTS=""
ENV PARAMS=""
# 运行jar包
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /trains-user.jar $PARAMS" ]