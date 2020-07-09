package com.java.scm.util;

import com.java.scm.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author hujunhui
 * @date 2020/7/7
 */
@Slf4j
public class FileUtil {

    /**
     * 文件读取
     * @param txtPath 文件路径
     * @return 返回
     */
    public static String read(String txtPath) {
        File file = new File(txtPath);
        if(file.isFile() && file.exists()){
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){

                StringBuilder sb = new StringBuilder();
                String text;
                while((text = bufferedReader.readLine()) != null){
                    sb.append(text);
                }
                return sb.toString();
            } catch (Exception e) {
                log.error("文件读取异常",e);
                throw new BusinessException("文件读取失败");
            }
        }else{
            throw new BusinessException("文件不存在");
        }
    }

    public static void write(String txtPath,String fileName ,String content){
        File dir = new File(txtPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(txtPath,fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            if(! file.exists()){
                //判断文件是否存在，如果不存在就新建一个txt
                boolean status = file.createNewFile();
                if(!status){
                    throw new BusinessException("文件创建失败");
                }
            }
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new BusinessException("文件写入失败");
        }
    }


    /**
     * 文件写入
     * @param txtPath 文件路径
     * @param content 文件内容
     */
    public static void write(String txtPath,String content){
        String dir = txtPath.substring(0,txtPath.lastIndexOf(File.separator));
        System.out.println(dir);
        File file = new File(txtPath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            if(! file.exists()){
                //判断文件是否存在，如果不存在就新建一个txt
                boolean status = file.createNewFile();
                if(!status){
                    throw new BusinessException("文件创建失败");
                }
            }
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new BusinessException("文件写入失败");
        }
    }

    public static void main(String[] args) {
        write("d:\\test.lic","113");
    }
}
