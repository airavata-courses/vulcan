from flask import current_app
from pykafka import KafkaClient

class kafka:
    def __init__(self, server, port, topic) -> None:
        current_app.config.from_pyfile('config.py')
        kafka_server = current_app.config[server]
        kafka_server_port = current_app.config[port]

        client = KafkaClient(hosts=f'{kafka_server}:{kafka_server_port}')

        self.kafka_topic = client.topics[current_app.config[topic]]