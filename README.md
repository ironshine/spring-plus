# SPRING PLUS

## EC2
|Method|기능|URL|Response|상태코드|
|-|-|-|-|-|
|GET|healthCheck|/actuator/health|{"status": "UP"}|200|

![스크린샷 2024-10-10 225844](https://github.com/user-attachments/assets/15007bac-a88c-4b92-bdf5-4f0c17efa09e)

## RDS

![스크린샷 2024-10-10 225542](https://github.com/user-attachments/assets/d41e74c9-43a3-4e42-9114-90918648702b)

## S3

![스크린샷 2024-10-10 230144](https://github.com/user-attachments/assets/f19a57be-9759-40be-b46f-3ca2f7ccd4b5)

## 회고
1-5 문제를 잘 못 이해한것 같다.
weather, 기간의 시작, 기간의 끝 각각 null 가능성을 열어두고 만들어서

![스크린샷 2024-10-10 234553](https://github.com/user-attachments/assets/baa319e7-4920-4432-bd10-f580505d2c89)
![스크린샷 2024-10-10 235408](https://github.com/user-attachments/assets/ac063d88-afd7-4918-9d84-207c7559b8e0)

너무 많은 jPQL을 작성했다.
솔직히 너무 더럽다.. 그래서 후에 QueryDSL로 변경했지만 여전히 별로다.. 더 좋은 방법이 생각나지 않는다..
빨리 해설영상 보고싶다.

----

3-12-1 EC2 health check api 가 맞는지 모르겠다.

load balancer 에서 health check api를 "/health" 로 했지만 http://{ip주소}/health 하면 404 에러가 뜬다.

actuator를 사용해서 "/actuator/health" 로 {"status" : "up"} 를 했지만 뭔가... "/health" 를 건드려야할것같다..해설영상 보고싶다.
