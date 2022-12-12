# android-jetpack-compose-practice

## Compose 개요

Compose란 기존 XML 방식에서 벗어난 Android의 최신 **UI 도구 키트**로서 XML 방식에 비해 더 쉽고 빠르게 UI를 작성할 수 있다.

Compose는 다음과 같은 특징을 통해 UI를 쉽고 빠르게 작성할 수 있다.

* 선언형 UI(Declarative UI): 선언형 UI란 어떤 방법으로 UI를 생성해야 하는지를 **생성방법**을 설명하는 것이 아닌, 어떤 결과가 나와야 하는지를 나타내도록 프로그래밍 하는 것. (어떤 방법으로 그릴지는 전적으로 프레임워크에 맡김)

<br/>

## <a href="https://github.com/younhwan97/android-jetpack-compose-practice/tree/main/jetpack-compose-basic">Compose basic</a>

### 1. 프로젝트 생성

Android Studio -> New Project -> Empty Compose Activity

<div align="center">
    <img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/create-compose-project-step-1.png?raw=true" width="700">
</div>

(min SDK: API 21)

<br/>

### 2. Composable

Composable은 `@Composable`이라는 주석이 달린 일반 함수이다. 함수 내부에서 다른 `@Composable`를 호출할 수 있으며, 새로운 UI를 생성할 때 해당 함수만 있으면 된다.

```Kotlin
@Composable
private fun Greeting(name: String) {
   Text(text = "Hello $name!") // 함수 내부에서 다른 Composable를 호출할 수 있다.
}
```

<br/>

### 3. Tweaking the UI

#### **Surface, color**

Greeting에 다른 배경 색상을 설정하기 위해 Text 컴포저블을 Surface로 래핑한다. 그리고 Surface의 색상 속성에 MaterialTheme.colorScheme.primary를 적용한다.

```Kotlin
@Composable
fun Greeting(name: String) {
    Surface(
        color = MaterialTheme.colors.primary
    ) {
        Text(text = "Hello $name!")
    }
}
```
<div align="center">
    <img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/tweaking-the-ui.png?raw=true" width="500">
</div>

(배경색만 바꿨지만 Material Design에 의해 글자색도 함께 바뀐 것을 볼 수 있다.)

<br/>

#### **modifier**

Surface 및 Text와 같은 대부분의 Compose UI 요소는 modifier라는 매개변수를 허용한다. modifer를 통해 composable을 다양하게 수정할 수 있다.

```Kotlin
@Composable
fun Greeting(name: String) {
    Surface(
        color = MaterialTheme.colors.primary
    ) {
        Text(text = "Hello $name!", modifier = Modifier.padding(16.dp))
    }
}
```

<div align="center">
    <img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/tweaking-the-ui-2.png?raw=true" width="500">
</div>

<br/>

### 4. Columns and Rows

<div align="center">
    <img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/columns-and-rows-1.png?raw=true" width="700">
</div>