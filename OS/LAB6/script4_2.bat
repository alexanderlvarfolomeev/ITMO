for /f "delims==" %%g in ('driverquery /v /fo list ^| findstr /c:"Название:"') do (
	for /f "tokens=1,*" %%a in ("%%g") do echo %%b >> DRIVERS
)

sort /r DRIVERS /o DRIVERS_SORTED