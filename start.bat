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

set CP="%CD%/openbravopos.jar"

set CP=%CP%;"%CD%/lib/jasperreports-2.0.1.jar"
set CP=%CP%;"%CD%/lib/jcommon-1.0.0.jar"
set CP=%CP%;"%CD%/lib/jfreechart-1.0.0.jar"
set CP=%CP%;"%CD%/lib/jdt-compiler-3.1.1.jar"
set CP=%CP%;"%CD%/lib/commons-beanutils-1.7.jar"
set CP=%CP%;"%CD%/lib/commons-digester-1.7.jar"
set CP=%CP%;"%CD%/lib/itext-1.3.1.jar"
set CP=%CP%;"%CD%/lib/poi-3.0.1-FINAL-20070705.jar"
set CP=%CP%;"%CD%/lib/barcode4j-light.jar"
set CP=%CP%;"%CD%/lib/commons-codec-1.3.jar"
set CP=%CP%;"%CD%/lib/velocity-1.5.jar"
set CP=%CP%;"%CD%/lib/oro-2.0.8.jar"
set CP=%CP%;"%CD%/lib/commons-collections-3.1.jar"
set CP=%CP%;"%CD%/lib/commons-lang-2.1.jar"
set CP=%CP%;"%CD%/lib/bsh-core-2.0b4.jar"
set CP=%CP%;"%CD%/lib/RXTXcomm.jar"
set CP=%CP%;"%CD%/lib/jpos111.jar"
set CP=%CP%;"%CD%/lib/swingx.jar"
set CP=%CP%;"%CD%/lib/substance.jar"
set CP=%CP%;"%CD%/lib/substance-swingx.jar"

rem Apache Axis SOAP libraries.
set CP=%CP%;"%CD%/lib/axis.jar"
set CP=%CP%;"%CD%/lib/jaxrpc.jar"
set CP=%CP%;"%CD%/lib/saaj.jar"
set CP=%CP%;"%CD%/lib/wsdl4j-1.5.1.jar"
set CP=%CP%;"%CD%/lib/commons-discovery-0.2.jar"
set CP=%CP%;"%CD%/lib/commons-logging-1.0.4.jar"

set CP=%CP%;"%CD%/locales/"
set CP=%CP%;"%CD%/reports/"

start /B javaw -cp %CP% -Djava.library.path="%CD%/lib/Windows/i368-mingw32" com.openbravo.pos.forms.StartPOS
