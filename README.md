## Index

* [Compose](#Compose)
* [Compose basic](#Compose-basic)
* [Compose basic layout](#Compose-basic-layout)
* [State in Compose](#State-in-Compose)

<br/>

## Compose

Compose란 기존 XML 방식에서 벗어난 Android의 최신 **UI 도구 키트**로서 XML 방식에 비해 더 쉽고 빠르게 UI를 작성할 수 있다.

<br>

**Compose의 특성**

* 선언형 UI(Declarative UI): 선언형 UI란 어떤 방법으로 UI를 생성해야 하는지를 **생성방법**을 설명하는 것이 아닌, 어떤 결과가 나와야 하는지를 나타내도록 프로그래밍 하는 것. (어떤 방법으로 그릴지는 전적으로 프레임워크에 맡김)

<br/>

## <a href="https://github.com/younhwan97/android-jetpack-compose-practice/tree/main/jetpack-compose-basic">Compose basic</a>

### 1. Starting a new Compose project

Android Studio → New Project → Empty Compose Activity

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/create-compose-project-step-1.png?raw=true" width="700" height="500">

> minimumSdkVersion으로 API 수준 21 이상을 선택해야 한다. 이는 Compose에서 지원하는 최소 API 수준이다.

<br/>

### 2. Composable

컴포저블은 `@Composable`이라는 주석이 달린 일반 함수이다. 

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

modifer를 통해 컴포저블을 다양하게 수정할 수 있다.

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

이제 `expanded` state에 따라 항목이 펼쳐지도록 하기 위해 다음과 같이 코드 작성하면 된다.

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

<br/>

### 6. State hoisting

State hoisting이란 state를 상위 항목으로 이동시키는 과정을 의미한다. 

state를 상위 항목으로 올릴 수 있게 되면 상태가 중복되는 버그를 막고, 컴포저블을 재사용하는데 도움이 된다. 

<br/>

반면 컴포저블을 상위 항목에서 제어하지 않는 경우는 hoisting 해서는 안된다. 앞선 Greeting의 예제가 그랬다. 

다음 코드를 이용해 온보딩 화면을 구현하고, hoisting에 대해 알아본다. 

```Kotlin
@Composable
fun OnboardingScreen(modifier: Modifier = Modifier) {
    // TODO: This state should be hoisted
    var shouldShowOnboarding by remember { mutableStateOf(true) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { shouldShowOnboarding = false }
        ) {
            Text("Continue")
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    BasicsCodelabTheme {
        OnboardingScreen()
    }
}
```

그리고 `shouldShowOnboarding` state에 따라 OnboardingScreen 또는 Greetings을 보여줄지 선택한다. 

그렇기 때문에 OnboardingScreen 컴포저블에서 `shouldShowOnboarding` state는 hoisting 돼야 한다.

```Kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsComposeTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    var shouldShowOnboarding by remember { mutableStateOf(true) }

    if (shouldShowOnboarding) {
        OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
    } else {
        Greetings()
    }
}

// Greeting
@Composable
fun Greetings(names: List<String> = listOf("World", "Compose")) {
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

// Onboarding
@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}
```

<br/>

### 7. Creating a performant lazy list

기존 XML의 RecyclerView에 해당하는 부분으로, Greeting 항목이 늘어났을 때 처리하는 방법에 대해 알아본다.

```Kotlin
@Composable
fun Greetings(names: List<String> = List(1000) {"$it"}) {
    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            LazyColumn {
                items (names) { name ->
                    Greeting(name = name)
                }
            }
        }
    }
}
```

<img src="https://developer.android.com/static/codelabs/jetpack-compose-basics/img/2e29949d9d9b8690.gif?hl=ko" width="350" height="500">

더이상 리스트를 만들기 위해 `ViewGroup`, `LayoutManager`, `ViewHolder`, `Adapter`, `DiffUtil`, `Item layout`를 사용 할 필요가 없다.

Compose에서는 `LazyColumn` 컴포저블에 데이터만 넣으면 (프레임워크가) 알아서 그려준다.

<br/>

## <a href="https://github.com/younhwan97/android-jetpack-compose-practice/tree/main/jetpack-compose-basic-layout">Compose basic layout</a>

Compose baisc에서 기본적인 compose 사용법에 대해 익혔다면, 지금부터는 다음과 같은 디자인 가이드라인에 따라 앱 디자인을 구현한다.

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-design-guide.png?raw=true" width="350">

시작하기 앞서 [Android Github BasicLayoutsCodelab](https://github.com/googlecodelabs/android-compose-codelabs/tree/main/BasicLayoutsCodelab)에서 기본 테마 및 리소스가 포함된 프로젝트를 복제한다.

<br/>

### 1. Start with a plan

디자인을 구현하라는 요청을 받았을 때 먼저 디자인의 구조를 명확하게 파악하는 것이 좋다. 

곧바로 코딩을 시작하는 대신 디자인을 분석해 보자. 위 UI를 여러 개의 재사용 가능한 부분으로 나누려면 어떻게 해야 할까?

<br>

먼저 가장 높은 추상화 단계에서는 이 다지인을 두 부분으로 나눌 수 있다.

* 콘텐츠
* 바텀 내비게이션

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-design-guide-2.png?raw=true" width="350">

그리고 콘텐츠는 다음과 같이 세 부분으로 구성된다.

* 검색창
* ALIGN YOUR BODY
* FAVORITE COLLECTIONS

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-design-guide-3.png?raw=true" width="350">

<br/>

### 2. Search bar

 ```Kotlin
 @Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        modifier = modifier
            .heightIn(min = 56.dp)
            .fillMaxWidth(),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        placeholder = {
            Text(stringResource(id = R.string.placeholder_search))
        },
    )
}
 ```

SearchBar 컴포저블 함수는 modifier 매개변수를 받아서 TextField에 전달한다.

이 방식은 Compose 가이드라인에 따른 권장사항이며, 이 방식을 사용하면 메서드의 호출자가 컴포저블의 디자인과 분위기를 수정할 수 있어 유연성이 높아지고 재사용이 가능하게 된다.

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-search-bar.png?raw=true" width="350">

<br>

### 3. "ALIGN YOUR BODY" Section

#### 3-1. Element

```Kotlin
@Composable
fun AlignYourBodyElement(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painterResource(id = drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
        )
        Text(
            stringResource(id = text),
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .paddingFromBaseline(top = 24.dp, bottom = 8.dp)
        )
    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-element-2.png?raw=true" width="150">

<br/>

#### 3-2. LazyRow

```Kotlin
private val alignYourBodyData = listOf(
    R.drawable.ab1_inversions to R.string.ab1_inversions,
    R.drawable.ab2_quick_yoga to R.string.ab2_quick_yoga,
    R.drawable.ab3_stretching to R.string.ab3_stretching,
    R.drawable.ab4_tabata to R.string.ab4_tabata,
    R.drawable.ab5_hiit to R.string.ab5_hiit,
    R.drawable.ab6_pre_natal_yoga to R.string.ab6_pre_natal_yoga
).map { DrawableStringPair(it.first, it.second) }

@Composable
fun AlignYourBodyRow(
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier
    ) {
        items(alignYourBodyData) { item ->
            AlignYourBodyElement(
                drawable = item.drawable,
                text = item.text
            )
        }
    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-lazy-row.png?raw=true" width="550">

<br/>

### 4. "FAVORITE COLLECTIONS" Section

#### 4-1. Element

```Kotlin
@Composable
fun FavoriteCollectionCard(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(192.dp)
        ) {
            Image(
                painterResource(id = drawable),
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                stringResource(id = text),
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-element-4.png?raw=true" width="350">

<br/>

#### 4-2. LazyGrid

```Kotlin
private val favoriteCollectionsData = listOf(
    R.drawable.fc1_short_mantras to R.string.fc1_short_mantras,
    R.drawable.fc2_nature_meditations to R.string.fc2_nature_meditations,
    R.drawable.fc3_stress_and_anxiety to R.string.fc3_stress_and_anxiety,
    R.drawable.fc4_self_massage to R.string.fc4_self_massage,
    R.drawable.fc5_overwhelmed to R.string.fc5_overwhelmed,
    R.drawable.fc6_nightly_wind_down to R.string.fc6_nightly_wind_down
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

@Composable
fun FavoriteCollectionsGrid(
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        modifier = modifier.height(120.dp)
    ) {
        items(favoriteCollectionsData) { item ->
            FavoriteCollectionCard(
                drawable = item.drawable,
                text = item.text
            )
        }
    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-lazy-grid.png?raw=true" width="550">

<br/>

### 5. Home Section

"ALIGN YOUR BODY"와 "FAVORITE COLLECTIONS"는 제목과 섹션으로 서로 구성이 같아 컴포저블로 만들어 재사용 할 수 있다.

이때 섹션에 들어갈 내용은 동적으로 바뀐다. 이 유연한 섹션 컨테이너를 구현하려면 슬롯 API를 사용해야 한다.

```Kotlin
@Composable
fun HomeSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            stringResource(id = title).uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .paddingFromBaseline(bottom = 8.dp, top = 40.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-home-section.png?raw=true" width="550">

<br/>

### 6. HomeScreen

```Kotlin
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        SearchBar(
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        HomeSection(title = R.string.align_your_body) {
            AlignYourBodyRow()
        }

        HomeSection(title = R.string.favorite_collections) {
            FavoriteCollectionsGrid()
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-home-screen.png?raw=true" width="550">

<br/>

### 7. BottomNavigation

```Kotlin
@Composable
private fun SootheBottomNavigation(modifier: Modifier = Modifier) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        BottomNavigationItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Spa, contentDescription = null) },
            label = { Text(stringResource(id = R.string.bottom_navigation_home)) }
        )

        BottomNavigationItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text(stringResource(id = R.string.bottom_navigation_profile)) }
        )
    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-bottom-navigation.png?raw=true" width="550">

<br/>

### 8. Scaffold

```Kotlin
@Composable
fun MySootheApp() {
    MySootheTheme {
        Scaffold(
            bottomBar = { SootheBottomNavigation() }
        ) { padding ->
            HomeScreen(Modifier.padding(padding))
        }
    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout.png?raw=true" width="350">

<br/>

## <a href="https://github.com/younhwan97/android-jetpack-compose-practice/tree/main/jetpack-compose-basic">State in Compose</a>

