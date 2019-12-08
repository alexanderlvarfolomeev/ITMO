md LAB6
cd LAB6

set comnd='wmic os get version /format:list'
call :writestr
echo OS Version: %res% > os_get_version.log

set comnd='wmic os get totalvisiblememorysize /format:list'
call :writestr
set total=%res%

set comnd='wmic os get freephysicalmemory /format:list'
call :writestr
set /a used=%total%-%res%
echo Free memory: %res% byte > os_get_totalvisiblememorysize.log
echo Used memory: %used% byte > os_get_freephysicalmemory.log

wmic diskdrive list brief > diskdrive.log

md TEST
xcopy . TEST
type "*" > data.txt

set index=1
setlocal enabledelayedexpansion
for /f "delims=*" %%a in ('dir /b /o:-D /t:c /a:-d-h') do (
	if !index! gtr 1 (
		echo deleting %%a
		del %%a
	) else (
		set /a index=!index!+1
	)
)
endlocal

pause

exit /b

:writestr
(
	for /f "tokens=2 delims==" %%a in (%comnd%) do (
		if not [%%a]==[] (
			set res=%%a
		)
	)
	goto :eof
)
