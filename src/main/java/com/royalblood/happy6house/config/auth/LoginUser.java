package com.royalblood.happy6house.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 메소드 인자로 session의 user attribute를 받을 수 있는 어노테이션 생성 class
 */
// 이 어노테이션이 생성될 수 있는 위치 지정
@Target(ElementType.PARAMETER) // 메서드의 파라미터로 선언된 객체이서만 사용 가능
// 어느 시점까지 어노테이션의 메모리를 가져갈 지 설정
@Retention(RetentionPolicy.RUNTIME) // 런타임에도 JVM에 의해서 참조 가능
public @interface LoginUser {
}
