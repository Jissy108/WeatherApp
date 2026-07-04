## NOTES

---
### Gradle Dependency Management

#### Old Gradle (Groovy)
Libraries were added directly in `build.gradle`. Example: implementation 'com.squareup.retrofit2:retrofit:2.11.0'

#### New Gradle (Version Catalog)
Dependency management is split into **2 files**.
 1. `libs.versions.toml` Stores: Library versions  Example:
    retrofit = "2.11.0"  and Library aliases (short names) Eg-> retrofit = { module = "com.squareup.retrofit2:retrofit", version.ref = "retrofit" }
 2. `app/build.gradle.kts`
Uses the aliases defined above. 
dependencies {   implementation(libs.retrofit)   }

#### Why Version Changed?
Instead of writing  implementation "com.squareup.retrofit2:retrofit:2.11.0" every time, we define it once in `libs.versions.toml` and use
implementation(libs.retrofit) help to keep 
* Centralized version management. 
* Easier updates.
* Cleaner `build.gradle`.
* Better for large projects.

> **Whenever you add, remove, or modify a dependency (or plugin), always perform a Gradle Sync before expecting Android Studio to recognize the changes.**

---


## Weather API - Data Flow

##### How does API know the temperature?
- Latitude & Longitude uniquely identify a location and server uses it to find weather data of that place
- API returns the **latest available** weather for the requested location.
- Since weather changes continuously, every request may return different data. As sources below continuously servers and give accurate and current data
- Api collects weather data from:
    - Weather Stations, Satellites, Radars, Government Weather Organizations


##### Does API data change?

- Weather changes continuously. So every API request & returns the **latest available** data.
- API responds **only when a request is made**.
- To get updated weather, the app must send another request.
- Common triggers:
   - App opens
   - User refreshes
    - Periodic refresh (if app chooses)



### Offline Handling

##### Internet Available

```
API
 │
 ▼
Latest Weather
 │
 ├── Update UI
 └── Save to Room
```

##### No Internet

```
API Request
     │
     ▼
Fails
     │
     ▼
Read Room
     │
     ▼
Show Last Saved Weather
+
"No Internet. Showing previously downloaded weather."
```

##### Production Architecture (Recommended)



```
Instead of    Most production apps use
this          this
API           API   
 │             |
 ▼             ▼
UI            Room (Single Source of Truth)
 │             |
 ▼             ▼
Room          UI (Observes Room)
```



##### Why API → Room → UI?

- Single Source of Truth (Room).
- UI & database stay synchronized.
- Multiple screens update automatically.
- Better MVVM architecture.




>**Q. Why do modern Android apps prefer `API → Room → UI` instead of `API → UI → Room`?**
**Ans:** Room acts as the **Single Source of Truth**, keeping the UI and local database synchronized. Any screen observing Room automatically receives updated data, making the architecture more reliable and scalable.
---
---
## Location Permission in Android Notes


#### Overall Permission Flow

Before an app can access the user's location, **two conditions must be satisfied**:

1.  Phone's Location Service (GPS/Network) must be ON.(Checked by isLocationEnabled())
2. App must have Location Permission. Both are independent.(Checked by requestPermissions() )


```text
Location Service = OFF          Location Service = ON
Permission = Granted            Permission = Denied
Result-                         Result-
Cannot access location.         Cannot access location.
```
### Complete Flow

```text
App Starts
↓
isLocationEnabled()
↓
Location Service Enabled?
├── NO
│      ↓
│    Show Toast
│    Open Phone Location Settings
└── YES
       ↓
      requestPermissions()  (as got location so get permission now)
              ↓
          Permission Already Granted?(if and else if block fine and coarse location)
              ├── YES
              │      ↓
              │ Continue with Location
              │
              └── NO
                   ↓
                   Need Explanation?
                    ├── YES
                    │      ↓
                    │ showRequestDialog()
                    │
                    └── NO
                         ↓
                      Android Permission Popup (the else block operate)
                         ↓
                        User Choice
                            ↓
                           Allow / Deny / Ask everytime I open
```

