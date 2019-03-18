# 테스트 코드와 Rest Docs 예제

```
$ mvn package
$ mvn spring-boot:run
```

Run with

http://127.0.0.1:8080/docs/index.html

문서 기본 형태 만들었다.

대체 옵션은 왜 안되는거야???

## 아직 안된 부분

- src/main/asciidoc/index.adoc 에 :source-highlighter: highlightjs 가 먹질 않는다.
- :operation-curl-request-title: Example request
  :operation-http-response-title: Example response 가 적용이 안된다  