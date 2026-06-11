-- =====================================================
-- 장르 초기 데이터 (부모 장르 먼저)
-- =====================================================
INSERT INTO genre (code, label, parent_code) VALUES ('NV', '소설', NULL);
INSERT INTO genre (code, label, parent_code) VALUES ('LT', '시/에세이', NULL);
INSERT INTO genre (code, label, parent_code) VALUES ('HU', '인문/교양', NULL);
INSERT INTO genre (code, label, parent_code) VALUES ('SS', '사회/경제/경영', NULL);
INSERT INTO genre (code, label, parent_code) VALUES ('SC', '과학/기술', NULL);
INSERT INTO genre (code, label, parent_code) VALUES ('ED', '교육/참고서', NULL);
INSERT INTO genre (code, label, parent_code) VALUES ('EX', '수험서/자격증', NULL);
INSERT INTO genre (code, label, parent_code) VALUES ('SD', '자기계발', NULL);
INSERT INTO genre (code, label, parent_code) VALUES ('AR', '예술/취미', NULL);
INSERT INTO genre (code, label, parent_code) VALUES ('KD', '어린이/청소년', NULL);
INSERT INTO genre (code, label, parent_code) VALUES ('HE', '건강/의학', NULL);

-- 소설 하위
INSERT INTO genre (code, label, parent_code) VALUES ('NV-01', '로맨스', 'NV');
INSERT INTO genre (code, label, parent_code) VALUES ('NV-02', '판타지', 'NV');
INSERT INTO genre (code, label, parent_code) VALUES ('NV-03', '스릴러/미스터리', 'NV');
INSERT INTO genre (code, label, parent_code) VALUES ('NV-04', 'SF/공상과학', 'NV');
INSERT INTO genre (code, label, parent_code) VALUES ('NV-05', '호러', 'NV');
INSERT INTO genre (code, label, parent_code) VALUES ('NV-06', '역사소설', 'NV');
INSERT INTO genre (code, label, parent_code) VALUES ('NV-07', '순문학', 'NV');

-- 시/에세이 하위
INSERT INTO genre (code, label, parent_code) VALUES ('LT-01', '시', 'LT');
INSERT INTO genre (code, label, parent_code) VALUES ('LT-02', '에세이/수필', 'LT');
INSERT INTO genre (code, label, parent_code) VALUES ('LT-03', '여행기', 'LT');
INSERT INTO genre (code, label, parent_code) VALUES ('LT-04', '일기/다이어리', 'LT');

-- 인문/교양 하위
INSERT INTO genre (code, label, parent_code) VALUES ('HU-01', '철학', 'HU');
INSERT INTO genre (code, label, parent_code) VALUES ('HU-02', '역사', 'HU');
INSERT INTO genre (code, label, parent_code) VALUES ('HU-03', '심리학', 'HU');
INSERT INTO genre (code, label, parent_code) VALUES ('HU-04', '종교/사상', 'HU');
INSERT INTO genre (code, label, parent_code) VALUES ('HU-05', '언어/문화', 'HU');

-- 사회/경제/경영 하위
INSERT INTO genre (code, label, parent_code) VALUES ('SS-01', '경제', 'SS');
INSERT INTO genre (code, label, parent_code) VALUES ('SS-02', '경영/마케팅', 'SS');
INSERT INTO genre (code, label, parent_code) VALUES ('SS-03', '정치/사회', 'SS');
INSERT INTO genre (code, label, parent_code) VALUES ('SS-04', '법률', 'SS');

-- 과학/기술 하위
INSERT INTO genre (code, label, parent_code) VALUES ('SC-01', 'IT/컴퓨터', 'SC');
INSERT INTO genre (code, label, parent_code) VALUES ('SC-02', 'AI/데이터과학', 'SC');
INSERT INTO genre (code, label, parent_code) VALUES ('SC-03', '수학', 'SC');
INSERT INTO genre (code, label, parent_code) VALUES ('SC-04', '자연과학', 'SC');
INSERT INTO genre (code, label, parent_code) VALUES ('SC-05', '공학', 'SC');

