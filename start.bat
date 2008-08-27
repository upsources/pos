@echo off

REM    Openbravo POS is a point of sales application designed for touch screens.
REM    Copyright (C) 2008 Openbravo, S.L.
REM    http://sourceforge.net/projects/openbravopos
REM
REM    This program is free software; you can redistribute it and/or modify
REM    it under the terms of the GNU General Public License as published by
REM    the Free Software Foundation; either version 2 of the License, or
REM    (at your option) any later version.
REM
REM    This program is distributed in the hope that it will be useful,
REM    but WITHOUT ANY WARRANTY; without even the implied warranty of
REM    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
REM    GNU General Public License for more details.
REM
REM    You should have received a copy of the GNU General Public License
REM    along with this program; if not, write to the Free Software
REM    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  

set DIRNAME=%~dp0

set CP="%DIRNAME%openbravopos.jar"

set CP=%CP%;"%DIRNAME%lib/jasperreports-3.0.0.jar"
set CP=%CP%;"%DIRNAME%lib/jcommon-1.0.0.jar"
set CP=%CP%;"%DIRNAME%lib/jfreechart-1.0.0.jar"
set CP=%CP%;"%DIRNAME%lib/jdt-compiler-3.1.1.jar"
set CP=%CP%;"%DIRNAME%lib/commons-beanutils-1.7.jar"
set CP=%CP%;"%DIRNAME%lib/commons-digester-1.7.jar"
set CP=%CP%;"%DIRNAME%lib/itext-1.3.1.jar"
set CP=%CP%;"%DIRNAME%lib/poi-3.0.1-FINAL-20070705.jar"
set CP=%CP%;"%DIRNAME%lib/barcode4j-light.jar"
set CP=%CP%;"%DIRNAME%lib/commons-codec-1.3.jar"
set CP=%CP%;"%DIRNAME%lib/velocity-1.5.jar"
set CP=%CP%;"%DIRNAME%lib/oro-2.0.8.jar"
set CP=%CP%;"%DIRNAME%lib/commons-collections-3.1.jar"
set CP=%CP%;"%DIRNAME%lib/commons-lang-2.1.jar"
set CP=%CP%;"%DIRNAME%lib/bsh-core-2.0b4.jar"
set CP=%CP%;"%DIRNAME%lib/RXTXcomm.jar"
set CP=%CP%;"%DIRNAME%lib/jpos1121.jar"
set CP=%CP%;"%DIRNAME%lib/swingx-0.9.3.jar"
set CP=%CP%;"%DIRNAME%lib/substance.jar"
set CP=%CP%;"%DIRNAME%lib/substance-swingx.jar"

rem Apache Axis SOAP libraries.
set CP=%CP%;"%DIRNAME%lib/axis.jar"
set CP=%CP%;"%DIRNAME%lib/jaxrpc.jar"
set CP=%CP%;"%DIRNAME%lib/saaj.jar"
set CP=%CP%;"%DIRNAME%lib/wsdl4j-1.5.1.jar"
set CP=%CP%;"%DIRNAME%lib/commons-discovery-0.2.jar"
set CP=%CP%;"%DIRNAME%lib/commons-logging-1.0.4.jar"

set CP=%CP%;"%DIRNAME%locales/"
set CP=%CP%;"%DIRNAME%reports/"

start /B javaw -cp %CP% -Djava.library.path="%DIRNAME%lib/Windows/i368-mingw32" -Ddirname.path="%DIRNAME%./" com.openbravo.pos.forms.StartPOS %1
