import logging
import random
from kafka import KafkaConsumer, KafkaProducer
import json

def get_messages():
    consumer = KafkaConsumer(KAFKA_CONFIG['consumer_topic'],
                            bootstrap_servers=[KAFKA_CONFIG['kafka_server']],
                            value_deserializer=json.loads,
                            enable_auto_commit=False)

    if consumer.bootstrap_connected():
        while True:
            try:
                yield next(consumer)
            except (StopIteration, KeyboardInterrupt):
                logging.info('Exiting from stop command.')
            except Exception as e:
                logging.error(e)

def send_message(coordinates):
    producer = KafkaProducer(bootstrap_servers=[KAFKA_CONFIG['kafka_server']],
                            value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                            retries=3)

    if producer.bootstrap_connected():
        producer.send(KAFKA_CONFIG['producer_topic'], coordinates)

if __name__ == '__main__':
    logging.info('Service started.')
    config_file = open('config.json')
    KAFKA_CONFIG = json.load(config_file)

    for message in get_messages():
        try:
            coordinates = message.value
            assert isinstance(coordinates, list) and all(map(lambda pair: len(pair) == 2, coordinates)), "Incorrect structuring for coordinates."
            new_coordinates = []
            for long, lat in coordinates:
                for i in range(5):
                    # Mock coordinates from initial coordinates generated by detection service
                    # TODO: Update to cluster data points for region-based summaries instead
                    coords = (long + random.uniform(-0.25, 0.25),
                            lat +  random.uniform(-0.25, 0.25))
                    new_coordinates.append(coords)
            
            send_message(new_coordinates)
        except Exception as e:
            logging.error(e)
