CREATE FUNCTION currentTimeMillis()
RETURNS BIGINT
	RETURN UNIX_TIMESTAMP()*1000;
