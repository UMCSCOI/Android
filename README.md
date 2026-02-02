# 1. Project Overview (프로젝트 개요)
- 프로젝트 이름: SCOI
- 프로젝트 설명: 한국형 스테이블 코인 플랫폼

<br/>
<br/>

# 2. Key Features (주요 기능)
- **회원가입**:
  - 회원가입 시 DB에 유저정보가 등록됩니다.

- **로그인**:
  - 사용자 인증 정보를 통해 로그인합니다.

- **송금**:
  - 스테이블 코인으로 거래

- **이체**:
  - 일반 계좌로 이체

- **충전**:
  - USDT, USDC 중 코인 충전 및 환전

- **내 지갑**:
  - 거래 내용 및 자산 확인

- **분석**:
  - 실시간 차트 분석 
  
- **마이페이**:
  - 나의 계정 정보 확인 
<br/>
<br/>

# 3. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |
|-----------------|-----------------|
| 조경석    | <ul><li>프로젝트 구조 설계</li><li>공통 컴포넌트 제작</li><li>충전 파트 개발</li></ul>     |
| 김재환    | <ul><li>이체 파트 개발</li></ul>     |
| 박은채    | <ul><li>회원가입/로그인 개발</li></ul>     |
| 강진주    | <ul><li>마이페이지, 내 지갑</li></ul>     |

<br/>
<br/>

# 4. Technology Stack (기술 스택)
## 🛠 Environment
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white">

## 💻 Development
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) <img src="https://img.shields.io/badge/Retrofit2-orange?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/Hilt-0078D4?style=for-the-badge&logo=google&logoColor=white"> <img src="https://img.shields.io/badge/Room-3DDC84?style=for-the-badge&logo=android&logoColor=white"> ![Firebase](https://img.shields.io/badge/firebase-a08021?style=for-the-badge&logo=firebase&logoColor=ffcd34)

<br/>
<br/>

# 5. Project Structure (프로젝트 구조)
```text
com.project.name
├── di              # Hilt Module 주입
├── extension       # 확장 함수 정의 (ex. String.toJson())
├── data            # 데이터 로직 처리 (Data Layer)
│   ├── api         # API Interface
│   ├── base        # Data 모듈 베이스 코드
│   ├── dto         # Request / Response 데이터 객체
│   ├── dataSource  # Local(Room, DataStore), Remote 인터페이스
│   │   ├── local   # dataSource의 Local implementation (ex. RoomDB)
│   │   └── remote  # dataSource의 Network implementation
│   └── repository  # Domain Repository Implementation
├── domain          # 비즈니스 로직 (Domain Layer)
│   ├── repository  # Repository Interface
│   ├── model       # UI 전달용 실제 Data Class
│   └── usecase     # Repository 호출을 위한 UseCase
└── presentation    # UI 레이어 (Presentation Layer)
    ├── base        # 공통 내용 정의 (BaseFragment, ViewModel)
    └── ui          # UI 구현 (Activity, Fragment, Adapter)
