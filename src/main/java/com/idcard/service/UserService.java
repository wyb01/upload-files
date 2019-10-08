package com.idcard.service;

import com.idcard.entity.IdUser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    int insert(String userName,String type,String number,String file1, String file2,String file3,String status);

    void executeUpload(String uploadDir, MultipartFile multipartFile);

    List<IdUser> getAll();

    IdUser getUserByUserId(Integer userId);

    int update(IdUser user);

    int deleteById(Integer userId);
}
