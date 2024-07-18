FROM python:3.9-slim

RUN pip install psycopg2-binary requests

COPY listening.py /listening.py
COPY trigger.sql /trigger.sql

ENTRYPOINT ["python", "/listening.py"]
