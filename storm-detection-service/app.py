from distutils.log import debug
from flask import Flask
# from nexrad import NexRad
from consumer import consumer
from producer import producer
from pykml.factory import KML_ElementMaker as KML
from pykml.factory import GX_ElementMaker as GX
from lxml import etree
import json

# import nexradaws
app = Flask(__name__)


@app.route('/')
def index():
    return 'Storm Detection Service'


def generate_kml(self, data):
    """Read data from ingestor service and parse paramaters and generate KML for storm data"""

    """Using Dummy KML to mock data for Assignment 1"""

    pm1 = KML.Placemark(
             KML.name("Storm brewing.."),
             KML.Point(
               KML.coordinates("-88.23999786376953,30.679445266723633")
             )
          )
          
    pm2 = KML.Placemark(
             KML.name("Storm... "),
             KML.Point(
               KML.coordinates("-88.24999786376953,30.699445266723633")
             )
           )
    pm3 = KML.Placemark(
             KML.name("Storm Alert...!"),
             KML.Point(
               KML.coordinates("-88.25999786376953,30.679445266723633")
             )
           )
    fld = KML.Folder(pm1,pm2)
    fld2= KML.Folder(fld,pm3)
    stri = etree.tostring(fld2, pretty_print=True)
    
    status = producer(topic='KAFKA_PRODUCER_TOPIC').produce(stri)
    print(status)
    


if __name__ == "__main__": 
  # app.run(debug= True) 
  messages = consumer(topic='KAFKA_INGESTOR_TOPIC').consume()
  
  for message in messages:
    if message is not None:
      data = json.loads(message.value.decode('utf-8'))
      generate_kml(data)


# app.config.from_pyfile('config.py')


# app.config.from_pyfile('config.py')
# kafka_server = app.config['KAFKA_SERVER']
# kafka_server_port = app.config['KAFKA_SERVER_PORT']
# client = KafkaClient(hosts=f'{kafka_server}:{kafka_server_port}')

# messages = client.topics[app.config['KAFKA_CONSUMER_TOPIC']].get_simple_consumer(
  #           auto_offset_reset=OffsetType.LATEST,
  #           reset_offset_on_start=True)

# topic = client.topics[app.config['KAFKA_PRODUCER_TOPIC']]
    # with topic.get_producer(delivery_reports = False) as producer:
    #   producer.produce(json(stri))

# outfile = open('pls_work_now1.kml','wb')
    # outfile.write(stri)