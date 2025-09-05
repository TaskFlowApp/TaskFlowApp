# @JsonInclude(JsonInclude.Include.NON_NULL)
참고링크 : https://yuma1029.tistory.com/9

## 1. 설명
자바 객체 → JSON 형식으로 직렬화 때 포함할 속성을 지정할 때 사용. <br>
예를 들어 null 값을 자바 객체를 직렬화할 때 JSON문자열에 해당 null 값이 포함된다. <br>
@JsonInclude를 사용해 JSON에서 포함돼야 하는 속성과 제외할 속성이 지정 가능.

## 1-1. 사용 예시
```java
// 널 값의 속성은 포함하지 않음
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestClass {
    ...
}
```

## 2. 옵션
@JsonInclude는 옵션을 가진다. 대표적으로 다섯 가지가 있으며 아래와 같다.

## 2-1 옵션의 종류
| 옵션 이름 | 설명| 
|-------|----|
|JsonInclude.Include.ALWAYS | 값에 상관없이 출력|
|NON_NULL| NULL 값이 아닌 경우에만 직렬화된 출력|
|NON_EMPTY| NULL 값이 아니고 빈 컬렉션, 맵이 아닌 경우 출력|
|NON_DEFAULT| 속성의 값이 해당 데이터 형식의 기본값과 다른 경우 출력|
|CUSTOM| 속성이 직렬화된 출력에 포함돼야 하는 지를 결정 가능|

## 2-1 옵션 적용 예시 설명
```java
@JsonInclude(JsonInclude.Include.NON_NULL) // 널 값이 아닌 경우에만 반환
public class Person {
    private String name;
    private Integer age;
    @JsonInclude(JsonInclude.Include.NON_NULL) // 널 값이 아닌 경우에만 반환 hobbies에 지정
    private List<String> hobbies;
        ...
}
```
해당 클래스는 Null 값이 아닌 값만 반환하게 돼 있고, <br>
컬렉션인 `hobbies` 또한 Null값인 경우 반환하지 않는 `@JsonInclude(content = Include.NON_NULL)`이 적용됐다.

```java
Person person = new Person();
person.setName("John");
person.setAge(null);
person.setHobbies(Arrays.asList("reading", null, "swimming"));
```

## 2-2. 변경된 결과 설명
```java
{
        "name": "John",
        "age" : null,
        "hobbies": ["reading", null, "swimming"]
        }
```

```java
{
    "name": "John",
    "hobbies": ["reading", "swimming"]
}
```

JSON의 결괏값을 확인해 보면 Null 값이던 age 속성과 hobbies 내의 null 값의 경우 반환이 되지 않는 것을 확인.
