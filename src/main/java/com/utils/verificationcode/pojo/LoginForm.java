package com.utils.verificationcode.pojo;

import lombok.Data;

/**
 * @author 林逸
 * cool boy
 * 1.0
 */

/**
 * 作用：
 * @description: 用户登录表单信息
 * 封装了登陆返回给后端的请求。便于使用Body
 * 实现登陆功能时传回来的数据类
 * 包括账号，密码，验证码，身份权限等
 */
@Data
public class LoginForm {
    private String username;
    private String password;
    private String verifiCode; //验证码
    private Integer userType;
}