-- 교육/참고서 하위
INSERT INTO genre (code, label, parent_code) VALUES ('ED-01', '초등참고서', 'ED');
INSERT INTO genre (code, label, parent_code) VALUES ('ED-02', '중등참고서', 'ED');
INSERT INTO genre (code, label, parent_code) VALUES ('ED-03', '고등참고서', 'ED');
INSERT INTO genre (code, label, parent_code) VALUES ('ED-04', '어학교재', 'ED');

-- 수험서/자격증 하위
INSERT INTO genre (code, label, parent_code) VALUES ('EX-01', '공무원시험', 'EX');
INSERT INTO genre (code, label, parent_code) VALUES ('EX-02', 'IT자격증', 'EX');
INSERT INTO genre (code, label, parent_code) VALUES ('EX-03', '어학시험', 'EX');
INSERT INTO genre (code, label, parent_code) VALUES ('EX-04', '전문자격증', 'EX');
INSERT INTO genre (code, label, parent_code) VALUES ('EX-05', '금융자격증', 'EX');

-- 자기계발 하위
INSERT INTO genre (code, label, parent_code) VALUES ('SD-01', '동기부여/성공', 'SD');
INSERT INTO genre (code, label, parent_code) VALUES ('SD-02', '리더십/경력', 'SD');
INSERT INTO genre (code, label, parent_code) VALUES ('SD-03', '시간관리/생산성', 'SD');
INSERT INTO genre (code, label, parent_code) VALUES ('SD-04', '인간관계/소통', 'SD');

-- 예술/취미 하위
INSERT INTO genre (code, label, parent_code) VALUES ('AR-01', '미술/디자인', 'AR');
INSERT INTO genre (code, label, parent_code) VALUES ('AR-02', '음악', 'AR');
INSERT INTO genre (code, label, parent_code) VALUES ('AR-03', '요리/제과', 'AR');
INSERT INTO genre (code, label, parent_code) VALUES ('AR-04', '여행/지리', 'AR');
INSERT INTO genre (code, label, parent_code) VALUES ('AR-05', '스포츠/레저', 'AR');

-- 어린이/청소년 하위
INSERT INTO genre (code, label, parent_code) VALUES ('KD-01', '그림책', 'KD');
INSERT INTO genre (code, label, parent_code) VALUES ('KD-02', '동화', 'KD');
INSERT INTO genre (code, label, parent_code) VALUES ('KD-03', '청소년소설', 'KD');
INSERT INTO genre (code, label, parent_code) VALUES ('KD-04', '학습만화', 'KD');

-- 건강/의학 하위
INSERT INTO genre (code, label, parent_code) VALUES ('HE-01', '건강관리', 'HE');
INSERT INTO genre (code, label, parent_code) VALUES ('HE-02', '의학/약학', 'HE');
INSERT INTO genre (code, label, parent_code) VALUES ('HE-03', '운동/다이어트', 'HE');
INSERT INTO genre (code, label, parent_code) VALUES ('HE-04', '정신건강', 'HE');

