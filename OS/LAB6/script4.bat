driverquery /fo:table /nh > DRIVERS
sort /r DRIVERS /o DRIVERS_SORTED

rem for delims^=^" %%g in (DRIVERS) do set line=%%g
rem for /f "tokens=1,*" %%g in (%line%) do set tail=%%g