SET GLOBAL log_bin_trust_function_creators = 1;

# 题库题干过滤base64的图片
DROP FUNCTION IF EXISTS filterBase64Img;
DELIMITER |
CREATE FUNCTION filterBase64Img(Dirty longtext)
    RETURNS longtext
    DETERMINISTIC
BEGIN
    DECLARE iStart, iEnd, iLength int;
    WHILE Locate('<img', Dirty) > 0 And Locate('>', Dirty, Locate('<img', Dirty)) > 0
        DO
            BEGIN
                SET iStart = Locate('<img', Dirty), iEnd = Locate('>', Dirty, Locate('<img', Dirty));
                SET iLength = (iEnd - iStart) + 1;
                IF iLength > 0 THEN
                    BEGIN
                        SET Dirty = Insert(Dirty, iStart, iLength, '');
                    END;
                END IF;
            END;
        END WHILE;
    RETURN Dirty;
END;
|
DELIMITER ;