### 1. isLocationEnabled()


#### Getting the Location Manager

```kotlin
val locationManager =
    getSystemService(Context.LOCATION_SERVICE) as LocationManager
```

Android provides many system services.

├── Camera Manager
├── Audio Manager
├── Bluetooth Manager
├── Wifi Manager
└── Location Manager


This line **retrieves** the **Location Manager**.


#### Checking GPS/Network provider

```kotlin
//Checks if GPS is ON.
locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//Checking Network Provider
locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
```



Both will return either true or false and there OR will give final result
- return GPS_PROVIDER || NETWORK_PROVIDER

Now if we hv **location** return **true** and **if block** takes **false** hence **enter in else block** and **request permission**
otherwise give toast and open settings




```kotlin
Settings.ACTION_LOCATION_SOURCE_SETTINGS
//This opens Phone Location Settings
```



### 2. requestPermissions()

Android internally remembers permission state.

#### State 1

```text
Permission Never Asked user enter for first time
  |──>shouldShowRequestPermissionRationale() returns false
         ↓
         Enters else block and user shows its in built dialog to ask permission
         Allow/Deny/While using app
```


#### State 2

```text
 user presses deny
  |──>Android rem permission denied
        |──>shouldShowRequestPermissionRationale() returns True
              ↓
            Enters if block 
              ↓
            showRequestDialog()  "Explain to the user why this permission is needed before asking again."
```


**shouldShowRequestPermissionRationale()** Explains why Should the app needs the permission before requesting again?




#### Difference Between Fine and Coarse Location

#### Fine Location
1. Permission - ACCESS_FINE_LOCATION
2. Uses - GPS ,WiFi ,Mobile Towers
3. Accuracy - Around 5–20 meters
4. Used in - Google Maps Navigation ,Ride Booking ,Food Delivery Tracking

#### Coarse Location
1. Permission - ACCESS_COARSE_LOCATION
2. Uses - WiFi ,Mobile Towers
3. Accuracy - Around 100–1000 meters
4. Used in - Weather Apps ,Nearby Restaurants ,Current City





### 3. showRequestDialog()

#### Purpose

Shows a **custom explanation dialog** before requesting permission again.
This dialog is **created by the app**, not by Android.






- Created using **AlertDialog.Builder()**
This is completely designed by your app.
- While Android Permission Dialog Created using
**ActivityCompat.requestPermissions(...)**
This dialog is provided by Android.



#### How AlertDialog.Builder Works

Initially
Dialog,
Title = Empty,
Message = Empty,
Buttons = None
Then every function adds one component.

```text
Builder  The Builder only stores the dialog configuration.
↓
setTitle()    Displays the title at the top of the dialog.
↓
setMessage()  Displays the explanation shown to the user.
↓
setPositiveButton()
↓
setNegativeButton()
↓
show()        Displays the dialog.
```

### Positive Button

```text
.setPositiveButton("GO TO SETTINGS") { _, _ ->
}
```

Runs when the user taps
GO TO SETTINGS


```kotlin
//Inside it
val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
```
creates an Intent to open
App Settings

#### Why Uri.fromParts()?

```kotlin
Uri.fromParts("package", packageName, null)
```

>Android asks
Which application's settings should I open? **`packageName`**
returns something like
**com.example.weatherapp**
so
The URI becomes
**package:com.example.weatherapp**
Now Android knows exactly which app's settings page to open.




- intent.data = uri
Attaches the package information to the Intent.
Without this,
Android knows
Open App Settings
but **doesn't know which app**.

- Actually launches the Settings screen.
Flow
  - Create Intent -->
Attach Package Name
-->
Launch Activity
-->
App Settings Opens



#### Why try-catch?


