@echo off
SET count=1
FOR /f "tokens=*" %%G IN ('dir "%CD%\src\*.*" /b /s') DO (type "%%G") >> lines.txt
SET count=1
FOR /f "tokens=*" %%G IN ('type lines.txt') DO (set /a lines+=1)
echo Java Files Has Total %lines% Lines. 
del lines.txt

PAUSE