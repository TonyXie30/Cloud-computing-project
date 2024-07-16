import psycopg2
import requests
import json
import os

PRIMARY_DB_HOST = os.getenv("PRIMARY_DB_HOST", "localhost")
PRIMARY_DB_PORT = os.getenv("PRIMARY_DB_PORT", "5432")
PRIMARY_DB_USER = os.getenv("PRIMARY_DB_USER", "postgres")
PRIMARY_DB_PASSWORD = os.getenv("PRIMARY_DB_PASSWORD", "123456")
PRIMARY_DB_NAME = os.getenv("PRIMARY_DB_NAME", "cloud")
OPERATOR_URL = os.getenv("OPERATOR_URL", "http://localhost:8080/sync")

def listen_for_changes():
    while True:
        try:
            conn = psycopg2.connect(
            host=PRIMARY_DB_HOST,
            port=PRIMARY_DB_PORT,
            user=PRIMARY_DB_USER,
            password=PRIMARY_DB_PASSWORD,
            dbname=PRIMARY_DB_NAME
            )
            conn.set_isolation_level(psycopg2.extensions.ISOLATION_LEVEL_AUTOCOMMIT)
            cur = conn.cursor()
            cur.execute("LISTEN user_table_update;")
            print("Waiting for notifications on channel 'user_table_update'")

            while True:
                conn.poll()
                while conn.notifies:
                    notify = conn.notifies.pop(0)
                    print(f"Got NOTIFY: {notify.payload}")
                    payload = json.loads(notify.payload)
                    response = requests.post(OPERATOR_URL, json=payload)
                    print(f"Forwarded to Operator, response status: {response.status_code}")
        except Exception as e:
            print(e)
            continue

if __name__ == "__main__":
    listen_for_changes()
