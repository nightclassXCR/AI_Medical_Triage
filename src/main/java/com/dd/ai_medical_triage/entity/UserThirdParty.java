package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dd.ai_medical_triage.enums.SimpleEnum.ThirdPartyTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 第三方账号关联实体
 * 对应数据库表：user_third_party
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_third_party")
public class UserThirdParty {

    /** 自增主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 关联平台用户ID（外键关联user.user_id） */
    private Long userId;

    /** 第三方平台类型（WECHAT/QQ/ALIPAY） */
    private ThirdPartyTypeEnum thirdType;

    /** 第三方平台唯一标识（如微信openid） */
    private String openid;

    /** 第三方临时凭证（加密存储） */
    private String accessToken;

    /** 备注 */
    private String remark;

    /** 绑定时间 */
    private LocalDateTime bindTime;

    /** 绑定状态（1-有效，0-已解绑） */
    private Integer isValid;

}
