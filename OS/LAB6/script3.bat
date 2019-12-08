@echo off
cd C:\scripts
wmic service get name,displayname,state /value > services.temp
set index=0
set step=0

type services.temp > services2.temp

setlocal enabledelayedexpansion

for /f "tokens=1,2 delims==" %%a in (services2.temp) do (
	set "service[!index!].%%a=%%b"
	set /a "step=!step! + 1"
	if !step! equ 3 (
		set step=0
		set /a "index=!index! + 1"
	)
)

set /a "last=!index! - 1"
echo. > service1.log

set serviceName=
set serviceDisplayName=
set serviceState=

for /l %%g in (0,1,%last%) do (
	for /f "tokens=2,3 usebackq delims==." %%a in (`set service[%%g]`) do (
		set service%%a=%%b
	) 
	if [!serviceState!]==[Running] (
		echo !serviceName!>> service1.log
	)
	if [!serviceDisplayName!]==[DNS-клиент] (
		sc stop !serviceName!
	)
)

timeout /nobreak /t 1

wmic service get name,displayname,state /value > services.temp
set index=0
set step=0

type services.temp > services2.temp

for /f "tokens=1,2 delims==" %%a in (services2.temp) do (
	set "service[!index!].%%a=%%b"
	set /a "step=!step! + 1"
	if !step! equ 3 (
		set step=0
		set /a "index=!index! + 1"
	)
)

set /a "last=!index! - 1"
echo. > service2.log

for /l %%g in (0,1,%last%) do (
	for /f "tokens=2,3 usebackq delims==." %%a in (`set service[%%g]`) do (
		set service%%a=%%b
	) 
	if [!serviceState!]==[Running] (
		echo !serviceName!>> service2.log
	)
)

call subscript3.bat
for /f "tokens=*" %%a in (servicediff.log) do (
	sc start %%a
)

endlocal

pause
exit /b

:writestr
(
	for /f "tokens=1,2 delims==" %%a in (%comnd%) do (
		if [%%b]==[] (
			if (%index% neq 0)
		)
		if not [%%a]==[] (
			set res=%%a
		)
	)
	goto :eof
)