## Index

* [Compose](#Compose)
* [Compose basic](#Compose-basic)
* [Compose basic layout](#Compose-basic-layout)
* [State in Compose](#State-in-Compose)
* [Compose theming](#Compose-theming)

<br/>

## Compose

Compose란 기존 XML 방식에서 벗어난 Android의 최신 **UI 도구 키트**로서 XML 방식에 비해 더 쉽고 빠르게 UI를 작성할 수 있다.

<br>

**Compose의 특성**

* **선언적 UI(Declarative UI):** 선언적 UI란 어떤 방법으로 UI를 생성해야 하는지를 **생성방법**을 설명하는 것이 아닌, 어떤 결과가 나와야 하는지를 나타내도록 프로그래밍 하는 것. (어떤 방법으로 그릴지는 전적으로 프레임워크에 맡김)

<br/>

## <a href="https://github.com/younhwan97/android-jetpack-compose-practice/tree/main/jetpack-compose-basic">Compose basic</a>

> 🎯 Compose 프로젝트를 생성하고 선언적 UI의 기본사항을 직접 사용해본다. <br/> 
이를 통해 컴포저블과 Modifier가 무엇인지 알아보고, Row 및 Column과 같은 기본 UI 요소를 사용하는 방법과 앱에 State를 지정하는 방법을 익힌다.

### 1. Starting a new Compose project

`Android Studio` → `New Project` → `Empty Compose Activity`

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/create-compose-project-step-1.png?raw=true" width="700" height="500">

> 💡 minimumSdkVersion으로 API 수준 21 이상을 선택해야 한다. 이는 Compose에서 지원하는 최소 API 수준이다.

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

#### 3-2. Modifier

Surface 및 Text와 같은 대부분의 Compose UI는 modifier라는 매개변수를 허용한다. 

Modifer를 통해 컴포저블을 다양하게 수정할 수 있다.

```Kotlin
@Composable
fun Greeting(name: String) {
    Surface(
        color = MaterialTheme.colors.primary
    ) {
        Text(
            text = "Hello $name!", 
            modifier = Modifier.padding(16.dp)
        )
    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/tweaking-the-ui-2.png?raw=true" width="350">

> 💡 <a href="https://developer.android.com/jetpack/compose/modifiers-list" target="_blank">Compose modifier list</a>에서 modifer를 이용해 컴포저블에 할 수 있는 다양한 수정 사항을 알아볼 수 있다.

<br/>

### 4. Columns and Rows

Compose에서 다음과 같은 UI를 어떻게 생성할까?

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/columns-and-rows-2.png?raw=true" width="550">

<br/>

Compose에서는 다양한 UI를 생성하기 위해 `Column`, `Row` 그리고 `Box` 레이아웃을 사용할 수 있다.

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/columns-and-rows-1.png?raw=true" width="550">

```Kotlin
@Composable
fun MyApp(names: List<String> = listOf("World", "Compose")) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ){
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
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
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

<br/>

### 5. State in Compose

지금까지 정적인 레이아웃을 생성했다면, 이제는 state를 이용해 사용자와 상호작용을 할 수 있다.

아래와 같이 버튼을 클릭했을 때 영역이 확장되는 레이아웃을 만들어 본다.

<img src="https://developer.android.com/static/codelabs/jetpack-compose-basics/img/783e161e8bb1b2d5.gif?hl=ko" width="550" height="500">

위와 같은 레이아웃을 만들기 위해서 각 항목이 펼치진 상태인지를 가리키는 값을 어딘가에 저장해야 한다. 이 값을 **state**라고 한다.

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

이때 `expanded` state 또한 다시 생성, 초기화 되기 때문에 위 코드가 정상 동작 하지 않는 것 이다. 

`false -> [클릭 이벤트] -> true -> [리컴포지션] -> false`

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

`shouldShowOnboarding` state에 따라 OnboardingScreen 또는 Greetings을 보여줄지 선택한다. 

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
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
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

Compose에서는 `LazyColumn` 컴포저블에 데이터만 넣으면 나머진 프레임워크가 알아서 그려준다.

<br/>

## <a href="https://github.com/younhwan97/android-jetpack-compose-practice/tree/main/jetpack-compose-basic-layout">Compose basic layout</a>

> 🎯 Compose baisc에서 기본적인 compose 사용법에 대해 익혔다면, 지금부터는 다음과 같은 디자인 가이드에 따라 보다 현실적이고 복잡한 레이아웃을 구현해 본다.

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-design-guide.png?raw=true" width="350">

시작하기 앞서 [Android Github BasicLayoutsCodelab](https://github.com/googlecodelabs/android-compose-codelabs/tree/main/BasicLayoutsCodelab)에서 기본 테마 및 리소스가 포함된 프로젝트를 복제한다.

<br/>

### 1. Start with a plan

디자인을 구현하기 전 먼저 그 구조를 명확하게 파악하는 것이 좋다. 

곧바로 코딩을 시작하는 대신 디자인을 분석해 보자. 위 UI를 여러 개의 재사용 가능한 컴포저블로 나누려면 어떻게 해야 할까?

<br>

먼저 가장 높은 추상화 단계에서는 이 다지인을 두 부분으로 나눌 수 있다.

* 콘텐츠
* 바텀 내비게이션

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-design-guide-2.png?raw=true" width="350">

그리고 콘텐츠는 다음과 같이 세 부분으로 구성된다.

* 검색창
* "ALIGN YOUR BODY" Section
* "FAVORITE COLLECTIONS" Section

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
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, 
                contentDescription = "Search"
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        placeholder = {
            Text(stringResource(id = R.string.placeholder_search))
        },
        modifier = modifier
            .heightIn(min = 56.dp)
            .fillMaxWidth()
    )
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/basic-layout-search-bar.png?raw=true" width="350">

> 💡 SearchBar 컴포저블은 modifier를 받아서 TextField에 전달한다. <br/> 이 방식은 Compose 가이드라인에 따른 권장사항이며, 이 방식을 사용하면 메서드의 호출자가 컴포저블의 디자인과 분위기를 수정할 수 있어 유연성이 높아지고 재사용이 가능하게 된다.

<br>

### 3. "ALIGN YOUR BODY" Section

#### 3-1. Item

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

> 💡 컴포저블의 재사용을 위해 이미지와 텍스트 리소스를 상위 컴포저블로 부터 매개변수로 전달 받도록 했다.

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

#### 4-1. Item

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

State에 관해 자세히 알아보기 위해 다음과 같은 Wellness 앱을 구현한다.

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/state-in-compose-purpose.png?raw=true" width="350">

State란 **변경될 수 있는 모든 값**이다. 예를 들면 다음과 같은 것도 모두 state이다.

* 카카오톡에서 가장 최근에 수신된 메시지
* 사용자 프로필 사진
* 리스트 스크롤 위치

> 💡 핵심 아이디어: State에 따라 특정 시점에 UI에 표시되는 항목이 결정된다.

<br/>

### 1. Water Counter

remember을 이용해 리컴포지션에 대응할 수 있는 컴포저블을 다음과 같이 생성할 수 있다.

```Kotlin
@Composable
fun WaterCounter(
    modifier: Modifier = Modifier
) {
    var count by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = "You've had ${count} glasses.",
        )

        Button(
            onClick = { count++ },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Add one")
        }
    }
}
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/state-in-compose-1.png?raw=true" width="350">

화면 전환 및 다크 모드등의 모든 리컴포지션에 대응가능한 컴포저블을 생성했다.

<br/>

하지만 위와 같이 state를 내부에 가지고 있는 컴포저블은 재사용 가능성이 낮고, 테스트하기 어려운 경향이 있다.

그렇기 때문에 state를 상위 컴포저블로 올려 state를 갖지 않는 컴포저블로 만들면 이점을 가져올 수 있다. (= State hoisting)

```Kotlin
@Composable
fun WaterCounter(
    modifier: Modifier = Modifier
) {
    var count by rememberSaveable { mutableStateOf(0) }

    StatelessCounter(
        onIncrement = { count++ },
        count = count,
        modifier = modifier
    )

    var juiceCount by rememberSaveable { mutableStateOf(0) }

    StatelessCounter(
        onIncrement = { juiceCount++ },
        count = juiceCount, modifier =
        modifier
    )
}

@Composable
private fun StatelessCounter(
    onIncrement: () -> Unit,
    count: Int,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {


        Text(
            text = "You've had ${count} glasses.",
        )

        Button(
            onClick = onIncrement,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Add one")
        }
    }
}
```

<br/>

### 2. Wellness Task Item

```Kotlin
@Composable
fun WellnessTaskItem(
    taskName: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var checkedState by rememberSaveable { mutableStateOf(false) }

    WellnessTaskItem(
        taskName = taskName,
        checked = checkedState,
        onCheckedChange = { newValue -> checkedState = newValue },
        onClose = onClose,
        modifier = modifier,
    )
}

@Composable
fun WellnessTaskItem(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = taskName
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}
```

<br/>

### 3. Wellness Task List (LazyColumn)

```Kotlin
@Composable
fun WellnessTaskList(
    list: List<WellnessTask>,
    onCloseTask: (WellnessTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = list, key = { task -> task.id }) { task ->
            WellnessTaskItem(
                taskName = task.label,
                onClose = { onCloseTask(task) }
            )
        }
    }
}
```

**WellnessTask**

```Kotlin
data class WellnessTask(
    val id: Int,
    val label: String
)
```

**WellnessScreen**

```Kotlin
@Composable
fun WellnessScreen() {
    Column {
        WaterCounter()

        val list = remember { getWellnessTasks().toMutableStateList() }
        WellnessTaskList(list = list, onCloseTask = { task -> list.remove(task)})
    }
}

fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task $i") }
```

<img src="https://github.com/younhwan97/android-jetpack-compose-practice/blob/main/images/state-in-compose-2.png?raw=true" width="350">

<br/>

### 4. ViewModel

```Kotlin
class WellnessViewModel : ViewModel() {
    /**
     * Don't expose the mutable list of tasks from outside the ViewModel.
     * Instead define _tasks and tasks. _tasks is internal and mutable inside the ViewModel.
     * tasks is public and read-only.
     */
    private val _tasks = getWellnessTasks().toMutableStateList()
    val tasks: List<WellnessTask>
        get() = _tasks

    fun remove(item: WellnessTask) {
        _tasks.remove(item)
    }
}

fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task $i") }
```

<br/>

### 5. LiveData vs State

기존 XML를 사용하는 MVVM 아키텍쳐에서는 viewModel에서 다음과 같이 LiveData를 사용했다. 

```Kotlin
class MainViewModel : ViewModel() {

    private var _testMutableLiveData = MutableLiveData(0)
    val testLiveData : LiveData<Int>
        get() = _testMutableLiveData

    fun plus(){
        _testMutableLiveData.value = _testMutableLiveData.value!!.plus(1)
    }
}
```

<br/>

**그렇다면 LiveData와 State는 무엇이 다를까 ❓**

LiveData의 특징은 observe 메서드를 통해 값이 변하는 것을 관찰 할 수 있다는 것이다.

이를 이용해 값의 변화를 감지하면, 다음과 같이 미리 정의된 로직이 실행된다. 

```Kotlin
viewModel.testLiveData.observe(this) {
    findViewById<TextView>(R.id.count).text = it.toString()
}
```

위 방법은 UI 업데이트를 **어떻게 할지** 전적으로 개발자가 책임진다. 

<br>

하지만 State는 그렇지 않다. 값이 바뀌면 State에 알맞은 UI로 프레임워크가 알아서 다시 그려준다. 

Compose에서는 한 번 그려진 UI를 수정할 수 없다. state가 바뀌면 그에 맞는 UI를 다시 그릴 뿐이다.

개발자가 직접 UI를 갱신하지 않는다.

<br/>

## <a href="https://github.com/younhwan97/android-jetpack-compose-practice/tree/main/jetpack-compose-theming">Compose theming</a>

Jetpack compose 프로젝트 생성시 다음과 같은 디렉토리 및 kotlin 파일이 생성되는 것을 확인할 수 있다.

    - ui  
        - theme
            - Color.kt
            - Shape.kt
            - Theme.kt
            - Type.kt

Compose에서 새로 생긴 UI 테마로써 그 역할은 다음과 같다.

- `Color.kt`: 기존에는 앱에서 자주쓰는 색상들을 res → colors.xml 에 저장해왔다. 이제는 Colors.kt 같은 kotlin 파일에 저장한다.
- `Shape.kt`: 원래 Shape들을 drawables 아래에 xml로 만들어서 배경으로 넣어주곤 했다. Compose에는 Shape라는 파라미터를 정의해주면 알아서 둥글게 처리된다. 통일감있는 둥글기 처리를 위해 이 Shape의 Radius값들도 테마로서 정의할 수 있다. 
- `Theme.kt`: theme.xml에 저장하고는 했었던 내용들을 Theme.kt에 작성할 수 있다.
- `Type.kt`: 타이포 그래피를 정의할 수 있다. 커스텀 폰트를 사용하기 위해서 앱의 res → font에 사용할 font를 넣는 방식은 같다.

