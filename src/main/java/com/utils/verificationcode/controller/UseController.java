package com.utils.verificationcode.controller;

import com.utils.verificationcode.pojo.LoginForm;
import com.utils.verificationcode.utils.CreateVerificationCodeImage;
import com.utils.verificationcode.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 林逸
 * cool boy
 * 1.0
 */

@RestController
public class UseController {

    /**
     * 获取验证码，并将其放在session域
     */
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        // 获取图片
        BufferedImage verificationCodeImage = CreateVerificationCodeImage.getVerifiCodeImage();
        // 获取图片上的文字
        //char[] verifiCode1 = CreateVerificationCodeImage.getVerifiCode();
        //把上面的char[]格式转换成字符串String格式
        String verificationCode = new String(CreateVerificationCodeImage.getVerifiCode());
        // 将验证码放入session域，为下次验证准备
        HttpSession session = request.getSession();
        session.setAttribute("VerificationCode", verificationCode);
        // 将验证码图片响应给浏览器
        /**
         * 利用输出流发送
         * response.getOutputStream()输出流
         * ImageIO方法可以帮助我们直接通过io数据流将图片显示给前端
         */
        try {
            ImageIO.write(verificationCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 用户登录
     *
     * @param loginForm
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request) {

        /**
         * 验证码校验：
         * 获取后段传过去的验证码图片上的验证码
         * request.getSession().getAttribute("verifiCode");
         * 获取用户输入的验证码
         * loginForm.getVerifiCode();
         */
        String verifiCode = (String) request.getSession().getAttribute("verifiCode");
        String inputverifiCode = loginForm.getVerifiCode();
        if ("".equals(verifiCode) || verifiCode == null) { //传过去的为{}或null，则验证码失效
            return Result.fail().message("验证码失效，请刷新");
        }
        if (!verifiCode.equalsIgnoreCase(inputverifiCode)) { //传过去的验证码与用户输入的验证码不匹配，则验证码有误
            return Result.fail().message("验证码有误，请重试");
        }
        // 验证码验证成功后，就移除使用过的验证码
        request.getSession().removeAttribute("verifiCode");
        // 用户类型校验
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("verifiCode",loginForm.getVerifiCode());
        map.put("user",loginForm.getUsername());
        map.put("password",loginForm.getPassword());
        map.put("type",loginForm.getUserType());
        return Result.ok(map);
        /**
         * 后面就是用拿到的数据去做权限验证登陆等操作，以及token的创建和携带的信息
         */
//        Integer userType = loginForm.getUserType();
//        switch (loginForm.getUserType()){
//            case 1:
//                try {
//                    Admin admin = adminService.login(loginForm);
//                    if (admin != null) {
//                        // 将用户的类型和用户id转换成一个密文，以token的名称返回客户端
//                        //.longValue() ：将Integer型转为long类型
//                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
//                        map.put("token", token);
//                    } else {
//                        throw new RuntimeException("用户名或密码有误");
//                    }
//                    return Result.ok(map);
//                } catch (RuntimeException ex) {
//                    ex.printStackTrace();
//                    return Result.fail().message(ex.getMessage());
//                }
//            case 2:
//                try {
//                    Student student = studentService.login(loginForm);
//                    if (student != null) {
//                        // 将用户的类型和用户id转换成一个密文，以token的名称返回客户端
//                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
//                        map.put("token", token);
//                    } else {
//                        throw new RuntimeException("用户名或密码有误");
//                    }
//                    return Result.ok(map);
//                } catch (RuntimeException ex) {
//                    ex.printStackTrace();
//                    return Result.fail().message(ex.getMessage());
//                }
//            case 3:
//                try {
//                    Teacher teacher = teacherService.login(loginForm);
//                    if (teacher != null) {
//                        // 将用户的类型和用户id转换成一个密文，以token的名称返回客户端
//                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
//                        map.put("token", token);
//                    } else {
//                        throw new RuntimeException("用户名或密码有误");
//                    }
//                    return Result.ok(map);
//                } catch (RuntimeException ex) {
//                    ex.printStackTrace();
//                    return Result.fail().message(ex.getMessage());
//                }
//            default:
//                return Result.fail().message("没有该用户");
//
//        }
    }
}
