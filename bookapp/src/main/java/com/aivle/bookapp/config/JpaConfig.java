package com.aivle.bookapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing(자동 기록) 기능을 켜는 설정.
 *
 *  ▶ 이게 왜 필요한가?
 *    Book 엔티티의 createdAt/updatedAt(생성·수정 시각)을 매번 손으로 넣으면 실수도 잦고 귀찮습니다.
 *    JPA엔 이걸 자동으로 채워주는 'Auditing' 기능이 있는데 기본은 꺼져 있습니다.
 *    @EnableJpaAuditing을 한 번 켜두면, 엔티티의 @CreatedDate / @LastModifiedDate가 저장·수정 시점에
 *    자동으로 동작합니다. (Book.java에서 실제로 쓰는 걸 봤습니다.)
 *
 *  ▶ 왜 메인 클래스가 아니라 별도 Config로?
 *    "설정은 config 패키지에 모은다"는 관례를 지키면 설정이 늘어나도 어디를 볼지 한눈에 보입니다.
 *    본문이 비어 있어도 됩니다 — 어노테이션을 '켜는 것' 자체가 목적이라서요.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
