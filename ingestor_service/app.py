from flask import Flask
from nexrad import NexRad
from consumer import consumer
from producer import producer
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
            message = nexRad.saveNexradData()

            print(f'Message returned from db management service - {message}')

            output = [data["coords"]]

            status = producer(topic='KAFKA_STORM_DETECTION_TOPIC').produce(json.dumps(output))
            print(status)
        else:
            print("fail")


if __name__ == "__main__":
    
    # with app.app_context():
    #     produce('sample message')

    with app.app_context():
        messages = consumer(topic='KAFKA_INGESTOR_TOPIC').consume()
        for m in messages:
            if m is not None:
                data = json.loads(m.value.decode('utf-8'))
                saveurl(data)
    

    
