from flask import current_app
from pykafka.common import OffsetType
from kafka import kafka

class consumer(kafka):
    def __init__(self, topic, server='', port='', default = True) -> None:
        if default:
            server = 'KAFKA_SERVER'
            port = 'KAFKA_SERVER_PORT'
        super().__init__(server, port, topic)
    
    def consume(self):
        try:
            messages = self.kafka_topic.get_simple_consumer(
            auto_offset_reset=OffsetType.LATEST,
            reset_offset_on_start=True)
        
            return messages
        except:
            print('an error occured while consuming message from kafka consumer')
            return 'fail'