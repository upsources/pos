#!/bin/sh

#    Openbravo POS is a point of sales application designed for touch screens.
#    Copyright (C) 2007-2008 Openbravo, S.L.
#    http://sourceforge.net/projects/openbravopos
#
#    This program is free software; you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation; either version 2 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program; if not, write to the Free Software
#    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  

DIRNAME=`dirname $0`

CP=$DIRNAME/openbravopos.jar

CP=$CP:$DIRNAME/lib/jasperreports-2.0.1.jar
CP=$CP:$DIRNAME/lib/jcommon-1.0.0.jar
CP=$CP:$DIRNAME/lib/jfreechart-1.0.0.jar
CP=$CP:$DIRNAME/lib/jdt-compiler-3.1.1.jar
CP=$CP:$DIRNAME/lib/commons-beanutils-1.7.jar
CP=$CP:$DIRNAME/lib/commons-digester-1.7.jar
CP=$CP:$DIRNAME/lib/itext-1.3.1.jar
CP=$CP:$DIRNAME/lib/poi-3.0.1-FINAL-20070705.jar
CP=$CP:$DIRNAME/lib/barcode4j-light.jar
CP=$CP:$DIRNAME/lib/commons-codec-1.3.jar
CP=$CP:$DIRNAME/lib/velocity-1.5.jar
CP=$CP:$DIRNAME/lib/oro-2.0.8.jar
CP=$CP:$DIRNAME/lib/commons-collections-3.1.jar
CP=$CP:$DIRNAME/lib/commons-lang-2.1.jar
CP=$CP:$DIRNAME/lib/bsh-core-2.0b4.jar
CP=$CP:$DIRNAME/lib/RXTXcomm.jar
CP=$CP:$DIRNAME/lib/jpos111.jar
CP=$CP:$DIRNAME/lib/swingx.jar
CP=$CP:$DIRNAME/lib/substance.jar
CP=$CP:$DIRNAME/lib/substance-swingx.jar

# Apache Axis SOAP libraries.
CP=$CP:$DIRNAME/lib/axis.jar
CP=$CP:$DIRNAME/lib/jaxrpc.jar
CP=$CP:$DIRNAME/lib/saaj.jar
CP=$CP:$DIRNAME/lib/wsdl4j-1.5.1.jar
CP=$CP:$DIRNAME/lib/commons-discovery-0.2.jar
CP=$CP:$DIRNAME/lib/commons-logging-1.0.4.jar

CP=$CP:$DIRNAME/locales/
CP=$CP:$DIRNAME/reports/

java -cp $CP -Dswing.defaultlaf=javax.swing.plaf.metal.MetalLookAndFeel -Djava.library.path=$DIRNAME/lib/Linux/i686-unknown-linux-gnu com.openbravo.pos.forms.StartPOS