# ===== Stage 1: Build =====
# Gradle wrapper(9.5.1)가 알아서 Gradle을 받으므로 JDK 17 베이스만 있으면 됩니다.
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

# 1) wrapper / 빌드 스크립트 먼저 복사 → 의존성 캐시 레이어 분리
COPY bookapp/gradlew .
COPY bookapp/gradle gradle
COPY bookapp/build.gradle bookapp/settings.gradle ./
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# 2) 소스 복사 후 실행 가능한 boot jar 빌드 (테스트 제외)
COPY bookapp/src src
RUN ./gradlew clean bootJar -x test --no-daemon \
    && rm -f build/libs/*-plain.jar

# ===== Stage 2: Run =====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
