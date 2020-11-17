@echo off
type nul > temp.txt
type nul > result.txt,
copy rjecnik.dic+croatianToMerge.dic temp.txt
for /f "delims=" %%I in (temp.txt) do findstr /X /C:"%%I" result.txt >NUL ||(echo;%%I)>>result.txt
del temp.txt