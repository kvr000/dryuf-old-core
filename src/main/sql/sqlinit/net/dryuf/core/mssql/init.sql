CREATE FUNCTION currentTimeMillis()
RETURNS BIGINT
AS
BEGIN
	RETURN cast(((cast(GETUTCDATE() as float)-cast(cast('1970-01-01 00:00:00' as datetime) as float))*24*3600*1000) as bigint);
END;
