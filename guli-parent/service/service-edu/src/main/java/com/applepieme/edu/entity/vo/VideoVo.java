package com.applepieme.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author applepieme
 * @date 2021/10/3 16:44
 */
@Data
public class VideoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String videoSourceId;
    private Integer isFree;
}
