package io.github.jiajun2001.community.community.util;

public interface CommunityConstant {

    int ACTIVATION_SUCCESS = 0;

    int ACTIVATION_REPEAT = 1;

    int ACTIVATION_FAIL = 2;

    // Default expired time for login session
    int DEFAULT_EXPIRED_SECOND = 3600 * 12;

    // After clicking remember me
    int REMEMBER_EXPIRED_SECOND = 3600 * 24 * 100;

    // Entity Type: post
    int ENTITY_TYPE_POST = 1;

    // Entity Type: comment
    int ENTITY_TYPE_COMMENT = 2;

    // Entity Type: user
    int ENTITY_TYPE_USER = 3;

}
