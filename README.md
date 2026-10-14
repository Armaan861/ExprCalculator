# Expression Parser and Calculator (Compiler Design TEC)

A Java Swing application that works like a mini compiler front end for arithmetic expressions. It reads an expression such as `(2+3)*4-5`, splits it into tokens (lexical analysis), checks it against a context-free grammar using a recursive descent parser, builds a parse tree, generates three-address code, and evaluates the result. Syntax errors and division by zero are reported clearly, and every calculation is saved in a SQL Server database (history table).

## Tech
- Java (JDK 17+), Swing GUI, JDBC
- SQL Server Express (SSMS)

## Run
1. Run `schema.sql` in SSMS to create the database.
2. Add `mssql-jdbc` jar to the project libraries.
3. Set your login in `DatabaseHelper.java`.
4. Run `MainGUI.java`.
