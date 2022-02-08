from kafka import kafka

class producer(kafka):
    def __init__(self, topic, server='', port='', default = True) -> None:
        if default:
            server = 'KAFKA_SERVER'
            port = 'KAFKA_SERVER_PORT'
        super().__init__(server, port, topic)
    
    def produce(self, message):
        try:
            with self.kafka_topic.get_sync_producer() as _producer:
                _producer.produce(bytes(message,'utf-8'))
            
            return 'success'
        except:
            print('an error occured while posting message to producer')
            return 'fail'
