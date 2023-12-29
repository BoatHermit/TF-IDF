package org.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.model.po.Mark;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkMapper extends BaseMapper<Mark> {
    @Select("select * from hum_mark where ex_id=#{externalId} and in_id=#{internalId}")
    Mark findByExternalIdAndInternalId(Long externalId, Long internalId);
}
