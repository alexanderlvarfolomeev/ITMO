type service1.log service2.log | sort > service.temp

set "prevstr="
set first=true
set last_comp=false
setlocal enabledelayedexpansion

for /f "tokens=*" %%a in (service.temp) do (
	if [!first!]==[true] (
		set prevstr=%%a
		set first=false
	) else (
		if [!last_comp!]==[true] (
			set prevstr=%%a
			set last_comp=false
		) else (
			if [!prevstr!]==[%%a] (
				set last_comp=true
			) else (
				echo !prevstr!> servicediff.log
				set prevstr=%%a
			)
		)
		
	)		
	)
)
if [!last_comp!]==[false] (
	echo %prevstr%> servicediff.log
)

endlocal