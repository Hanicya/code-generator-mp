package cn.iocoder.yudao.framework.mybatis.core.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author HuangYiCheng
 * @since 2026/4/16
 */
@Data
public class BaseDO {

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String creator;

    private String updater;

    private Boolean deleted;

}
