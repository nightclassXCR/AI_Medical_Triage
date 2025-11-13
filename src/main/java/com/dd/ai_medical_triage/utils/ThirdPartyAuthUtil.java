package com.dd.ai_medical_triage.utils;

import com.dd.ai_medical_triage.enums.SimpleEnum.ThirdPartyTypeEnum;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 第三方授权工具类
 */
@Component
public class ThirdPartyAuthUtil {
    /**
     * 获取第三方OpenID（模拟实现）
     */
    public String getOpenId(ThirdPartyTypeEnum platform, String code) {
        // 实际需调用微信/QQ官方API
        return UUID.randomUUID().toString().replace("-", "");
    }

}