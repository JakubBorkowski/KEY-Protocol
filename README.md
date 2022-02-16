# KEY-Protocol
Client-server application using the Key-Protocol. Application provide service on port 2011, that authenticates a given key.
EBNF syntax of request and response in the Key-Protocol:

```EBNF
<specification> ::= <definition> ";"
<definition>    ::= <request>
                |   <respond>
                
<request>       ::= "key_in -" <question>
<question>      ::= "get" "{" <parametr1> "}"
                |   "set" "{" <parametr2> "}"
<parametr1>     ::= <nr_id>
<parametr2>     ::= <password> ":" <nr_id>

<respond>       ::= "key_out-{" <nr_id> ":" <contents> "}"
<contents>      ::= "yes"
                |   "not"
                |   "error"
