# Jetpack Compose Migration Guide for Smouldering Durtles

## Current Status
✅ Compose dependencies added to `build.gradle`
✅ Kotlin plugin configured
✅ Legacy XML-based layouts working
✅ All navigation and intents functional

## Why Migrate to Compose?
1. **Reactive UI** - State changes automatically update the UI
2. **Less boilerplate** - No need for XML files and view binding
3. **Type-safe** - Composable functions are Kotlin code
4. **Better testing** - Easier to unit test composables
5. **Modern Android development** - Jetpack Compose is the recommended approach

## Architecture Overview

```
AbstractActivity (keeps working with XML)
├── onCreate → setContentView(layoutId) [existing flow]
└── onCreateLocal() [you implement this]

For Compose migration:
AbstractActivity (supports both)
├── setContent {} [new Compose approach]
└── setContentView(layoutId) [old XML approach - still works]
```

## Step-by-Step Migration Process

### Phase 1: Simple Screens (No Complex State)
Start with screens that don't require complex logic:
- `SupportActivity` (R.layout.activity_about)
- `DigraphHelpActivity` (R.layout.activity_digraph_help)
- `KeyboardHelpActivity` (R.layout.activity_keyboard_help)
- `AboutActivity` (simple info display)

### Phase 2: Interactive Screens (Button Clicks, Simple State)
- `DataImportExportActivity`
- `FontImportActivity`
- `DownloadAudioActivity`

### Phase 3: Complex Screens (LiveData, ViewModels, Fragment Management)
- `BrowseActivity` (with fragments)
- `SessionActivity` (complex UI with state)
- `MainActivity` (dashboard with many widgets)

## Example: Migrating SupportActivity

### Before (Current XML approach):
```java
// SupportActivity.java
public final class SupportActivity extends AbstractActivity {
    public SupportActivity() {
        super(R.layout.activity_about, R.menu.generic_options_menu);
    }

    @Override
    protected void onCreateLocal(final @Nullable Bundle savedInstanceState) {
        // No custom logic needed
    }

    @Override
    protected void onResumeLocal() { }

    @Override
    protected void onPauseLocal() { }

    @Override
    protected void enableInteractionLocal() { }

    @Override
    protected void disableInteractionLocal() { }

    @Override
    protected boolean showWithoutApiKey() {
        return true;
    }
}
```

### After (Compose approach):
```kotlin
// SupportScreen.kt
@Composable
fun SupportScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("About", style = MaterialTheme.typography.h5)
        Text("This is support information")
    }
}
```

```java
// SupportActivity.java - updated
public final class SupportActivity extends AbstractActivity {
    public SupportActivity() {
        super(R.layout.activity_about, R.menu.generic_options_menu);
    }

    @Override
    protected void onCreateLocal(final @Nullable Bundle savedInstanceState) {
        setComposeScreen(() -> SupportScreenKt.SupportScreen());
    }

    // ... rest stays the same
}
```

## Compose Basics You Need to Know

### 1. Composable Functions
```kotlin
@Composable
fun MyUI() {
    Text("Hello")
}
```

### 2. Layouts
```kotlin
Column { }              // Vertical stack (like LinearLayout)
Row { }                 // Horizontal stack
Box { }                 // FrameLayout equivalent
LazyColumn { }          // Recyclable list
```

### 3. Modifiers (Styling/Layout)
```kotlin
Modifier
    .fillMaxSize()      // Fill parent
    .padding(16.dp)     // Padding
    .background(Color.Blue)
    .clickable { }      // Click handler
    .size(100.dp)
    .align(Alignment.Center)
```

### 4. State Management
```kotlin
// Local state
var count by remember { mutableStateOf(0) }
Button(onClick = { count++ }) {
    Text("Count: $count")
}

// LiveData in Compose
val data: State<Int> = liveData.observeAsState(0)
Text("Value: ${data.value}")
```

### 5. Buttons & Inputs
```kotlin
Button(onClick = { /* action */ }) {
    Text("Click me")
}

TextField(
    value = text,
    onValueChange = { text = it }
)
```

## Key Files Created
- `ComposeActivitySupport.kt` - Provides `setComposeContentView()` helper
- `ComposeActivitySupport.kt` - Original helper (can be removed)
- `ComposeHelpers.kt` - Extension functions for easier adoption
- `screens/SupportScreen.kt` - Example screen template

## Navigation Strategy
**Keep using Intents** - Don't change how activities navigate:
```java
// Still works the same way
startActivity(new Intent(this, BrowseActivity.class));
goToActivity(MainActivity.class);
```

Each activity can gradually migrate to Compose without affecting the navigation system.

## LiveData Integration
Compose has built-in support for LiveData:
```kotlin
@Composable
fun MyScreenWithLiveData(viewModel: MyViewModel) {
    val count: State<Int> = viewModel.count.observeAsState(0)
    Text("Count: ${count.value}")
}
```

## Testing & Validation
For each screen you migrate:
1. Create the Compose version in `screens/`
2. Test in the activity using `setComposeScreen()`
3. Verify navigation and intents still work
4. Delete the old XML layout once confirmed

## Recommended First Migration Target
**SupportActivity** - Simplest with no complex logic
- No ViewModels needed
- No LiveData subscriptions
- No button click handlers
- Just display static information

Once you see this working, the pattern becomes clear for other screens.

## Resources
- [Compose Documentation](https://developer.android.com/jetpack/compose)
- [Compose StateFlow/LiveData](https://developer.android.com/jetpack/compose/state#livedata)
- [Compose Lists](https://developer.android.com/jetpack/compose/lists)
- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
