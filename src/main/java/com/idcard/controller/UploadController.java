package com.idcard.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.idcard.entity.IdUser;
import com.idcard.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private UserService userService;

    //设置上传文件夹
    @Value("${file.path}")
    String uploadPath;

    @ApiOperation(value = "首页",notes = "首页")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "null",value = "")
    })
    @RequestMapping("/index")
    public String index(){
        return "/index";
    }

//    @ApiOperation(value = "上传文件",notes = "")
//    @ApiImplicitParams({
//    	@ApiImplicitParam(name = "null",value = "")
//    })
//    @RequestMapping(value = "/upload",method = RequestMethod.POST)
//    public String upload(HttpServletRequest request,MultipartFile file)
//    {
//        try {
//            //上传目录地址
//            String uploadDir = request.getSession().getServletContext().getRealPath("/") +"upload/";
//            //如果目录不存在，自动创建文件夹
//            File dir = new File(uploadDir);
//            if(!dir.exists())
//            {
//                dir.mkdir();
//            }
//            //调用上传方法
//            userService.executeUpload(uploadDir,file);
//        }catch (Exception e)
//        {
//            //打印错误堆栈信息
//            e.printStackTrace();
//            return "上传失败";
//        }
//        return "index";
//    }

    /**
     * 上传多个文件
     * @param request 请求对象
     * @param file 上传文件集合
     * @return
     */
    @RequestMapping(value = "/uploads",method = RequestMethod.POST)
    @ResponseBody
    public String uploads(HttpServletRequest request, MultipartFile[] file, IdUser user) {
        for(MultipartFile fil : file){
            if(fil .isEmpty()){
                return "附件不能为空！";
            }
        }
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            //上传目录地址
//            String uploadDir = request.getSession().getServletContext().getRealPath("/") +"upload/";
            String uploadDir = uploadPath+"upload/";

            //如果目录不存在，自动创建文件夹
            File dir = new File(uploadDir);
            if(!dir.exists())
            {
                dir.mkdir();
            }
            //遍历文件数组执行上传
            for (int i =0;i<file.length;i++) {
                if(file[i] != null) {
                    //调用上传方法
                    userService.executeUpload(uploadDir, file[i]);
                }
            }
            user.setFile1(uploadDir+file[0].getOriginalFilename());
            user.setFile2(uploadDir+file[1].getOriginalFilename());
            user.setFile3(uploadDir+file[2].getOriginalFilename());
            user.setStatus("0");
            userService.insert(user.getUserName(),user.getType(),user.getNumber(),user.getFile1(),user.getFile2(),user.getFile3(),user.getStatus());
            map.put("status",200);
            map.put("msg","上传成功,请等待审核！");
        }catch (Exception e)
        {
            //打印错误堆栈信息
            e.printStackTrace();
            map.put("status",500);
            map.put("msg","上传失败！");
        }
        String  content= JSON.toJSONString(map);
        return content;
    }

    @GetMapping("/check/{userId}")
    public String check(@PathVariable("userId") Integer userId, Model model){
        IdUser user = userService.getUserByUserId(userId);
        int no1=user.getFile1().lastIndexOf("/");
        String file1=user.getFile1().substring(no1);
        int no2=user.getFile2().lastIndexOf("/");
        String file2=user.getFile2().substring(no2);
        int no3=user.getFile3().lastIndexOf("/");
        String file3=user.getFile3().substring(no3);
        user.setFile1(file1);
        user.setFile2(file2);
        user.setFile3(file3);
        model.addAttribute("user",user);
        return "check";
    }

    @PostMapping("/checkIdCard")
    public @ResponseBody String checkIdCard(IdUser user){
        IdUser userCheck = userService.getUserByUserId(user.getUserId());
        userCheck.setStatus(user.getStatus());
        userCheck.setContent(user.getContent());
        userService.update(userCheck);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status",200);
        map.put("msg","审核成功！");
        String  param= JSON.toJSONString(map);
        return param;
    }

    @GetMapping("/deleteById/{userId}")
    public String deleteById(@PathVariable("userId") Integer userId, Model model){
        int result = userService.deleteById(userId);
        return "redirect:/getAllUsers";
    }

    @RequestMapping("/toUpload")
    public String toUpload(){
        return "upload";
    }

    @RequestMapping("/getAllUsers")
    public String getAll(Model model,@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<IdUser> users = userService.getAll();
        PageInfo pageInfo = new PageInfo<IdUser>(users, 5);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageNum", pageInfo.getPageNum());
        model.addAttribute("pageSize", pageInfo.getPageSize());
        //是否是第一页
        model.addAttribute("isFirstPage", pageInfo.isIsFirstPage());
        //获得总页数
        model.addAttribute("totalPages", pageInfo.getPages());
        //是否是最后一页
        model.addAttribute("isLastPage", pageInfo.isIsLastPage());
        model.addAttribute("total", pageInfo.getTotal());
        return "list";
    }
}
