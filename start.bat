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

set CP=openbravopos.jar

set CP=%CP%;lib/l2fprod-common-tasks.jar
set CP=%CP%;lib/jasperreports-2.0.1.jar
set CP=%CP%;lib/jcommon-1.0.0.jar
set CP=%CP%;lib/jfreechart-1.0.0.jar
set CP=%CP%;lib/jdt-compiler-3.1.1.jar
set CP=%CP%;lib/commons-beanutils-1.7.jar
set CP=%CP%;lib/commons-digester-1.7.jar
set CP=%CP%;lib/itext-1.3.1.jar
set CP=%CP%;lib/poi-3.0.1-FINAL-20070705.jar
set CP=%CP%;lib/barcode4j-light.jar
set CP=%CP%;lib/commons-codec-1.3.jar
set CP=%CP%;lib/velocity-1.5.jar
set CP=%CP%;lib/oro-2.0.8.jar
set CP=%CP%;lib/commons-collections-3.1.jar
set CP=%CP%;lib/commons-lang-2.1.jar
set CP=%CP%;lib/bsh-core-2.0b4.jar
set CP=%CP%;lib/RXTXcomm.jar
set CP=%CP%;lib/jpos111.jar
set CP=%CP%;lib/substance.jar

rem Apache Axis SOAP libraries.
set CP=%CP%;lib/axis.jar
set CP=%CP%;lib/jaxrpc.jar
set CP=%CP%;lib/saaj.jar
set CP=%CP%;lib/wsdl4j-1.5.1.jar
set CP=%CP%;lib/commons-discovery-0.2.jar
set CP=%CP%;lib/commons-logging-1.0.4.jar

set CP=%CP%;locales/

start /B javaw -cp %CP% -Djava.library.path=lib/Windows/i368-mingw32 com.openbravo.pos.forms.StartPOS
