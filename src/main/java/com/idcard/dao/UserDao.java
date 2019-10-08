package com.idcard.dao;


import com.idcard.entity.IdUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: upload
 * @description:
 * @author: wyb
 * @create: 2019-09-25 18:56
 **/
@Mapper
public interface UserDao {

    int insert(@Param("userName") String userName,@Param("type") String type,@Param("number") String number,
               @Param("file1") String file1,@Param("file2") String file2,@Param("file3") String file3,@Param("status") String status);

    List<IdUser> selectAll();

    IdUser getUserByUserId(Integer userId);

    int update(IdUser user);

    int deleteById(Integer userId);
}
