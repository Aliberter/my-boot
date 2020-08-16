package cn.aliberter.boot.common.security;

import cn.aliberter.boot.common.cache.RedisCache;
import cn.aliberter.boot.common.enums.RedisKeyEnum;
import cn.aliberter.boot.common.properties.CustomSetting;
import cn.aliberter.boot.common.utils.SpringUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.gson.io.GsonDeserializer;
import io.jsonwebtoken.gson.io.GsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Token工具类
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@SuppressWarnings("unused")
public class TokenUtil {

    private static final String SECRET_KEY = Base64.encode(CustomSetting.getStr("token.secretKey"));
    private static final Integer EXPIRE_TIME = CustomSetting.getInt("token.expireTime");
    private static final GsonDeserializer<Map<String, ?>> DESERIALIZER = new GsonDeserializer<>(new Gson());
    private static final Key KEY = KeyUtil.generateKey(HmacAlgorithm.HmacSHA256.getValue(), SECRET_KEY.getBytes());
    private static final Sign SIGN = SecureUtil.sign(SignAlgorithm.MD5withRSA);
    private static final String LOGIN_SIGN_STR = "loginSign";
    private static final String PASSWORD_SIGN_STR = "passwordSign";

    /**
     * 创建token
     *
     * @param username username
     * @param password password
     * @return token
     */
    public static String createToken(String username, String password) {
        //生成唯一性签名(只有携带最新唯一性签名的token才有效，保证一个用户只能有一个有效token)
        String loginSign = SecureUtil.md5(username + IdUtil.fastSimpleUUID());
        //生成密码签名(token中的密码签名必须与数据库内密码签名一致才有效)
        String passwordSign = SecureUtil.md5(password);
        String redisKey = StrFormatter.format(RedisKeyEnum.LOGIN_UNIQUE_SIGN.getKey(), username);
        //存入缓存，会替换掉之前的签名
        Map<String, Object> map = new HashMap<>(4);
        map.put(LOGIN_SIGN_STR, loginSign);
        map.put(PASSWORD_SIGN_STR, passwordSign);
        RedisCache.hmset(redisKey, map, 10800);
        //开始生成token
        DateTime now = DateUtil.date();
        Claims claims = Jwts.claims();
        claims.setSubject(username);
        claims.put(LOGIN_SIGN_STR, loginSign);
        claims.put(PASSWORD_SIGN_STR, passwordSign);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(DateUtil.offset(now, DateField.MINUTE, EXPIRE_TIME))
                .signWith(KEY)
                .serializeToJsonWith(new GsonSerializer<>(new Gson()))
                .compact();
    }

    public static Authentication getAuthentication(String token) {
        UserDetails userDetails = SpringUtil.getBean(UserDetailsService.class).loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public static String getUsername(String token) {
        return Jwts.parserBuilder().deserializeJsonWith(DESERIALIZER).setSigningKey(KEY).build().parseClaimsJws(token).getBody().getSubject();
    }

    public static String resolveToken(HttpServletRequest req) {
        return req.getHeader("Authorization");
    }

    public static boolean validateToken(String token) {
        //验证token是否正确
        Jwts.parserBuilder().deserializeJsonWith(DESERIALIZER).setSigningKey(KEY).build().parseClaimsJws(token);
        //验证token签名与缓存中的最新签名是否一致
        String loginSign = getTokenParam(token, LOGIN_SIGN_STR);
        String redisKey = StrFormatter.format(RedisKeyEnum.LOGIN_UNIQUE_SIGN.getKey(), getUsername(token));
        String redisLoginSign = (String) Optional.ofNullable(RedisCache.hget(redisKey, LOGIN_SIGN_STR)).orElse("");
        boolean loginSignEquals = StrUtil.equals(loginSign, redisLoginSign);
        //验证密码签名与最新的密码签名是否一致
        String passwordSign = getTokenParam(token, PASSWORD_SIGN_STR);
        String redisPasswordSign = (String) Optional.ofNullable(RedisCache.hget(redisKey, PASSWORD_SIGN_STR)).orElse("");
        boolean passwordSignEquals = StrUtil.equals(passwordSign, redisPasswordSign);
        //都一致才算校验通过
        return loginSignEquals && passwordSignEquals;
    }

    public static String getTokenParam(String token, String head) {
        try {
            return Jwts.parserBuilder().deserializeJsonWith(DESERIALIZER).setSigningKey(KEY).build().parseClaimsJws(token).getBody().get(head, String.class);
        } catch (ExpiredJwtException ex) {
            return (String) ex.getClaims().get(head);
        } catch (Exception e) {
            log.error("Get token param error", e);
            return StrUtil.EMPTY;
        }
    }

    /**
     * 签名
     *
     * @param str 需要签名的字符串
     * @return 签名后的字符串
     */
    public static String sign(String str) {
        return StrUtil.str(SIGN.sign(StrUtil.utf8Bytes(str)), StandardCharsets.UTF_8);
    }

    /**
     * 验证签名
     *
     * @param str 需要验证签名的字符串
     * @return 验证结果
     */
    public static boolean verify(String str) {
        byte[] data = StrUtil.utf8Bytes(str);
        return SIGN.verify(data, SIGN.sign(data));
    }

    private TokenUtil() {

    }
}
