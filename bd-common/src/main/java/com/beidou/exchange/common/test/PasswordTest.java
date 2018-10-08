package com.beidou.exchange.common.test;

import com.beidou.exchange.common.BCrypt;
import com.beidou.exchange.common.SHA;

/**
 * Created by fengguoqing on 2018/9/3.
 */
public class PasswordTest {

    private final static String salt1 = "sale1Str";

    private final static String salt2 = "salt2Str";

    public static void main(String args[]) {

        String uid = "0001";
        String pwd = "a111111";


        String saltHash1 = getSaltHash1(pwd,uid);
        String saltHash2 = getSaltHash2(saltHash1,uid);

        System.out.println(saltHash1);
        System.out.println(saltHash2);


    }
//$2a$10$zazJlJethQr8/v.cy8owdugQrQLc4VTlcVDbF5MktQZWkZdGrB8Dy
//61efd67badd2248587a90b63f3e1fa046ab68f3f9066b1304444ab7a13eeefbd10c69957e21aaccfdac9682e97f83f378183452263b7151463eed91e06eb1cf5

    public static String getSaltHash1 (String pwd, String uid) {
        SHA sha = new SHA();
        return BCrypt.hashpw(sha.SHA512(sha.SHA512(pwd) + salt1),BCrypt.gensalt());
    }
    public static String getSaltHash2 (String salHash1,String uid) {
        SHA sha = new SHA();
        return sha.SHA512(salHash1 + uid + salt2);
    }

}
