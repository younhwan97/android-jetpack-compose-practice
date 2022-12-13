# android-jetpack-compose-practice

## Compose 개요

Compose란 기존 XML 방식에서 벗어난 Android의 최신 **UI 도구 키트**로서 XML 방식에 비해 더 쉽고 빠르게 UI를 작성할 수 있다.

Compose는 다음과 같은 특징을 통해 UI를 쉽고 빠르게 작성할 수 있다.

* 선언형 UI(Declarative UI): 선언형 UI란 어떤 방법으로 UI를 생성해야 하는지를 **생성방법**을 설명하는 것이 아닌, 어떤 결과가 나와야 하는지를 나타내도록 프로그래밍 하는 것. (어떤 방법으로 그릴지는 전적으로 프레임워크에 맡김)

<br/>

## <a href="https://github.com/younhwan97/android-jetpack-compose-practice/tree/main/jetpack-compose-basic">Compose basic</a>

### 1. 프로젝트 생성

Android Studio -> New Project -> Empty Compose Activity

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/create-compose-project-step-1.png?raw=true" width="700" height="500">

(min SDK: API 21)

<br/>

### 2. Composable

Composable은 `@Composable`이라는 주석이 달린 일반 함수이다. 

함수 내부에서 다른 `@Composable`를 호출할 수 있으며, 새로운 UI를 생성할 때 해당 함수만 있으면 된다.

```Kotlin
@Composable
private fun Greeting(name: String) {
   Text(text = "Hello $name!") // 함수 내부에서 다른 Composable를 호출할 수 있다.
}
```

<br/>

### 3. Tweaking the UI

#### 3-1. Surface, color

Greeting에 다른 배경 색상을 설정하기 위해 Text 컴포저블을 Surface로 래핑한다. 

그리고 Surface의 색상 속성에 MaterialTheme.colorScheme.primary를 적용한다.

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

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/tweaking-the-ui.png?raw=true" width="350">

<br/>

#### 3-2. modifier

Surface 및 Text와 같은 대부분의 Compose UI 요소는 modifier라는 매개변수를 허용한다. 

modifer를 통해 composable을 다양하게 수정할 수 있다.

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

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/tweaking-the-ui-2.png?raw=true" width="350">

<br/>

### 4. Columns and Rows

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/columns-and-rows-1.png?raw=true" width="550">

Compose에서는 다양한 UI를 생성하기 위해 `Column`, `Row` 그리고 `Box` 표준 레이아웃을 사용할 수 있다.

```Kotlin
@Composable
fun MyApp(names: List<String> = listOf("World", "Compose")) {
    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            for (name in names) {
                Greeting(name = name)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = "Hello")

                Text(text = "$name!")
            }

            OutlinedButton(onClick = { /*TODO*/ }) {
                Text("Show more")
            }
        }
    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/columns-and-rows-2.png?raw=true" width="550">

<br/>

### 5. State in Compose

지금까지 정적인 레이아웃을 생성했다면, 이제는 state를 이용해 사용자와 상호작용을 할 수 있다.

아래와 같이 버튼을 클릭했을 때 영역이 확장되는 레이아웃을 만들어 본다.

<img src="https://developer.android.com/static/codelabs/jetpack-compose-basics/img/783e161e8bb1b2d5.gif?hl=ko" width="550" height="500">

이러한 레이아웃을 만들기 위해서 각 항목이 펼치진 상태인지를 가리키는 값을 어딘가에 저장해야 한다. 이 값을 **state**라고 한다.

요소마다 이러한 값이 필요하므로 이 값은 Greeting 컴포저블에 위치해야 한다.

```Kotlin
@Composable
fun Greeting(name: String) {
    var expanded = false

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = "Hello")

                Text(text = "$name!")
            }

            OutlinedButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "Show less" else "Show more")
            }
        }
    }
}
```

Greeting 컴포저블에 `expanded` state를 만들고 Button 컴포저블의 클릭이벤트로 값을 변경하도록 했다. 

하지만 위 코드는 정상적으로 작동하지 않는다. 그 이유는 리컵포지션에 있다. 

<br>

Jetpack compose에서 컴포저블의 state가 변경되면 해당 컴포저블 함수만을 다시호출해 UI를 다시 그린다. 

이때 `expanded` state 또한 다시 생성, 초기화 되기 때문에 위 코드가 정상 동작 하지 않는 것 이다. (false -> [클릭 이벤트] -> true -> [리컴포지션] -> false)

그래서 Compose에 해당 값을 잘 살피라고 알릴 방법을 찾아야한다.

```Kotlin
@Composable
fun Greeting(name: String) {
    var expanded = remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = "Hello")

                Text(text = "$name!")
            }

            OutlinedButton(onClick = { expanded.value = !expanded.value }) {
                Text(if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}
```

위와 같은 방법을 통해 컴포저블이 리컴포지션 될 때마다 state 값이 재설정 되지 않도록 해야한다.

이제 `expanded` state를 이용해 항목이 펼쳐지도록 하기 위해 다음과 같이 코드를 작성한다.

```Kotlin
@Composable
fun Greeting(name: String) {
    val expanded = remember { mutableStateOf(false) }
    val extraPadding = if (expanded.value) 48.dp else 0.dp

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding)
            ) {
                Text(text = "Hello")

                Text(text = "$name!")
            }

            OutlinedButton(onClick = { expanded.value = !expanded.value }) {
                Text(if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}
```