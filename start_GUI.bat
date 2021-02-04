@echo off
@title ZlhssMS_079
set CLASSPATH=.;dist\*
java -server -Xmx1024M -Dnet.sf.odinms.wzpath=wz gui.ZlhssMS
pause
