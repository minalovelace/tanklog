parser grammar TanklogParser;

options { tokenVocab=TanklogLexer; }

@header {
package de.tanklog.antlr4.generated;
}

document: BYTE_ORDER_MARK? lines EOF?;

lines: line*;

line: linecontent EOL?;

linecontent
		: airfullyladenline
		| airhalfladenline
		| airsparewheelline
		| carnameline
		| entryline
		| garageline
		| noteline
		| numberplateline
		| oilchangeline
		| oilchangewithfilterline
		| oilchangewithoutfilterline
		| purchasedetailline
		| wookline
		;

airhalfladenline: AIRPRESSURE HALFLADEN content;

airfullyladenline: AIRPRESSURE FULLYLADEN content;

airsparewheelline: AIRPRESSURE SPAREWHEEL content;

carnameline: CARNAME content;

garageline: GARAGE date content;

entryline: ENTRY date euro EURO liter LITER kilometer KILOMETER;

noteline: NOTE content;

numberplateline: NUMBERPLATE content;

oilchangeline: OILCHANGE date kilometer KILOMETER;

oilchangewithfilterline: OILCHANGE WITH FILTER content;

oilchangewithoutfilterline: OILCHANGE WITHOUT FILTER content;

purchasedetailline: PURCHASEDETAIL date kilometer KILOMETER?;

wookline: WOOK date;

euro: NUMBER;

kilometer: NUMBER;

liter: NUMBER;

date: ISODATE | GERMANDATE | AMERICANDATE;

content: STRING;
