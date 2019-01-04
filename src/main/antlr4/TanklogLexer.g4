lexer grammar TanklogLexer;

@header {
package de.tanklog.antlr4.generated;
}

SINGLE_LINE_COMMENT:     '//'  InputCharacter*    -> channel(HIDDEN);

BYTE_ORDER_MARK: '\u00EF\u00BB\u00BF';

WHITESPACES:	(Whitespace | NewLine)+            -> channel(HIDDEN);

AIRPRESSURE:    'airpressure';
ENTRY:          'entry';
GARAGE:         'garage' -> mode(GARAGEMODE);
PURCHASEDETAIL: 'purchasedetail';
OILCHANGE:      'oilchange';
WITH:           'with';
WITHOUT:        'without';
WOOK:           'wook'; // water and oil okay
EURO:           'euro' | 'eur';
LITER:          'liter' | 'l';
CARNAME:        'carname' -> mode(REMAINING);
FILTER:         'filter' -> mode(REMAINING);
FULLYLADEN:     'fullyladen' -> mode(REMAINING);
HALFLADEN:      'halfladen' -> mode(REMAINING);
NOTE:           'note' -> mode(REMAINING);
NUMBERPLATE:    'numberplate' -> mode(REMAINING);
SPAREWHEEL:     'sparewheel' -> mode(REMAINING);
KILOMETER:      ('kilometer' | 'km') -> mode(REMAINING);

ISODATE: Int Int Int Int '-' Int Int '-' Int Int;

GERMANDATE: Int Int '.' Int Int '.' Int Int Int Int;

AMERICANDATE: Int Int '/' Int Int '/' Int Int Int Int;

NUMBER: [0-9]+ ( ( ',' | '.' ) [0-9]+)?;

mode GARAGEMODE;

WHITESPACESGARAGE: WHITESPACES -> channel(HIDDEN);

ISODATEGARAGE: ISODATE -> type(ISODATE), mode(REMAINING);
GERMANDATEGARAGE: GERMANDATE -> type(GERMANDATE), mode(REMAINING);
AMERICANDATEGARAGE: AMERICANDATE -> type(AMERICANDATE), mode(REMAINING);

mode REMAINING;

STRING: InputCharacter+;

EOL: NewLine -> mode(DEFAULT_MODE);

fragment InputCharacter:       ~[\r\n\u0085\u2028\u2029];

fragment NewLine
	: '\r\n' | '\r' | '\n'
	| '\u0085' // <Next Line CHARACTER (U+0085)>'
	| '\u2028' //'<Line Separator CHARACTER (U+2028)>'
	| '\u2029' //'<Paragraph Separator CHARACTER (U+2029)>'
	;

fragment Int
	: [0-9]
	;

fragment Whitespace
	: UnicodeClassZS //'<Any Character With Unicode Class Zs>'
	| '\u0009' //'<Horizontal Tab Character (U+0009)>'
	| '\u000B' //'<Vertical Tab Character (U+000B)>'
	| '\u000C' //'<Form Feed Character (U+000C)>'
	;

fragment UnicodeClassZS
	: '\u0020' // SPACE
	| '\u00A0' // NO_BREAK SPACE
	| '\u1680' // OGHAM SPACE MARK
	| '\u180E' // MONGOLIAN VOWEL SEPARATOR
	| '\u2000' // EN QUAD
	| '\u2001' // EM QUAD
	| '\u2002' // EN SPACE
	| '\u2003' // EM SPACE
	| '\u2004' // THREE_PER_EM SPACE
	| '\u2005' // FOUR_PER_EM SPACE
	| '\u2006' // SIX_PER_EM SPACE
	| '\u2008' // PUNCTUATION SPACE
	| '\u2009' // THIN SPACE
	| '\u200A' // HAIR SPACE
	| '\u202F' // NARROW NO_BREAK SPACE
	| '\u3000' // IDEOGRAPHIC SPACE
	| '\u205F' // MEDIUM MATHEMATICAL SPACE
	;
