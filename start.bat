@echo off
@title ZlhssMS_079
set CLASSPATH=.;dist\
java -server -Dnet.sf.odinms.wzpath=wz server.Start
pause
