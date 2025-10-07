# Mbed Thermistor Android App 🌤️

This Android application connects via **Bluetooth** to an embedded Mbed-based thermistor sensor, receives live temperature readings, and displays them alongside weather and forecast data from the **OpenWeatherMap API**.

It represents the **mobile companion** to the [Mbed Bluetooth Temperature Sensor](https://github.com/Derek-Sykes/mbed-thermistor-sensor) project, providing a clean user interface for visualizing real-time sensor data and current weather conditions.

---

## 📱 Features
- Connects to an **HC-05/HC-06** Bluetooth module (Serial SPP)
- Displays **live temperature** readings received from the embedded sensor
- Fetches and displays **current weather** and **forecast** for any city using the OpenWeatherMap API
- User-friendly interface for manual city lookup and Bluetooth connection
- Built entirely in **Java** with **Android Studio**

---

## ⚙️ Requirements
| Item | Description |
|------|--------------|
| **Android Studio** | Version Electric Eel or newer |
| **Minimum SDK** | Android 7.0 (API 24) |
| **Dependencies** | OkHttp3, org.json |
| **Bluetooth** | Classic Bluetooth (HC-05 / HC-06 modules) |
| **Internet access** | Required for OpenWeather API calls |

---

## 🔧 Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/Derek-Sykes/mbed-thermistor-android.git
   ```
2. Open the project in Android Studio.
3. Insert your [OpenWeatherMap API key](https://openweathermap.org/api) in:
   ```java
   weatherClient = new WeatherClient("YOUR_API_KEY");
   ```
4. Pair your phone with the HC-05/HC-06 Bluetooth module.
5. Launch the app, enter the module’s MAC address, and tap **Connect**.
6. Observe live temperature readings and city weather data.

---

## 🧩 Permissions
Make sure the app requests and has the following permissions:
```xml
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT"/>
<uses-permission android:name="android.permission.BLUETOOTH_SCAN"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

> ⚠️ On Android 12+, `BLUETOOTH_CONNECT` and `BLUETOOTH_SCAN` must be requested at runtime.

---

## 🧪 Example Output
```
Bluetooth: Connected
Temperature: 24.5 °C
City: Tarragona | Weather: Clear 25.1°C
```

---

## 📂 Repository Structure
```
app/
├── java/edu/urv/weather/
│   ├── MainActivity.java
│   ├── BluetoothHandler.java
│   ├── WeatherApiClient.java
│   └── ...
├── res/
│   ├── layout/
│   ├── values/
│   └── mipmap/
└── AndroidManifest.xml
```

---

## 🧾 License
Released under the [MIT License](LICENSE).

---

**Developed by:** Derek Sykes  
**GitHub:** [https://github.com/Derek-Sykes](https://github.com/Derek-Sykes)

> *"Bridging embedded hardware and mobile interfaces for seamless IoT experiences."*
