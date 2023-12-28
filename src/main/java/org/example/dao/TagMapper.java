package org.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.model.po.Tag;
import org.springframework.stereotype.Repository;

@Repository
public interface TagMapper extends BaseMapper<Tag> {
    // todo
    @Select("select * from tag_file where external_title=#{externalTitle} and internal_id=#{internalId}")
    Tag findByExternalTitleAndInternalId(String externalTitle, Long internalId);
}
