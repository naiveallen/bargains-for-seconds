package com.allen.bargains_for_seconds.redis;

public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();

}
