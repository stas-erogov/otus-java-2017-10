REM -XX:+UseSerialGC -XX:+UseParallelGC -XX:+UseParNewGC -XX:+UseParallelOldGC -XX:+UseConcMarkSweepGC -XX:+UseG1GC
SET DEFAULT=-Xms512m -Xmx512m -Dcom.sun.management.jmxremote.port=15000 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false
SET JARFILE=.\target\L41_GCobserver-jar-with-dependencies.jar 
SET GC=-XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark
SET GC_LOG=-Xloggc:.\logs\vm_gc_log_%%p.txt -XX:+PrintGCDateStamps -XX:+PrintGCDetails

java %DEFAULT% %GC_LOG% -XX:+UseSerialGC -jar %JARFILE%
java %DEFAULT% %GC_LOG% -XX:+UseParallelGC -jar %JARFILE%
java %DEFAULT% %GC_LOG% -XX:+UseParallelOldGC -jar %JARFILE%
rem java %DEFAULT% %GC_LOG% -XX:+UseParNewGC -XX:+UseConcMarkSweepGC %GC% -jar %JARFILE%
