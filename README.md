# Android-Face-Detect-Library

Document Writing in process

![Android-Face-Detect-Library](https://media.giphy.com/media/cI5PPGv1990XfAt4RG/giphy.gif)


- **Points of Interest**

```
- Important classes like Camera Handler and Persistence Service
  are implemented using a abstraction so that its implementation can 
  be interchanged in any way without a single code change

- All the classes are Decoupled from one another. 

- Designed in a way to scale the library more in future

- Clean Architecture where View is seperated from Domain Logic

- There are lot more design decisions that has been taken in the library which I would welcome to discuss
```



- **Library Dependencies Explained**

```
Though it is necessary that to keep the library lean and it is best to
avoid libaries, I have used the best and latest libraries to portray my skills

    Constraint Layout 
    
    RxJava - To process the stream of information from camera (We can customize the amount of 
    information we process in the stream by controlling the timeoutInMillis parameter in Camera Handler)
    
    Android Life Cycle - Used to handle life cycle of Camera Handler, Persistence and 
    other Presenters and making it decoupled (Without ViewModel and LiveData)
    
```



- **Decision to use Fotoapparat Library**

```
Since The current library supports until API 14 and Camera API in Android is too low level to work within 48 hours
I decided to adopt a camera library

Fotoapparat vs CameraKit vs Others
-Much smaller in size compared to CameraKit. CameraKit will make the user do ABI
-CameraView in Google Repository is still in preview
-Mobile Vision from Google Play Service is not used since it is payable and wont be 
scalable for organizations
```



- **Customizing the View of the library**

```
faceCaptureBackground - Background Color

faceCaptureLightBackground - Light Background Color

faceCaptureStatusBarColor - Status Bar Color

faceCaptureActionBar - Action Bar Color

faceCaptureButtonColor - Button Color

faceCaptureButtonTextColor - Button Text Color

faceCaptureTextColor - Normal Text Color
```

This Library handles the "Dont Ask Again" in Permission properly where most library misses out. A seperate Dialog box is shown which will redirect the user to settings and can enable the permission there

<img src="https://image.ibb.co/iJn61z/Whats_App_Image_2018_09_30_at_8_44_17_PM.jpg" width="256">

Overlay could have been achieved if Mobile Vision from Google Play Service was used. But it costs $0.60 for every 1000 units.


- **TODO**
```
- Upload the library to bintray
- Ability to use custom fragmentss (Thats why library is designed in that manner)
- Though In Memory Persistence currently used is stable, Should analyze more deeply"
  whether to proceed with same method or can adopt different method like Local storage
```
