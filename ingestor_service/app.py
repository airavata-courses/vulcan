from flask import Flask, request
from nexrad import NexRad
from pykafka import KafkaClient
from pykafka.common import OffsetType
import json

app = Flask(__name__)

#creates an object of Nexrad class
#validates the received json data
#posts the data to Database Management service
def saveurl(data):
    print(f"data received - {data}")

    if data is not None:
        nexRad = NexRad(data)
        
        if(nexRad.validate()):
            #post the data to the grpc server
            print(nexRad.saveNexradData())
            print("success")
        else:
            print("fail")


if __name__ == "__main__":
    
    with app.app_context():
        app.config.from_pyfile('config.py')
        kafka_server = app.config['KAFKA_SERVER']
        kafka_server_port = app.config['KAFKA_SERVER_PORT']

        #initiate Kafka consumer
        client = KafkaClient(hosts=f'{kafka_server}:{kafka_server_port}')

        #consume data from the Ingestor topic
        messages = client.topics[app.config['KAFKA_INGESTOR_TOPIC']].get_simple_consumer(
            auto_offset_reset=OffsetType.LATEST,
            reset_offset_on_start=True)

        for m in messages:
            if m is not None:
                data = json.loads(m.value.decode('utf-8'))
                saveurl(data)
    

    
