GO  
SELECT   *,
    f.name AS foreign_key_name  
   ,OBJECT_NAME(f.parent_object_id) AS table_name  
   ,COL_NAME(fc.parent_object_id, fc.parent_column_id) AS constraint_column_name  
   ,OBJECT_NAME (f.referenced_object_id) AS referenced_object  
   ,COL_NAME(fc.referenced_object_id, fc.referenced_column_id) AS referenced_column_name  
   ,is_disabled  
   ,delete_referential_action_desc  
   ,update_referential_action_desc  
FROM sys.foreign_keys AS f  
INNER JOIN sys.foreign_key_columns AS fc   
   ON f.object_id = fc.constraint_object_id   
WHERE f.parent_object_id = OBJECT_ID('dbo.Categories');  


ALTER TABLe   [dbo].[Categories]
ADD CONSTRAINT UQ_Picture UNIQUE (CategoryName);   

SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
WHERE TABLE_NAME=  'EmployeeTerritories'

CREATE SCHEMA code;
GO
drop TABLE code.Codes
go

CREATE TABLE code.Codes (codeId varchar(16) not null);


ALTER TABLE code.Codes
ADD CONSTRAINT PK_Codes PRIMARY KEY (codeId) ;


ALTER TABLE [dbo].[Categories]
ADD CONSTRAINT FK_Categories_CodeId 
FOREIGN KEY (codeId) REFERENCES code.Codes(codeId) ;


SELECT  *,
    fk.name,
    OBJECT_NAME(fk.parent_object_id) as 'Parent table',
    c1.name 'Parent column',
    OBJECT_NAME(fk.referenced_object_id) 'Referenced table',
    c2.name 'Referenced column'
FROM 
    sys.foreign_keys fk
INNER JOIN 
    sys.foreign_key_columns fkc ON fkc.constraint_object_id = fk.object_id
INNER JOIN
    sys.columns c1 ON fkc.parent_column_id = c1.column_id AND fkc.parent_object_id = c1.object_id
INNER JOIN
    sys.columns c2 ON fkc.referenced_column_id = c2.column_id AND fkc.referenced_object_id = c2.object_id
where OBJECT_NAME(fk.referenced_object_id) = 'Categories'

SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
WHERE TABLE_SCHEMA= 'dbo'
AND TABLE_NAME= 'Categories'
and CONSTRAINT_TYPE = 'UNIQUE'



