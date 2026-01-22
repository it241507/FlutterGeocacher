# GeoCacher (Flutter)

Small Flutter client for the GeoCacher backend in this repository.

## Getting Started

## Quick start — run with the local backend

### Prerequisites

- Flutter SDK installed and on PATH
- Android Studio (for emulator) or a running Android emulator
- Java + Maven to run the Spring Boot backend in `/backend`

### Start backend

```bash
cd /Users/bumpfi/Code/github.com/it241507/FlutterGeocacher/backend
./mvnw spring-boot:run
```

### Run the Flutter app on Android emulator

```bash
cd /Users/bumpfi/Code/github.com/it241507/FlutterGeocacher/flutter/geo_cacher
flutter pub get
flutter devices    # confirm emulator is listed
flutter run -d <emulator-id>
```

## Files of interest

- `lib/constants.dart` — base URL used by the app
- `lib/services/api_repository.dart` — Dio configuration, interceptors, login/register/create cache
- `lib/models/cache.dart` — cache JSON parsing and request serialization
- `lib/pages/map.dart` — map, markers and fetch logic

