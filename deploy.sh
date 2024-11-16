#!/bin/bash

# 定义颜色
ORANGE='\033[33m'
NC='\033[0m' # No Color

# 定义变量
APP_NAME="MySchedule"
DOCKER_IMAGE_NAME="myschedule-image"
DOCKER_CONTAINER_NAME="myschedule-container"
SERVER_PORT=8090

# 检查数据库容器是否运行
if [ "$(docker ps -q -f name=mysql_container)" ]; then
    echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}数据库容器已运行，跳过启动...${NC}"
else
    echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}启动数据库容器...${NC}"
    docker start mysql_container
    echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}数据库容器已启动！${NC}"
fi

# 第一步：停止已存在的容器
if [ "$(docker inspect --format '{{.State.Running}}' $DOCKER_CONTAINER_NAME)" ]; then
    echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}停止已存在的容器...${NC}"
    docker stop $DOCKER_CONTAINER_NAME
fi

# 第二步：删除已存在的容器
if [ "$(docker ps -a -f name=$DOCKER_CONTAINER_NAME)" ]; then
    echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}删除已存在的容器...${NC}"
    docker rm $DOCKER_CONTAINER_NAME
fi

# 第三步：删除已存在的镜像
if [ "$(docker images -q $DOCKER_IMAGE_NAME)" ]; then
    echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}删除已存在的镜像...${NC}"
    docker rmi -f $DOCKER_IMAGE_NAME
fi

# 第四步：Maven 打包
echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}开始 Maven 打包...${NC}"
mvn clean package

if [ $? -ne 0 ]; then
    echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Maven 打包失败！${NC}"
    exit 1
fi

echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Maven 打包成功！${NC}"

# 第五步：构建 Docker 镜像
echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}构建 Docker 镜像...${NC}"
docker build -t $DOCKER_IMAGE_NAME .

if [ $? -ne 0 ]; then
    echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Docker 镜像构建失败！${NC}"
    exit 1
fi

echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Docker 镜像构建成功！${NC}"

# 第六步：创建 Docker 容器
IP_ADDRESS=$(hostname -I | awk '{print $1}')
echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}创建 Docker 容器...${NC}"
docker run -d --name $DOCKER_CONTAINER_NAME -p $SERVER_PORT:$SERVER_PORT --add-host=localhost:$IP_ADDRESS $DOCKER_IMAGE_NAME

if [ $? -ne 0 ]; then
    echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Docker 容器创建失败！${NC}"
    exit 1
fi

echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Docker 容器创建成功！${NC}"

echo "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}部署完成！${NC}"


# 等待容器启动
echo "${ORANGE}等待响应${NC}"
MAX_TIME=30
WAIT_TIME=0
while [ $WAIT_TIME -le $MAX_TIME ]; do
    sleep 1
    # 撤销上一行内容
    echo "\033[A\033[K${ORANGE}等待中... ${WAIT_TIME} 秒 ${NC}"
    if curl -s http://localhost:$SERVER_PORT/ > /dev/null; then
        echo "${ORANGE}启动成功${NC}"
        # 打开链接
        echo "${ORANGE}打开浏览器${NC}"
        explorer.exe http://localhost:$SERVER_PORT/
        break
    fi
    WAIT_TIME=$((WAIT_TIME+1))
done

if [ $WAIT_TIME -gt $MAX_TIME ]; then
    echo "${ORANGE}启动超时，未检测到服务响应${NC}"
fi