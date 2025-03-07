#!/bin/bash

# 定义颜色
ORANGE='\033[33m'
NC='\033[0m' # No Color

# 最大等待时间
MAX_TIME=30
WAIT_TIME=0

# 定义变量
APP_NAME="MySchedule"
DOCKER_IMAGE_NAME="myschedule-image"
DOCKER_CONTAINER_NAME="myschedule-container"
SERVER_PORT=8090

# 定义数据库变量
DB_CONTAINER_NAME="mysql_container"
DB_HOST="localhost"
DB_PORT=3306
DB_USER="root"
DB_PASSWORD="password"
DATABASE_NAME="schedule_db"

# 定义 SQL 文件路径
SQL_CONTEXT_DB="./sql/Database.sql"
SQL_CONTEXT_TABLE="./sql/Table.sql"
SQL_CONTEXT_DATA="./sql/InsertData.sql"

# 检查数据库容器是否运行
if [ "$(docker ps -q -f name=$DB_CONTAINER_NAME)" ]; then
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}数据库容器已运行.${NC}"
elif [ "$(docker ps -aq -f name=$DB_CONTAINER_NAME)" ]; then
    # 如果容器存在但未运行，则启动容器
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}启动数据库容器...${NC}"
    docker start $DB_CONTAINER_NAME
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}数据库容器已启动！${NC}"
else
    # 如果容器不存在，创建并启动容器
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}创建并启动数据库容器...${NC}"
    docker run -d --name $DB_CONTAINER_NAME -e MYSQL_ROOT_PASSWORD=$DB_PASSWORD -p $DB_PORT:$DB_PORT mysql:8.0.33
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}数据库容器已创建并启动！${NC}"
fi

# 检测 MySQL 服务是否启动
echo -e "${ORANGE}检测 MySQL 服务启动...${NC}"
while [ $WAIT_TIME -le $MAX_TIME ]; do
sleep 1
    echo -e "\033[A\033[K${ORANGE}等待中... ${WAIT_TIME} 秒 ${NC}"
    
    # 使用 mysqladmin ping 检测 MySQL 是否可用
    if docker exec $DB_CONTAINER_NAME mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "SELECT 1;" &> /dev/null; then
        echo -e "${ORANGE}MySQL 服务器已启动！${NC}"
        break
    fi

    WAIT_TIME=$((WAIT_TIME+1))
done

# 检查数据库容器中是否存在数据库
if docker exec $DB_CONTAINER_NAME mysql -u $DB_USER -p$DB_PASSWORD -e "SHOW DATABASES LIKE '$DATABASE_NAME';" | grep "$DATABASE_NAME" > /dev/null; then
    # 如果数据库存在，则跳过
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}数据库已存在！${NC}"
else
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}导入 SQL 文件...${NC}"
    docker exec -i $DB_CONTAINER_NAME mysql -u $DB_USER -p$DB_PASSWORD < $SQL_CONTEXT_DB
    docker exec -i $DB_CONTAINER_NAME mysql -u $DB_USER -p$DB_PASSWORD $DATABASE_NAME < $SQL_CONTEXT_TABLE
    docker exec -i $DB_CONTAINER_NAME mysql -u $DB_USER -p$DB_PASSWORD $DATABASE_NAME < $SQL_CONTEXT_DATA
fi

echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}数据库已就绪！${NC}"

# 第一步：停止已存在的容器
if [ "$(docker inspect --format '{{.State.Running}}' $DOCKER_CONTAINER_NAME)" ]; then
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}停止已存在的容器...${NC}"
    docker stop $DOCKER_CONTAINER_NAME
fi

# 第二步：删除已存在的容器
if [ "$(docker ps -a -f name=$DOCKER_CONTAINER_NAME)" ]; then
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}删除已存在的容器...${NC}"
    docker rm $DOCKER_CONTAINER_NAME
fi

# 第三步：删除已存在的镜像
if [ "$(docker images -q $DOCKER_IMAGE_NAME)" ]; then
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}删除已存在的镜像...${NC}"
    docker rmi -f $DOCKER_IMAGE_NAME
fi

# 第四步：Maven 打包
echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}开始 Maven 打包...${NC}"
mvn clean package

if [ $? -ne 0 ]; then
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Maven 打包失败！${NC}"
    exit 1
fi

echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Maven 打包成功！${NC}"

# 第五步：构建 Docker 镜像
echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}构建 Docker 镜像...${NC}"
docker build -t $DOCKER_IMAGE_NAME .

if [ $? -ne 0 ]; then
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Docker 镜像构建失败！${NC}"
    exit 1
fi

echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Docker 镜像构建成功！${NC}"

# 第六步：创建 Docker 容器
IP_ADDRESS=$(hostname -I | awk '{print $1}')
echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}创建 Docker 容器...${NC}"
docker run -d --name $DOCKER_CONTAINER_NAME -p $SERVER_PORT:$SERVER_PORT --add-host=localhost:$IP_ADDRESS $DOCKER_IMAGE_NAME

if [ $? -ne 0 ]; then
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Docker 容器创建失败！${NC}"
    exit 1
fi

echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}Docker 容器创建成功！${NC}"

echo -e "$(date +'%Y-%m-%d %H:%M:%S') ${ORANGE}部署完成！${NC}"


# 等待容器启动
echo -e "${ORANGE}等待响应${NC}"
MAX_TIME=30
WAIT_TIME=0
while [ $WAIT_TIME -le $MAX_TIME ]; do
    sleep 1
    # 撤销上一行内容
    echo -e "\033[A\033[K${ORANGE}等待中... ${WAIT_TIME} 秒 ${NC}"
    if curl -s http://localhost:$SERVER_PORT/ > /dev/null; then
        echo -e "${ORANGE}启动成功${NC}"
        # 打开链接
        echo -e "${ORANGE}打开浏览器${NC}"
        /mnt/c/Windows/explorer.exe "http://localhost:$SERVER_PORT/"
        break
    fi
    WAIT_TIME=$((WAIT_TIME+1))
done

if [ $WAIT_TIME -gt $MAX_TIME ]; then
    echo -e "${ORANGE}启动超时，未检测到服务响应${NC}"
fi