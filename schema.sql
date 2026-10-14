CREATE DATABASE ExprCalcDB;
GO
USE ExprCalcDB;
GO
CREATE TABLE history (
    id INT IDENTITY(1,1) PRIMARY KEY,
    expression NVARCHAR(200),
    tokens NVARCHAR(MAX),
    result NVARCHAR(200),
    status NVARCHAR(20),
    created_at DATETIME DEFAULT GETDATE()
);
USE ExprCalcDB;

SELECT * FROM history;