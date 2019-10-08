package com.idcard.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: upload
 * @description:
 * @author: wyb
 * @create: 2019-09-25 16:49
 **/
@Data
@NoArgsConstructor
public class IdUser {

    private Integer userId;

    private String userName;

    private String file1;

    private String file2;   //附件二

    private String file3;   //附件三

    private String type;    //证件类型

    private String number;  //证件号码

    private String status;  //审核状态

    private String content; //审核意见

}
