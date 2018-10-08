package com.beidou.exchange.common.sign;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

public class AccountUtils {
    static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
    static SnowflakeIdWorker tokenWorker = new SnowflakeIdWorker(0, 0);
    public static final int CLOUD_SERVER_TYPE = 4;
    public static final int CLOUD_ENT_ADD_ING = -1;


    //发送验证码时间间隔缓存
    public static String HQT_VERIFY_CODE_TEL = "CLOUD_VERIFY_TEL_";

    public static String getUId(Long v) {
        return "c" + v;
    }

    /**
     * 获取 SnowFlakeId
     *
     * @return
     */
    public static String getUIdBySnowFlake() {
        return "c" + idWorker.nextId();
    }

    public static String getEntId(Long v) {
        return "ce" + v;
    }



    public static String getPwdDigest(String password, Long createOn) {
        return BDSecurityKeyGenerator.getMessageDigestBase62(password + "-" + createOn, "SHA-1");
    }

    public static BDSecurityKeyGenerator.BcryptHashResult hashWithBcrypt(String pwd) {
        return BDSecurityKeyGenerator.bcryptHash(pwd, 8);
    }

    public static BDUserPwd getPwdHash(String password, String random1) {
        String pwdDigest = BDSecurityKeyGenerator.getMessageDigestBase62(password + "-" + random1, "SHA-1");
        BDSecurityKeyGenerator.BcryptHashResult bcryptHashResult = hashWithBcrypt(pwdDigest);
        String random2 = BDSecurityKeyGenerator.randomStr(16);
        String pwdHash = BDSecurityKeyGenerator.getMessageDigestBase64(
                bcryptHashResult.getHash() + "-" + random2, "SHA-256");
        BDUserPwd pwdHashResult = new BDUserPwd();
        pwdHashResult.setR1(random1);
        pwdHashResult.setR2(bcryptHashResult.getSalt());
        pwdHashResult.setR3(random2);
        pwdHashResult.setHash(pwdHash);
        return pwdHashResult;
    }

    public static boolean checkPwd(String encryptContent, String pwdHash, String random2) {
        try {
            String decryptContent = BDSecurityKeyGenerator.aesDecrypt(encryptContent, pwdHash);
            PwdCheckContent pwdCheckContent = JSONObject.parseObject(decryptContent, PwdCheckContent.class);
            String clientPwdHash = BDSecurityKeyGenerator.getMessageDigestBase64(
                    pwdCheckContent.getHash() + "-" + random2, "SHA-256");
            return BDSecurityKeyGenerator.safeCheckEqual(pwdHash, clientPwdHash);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getTokenKey(String uid, String clientTag) {
        return new StringBuilder()
                .append("c:tk:")
                .append(uid)
                .append(":")
                .append(StringUtils.isBlank(clientTag) ? "" : clientTag)
                .toString();
    }

    public static String getTokenCreateTimeKey(String uid, String clientTag) {
        return new StringBuilder()
                .append("c:tktime:")
                .append(uid)
                .append(":")
                .append(StringUtils.isBlank(clientTag) ? "" : clientTag)
                .toString();
    }


    @Data
    public static class PwdCheckContent {
        private String uid;
        private Long time;
        private String hash;
        private String random;
    }

    public static void main(String args[]) {
        String salt = BCrypt.gensalt();
        System.out.println("salt: " + salt);
        String hash = BCrypt.hashpw("a111111", salt);
        System.out.println("hash: " + hash);
        boolean result = BCrypt.checkpw("a111111", hash);
        System.out.println("result: " + result);

        String encryptContent1 = BDSecurityKeyGenerator.aesEncrypt("a111111", hash);
        System.out.println("encrypt content: " + encryptContent1);
        String decryptContent = BDSecurityKeyGenerator.aesDecrypt(encryptContent1, hash);
        System.out.println("content: " + decryptContent);

        //1.1密码明文加上"cloud-"前缀
        String pwd = "cloud-a111111";
        //1.2 计算SHA256，base62编码后取前32字符
        pwd = BDSecurityKeyGenerator.getMessageDigestBase62(pwd, "SHA-256").substring(0, 32);
        System.out.println("sha256:" + pwd);
        //客户端得到r1, r2, r3
        String r1 = "1515397870477";
        //1.3 计算密码摘要，base62编码
        String pwdDigest = BDSecurityKeyGenerator.getMessageDigestBase62(
                pwd + "-" + r1, "SHA-1");
        System.out.println("sha1:" + pwdDigest);
        //2. 用random2计算hash1（自带base64编码）
        String r2 = "$2a$05$gI0FhyEn2slhQ9RsE9psle";
        String hash1 = BDSecurityKeyGenerator.bcryptHash(pwdDigest, r2);
        System.out.println("hash1:" + hash1);
        //3.1 用random3计算hash2，base64编码
        String r3 = "JHyyyldJL6NV7IcQ";
        String hash2 = BDSecurityKeyGenerator.getMessageDigestBase64(
                hash1 + "-" + r3, "SHA-256");
        System.out.println("hash2:" + hash2);
        //3.2 把hash1放入JSON对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hash", hash1);
        //4. 用hash2加密包含hash1的JSON串
        String encryptContent = BDSecurityKeyGenerator.aesEncrypt(jsonObject.toJSONString(), hash2);
        System.out.println("encryptContent:" + encryptContent);

        //验证
        boolean chkResult = checkPwd(encryptContent, hash2, "JHyyyldJL6NV7IcQ");
        System.out.println("check result: " + chkResult);
    }
}

