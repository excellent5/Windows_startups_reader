# AutoRun
Read the Windows start ups like AutoRun.exe

## Function
Read the Windows logon, IE BHO, Services, Drivers, Scheduled Tasks, Winsock

List their file name, file path, file description
## Environment
Only for Windows x64 with JRE installed (successfully run on the Win7 and Win8.1)

## Open Source Library Used
- [com.ice.jni.registry](http://ganoro.blogspot.com/2011/05/windows-registry-api-for-windows-plus.html)
- [org.boris.pecoff4j](https://github.com/kichik/pecoff4j)

## Privilege requirement
Need to administrator privilege to show Scheduled Tasks