-- =====================================================
-- 도서 초기 데이터
-- =====================================================
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (1, '첫눈이 내리던 날', '이수민', 'NV-01', '서울 종로의 작은 카페에서 우연히 만난 두 사람. 엇갈린 시간과 감정을 교차 서술로 풀어낸 현대 로맨스.', '/covers/1.png', '2026-05-01T09:00:00.000Z', '2026-05-22T06:38:34.991Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (2, '별빛 아래의 서점', '홍길동', 'NV-02', '폐업을 앞둔 낡은 서점과 그것을 살리려는 청년의 1년. 소소한 기적이 깃든 판타지 소설.', '/covers/2.png', '2026-05-01T10:00:00.000Z', '2026-05-22T06:30:51.582Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (3, '밤의 추적자', '김도현', 'NV-03', '연쇄 실종 사건을 쫓는 전직 형사. 단서는 피해자들이 남긴 암호화된 일기뿐. 숨막히는 반전이 기다린다.', '/covers/3.png', '2026-05-01T11:00:00.000Z', '2026-05-22T06:40:50.177Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (4, '2150년의 기억', '박준혁', 'NV-04', '기억을 데이터로 저장하는 시대. 죽은 아버지의 기억 파일을 열어버린 딸이 마주한 충격적인 진실.', '/covers/4.png', '2026-05-02T09:00:00.000Z', '2026-05-22T06:39:14.124Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (5, '붉은 저택의 비밀', '최유리', 'NV-05', '100년 전 화재로 사라진 저택이 재건되던 날 밤, 첫 번째 입주자가 사라졌다. 기록에 없는 공포의 시작.', '/covers/5.png', '2026-05-02T10:00:00.000Z', '2026-05-22T06:40:39.061Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (6, '조선의 밀정', '정해원', 'NV-06', '일제강점기 경성. 독립군과 친일파 사이에서 정보를 팔며 살아가는 한 남자의 이중 첩보 활극.', '/covers/6.png', '2026-05-02T11:00:00.000Z', '2026-05-22T06:40:56.170Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (7, '강 건너 저 마을에서', '윤수진', 'NV-07', '고향을 떠난 지 20년. 어머니의 부고를 받고 돌아온 고향 마을에서 잊고 살았던 이름들을 다시 만난다.', '/covers/7.png', '2026-05-03T09:00:00.000Z', '2026-05-22T06:40:02.583Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (8, '빛과 어둠 사이', '이정희', 'LT-01', '계절이 바뀌는 경계에서 길어 올린 시 72편. 상실과 기다림, 그리고 다시 찾아오는 것들에 대한 기록.', '/covers/8.png', '2026-05-03T10:00:00.000Z', '2026-05-22T06:39:12.262Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (9, '혼자이지만 괜찮아', '김지현', 'LT-02', '30대 혼자 사는 일상. 출근길, 편의점 저녁, 늦은 밤 유튜브까지. 지극히 평범한 하루를 다독이는 에세이.', '/covers/9.png', '2026-05-03T11:00:00.000Z', '2026-05-22T06:39:17.013Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (10, '나 혼자 유럽 한 달', '박소연', 'LT-03', '퇴사 후 배낭 하나 메고 떠난 111일의 여행기. 리스본에서 부다페스트까지, 길 위에서 얻은 것들.', '/covers/10.png', '2026-05-04T09:00:00.000Z', '2026-05-22T06:40:31.470Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (11, '오늘도 무사히', '김하늘', 'LT-04', '평범한 직장인이 3년간 써내려간 일기. 별것 아닌 하루하루가 모여 삶이 된다는 것을 담담하게 기록한다.', '/covers/11.png', '2026-05-04T10:00:00.000Z', '2026-05-22T06:39:27.535Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (12, '생각한다는 것의 의미', '강민준', 'HU-01', '소크라테스부터 현대 인지과학까지. 인간이 생각을 어떻게 정의해왔는지 추적하는 철학 입문서.', '/covers/12.png', '2026-05-04T11:00:00.000Z', '2026-05-22T06:40:01.990Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (13, '한국 근현대사 100년', '이동훈', 'HU-02', '1919년부터 2019년까지. 격동의 100년을 인물과 사건 중심으로 재구성한 역사 교양서.', '/covers/13.png', '2026-05-05T09:00:00.000Z', '2026-05-22T06:40:05.509Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (14, '마음을 읽는 법', '신예진', 'HU-03', '행동심리학과 인지편향 이론을 토대로 타인의 감정과 의도를 파악하는 실전 심리학 가이드.', '/covers/14.png', '2026-05-05T10:00:00.000Z', '2026-05-22T06:40:07.295Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (15, '명상의 시작', '류명진', 'HU-04', '불교·기독교·이슬람의 명상법을 비교하며 현대인에게 맞는 마음 수련법을 제안하는 종교·사상 입문서.', '/covers/15.png', '2026-05-05T11:00:00.000Z', '2026-05-22T06:40:45.756Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (16, '언어의 온도', '장재형', 'HU-05', '같은 말도 누가, 언제, 어떻게 말하느냐에 따라 달라진다. 한국어의 뉘앙스와 언어문화를 섬세하게 분석.', '/covers/16.png', '2026-05-06T09:00:00.000Z', '2026-05-22T06:40:08.784Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (17, '돈의 흐름을 읽어라', '조현식', 'SS-01', '금리, 환율, 주식, 부동산이 서로 어떻게 연결되는지 한 권으로 설명하는 생활 경제학 입문서.', '/covers/17.png', '2026-05-06T10:00:00.000Z', '2026-05-22T06:40:06.114Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (18, '스타트업 생존 전략', '한승우', 'SS-02', '창업 3년 이내 90%가 폐업한다. 살아남은 스타트업 20곳의 공통점과 의사결정 프레임워크.', '/covers/18.png', '2026-05-06T11:00:00.000Z', '2026-05-22T06:39:28.912Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (19, '민주주의의 미래', '임태호', 'SS-03', 'SNS 시대의 여론 형성, 포퓰리즘의 부상, 대의민주주의의 한계. 민주주의가 나아갈 방향을 논한다.', '', '2026-05-07T09:00:00.000Z', '2026-05-07T09:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (20, '법, 알고 살자', '이법무', 'SS-04', '계약서·임대차·노동법·상속까지. 일상에서 꼭 알아야 할 법률 상식을 사례 중심으로 쉽게 풀어낸 법률 입문서.', '/covers/20.png', '2026-05-07T10:00:00.000Z', '2026-05-22T06:42:29.338Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (21, '클린 코드의 기술', '백승현', 'SC-01', '읽기 좋은 코드는 왜 중요한가. 네이밍, 함수 분리, 리팩터링까지 실무에서 바로 쓰는 코드 품질 가이드.', '/covers/21.png', '2026-05-07T11:00:00.000Z', '2026-05-22T06:42:51.467Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (22, 'GPT 이후의 세계', '서지원', 'SC-02', '생성형 AI가 바꾸는 일, 교육, 창작의 미래. AI 리터러시가 새로운 생존 능력이 된 시대를 분석한다.', '/covers/22.png', '2026-05-08T09:00:00.000Z', '2026-05-22T06:47:56.903Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (23, '수학으로 보는 세상', '김형준', 'SC-03', '확률, 통계, 위상수학이 실생활에 녹아있는 방식을 이야기로 풀어낸 수학 교양서.', '/covers/23.png', '2026-05-08T10:00:00.000Z', '2026-05-22T06:42:55.445Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (24, '양자역학 첫걸음', '이진수', 'SC-04', '수식 없이 이해하는 양자의 세계. 이중 슬릿 실험부터 양자 컴퓨터까지 과학 비전공자를 위한 입문서.', '', '2026-05-08T11:00:00.000Z', '2026-05-08T11:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (25, '전기차의 모든 것', '정기호', 'SC-05', '배터리 원리부터 충전 인프라, 자율주행까지. 전동화 시대를 이해하기 위한 공학 교양서.', '', '2026-05-09T09:00:00.000Z', '2026-05-09T09:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (26, '초등 수학 기초 다지기', '초등교육연구소', 'ED-01', '1~6학년 수와 연산·도형·측정을 단계별 개념 설명과 유형별 문제로 체계적으로 정리한 초등 참고서.', '/covers/26.png', '2026-05-09T10:00:00.000Z', '2026-05-22T06:42:35.611Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (27, '중학 수학 개념 완성', '수학교육연구소', 'ED-02', '중학 1~3학년 핵심 개념을 단계별로 정리. 개념 설명 → 예제 → 유제 → 서술형 문제로 구성된 참고서.', '', '2026-05-09T11:00:00.000Z', '2026-05-09T11:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (28, '수능 국어 실전 모의고사', '국어교육연구소', 'ED-03', '최근 5개년 수능 출제 경향 완벽 분석. 실전 난이도 모의고사 10회분 + 해설 강의 QR 제공.', '', '2026-05-10T09:00:00.000Z', '2026-05-10T09:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (29, '영어회화 30일 완성', '이미나', 'ED-04', '하루 20분, 30일 커리큘럼. 여행·비즈니스·일상 상황별 핵심 패턴 300개를 반복 훈련하는 어학 교재.', '', '2026-05-10T10:00:00.000Z', '2026-05-10T10:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (30, '9급 공무원 행정학 기출 총정리', '공무원시험연구소', 'EX-01', '최근 10개년 9급 공무원 행정학 기출문제 완전 분석. 빈출 이론 정리 + 단원별 예상문제 수록.', '/covers/30.png', '2026-05-10T11:00:00.000Z', '2026-05-22T06:45:57.394Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (31, '정보처리기사 필기 한권 완성', 'IT자격증연구소', 'EX-02', '소프트웨어 설계부터 정보시스템 구축관리까지 5과목 핵심 이론 정리. CBT 모의고사 5회 제공.', '', '2026-05-11T09:00:00.000Z', '2026-05-11T09:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (32, 'TOEIC 900 실전 모의고사', '토익연구소', 'EX-03', 'LC·RC 파트별 전략과 실전 모의고사 10회. 최신 토익 출제 패턴 반영, 정답률 분석 제공.', '/covers/32.png', '2026-05-11T10:00:00.000Z', '2026-05-22T06:30:21.431Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (33, '공인중개사 부동산학개론', '공인중개사연구소', 'EX-04', '1차 시험 부동산학개론 완전 정복. 부동산 경제론·시장론·정책론·투자론 핵심 이론 + 기출 해설.', '/covers/33.png', '2026-05-11T11:00:00.000Z', '2026-05-22T06:46:40.337Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (34, '투자자산운용사 핵심이론', '금융자격연구소', 'EX-05', '펀드·채권·파생상품 운용 이론 총정리. 투자자산운용사 시험 합격률 1위 교재, 핵심 요약 + 문제풀이.', '', '2026-05-12T09:00:00.000Z', '2026-05-12T09:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (35, '아침 5시의 기적', '정유진', 'SD-01', '새벽 루틴이 인생을 바꾼다. 30일 챌린지를 통해 자기계발 습관을 형성하는 구체적인 실천 가이드.', '', '2026-05-12T10:00:00.000Z', '2026-05-12T10:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (36, '리더의 언어', '황성민', 'SD-02', '팀을 움직이는 말, 신뢰를 쌓는 피드백, 갈등을 해소하는 대화법. 조직 리더를 위한 커뮤니케이션 교과서.', '', '2026-05-12T11:00:00.000Z', '2026-05-12T11:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (37, '딥워크', '최서준', 'SD-03', '집중력이 곧 경쟁력이다. 방해 없는 몰입 시간을 만드는 환경 설계와 집중 훈련법.', '/covers/37.png', '2026-05-13T09:00:00.000Z', '2026-05-22T06:47:39.102Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (38, '말의 품격', '오세민', 'SD-04', '듣는 사람을 배려하는 말하기, 상처 주지 않는 거절법, 호감을 얻는 첫 마디. 관계를 바꾸는 언어 습관.', '', '2026-05-13T10:00:00.000Z', '2026-05-13T10:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (39, '그림 보는 즐거움', '이예린', 'AR-01', '미술관에서 그림 앞에 서면 무슨 말을 해야 할지 모르는 사람을 위한 서양미술사 친절 안내서.', '/covers/39.png', '2026-05-13T11:00:00.000Z', '2026-05-22T06:45:43.458Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (40, '클래식이 쉬워지는 날', '박음악', 'AR-02', '베토벤, 모차르트, 쇼팽 교과서에서만 보던 작곡가들의 삶과 대표곡을 에피소드로 풀어낸 클래식 입문서.', '/covers/40.png', '2026-05-14T09:00:00.000Z', '2026-05-22T06:31:46.621Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (41, '집밥의 과학', '김미영', 'AR-03', '왜 식당 음식이 더 맛있을까? 불 세기, 간 맞추기, 식재료 손질의 과학적 원리를 풀어낸 요리책.', '', '2026-05-14T10:00:00.000Z', '2026-05-14T10:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (42, '제주 한달살기', '오지훈', 'AR-04', '숙소 고르기부터 렌트카, 숨은 맛집, 계절별 추천 코스까지. 제주 장기 체류 완전 실전 가이드.', '/covers/42.png', '2026-05-14T11:00:00.000Z', '2026-05-22T06:30:23.818Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (43, '달리기의 철학', '이운동', 'AR-05', '5km부터 풀마라톤까지. 달리기가 단순한 운동을 넘어 삶의 태도가 되는 과정을 담은 러닝 에세이.', '/covers/43.png', '2026-05-15T09:00:00.000Z', '2026-05-22T06:46:56.284Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (44, '파란 하늘 아이', '류지아', 'KD-01', '구름 위에 사는 꼬마 구름이 처음으로 땅을 밟는 이야기. 세상의 넓음과 다름을 배우는 그림책.', '/covers/44.png', '2026-05-15T10:00:00.000Z', '2026-05-22T06:31:00.232Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (45, '마법사 이든의 모험', '장준서', 'KD-02', '마법 학교 낙제생 이든이 친구들과 함께 고대 마법서를 찾아 떠나는 대모험. 어린이 판타지 동화.', '/covers/45.png', '2026-05-15T11:00:00.000Z', '2026-05-22T06:47:47.799Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (46, '열일곱의 여름', '김나연', 'KD-03', '고등학교 2학년 여름, 진로·우정·첫사랑이 한꺼번에 쏟아진다. 공감 100%의 청소년 성장소설.', '', '2026-05-16T09:00:00.000Z', '2026-05-16T09:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (47, '만화로 보는 한국사', '역사만화연구소', 'KD-04', '선사시대부터 현대까지 한국사의 흐름을 재미있는 만화와 간결한 해설로 정리한 어린이·청소년 학습만화.', '', '2026-05-16T10:00:00.000Z', '2026-05-16T10:00:00.000Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (48, '100세까지 건강하게', '박정호', 'HE-01', '대한내과학회 추천 도서. 연령대별 필수 건강검진, 만성질환 예방, 건강한 식습관을 체계적으로 안내.', '/covers/48.png', '2026-05-16T11:00:00.000Z', '2026-05-22T06:45:35.842Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (49, '약, 알고 먹기', '이약사', 'HE-02', '해열제, 항생제, 소화제 우리가 자주 먹는 약의 성분과 올바른 복용법을 약사가 직접 알려주는 실용 의학서.', '/covers/49.png', '2026-05-17T09:00:00.000Z', '2026-05-22T06:31:38.606Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (50, '30분 홈트레이닝', '이강민', 'HE-03', '기구 없이 집에서 할 수 있는 전신 운동 루틴. 초급·중급·고급 단계별 구성, QR 코드로 영상 연동.', '/covers/50.png', '2026-05-17T10:00:00.000Z', '2026-05-22T06:45:32.380Z');
INSERT INTO book (id, title, author, genre_code, content, cover_image_url, created_at, updated_at) VALUES (51, '번아웃에서 벗어나기', '정신건강연구소', 'HE-04', '직장인 번아웃의 원인과 회복 단계를 심리학적으로 분석. 자기 돌봄 루틴과 스트레스 대처법 수록.', '', '2026-05-17T11:00:00.000Z', '2026-05-17T11:00:00.000Z');
