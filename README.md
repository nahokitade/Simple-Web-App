Implements a simple server with convert, show, madlib, getword, and peers.

convert
Converts a given query into binary.

show
Calls convert on all servers that have implemented convert, and prints
out the result.

peers
shows all servers that have implemented a certain feature

getword
gets a random word of a specific part of speech

madlib
plays the madlib game using all server's getword (those which have implemented that feature)
Takes in a query formatted as such:
Don't <verb> your <animal> before they <verb>.

With the substituting part of speech in < > brackets.