If, for some reason, Android can't find a screen capable handling **ACTION_APPLICATION_DETAILS_SETTINGS** it throws **ActivityNotFoundException** The app would crash without a try-catch. due to it
prints the error in Logcat and prevents the crash.
 exception be very rare on normal Android devices, still good defensive programming.



#### Negative Button

Runs when user taps close btn that triggers .setNegativeButton("CLOSE") to run that and close the dialog




#### REQUEST_LOCATION_CODE
 **Purpose** - A **request code** is a unique identifier used to identify **which permission request is returning a result**.
- An app may request multiple permissions. Location Permission
,Camera Permission
,Storage Permission
- When the user responds, Android sends the result back. The request code tells Android
   - private val REQUEST_LOCATION_CODE = 123
   - private val REQUEST_CAMERA_CODE = 456
- When requesting location permission
Android remembers
Request Code = 123
Later, when the user responds,
Android returns the same request code.
- So now App knows this result belongs to the Location Permission request.
- It can be any unique integer
The value itself has no meaning.




---


## Getting User Location in Android
### 1. FusedLocationProviderClient

#### Declaration & Initialization

```kotlin
private lateinit var mFusedLocationClient: FusedLocationProviderClient
mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
```

- **FusedLocationProviderClient** used for fetching the user's location.
Instead of manually communicating with WiFi ,Mobile Towers ,Sensors etc. 
Google Play Services combines (**Fuses**) all of them and returns the **best available location**.
- The object needs an Activity Context (`this`) be available after `onCreate()`, so`lateinit` to initialize later.
- **`LocationServices`**
    - Google's Location API.
- **`getFusedLocationProviderClient()`**
    - Returns an object capable of fetching the device's location.


### 2. onRequestPermissionsResult()

Called automatically by Android after the user responds to the permission dialog.
-  **Why check `requestCode`?** **:** An app can request multiple permissions.
Camera ,Storage etc All permission results come to this callback.
so ensures we're handling the **Location Permission** result only. So REQUEST_LOCATION_CODE is soely responsible to determine type of permission be dealing with

**requestCode == REQUEST_LOCATION_CODE**


-  **Why `grantResults.isNotEmpty()`? :** Safety check. If Android unexpectedly returns an empty array, the app won't crash.
It **does not** check whether the user pressed **Allow** or **Deny**.

- **Why grantResults[0] == PackageManager.PERMISSION_GRANTED :** `grantResults` is an **IntArray** returned by Android.
It stores the result of every permission requested. where the grantResult[0] be for location while grantResult[1] etc will store other permission result like camera etc if we mention it here


- **Why call `requestLocationData()`? :** Earlier, we only obtained permission.
Now that permission is granted, we can finally request the user's location.

  



### 3. requestLocationData()

- **Why `@SuppressLint("MissingPermission")`? :**
Android Studio warns that location APIs require permission.
Although we've already checked permission before calling this function, Android Studio cannot always detect that.
This suppresses the warning.
- **Never use it unless you're absolutely sure permission has already been granted.**

- **LocationRequest.Builder()**
```kotlin
LocationRequest.Builder(         //Defines **how** location should be requested.
    Priority.PRIORITY_HIGH_ACCURACY,   //-> responsible to provide the most accurate location.
    1000              // Location updates are requested approximately every second.
)
```

- **requestLocationUpdates() :**  Starts listening for location updates.

Start Listening ---> Phone detects new location --->Calls onLocationResult()

- **LocationCallback:**
Creates a callback object.
Whenever Android gets a new location, it automatically calls this object.

  
- **onLocationResult() :**
Runs every time a new location is received.
   - lastLocation: Returns the most recent location available.
       From this object we can access
 **locationResult.lastLocation?.latitude**  **locationResult.lastLocation?.longitude**


- **Why `?.` (Safe Call)?**
Sometimes a location may not be available immediately.
Without `?.`
lastLocation = null
And App Crashes
Else it be able to Return null and prevent `NullPointerException`.

