#!/bin/sh

#    Openbravo POS is a point of sales application designed for touch screens.
#    Copyright (C) 2008 Openbravo, S.L.
#    http://sourceforge.net/projects/
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

CP=openbravopos.jar

CP=$CP:lib/l2fprod-common-tasks.jar
CP=$CP:lib/jasperreports-2.0.1.jar
CP=$CP:lib/jcommon-1.0.0.jar
CP=$CP:lib/jfreechart-1.0.0.jar
CP=$CP:lib/jdt-compiler-3.1.1.jar
CP=$CP:lib/commons-beanutils-1.7.jar
CP=$CP:lib/commons-digester-1.7.jar
CP=$CP:lib/itext-1.3.1.jar
CP=$CP:lib/poi-3.0.1-FINAL-20070705.jar
CP=$CP:lib/barcode4j-light.jar
CP=$CP:lib/commons-codec-1.3.jar
CP=$CP:lib/velocity-1.5.jar
CP=$CP:lib/oro-2.0.8.jar
CP=$CP:lib/commons-collections-3.1.jar
CP=$CP:lib/commons-lang-2.1.jar
CP=$CP:lib/bsh-core-2.0b4.jar
CP=$CP:lib/RXTXcomm.jar
CP=$CP:lib/jpos111.jar

# Apache Axis SOAP libraries.
CP=$CP:lib/axis.jar
CP=$CP:lib/jaxrpc.jar
CP=$CP:lib/saaj.jar
CP=$CP:lib/wsdl4j-1.5.1.jar
CP=$CP:lib/commons-discovery-0.2.jar
CP=$CP:lib/commons-logging-1.0.4.jar

CP=$CP:locales/

java -cp $CP -Dswing.defaultlaf=javax.swing.plaf.metal.MetalLookAndFeel -Djava.library.path=lib/Linux/i686-unknown-linux-gnu com.openbravo.pos.forms.StartPOS
