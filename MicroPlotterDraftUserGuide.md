# MicroPlotter User Guide

## 1. Introduction

Welcome to MicroPlotter! This is a robust desktop application designed for real-time plotting of data received from serial devices like microcontrollers (Arduino, ESP32, etc.), test equipment, or any other source that communicates over a serial port.

The application has been built with a modern, responsive user interface and a flexible backend to support both simple numeric plotting and advanced data-forwarding over the network.

## 2. Key Features

* **Real-Time Plotting:** Visualize up to 10 simultaneous data series using the powerful JFreeChart library.
* **Modern & Responsive UI:** The application features a clean, modern theme that adapts to your operating system and a responsive layout that fits any screen size.
* **Advanced Plot Configuration:** Customize plot appearance with variable line widths, static or dynamic (scrolling) views, configurable sample limits, and logarithmic or decimal axes.
* **Tagged Data Support:** The plotter can parse incoming data with text tags (e.g., `TEMP:25.5`) and use these tags as labels in the plot legend for clear data identification.
* **Data Logging:** Record all incoming serial data, with timestamps, directly to a text file.
* **Networking Capabilities:** Forward captured data in real-time to external services.
    * **HTTP Endpoint:** Send data in batches as a JSON payload to any webhook or web service.
    * **MQTT Client:** A general-purpose MQTT client to publish data to any standard broker, with support for SSL/TLS and authentication.
* **Session Persistence:** The application automatically saves your last-used configuration (plot and network settings) and reloads it on the next startup.
* **Configuration Management:** Manually save and load different configuration profiles for different projects or devices via the File menu.

---

## 3. User Interface Guide

The main window is divided into four primary sections.

### 3.1. Top Menu Bar

* **File Menu**
    * **Save Configuration...:** Opens a dialog to save all current plot and networking settings to a `.properties` file at a location of your choice.
    * **Load Configuration...:** Opens a dialog to load a previously saved `.properties` file, instantly applying all its settings.
* **Networking Menu**
    * **HTTP Endpoint...:** Opens the configuration dialog for sending data to a web service.
    * **MQTT Connection...:** Opens the configuration dialog for the general-purpose MQTT client.
* **Window Menu**
    * **About:** Displays a dialog with information about the application.

### 3.2. Port Configuration Panel

This panel controls the connection to your physical serial device.

* **Search port:** Scans your computer for available serial ports and populates the dropdown list.
* **Port List Dropdown:** Select the desired serial port from the list.
* **Baud rate:** Select the communication speed that matches your device's configuration.
* **Connect / Disconnect:** This button opens or closes the connection to the selected serial port.

### 3.3. Plot Configuration Panel

This panel allows you to customize the appearance and behavior of the plot. These controls are disabled while plotting is active.

* **Line width:** Sets the thickness of the lines on the plot.
* **Presentation:**
    * **Static:** All captured data points are displayed on the plot, with the axes adjusting to fit everything.
    * **Dynamic:** The plot shows a "sliding window" of only the most recent data points.
* **Sample limit:** (Only in Dynamic mode) Sets the number of recent data points to display in the sliding window.
* **Update time:** Sets the refresh rate of the plot visualization.
* **Tags as names:** If checked, the application will parse incoming data for `TAG:VALUE` pairs and use the tags as labels in the plot legend. If unchecked, it will treat all tokens as simple numeric values labeled D0, D1, etc.
* **X/Y Axis type:** Toggles the respective axis between a standard **Dec** (Decimal/Linear) scale and a **Log** (Logarithmic) scale.
* **Start/Stop plotting:** This button starts or stops the process of reading data from the serial port and sending it to the plot and networking modules.
* **Pause plotting:** Temporarily freezes the plot visualization without stopping data capture.
* **Clear Plot:** Instantly erases all data from the plot area and resets the data model.

### 3.4. Plot Panel

This is the main area where the chart is displayed. You can interact with it directly:
* **Right-Click (or Double-Click):** Opens a context menu with powerful options provided by the JFreeChart library, including **Save as...** (to export the chart as a PNG), **Zoom In/Out**, and **Properties...** to further customize the chart's appearance.

### 3.5. Terminal Panel

This panel shows the raw data coming from the serial port and allows you to send data back.

* **Serial Message field:** Type any text you want to send to your connected device here.
* **Send:** Sends the text from the message field.
* **Add CR / Add NL:** Appends a Carriage Return (`\r`) or New Line (`\n`) character to your sent messages.
* **TimeStamp:** If checked, prepends a date and time to each line of data displayed in the terminal and saved in the log file.
* **AutoScroll:** If checked, the terminal will automatically scroll to the bottom as new data arrives.
* **Begin rec / Stop rec:** Starts or stops recording all timestamped terminal output to a text file.
* **Clear:** Instantly erases all text from the terminal display area.

---

## 4. Serial Data Format

MicroPlotter's parser is flexible. It splits incoming lines of data by spaces or tabs.

#### Simple Numeric Format
When **"Tags as names" is unchecked**, the parser expects simple numbers separated by delimiters.
*Example:* `1024 -512 3.14` will plot three data points on series D0, D1, and D2.

#### Tagged Data Format
When **"Tags as names" is checked**, the parser looks for `TAG:VALUE` pairs.
*Example:* `TEMP:25.5 HUMID:60.2` will plot `25.5` on a series named "TEMP" and `60.2` on a series named "HUMID".
* The parser can handle spaces around the colon (e.g., `TEMP: 25.5` works too).
* Any token without a colon is ignored in this mode.

---

## 5. Networking Features Guide

### 5.1. HTTP Endpoint

This feature sends buffered data as a JSON payload in an HTTP `POST` request to a URL you specify.

* **Configuration:** Use the **Networking -> HTTP Endpoint...** dialog to enable the feature, set your webhook URL, and define the buffer size (how many lines of data to collect before sending).
* **Payload Format:** The data is sent as a JSON array of objects. Each object represents one line of data, with the tags as keys.
    *Example Payload for data `SINE:100 COS:200` followed by `SINE:105 COS:195`*:
    ```json
    [
      {
        "SINE": 100.0,
        "COSINE": 200.0
      },
      {
        "SINE": 105.0,
        "COSINE": 195.0
      }
    ]
    ```

### 5.2. MQTT Client

This is a general-purpose MQTT client that can connect to any standard broker.

* **Configuration:** Use the **Networking -> MQTT Connection...** dialog.
* **How to Test:** You can test this feature with a public broker.
    1.  Open the MQTT dialog.
    2.  Check **Enable MQTT Publishing**.
    3.  Leave **Use SSL/TLS** unchecked.
    4.  **Broker Address:** `broker.hivemq.com`
    5.  **Port:** `1883`
    6.  Leave **Username** and **Password** blank.
    7.  **Base Topic:** `microplotter/test/mydata`
    8.  Click **Save & Apply**.
    9.  Use a public web client like the [HiveMQ Web Client](http://www.hivemq.com/demos/websocket-client/) to subscribe to the topic `microplotter/test/mydata/#` to see your messages arrive in real-time.