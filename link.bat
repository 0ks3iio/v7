echo "current dir is %cd%"
rmdir /s %cd%\framework\web\WEB-INF\lib
echo "delete %cd%\framework\web\Web-INF\lib"
mklink /j %cd%\framework\web\WEB-INF\lib %cd%\common-lib
echo "symbolic link create success"