import grpc
import ingestor_pb2_grpc
import ingestor_pb2
import nexradaws
from google.protobuf.json_format import MessageToJson
from flask import current_app

class NexRad:
    def __init__(self, data) -> None:
        self.grpc_server = current_app.config["GRPC_SERVER"]
        self.grpc_server_port = current_app.config["GRPC_SERVER_PORT"]
        self.date = data['date']
        self.month = data['month']
        self.year = data['year']
        # self.station = data['station']
        # self.time = data['time']
        self.station = "KCCX"
        self.time = "12PM"
    
    #if there are radar scans available for the given date and station return true
    def validate(self):
        conn = nexradaws.NexradAwsInterface()
        try:
            scans = conn.get_avail_scans(self.year, self.month, self.date, self.station)
            return len(scans) != 0
        except:
            print('invalid user input')
            return False
    
    def frameUrl(self):
        return f'{self.year}/{self.month}/{self.date}/{self.station}'

    #post the nexrad data to the dbmanagement service
    def saveNexradData(self):
        server_url = f'{self.grpc_server}:{self.grpc_server_port}'
        channel = grpc.insecure_channel(server_url)
        stub = ingestor_pb2_grpc.DbMgmtServiceStub(channel)
        message =  stub.saveurl(ingestor_pb2.msgUrl(
            date = self.date, 
            month = self.month, 
            year = self.year, 
            station = self.station, 
            time = self.time, 
            url = self.frameUrl()
            ))
        return MessageToJson(message)

