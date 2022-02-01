import grpc
import ingestor_pb2_grpc
import ingestor_pb2
import nexradaws

class NexRad:
    def __init__(self, grpc_server, grpc_server_port, date, month, year, station, time) -> None:
        self.grpc_server = grpc_server
        self.grpc_server_port = grpc_server_port
        self.date = date
        self.month = month
        self.year = year
        self.station = station
        self.time = time
    
    #if there are radar scans available for the given date and station return true
    def validate(self):
        conn = nexradaws.NexradAwsInterface()
        scans = conn.get_avail_scans(self.year, self.month, self.date, self.station)
        return len(scans) != 0
    
    def frameUrl(self):
        return f'{self.year}/{self.month}/{self.date}/{self.station}'

    #post the nexrad data to the dbmanagement service
    def saveNexradData(self):
        server_url = f'{self.grpc_server}:{self.grpc_server_port}'
        channel = grpc.insecure_channel(server_url)
        stub = ingestor_pb2_grpc.DbMgmtServiceStub(channel)
        return stub.saveurl(ingestor_pb2.msgUrl(
            date = self.date, 
            month = self.month, 
            year = self.year, 
            station = self.station, 
            time = self.time, 
            url = self.frameUrl()
            ))

