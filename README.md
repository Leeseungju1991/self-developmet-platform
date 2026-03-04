# 자기개발 플랫폼 (구조 기반 구현 코드)

프론트/백엔드 분리, JWT, 캐시, 스케줄러, CI를 “동작 가능한 데모”로 구현했습니다.

- Frontend: React + Recoil (Vite)
- Backend: Spring Boot + JWT + JPA (기본 H2 / profile=mysql)
- Infra: docker-compose(MySQL/Redis) + GitHub Actions CI 샘플

## 실행

### 1) Backend
```bash
cd backend
mvn spring-boot:run
# H2 콘솔: http://localhost:8080/h2-console
```

### 2) Frontend
```bash
cd frontend
npm install
npm run dev
# http://localhost:5173
```

## API 요약
- POST /api/auth/signup
- POST /api/auth/login
- POST /api/auth/refresh
- GET  /api/habits
- GET  /api/habits/today
- POST /api/habits
- PUT  /api/habits/{habitId}
- POST /api/habits/{habitId}/complete
- GET  /api/habits/history

## MySQL로 실행(선택)
```bash
cd infra
docker compose up -d
cd ../backend
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

> Redis는 compose에 포함돼 있지만, 데모 코드는 Spring Cache(Simple)로 동작합니다.
> 실서비스에서는 Redis CacheManager로 교체하면 됩니다.
