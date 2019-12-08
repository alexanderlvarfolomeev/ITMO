cd /d C:\cd
for /f %%a in ('hostname') do set hstname=%%a
setlocal enabledelayedexpansion
for /f %%a in ('dir /b /o:d /t:c /a:-d-h') do (
	set size=%%~za
	if !size! geq 2097152 (
		xcopy /z /y %%a \\!hstname!\temp
	)
)