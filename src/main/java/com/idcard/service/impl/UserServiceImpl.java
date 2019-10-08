package com.idcard.service.impl;

import com.idcard.dao.UserDao;
import com.idcard.entity.IdUser;
import com.idcard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @program: upload
 * @description:
 * @author: wyb
 * @create: 2019-09-25 18:55
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public int insert(String userName,String type,String number,String file1, String file2,String file3,String status){

            return userDao.insert(userName,type,number,file1,file2,file3,status);

        }

    /**
     * 提取上传方法为公共方法
     * @param uploadDir 上传文件目录
     * @param file 上传对象
     * @throws Exception
     */
    @Override
    public void executeUpload(String uploadDir, MultipartFile file) {
        try {
            //文件后缀名
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            //上传文件名
            //String filename = UUID.randomUUID() + suffix;
            String filename = file.getOriginalFilename();
            //服务器端保存的文件对象
            File serverFile = new File(uploadDir + filename);
            //将上传的文件写入到服务器端文件内
            file.transferTo(serverFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<IdUser> getAll(){
        return userDao.selectAll();
    }

    public IdUser getUserByUserId(Integer userId){
        return userDao.getUserByUserId(userId);
    }

    public int  update(IdUser user){
        return userDao.update(user);
    }

    public int deleteById(Integer userId){
        return userDao.deleteById(userId);
    }

}
