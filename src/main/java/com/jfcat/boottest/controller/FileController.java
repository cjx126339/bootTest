package com.jfcat.boottest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfcat.boottest.result.ResponseResult;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/picture")
@CrossOrigin
public class FileController {

    //图片的物理存储路径
    private static final String filePath = "D:/aaa/";

    //tomcat静态虚拟路径
    private static final String vFilePath = "http://localhost:8888/pic/";


    @RequestMapping("/multiImport")
    public ResponseResult multiImport(@RequestParam("uploadFile") MultipartFile[] uploadFile) {
        Map<String, Object> result = new HashMap<>();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < uploadFile.length; i++) {
            String fileName = uploadFile[i].getOriginalFilename();
            //获取文件后缀
            int lastIndexOf = fileName.lastIndexOf(".");
            String suffix = fileName.substring(lastIndexOf + 1);
            if (!suffix.equals("jpg")&&!suffix.equals("png")) {
                System.out.println("suffix error");
                return ResponseResult.build(0,"后缀名错误");
            }
            //date文件夹，方便管理
            String dateFile = new SimpleDateFormat("yyyyMMdd").format(new Date());
            System.out.println("dateFile = " + dateFile);
            String newFileName = filePath + dateFile;
            File newFile = new File(newFileName);
            //判断文件夹是否存在
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            fileName = UUID.randomUUID().toString().replace("-","") + fileName;
            String newFilePath = newFileName + "/" + fileName;
            list.add(i,vFilePath + dateFile + "/" + fileName);
            result.put("path",list);
            try {
                uploadFile[i].transferTo(new File(newFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseResult.ok(result);
    }



    @RequestMapping("/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile[] file) {
        //返回Map来获取图片的虚拟路径
        Map<String ,Object> result = new HashMap<>();
        List<String > list = new ArrayList<>();
        for (int i = 0; i < file.length; i++) {
            //获取图片的名字
            String fileName = file[i].getOriginalFilename();
            //获取文件后缀名
            String suffix = getSuffix(fileName);
            //判断后缀名
            if (!suffix.equals("jpg") && !suffix.equals("png")) {
                System.out.println("suffix error");
                return ResponseResult.build(0,"后缀名错误");
            }

            //date文件夹，方便管理
            String dateFile = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String newFileName = filePath + dateFile;
            File newFile = new File(newFileName);
            //判断文件夹是否存在
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            //为文件名添加UUID防止名字重复
            fileName = UUID.randomUUID().toString().replace("-","") + fileName;
            String newFilePath = newFileName + "/" + fileName;
            list.add(i,vFilePath + dateFile + "/" + fileName);
            result.put("path",list);
            try {
                file[i].transferTo(new File(newFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseResult.ok(result);
    }

    @RequestMapping("/upload1")
    public ResponseResult upload1(@RequestParam("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        int index;
        byte[] bytes = new byte[1024];
        String dateFile = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String newFileName = filePath + dateFile + UUID.randomUUID().toString() + ".png";
        File newFile = new File(newFileName);
        FileOutputStream outputStream = new FileOutputStream(newFile);
        while ((index = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, index);
            outputStream.flush();
        }
        outputStream.close();
        inputStream.close();
        return ResponseResult.ok();
    }

    public static String getSuffix(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        return fileName.substring(lastIndexOf + 1);
    }

    @RequestMapping(value = "/postBase64Img",method = RequestMethod.POST)
    public ResponseResult base64Img(@RequestParam("myPhoto") String base64Data, HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        String dataPrix = "";
        String data = "";
        System.out.println("对数据进行判断");
        if (null == base64Data || "".equals(base64Data)) {
            throw new Exception("上传失败，图片数据为空");
        }else {
            String[] d = base64Data.split("base64,");
            if (d.length == 2) {
                dataPrix = d[0];
                data = d[1];
            }else {
                throw new Exception("上传失败，数据不合法");
            }
        }
        String suffix = "";
        if("data:image/jpeg;".equalsIgnoreCase(dataPrix)){//data:image/jpeg;base64,base64编码的jpeg图片数据
            suffix = ".jpg";
        } else if("data:image/x-icon;".equalsIgnoreCase(dataPrix)){//data:image/x-icon;base64,base64编码的icon图片数据
            suffix = ".ico";
        } else if("data:image/gif;".equalsIgnoreCase(dataPrix)){//data:image/gif;base64,base64编码的gif图片数据
            suffix = ".gif";
        } else if("data:image/png;".equalsIgnoreCase(dataPrix)){//data:image/png;base64,base64编码的png图片数据
            suffix = ".png";
        }else{
            throw new Exception("上传图片格式不合法");
        }
        String imgName = UUID.randomUUID().toString();
        String tempFileName = imgName + suffix;
        //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
        byte[] bs = Base64Utils.decodeFromString(data);
        FileOutputStream fileOutputStream = null;
        try {
            File file = null;
            //使用apache提供的工具类操作流
            String url = request.getSession().getServletContext().getRealPath("/");
            System.out.println("url = " + url);
            String paths = vFilePath + tempFileName;
            fileOutputStream = new FileOutputStream(filePath + tempFileName);
            fileOutputStream.write(bs);
            map.put("dizhi",paths);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        return ResponseResult.ok(map);
    }

}
