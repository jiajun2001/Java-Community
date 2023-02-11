package io.github.jiajun2001.community.community.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_CAPTCHA = "captcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";



    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // like:user:userId
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // A user's followee
    // followee:userId:entityType -> zset(entityId, date)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // An entity's follower:
    // follower:entityType:entityId -> zset(userId, date)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    // Captcha redis key
    public static String getKaptchaKey(String owner) {
        return PREFIX_CAPTCHA + SPLIT + owner;
    }

    // Ticket redis key
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // User redis key
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }


}
