package com.local.MySchedule.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.local.MySchedule.common.ScheduleException;
import com.local.MySchedule.dao.TypeMapper;
import com.local.MySchedule.entity.ScheduleType;

@Service
public class TypeService {

    private static Logger LOGGER = LoggerFactory.getLogger(TypeService.class);

    @Autowired
    private TypeMapper typeMapper;
    /**
     * 获取所有的日程类型
     * @return 日程类型列表
     * @throws ScheduleException
     */
    public List<ScheduleType> getType () throws ScheduleException{
        try {
            List<ScheduleType> scheduleTypes = typeMapper.selectScheduleTypes();
            LOGGER.info("get schedule types size {}", scheduleTypes.size());
            return scheduleTypes;
        } catch (Exception e) {
            throw new ScheduleException("Select schedule types failed !");
        }
    }

    /**
     * 根据类型ID获取日程类型
     * @param typeId 类型ID
     * @return 日程类型
     * @throws ScheduleException
     */
    public ScheduleType getTypeById (int TypeId) throws ScheduleException{
        try {
            return typeMapper.selectScheduleTypeById(TypeId);
        } catch (Exception e) {
            LOGGER.error("Get type id : \" + TypeId + \" failed !");
            throw new ScheduleException("Get type id : " + TypeId + " failed !");
        }
    }

    /**
     * 创建新的日程类型
     * @param typeName 类型名称
     * @param typeDescription 类型描述
     * @throws ScheduleException
     */
    public void createScheduleType (String typeName, String typeDescription, String bgColor) throws ScheduleException{
        List<ScheduleType> types = getType();
        for (ScheduleType type : types) {
            if (type.getTypeName().equals(typeName)) {
                LOGGER.error("Type name : " + typeName + " is already exists !");
                throw new ScheduleException("Type name : " + typeName + " is already exists !");
            }
        }
        ScheduleType type = new ScheduleType();
        type.setTypeName(typeName);
        type.setTypeDescription(typeDescription);
        type.setBgColor(bgColor);
        boolean result = typeMapper.createScheduleType(type);
        if (!result) {
            LOGGER.error("Create schedule type failed !");
            throw new ScheduleException("Create schedule type failed !");
        }
    }

    /**
     * 更新日程类型
     * @param typeId 类型ID
     * @param typeName 类型名称
     * @param typeDescription 类型描述
     * @throws ScheduleException
     */
    public void updateScheduleType (int typeId, String typeName, String typeDescription, String bgColor) throws ScheduleException {
        ScheduleType type = getTypeById(typeId);
        if (type == null || type.getTypeId() == 0) {
            throw new ScheduleException("Type id : " + typeId + "is not exists");
        }
        type.setTypeName(typeName);
        type.setTypeDescription(typeDescription);
        type.setBgColor(bgColor);
        boolean result = typeMapper.updateScheduleType(type);
        if (!result) {
            LOGGER.error("Update schedule type failed !");
            throw new ScheduleException("Update schedule type failed !");
        }
    }

    /**
     * 删除日程类型
     * @param typeId 类型ID
     * @throws ScheduleException
     */
    public void deleteScheduleType (int typeId) throws ScheduleException {
        ScheduleType type = getTypeById(typeId);
        if (type == null || type.getTypeId() == 0) {
            throw new ScheduleException("Type id : " + typeId + "is not exists");
        }
        boolean result = typeMapper.deleteScheduleType(typeId);
        if (!result) {
            LOGGER.error("Delete schedule type failed !");
            throw new ScheduleException("Delete schedule type failed !");
        }
    }

}
