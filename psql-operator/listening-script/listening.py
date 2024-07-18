import psycopg2
import requests
import json
import os
import logging
import time

# Setup logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# Database and Operator URL configurations
PRIMARY_DB_HOST = os.getenv("PRIMARY_DB_HOST", "localhost")
PRIMARY_DB_PORT = os.getenv("PRIMARY_DB_PORT", "5432")
PRIMARY_DB_USER = os.getenv("PRIMARY_DB_USER", "postgres")
PRIMARY_DB_PASSWORD = os.getenv("PRIMARY_DB_PASSWORD", "123456")
PRIMARY_DB_NAME = os.getenv("PRIMARY_DB_NAME", "cloud")
OPERATOR_URL = os.getenv("OPERATOR_URL", "http://localhost:8080/sync")

attempt_delay = 60

def execute_sql_script(file_path, connection):
    with open(file_path, 'r') as file:
        sql_script = file.read()
    cur = connection.cursor()
    cur.execute(sql_script)
    connection.commit()

def get_db_connection():
    return psycopg2.connect(
        host=PRIMARY_DB_HOST,
        port=PRIMARY_DB_PORT,
        user=PRIMARY_DB_USER,
        password=PRIMARY_DB_PASSWORD,
        dbname=PRIMARY_DB_NAME
    )

def listen_for_changes():
    try:
        conn = get_db_connection()
        conn.set_isolation_level(psycopg2.extensions.ISOLATION_LEVEL_AUTOCOMMIT)
        execute_sql_script("trigger.sql", conn)

        cur = conn.cursor()
        cur.execute("LISTEN user_table_update;")
        logging.info("Waiting for notifications on channel 'user_table_update'")

        while True:
            conn.poll()
            while conn.notifies:
                notify = conn.notifies.pop(0)
                logging.info(f"Got NOTIFY: {notify.payload}")
                payload = json.loads(notify.payload)
                response = requests.post(OPERATOR_URL, json=payload)
                logging.info(f"Forwarded to Operator, response status: {response.status_code}")
    except Exception as e:
        logging.error(e)
        logging.info(f"Waiting for {attempt_delay} seconds before retrying...")
        time.sleep(attempt_delay)
        listen_for_changes()

if __name__ == "__main__":
    listen_for_changes()