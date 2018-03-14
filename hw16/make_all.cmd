cd MessageSocketServer
call mvn clean install
cd ..
del /Q Frontend1 Frontend2 dbService1 dbService2
cd FrontendService
call mvn clean package
cd ..\DBService
call mvn clean package
cd ..