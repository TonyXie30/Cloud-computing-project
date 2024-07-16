-- 创建触发器函数
CREATE OR REPLACE FUNCTION notify_user_table_update() RETURNS trigger AS $$
DECLARE
record RECORD;
BEGIN
    IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
        record := NEW;
ELSE
        record := OLD;
END IF;
    PERFORM pg_notify('user_table_update', json_build_object('table', TG_TABLE_NAME, 'operation', TG_OP, 'record', row_to_json(record))::text);
RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- 创建触发器
CREATE TRIGGER user_table_update_trigger
    AFTER INSERT OR UPDATE OR DELETE ON "user"
    FOR EACH ROW EXECUTE FUNCTION notify_user_table_update();

