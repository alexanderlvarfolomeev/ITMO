set hour=%time:~0,2%
set min=%time:~3,2%
set /a min=%min%+1
if "%min:~0,1%"==" " set min=0%min:~1,1%
if [%min:~1,1%]==[] set min=0%min:~0,1%
if "%hour:~0,1%"==" " set hour=0%hour:~1,1%
if [%hour:~1,1%]==[] set hour=0%hour:~0,1%
if %min% equ 60 (
	set min=00
	set hour=%hour%+1
	if [%hour:~1%]==[] set hour=0%hour%
	if [%hour%]==[24] set hour=00
)
echo 12345| schtasks /create /tn:scriptrunner /v1 /z /sc once /tr "%cd%\subscript2.bat" /sd %date% /st %hour%:%min%

for /l %%g in (0,1,60) do (
for /f "tokens=1,2" %%a in ('wmic process get name^,processId ^| find /i "xcopy.exe"') do (
		if not [%%b]==[] (
			taskkill /pid %%b /f 
			echo ok
			goto break
		)
	)
	timeout /nobreak /t 1
)

:break

setlocal enabledelayedexpansion

set copying=false
set src=C:\cd\
for /f %%a in ('hostname') do set hstname=%%a
for /f %%a in ('dir C:\cd /b /o:d /t:c /a:-d-h') do (
	set "f=%src%%%a"
	if !size! geq 2097152 (
		if [!copying!]==[false] (
			echo n| comp !f! \\User\temp\%%a
			if [!errorlevel!]==[1] (
				set copying=true
			)
		)
		if [!copying!]==[true] (
			xcopy /z /y !f! \\!hstname!\temp
		)
	)
)

endlocal

pause

exit /b

:sizef
set size=%~z1
goto :eof