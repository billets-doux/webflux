package com.billetsdoux.webfluxcommon.util;



import com.billetsdoux.webfluxcommon.exception.StudentException;

import java.util.stream.Stream;

public class NameValidateUtil {

    // 无效姓名列表
    private static final String[] INVALIDE_NAMES = {"admin", "administrator"};

    public static void validateName(String name) {

        Stream.of(INVALIDE_NAMES)
                .filter(name::equalsIgnoreCase)
                .findAny()
                .ifPresent(invalideName ->{
                    throw new StudentException("name",invalideName,"使用了非法签名");
                });
    }
}
