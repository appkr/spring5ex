# 스프링 5 프레임웍 학습 프로젝트

초보 웹 개발자를 위한 스프링 5 프로그래밍 입문, 최범균 저

## ch 02 스프링 시작하기

#### mvn
```bash
$ mvn compile
```

#### gradle
```bash
$ gradle wrapper
$ gradle compileJava
```

#### Container & Bean
Role|Spring|Laravel
---|---|---
의존 역전 컨테이너|BeanFactory, ApplicationContext|ServiceContainer
객체 조립 공식 제공|Bean|ServiceProvider

**스프링의 Bean 제공 방법**
- `AnnotationConfigApplicationContext`
- `GenericXmlApplicationContext`
- `GenericGroovyApplicationContext`

#### 예제

```
+--------------------+
| AppContext         |
+--------------------+
| greeter(): Greeter |
+--------------------+
           ↑ 객체 조립 공식 요청
+--------------------+
| AnnotationConfig.. |
+--------------------+
    ↑ 객체 요청
+------+   call   +---------+
| Main | -------> | Greeter |
+------+          +---------+
```

![](img/ch02.png)

---

## ch 03 스프링 DI
DI를 하는 이유는 변경의 유연함때문이다.

#### 조립기를 이용한 DI
- 조립기를 new up하면 하위 객체들도 모두 생성된다.
- 조립기를 사용해도 의존의 의존을 쉽게 교체할 수 있다.

![](img/ch03-01.png)

#### 스프링 설정을 이용한 DI
- 설정용 클래스를 만들다 (e.g. `AppConf.java`).
- 클래스 선언에 `@Configuration` annotation을 붙여 설정임을 명시
- `@Configuration` 선언한 클래스의 각 메서드에 `@Bean` annotation을 붙여 Bean임을 명시
- 스프링 컨테이너를 new up하고 `getBean()` 함수를 호출하여 Bean으로 조립 공식을 명시했던 객체를 구한다.

```java
ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConf.class);
Foo foo = ctx.getBean("foo", Foo.class);
// getBean(name: String, type: String)
```

- 생성자 주입 vs 세터(setter) 주입
- 스프링 컨테이너는 기본적으로 싱글톤으로 객체를 생성한다. `getBean()` 함수를 호출해 객체를 여러번 구하더라도 반환되는 객체는 호출할 때마다 다른 불변 객체가 아니라, 최초 한번 생성된 객체가 계속 반환된다.
- `@Autowired` annotation을 의존 주입 대상에 붙이면 스프링 설정 클래스(e.g. `AppConf.java`)의 `@Bean` 메서드에서 의존 주입을 하지 않아도 자동 주입됨
- 항상 스프링 DI, 즉 `@Bean`을 이용해서 의존 주입을 해야 하나? NO.

![](img/ch03-02.png)
그림원본: https://docs.google.com/presentation/d/19irgFnDIMzhn34Q0n2BIusquKzra3VilYK9B-nqsaRQ/edit?usp=sharing

--- 

## ch04 의존 자동 주입

- 의존 자동 주입이란? `@Autowired`
- `@Autowired` 적용 가능 위치는? Field, Setter method
- 의존 자동 주입을 했는데, 일치하는 Bean이 선언되어 있지 않은 경우? `NoSuchBeanDefinitionException`
- Bean이 중복 선언되었고, 스프링이 하나를 선택할 수 없을 때? `NoUniqueBeanDefinitionException`
- 스프링에게 힌트 제공하기 `@Qualifier`
```java
@Configuration
public class AppCtx {
    @Bean
    @Qualifier("foo")
    public Foo foo() {}    
}

public class SomeService {
    @Autowired
    @Qualifier("foo")
    public void funcRequiresFoo() {}    
}
```
- `@Qualifier` 적용 가능 위치는? Field, Setter method
- 상속 그래프에 포함된 자식 객체일 때, 정확한 객체를 주입 받으려면?
    - `@Qualifier`를 이용해서 Bean 이름을 명시하거나
    - `@Autowired`선언된 필드 또는 함수의 매개 변수 타입을 하위 타입으로 명시
- 의존 자동 주입이 되지 않아도 작동하는 로직일 때는? 
    - `@Autowired(required = false)` 하거나
    - 필드 또는 매개 변수의 타입을 `Optional<Foo> foo`로 선언하거나
    - 필드 또는 매개 변수의 타입을 `@Nullable`로 선언
- `@Autowired(required = false)`는 Setter 함수가 호출되지 않는 반면, Optional, Nullable은 호출됨 유의
- 생성자에서 `@Autowired` 선언된 필드를 초기화했다면, 스프링이 의존 자동 주입을 다시 시도하므로 초기화된 필드값은 덮어써짐 유의

> 자동 주입을 하는 코드와 수동으로 주입하는 코드가 섞여 있으면 주입을 제대로 하지 않아서 `NullPointerException`이 발생했을 때 원인을 찾는데 오랜 시간이 걸릴 수 있다. 의존 자동 주입을 사용한다면 일관되게 사용해야 이런 문제가 줄어든다. 의존 자동 주입을 사용하고 있다면 일부 자동 주입을 적용하기 어려운 코드를 제외한 나머지 코드는 의존 자동 주입을 사용하자. 

---

## ch 05 컴포넌트 스캔

- 스프링이 직접 클래스를 검색해서 Bean으로 등록해 주는 기능
    - 자동 Bean 등록할 클래스 선언 바로 위에 `@Component` annotation 추가
    - 설정 클래스 선언 바로 위에 `@ComponentScan(basePackage = {"target package 1","target package 2"})` 추가
- 별칭을 부여하려면? `@Component("foo")`
- 별칭을 부여하지 않으면? 클래스 이름을 camelCase로 바꿔 Bean 이름으로 사용함. e.g. `class MemberDao` -> `memberDao`
- 스캔 대상에서 제외하려면?
```java
@Configuration
@ComponentScan(basePackage = {"foo"}, excludeFilters = @Filter(type = FilterType.REGEX, pattern = "foo\\..*Dao"))
public class AppCtx { }
```
- 다른 필터?
    - `excludeFilters = @Filter(type = FilterType.ASPECTJ, pattern = "foo.*Dao")`
    - `excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = {NoProduct.class, ManualBean.class})`
    - `excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MemberDao.class)` 자신 및 하위 타입 제외
- 다른 스캔 대상? `@Controller`, `@Service`, `@Repository`, `@Aspect`, `@Controller`
- Bean 이름 충돌?
    - 자동 스캔 과정에 충돌이 발생하면, 둘 중에 하나에 이름을 명시
    - 수동 등록한 Bean과 자동 스캔한 Bean 이름이 충돌하면, 수동 등록한 Bean 우선 사용

---
