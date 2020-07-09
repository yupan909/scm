package com.java.scm.util;

import com.java.scm.bean.base.BaseResult;
import com.java.scm.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author hujunhui
 * @date 2020/7/7
 */
@Slf4j
public class LicenseUtil {

    private static final String DES_KEY = "hjh_yp99";

    private static String LICENSE_NAME = "scm.lic";

    private static String DIR = ".config";



    private static  String createLicense(Date endDate){
        long time = endDate.getTime();
        String uuid = UUID.randomUUID().toString().replace("-","");
        int random = randomInt(10000000,99999999);
        StringBuilder key = new StringBuilder();
        key.append(uuid).append("_").append(time).append("\\").append(random);
        try {
            return DesUtil.toHexString(DesUtil.encrypt(key.toString(),DES_KEY ));
        }catch (Exception e){
            log.error("license生成失败",e);
            throw new BusinessException("license生成失败");
        }
    }

    public static Date getLicenseDate(String license) {
        String key ;
        try {
            key =  DesUtil.decrypt(license,DES_KEY);
        }catch (Exception e){
            log.error("license无效",e);
            throw new BusinessException("license无效");
        }
        String[] keyOne = key.split("_");
        if(keyOne.length != 2){
            log.error("license异常1");
            throw new BusinessException("license无效");
        }
        String[] keyTwo = keyOne[1].split("\\\\");
        if(keyTwo.length != 2){
            log.error("license异常2");
            throw new BusinessException("license无效");
        }
        String finalKey = keyTwo[0];
        Date date ;
        try {
            date = new Date(Long.parseLong(finalKey));
        }catch (Exception e){
            log.error("license转换为截止时间异常",e);
            throw new BusinessException ("license无效");
        }
        return date;
    }

    public static Date initLicense(String license){
        Date licenseDate =  getLicenseDate(license);
        Date now = new Date();
        if(licenseDate.before(now)){
            throw new BusinessException ("该license已过期");
        }
        ApplicationHome h = new ApplicationHome(LicenseUtil.class);
        File jarF = h.getSource();
        String path = jarF.getParentFile().toString();
        path = path + File.separator + DIR;
        FileUtil.write(path,LICENSE_NAME,license);
        return licenseDate;
    }

    public static Date getLicenseDate(){
        ApplicationHome h = new ApplicationHome(LicenseUtil.class);
        File jarF = h.getSource();
        String path = jarF.getParentFile().toString();
        path = path + File.separator + DIR + File.separator + LICENSE_NAME;
        String licenseInfo;
        try {
            licenseInfo = FileUtil.read(path);
            if(StringUtil.isEmpty(licenseInfo)){
                throw new BusinessException("系统未授权");
            }
        }catch (BusinessException e){
            throw new BusinessException("系统未授权");
        }
        return getLicenseDate(licenseInfo);
    }

    public static void main(String[] args) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2020-07-09 13:00:00");
        String license = createLicense(date);
        System.out.println(license);
        Date tem = getLicenseDate(license);
        System.out.println(tem);
        Date now = new Date();
        System.out.println(date.before(now));
    }

    private static int randomInt(int min, int max){
        return new Random().nextInt(max)%(max-min+1) + min;
    }
}
