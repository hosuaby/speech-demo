#!/bin/sed -rf

# Lowercase all characters
#s/[A-Z]/\L&/g

# Remove non characters
#s/[^a-z[:space:]]//g

# Remove non-text lines
/^\W*$/d

# Remove trailing spaces
s/^\s+//
s/\s+$//

# Remove blanc lines
/^\s*$/d

# Wrap each line into JSON document
s/(.*)/{ "text": "\1" }/

# Prepend each line with index command
s/(.*)/{ "index" : {} }\n\1/