- **Looper.myLooper() :**
Specifies the thread that should receive location callbacks.
Since this is called from the **main UI thread**, `onLocationResult()` also runs on the main thread, allowing UI updates such as
Toast, TextView, Map updates


### Complete Flow

```text
Create FusedLocationProviderClient
            ↓
Create LocationRequest
            ↓
requestLocationUpdates()
            ↓
Google Starts Listening
            ↓
Phone Receives Location
            ↓
onLocationResult()
            ↓
Get lastLocation
            ↓
Read Latitude & Longitude
            ↓
Display / Use Location
```

---

### Common Mistakes


- Calling `requestLocationData()` before permission is granted.
- Forgetting the safe call (`?.`) on `lastLocation`.
- Setting a very small update interval, which increases battery consumption.
- Using `@SuppressLint("MissingPermission")` without actually checking permissions.





---

## Internet Connectivity Check in Android

> Before calling the Weather API, the app first checks whether connected to the internet. prevents unnecessary API calls .

#### Required Permissions

```xml
<uses-permission android:name="android.permission.INTERNET"/>   <!-- Allows the app to access the internet.Without it no API req can be made -->

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>    <!-- Allows the app to check the current network status.
It can determine whether the device is connected through WiFi,No Internet,etcIt does NOT provide internet access. -->

<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>     <!-- Allows the app to retrieve WiFi-related information.Examples : WiFi Enabled?,Connected to WiFi?,WiFi State
```



#### Constant.kt file and func isNetworkAvailable()

- **Why inside `object Constants`?**

This function does not depend on any Activity.
It simply checks whether the internet is available.
So it can be called from anywhere using instead of rewriting the code multiple times.
```kotlin
Constants.isNetworkAvailable(this)
```


- **ConnectivityManager**
```kotlin
val connectivityManager =
    context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
```
This line retrieves the **ConnectivityManager**, which is responsible for checking network connectivity.


- **Why API Level Check?**
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
```

 APIs starting from **Android 6 (API 23)**.
Android < 6
Uses activeNetworkInfo
while Android ≥ 6
Uses NetworkCapabilities
This ensures compatibility with both older and newer Android versions.



- **activeNetwork**
```kotlin
val network =
    connectivityManager.activeNetwork ?: return false
```
   - Returns the currently active network.
Eg
WiFi Connected
Returns WiFi Network
   - It doesn't tell anything about that network. Kinda ID representing the current active network.

Uses the **Elvis Operator**.
Network Exists? -> YES  THEN Continue  NO Return false
This prevents unnecessary processing when no network exists.

- **getNetworkCapabilities()**

```kotlin
val activeNetwork =
    connectivityManager.getNetworkCapabilities(network)
        ?: return false
```

- Returns abt the above selected information about that network.
- Eg- Internet = Yes |
Validated = Yes |
VPN = No |
Metered = No


-  **hasTransport()**

Checks the transport type of the active network.

WiFi

```kotlin
activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) //Returns `true` if connected through WiFi.
```




- **Why `when`?**

Helps to check all the options and returns true if they valid else goes auto to next one

```text
Connected to WiFi?
├── YES → true
└── NO
    ▼
Connected to Mobile Data?
├── YES → true
└── NO
    ▼
Connected to Ethernet?
├── YES → true
└── NO
    ▼
false
```

- **Older Android (Below API 23)**

```kotlin
val networkInfo = connectivityManager.activeNetworkInfo

return networkInfo != null &&
        networkInfo.isConnectedOrConnecting
```

Older Android versions do not support `NetworkCapabilities`.
Instead, they use `activeNetworkInfo` to determine whether the device is connected (or currently connecting) to the internet




#### getLocationWeatherDetails()
If you get network from Constant.isNetworkAvailable(this) then toast Got network else not and call it with lat and long 
