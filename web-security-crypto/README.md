## Web Security Crypto

```sh
keytool

키 및 인증서 관리 툴

명령:

 -certreq            인증서 요청을 생성합니다.
 -changealias        항목의 별칭을 변경합니다.
 -delete             항목을 삭제합니다.
 -exportcert         인증서를 익스포트합니다.
 -genkeypair         키 쌍을 생성합니다.
 -genseckey          보안 키를 생성합니다.
 -gencert            인증서 요청에서 인증서를 생성합니다.
 -importcert         인증서 또는 인증서 체인을 임포트합니다.
 -importpass         비밀번호를 임포트합니다.
 -importkeystore     다른 키 저장소에서 하나 또는 모든 항목을 임포트합니다.
 -keypasswd          항목의 키 비밀번호를 변경합니다.
 -list               키 저장소의 항목을 나열합니다.
 -printcert          인증서의 콘텐츠를 인쇄합니다.
 -printcertreq       인증서 요청의 콘텐츠를 인쇄합니다.
 -printcrl           CRL 파일의 콘텐츠를 인쇄합니다.
 -storepasswd        키 저장소의 저장소 비밀번호를 변경합니다.
```

```sh
# keytool -genkey -alias kdevkr -keyalg RSA -keystore keystore.jks -storepass changeit -dname "CN=Demo"
# keytool -importkeystore -srckeystore keystore.jks -destkeystore keystore.jks -deststoretype pkcs12

keytool -genkey -alias kdevkr -keyalg RSA -keystore keystore.jks -storetype pkcs12 -storepass changeit -dname "CN=Demo"
```


