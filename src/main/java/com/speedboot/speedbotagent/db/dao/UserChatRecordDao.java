package com.speedboot.speedbotagent.db.dao;

import com.speedboot.speedbotagent.db.entity.UserChatRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserChatRecordDao {
    void insert(UserChatRecord  userChatRecord);
}
