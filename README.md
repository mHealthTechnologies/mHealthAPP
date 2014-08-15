mHealthAPP
==========

This exemplary APP is intended to show the usefulness and potential of mHealthDroid, an implementation of an mHealth framework. We wish to point out that this app was developed in a very short time and almost efortless thanks to the abstraction level that brings the mHealthDroid API. As a matter of fact, most of the development time was invested on the graphical interface. The app can be found on Google Play by the name of mHealthAPP APP or <a href="https://play.google.com/store/apps/details?id=com.mHealthDroid.mhealthpp">here</a>.


### Main Features

- Management of human health data (physiologic and kinematic) recorded through portable biomedical devices (concretely Shimmer Sensors are used here) or the sensors embedded into the mobile device (inertial sensors). 
- Online visualization of the data collected through the portable biomedical devices and the mobile device.
- Local and remote storage of the collected data. 
- Online human activity tracking and detection through the use of an expert system built as part of the app.
- Guidelines to help the user to keep healthy habits, supported through online video broadcasting and schedulable notifications.

### Demostration Video

In the following video the mHealthDroid APP and its main functionalities are presented: 

[<img src="http://apuestasrafag.files.wordpress.com/2014/03/thumbnailvideo.png?w=673" height=300>](https://www.youtube.com/watch?v=AMdxw4osjCU)

### APP Usage

It is possible to navigate around the app making use of the available tabs on top of the screen. Each tab provides access to a specific functionality, namely: Connectivity, Visualization, Activity Recognition, Remote Storage, Notifications Manager and YouTube Guidelines.

#### Connectivity

This tab provides the user with all the connectivity features and devices configuration. The tab has a Button to add  new devices, and a ListView to visualize them.

In order to add a new device, the "Plus" Button must be pressed. Then, a message will be thrown asking for the device name. This pop-up contains two buttons to select the kind of device. It can be either *Mobile* (i.e., the smartphone or tablet) or *Shimmer* (i.e., the portable biomedical device used here).

<img src="http://apuestasrafag.files.wordpress.com/2014/03/connectivity1.png?w=253" height=300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/addingdevice1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/scanning.png?w=253" height = 300>

Once the device is added, it will be displayed on the ListView. Three tags are shown for each device: The first one is the device's name, the second one is the device's type, and the third one corresponds to its actual state. The state is represented by a coloured circle. It will be red when the device is disconnected, orange when the device is streaming, or green when the device is connected but not streaming any data.

<img src="http://apuestasrafag.files.wordpress.com/2014/03/errorinsertion11.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/errorinsertion21.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/devicesstatus1.png?w=253" height = 300>

After selecting a particular device from the list, a menu with several options is displayed. The options vary depending on the device's type and its status. The possible options are listed below:

- *Connect/Disconnect*. It allows to connect or disconnect the device. Since the mobile device does not need to be connected or disconnected, this function is only available for Shimmer devices.
- *Start/Stop streaming*. It permits to init or finalize the streaming process.
- *Sensors*. This option opens a window to set the enabled sensors. The available sensors can be different for each kind of device. This option is only showed when the device is not streaming.
- *Configuration*. This option opens a window to set the device configuration. Here, it is also set whether the data must be stored into the database. The device configuration can be different for each kind of device. It is only showed when the device is not streaming.
- *Remove*. This option removes a device from the devices list.

<img src="http://apuestasrafag.files.wordpress.com/2014/03/menumobile1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/menumobilestreaming1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/menushimmerconnected1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/menushimmerstreaming.png?w=253" height = 300>

<img src="http://apuestasrafag.files.wordpress.com/2014/03/sensorsmobile.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/shimmersensors.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/mobileconfiguration.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/shimmerconfiguration.png?w=253" height = 300>


#### Visualization

This tab permits to visualize the signals recorded through the enabled sensors. The tab is composed by a graph and two buttons: the *Configuration* button and the *Start* button.

When the *Configuration* button is pressed, a menu is shown in order to set the graph configurations. The options are initially disabled (default configuration). The configuration menu is not available during the visualization, so the graph features must be set before. The configuration options are:

- *View Port*. This option sets the graph view port. That is, the number of samples to be shown in the graph. 
- *Y coordinates*. This option sets the maximum and minimum values for the Y axis. 
- *Legend*. This option permits to show a legend of the graph series. It can be aligned in three different positions. On top of the graph, in the middle, or at the bottom.

<img src="http://apuestasrafag.files.wordpress.com/2014/03/visualization.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/visualizationinit.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/visualizationconfiguration1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/visualizationconfiguration2.png?w=253" height = 300>

Once the *Start* button is pressed, a list of devices currently streaming is showed. From this list the user can select sensor data streams to be visualized. In case there are no devices streaming, an error message will be showed. When a device is selected, it is displayed a list with the available sensors. The visualization starts after selecting the sensors and pressing the OK button.

<img src="http://apuestasrafag.files.wordpress.com/2014/03/devicesvisualization1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/deviceandsensors1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/deviceandsensors21.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/errorvisualization1.png?w=253" height = 300>

#### Activity Recognition

This tab uses inference of knowledge capabilities to perform human activity recognition tasks.  

The activities particularly considered are the following: standing still, sitting and relaxing, lying down, walking, climbing stairs, waist bends forward, frontal (vertical) up/down, knees bended, cycling, jogging, running, jump forward and back.

In order to perform the activity recognition some wearable devices must be placed on the user body as shown below:

<img src="http://apuestasrafag.files.wordpress.com/2014/03/subjectsensors.png?w=601" height = 375>

These sensors are capable of capturing the dynamics of the user's body. In order to train the recognizer (see "Full documentation" for more details) we performed a research study to define a suitable and accurate sensor deployment. Some of the scientific background used for the experiments is available <a href="http://www.ugr.es/~oresti/publications.htm">here</a>. 

To start the activity recognition process it is necessary to set each connected device to its position in the body (chest, right wrist or left ankle). For this purpose there are three spinners on the screen, one for each position. Once this is done, the button start is used to begin the activity recognition process. On the top of the screen there will appear an image and text representing the activity performed by the user. In case there are no streaming devices an error message will be showed.

<img src="http://apuestasrafag.files.wordpress.com/2014/03/activityspinner1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/activitystart1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/erroractivity1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/activityrecognition1.png?w=253" height = 300>

<img src="http://apuestasrafag.files.wordpress.com/2014/03/running-activity.png?w=346" height = 280>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/jogging-activity.png?w=337" height = 280>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/cycling-activity.png?w=346" height = 280>

<img src="http://apuestasrafag.files.wordpress.com/2014/03/walking-activity.png?w=337" height = 280>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/up-down-activity.png?w=338" height = 280>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/waist-bends-activity.png?w=337" height = 280>


#### Notifications

This tab permits to create different kind of notifications. When the tab is selected, it appears a form that it must be filled in order to create the notification. This field is composed:

- *Title*. Title of the notification.
- *Description*. Full description text of the notification.
- *Sound*. A field where three different sounds may be selected to be reproduced when the notification is launched: Alarm, Ringtone and Notification sounds. There is also the possibility of set this sound to "None".    
- *Launch Recommendations*. It is a checkbox that in the case of being checked, when the notification is clicked the YouTube guidelines is launched.
- *Schedule Notification*. It allows for the scheduling the notification to a specified date and time.

As an example, it can be used an app user with back problems that needs to be reminded about his daily exercises. Thus, for this purpose, the user can create a notification and customize it by putting the notification title and description, as well as setting the hour of its appearance. Moreover, if the user wants to visualize some recommended exercises he can mark the *Launch Recommendations* option that after clicking on the notification will automatically open the YouTube Guidelines tab.

<img src="http://apuestasrafag.files.wordpress.com/2014/03/notifications1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/notifications2.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/notifications3.png?w=253" height = 300>

<img src="http://apuestasrafag.files.wordpress.com/2014/03/notifications4.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/notifications5.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/notifications6.png?w=253" height = 300>

#### YouTube Guidelines

This tab allows the app user the visualization of videos in order to keep healthy habits or learn new exercises with physical therapy purposes. There are five different playlists that can be selected by the user using the button *Playlists*:

- General Health: playlist about how to keep healthy daily habits and how to maintain a healthy diet.
- Knee: playlist with videos of knee rehabilitation, orientate to user with knee problems.
- Back: playlist with videos of back rehabilitation, orientate to user with back problems.
- Ankle: playlist with videos of ankle rehabilitation, orientate to user with ankle problems.
- Neck: playlist with videos of neck rehabilitation, orientate to user with neck problems.	
    
<img src="http://apuestasrafag.files.wordpress.com/2014/03/youtube1.png?w=253" height = 350>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/youtube2.png?w=253" height = 350>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/youtube3.png?w=253" height = 350>

Once that a playlist has been selected, a scrollable list of videos will appear on the screen. This list can be extended pressing on the *Extend List* button and collapse by pressing on it again. When the user selects a video to be reproduced, this appears on the player and can be reproduced when the start icon is clicked. The player may also be extended using the button *Extend Player* or collapse clicking on it again.

<img src="http://apuestasrafag.files.wordpress.com/2014/03/youtube4.png?w=253" height = 350>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/youtube5.png?w=253" height = 350>

#### Remote Storage

This tab allows the user app to upload to a remote storage the collected data from the portable biomedical devices. It is composed of a list with every of the available devices in the app and a button to set the WiFi connection (for the sake of simplicity, since 3G connection can be used as well).

When a device of the list is pressed on, it appears a dialog asking the app user to define which tables must be uploaded. There are three options: Units, Metadata and Signals. After pressing the OK button the uploading of the selected data starts. A message will appear in the screen to notify the end of the uploading process.

<img src="http://apuestasrafag.files.wordpress.com/2014/03/remote1.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/remote2.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/remote3.png?w=253" height = 300>
<img src="http://apuestasrafag.files.wordpress.com/2014/03/remote4.png?w=253" height = 300>

### Copyright and license

Project released under the license GPL V.3.
