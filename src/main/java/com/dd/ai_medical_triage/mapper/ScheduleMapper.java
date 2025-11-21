package com.dd.ai_medical_triage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dd.ai_medical_triage.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    /**
     * 【核心防超卖代码】
     * 原子性扣减库存：利用数据库行锁
     * 条件：id 匹配 且 已售数量 < 总数量
     * * @param id 排班ID
     * @return 影响行数 (1表示扣减成功，0表示扣减失败即无号)
     */
    @Update("UPDATE doctor_schedule SET sold_count = sold_count + 1 WHERE id = #{id} AND sold_count < total_quota")
    int deductStock(@Param("id") Long id);
}
