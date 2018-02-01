/*
ORIGINAL GRAMMAR ==================

expr    ::=	num | lvalue | incrop expr | expr incrop | expr binop expr | (expr)
lvalue	::=	$expr
incrop	::=	++ | --
binop	::=	+ | - |
num	::=	0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

LL(1) GRAMMAR =========================
- LL(1): left to right scan, left-most derivation, 1-token lookahead
- ignore and discard tabs, spaces, newlines, and Awk comments
- Your parser should use a greedy scanner ('++' is ++, not + +)

<expr>    ::= <expr'><term>
<term>    ::= \s<expr>
           |  [null]

<expr'>   ::= <expr''><term'>
<term'>   ::= binop <expr'>
           |  [null]

<expr''>  ::= incrop <expr''>
           |  <expr3>

<expr3>   ::= <expr4> <term''>
<term''>  ::= incrop <term''>
           |  [null]

<expr4>   ::= $<expr5>
           |  <expr5>

<expr5>   ::=

incrop  ::=	++ | --
binop   ::=	+ | -
num     ::=	0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

Examples ===============================

** Ex. 1
INPUT
$1 + (1 - ++$2) $3

 */

public class Parse {

    public static void main(String[] args) {
        // write your code here
    }
}
